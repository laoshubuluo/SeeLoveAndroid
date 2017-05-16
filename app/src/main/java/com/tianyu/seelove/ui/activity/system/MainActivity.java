package com.tianyu.seelove.ui.activity.system;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.application.SeeLoveApplication;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.SystemController;
import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.network.response.NewVersionRspInfo;
import com.tianyu.seelove.service.MessageSendService;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.fragment.FindFragment;
import com.tianyu.seelove.ui.fragment.FollowFragment;
import com.tianyu.seelove.ui.fragment.ManageFragment;
import com.tianyu.seelove.ui.fragment.MessageFragment;
import com.tianyu.seelove.utils.FileUtil;
import com.tianyu.seelove.utils.NetworkUtil;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.view.RedDotView;
import com.tianyu.seelove.view.dialog.VersionUpdateDialog;
import java.io.File;

/**
 * 主页－统一对fragment进行管理
 * @author shisheng.zhao
 * @date 2017-03-28 16:27
 */
public class MainActivity extends BaseActivity {
    public static FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private Class<?>[] fragmentArray = {FindFragment.class, MessageFragment.class,
            FollowFragment.class, ManageFragment.class};
    private String[] mTextviewArray;
    private int[] mImageViewArray = {R.drawable.selector_bottom_find,
            R.drawable.selector_bottom_message, R.drawable.selector_bottom_follow,
            R.drawable.selector_bottom_manager};
    private MessageDao messageDao;
    private RedDotView redDotView;
    private int unReadCount = 0;
    private SystemController controller;
    private BroadcastReceiver broadcastReceiver;
    DownloadManager dManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new SystemController(this, handler);
        messageDao = new MessageDaoImpl();
        mTextviewArray = this.getResources().getStringArray(R.array.frag_text);
        initIntent();
        initView();
        initService();
        updateVersion();
    }

    private void initService() {
        Intent intent;
        // 启动发送消息Service
        intent = IntentManager.createIntent(getApplicationContext(), MessageSendService.class);
        startService(intent);
    }

    private void initIntent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Actions.MESSAGE_READ_CHANGE);
        registerReceiver(receiver, intentFilter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 未读消息刷新
            if (action.equals(Actions.MESSAGE_READ_CHANGE)) {
                unReadCount = messageDao.getAllUnReadMessageCount();
                redDotView.initView(unReadCount);
            }
        }
    };

    private void initView() {
        mInflater = LayoutInflater.from(this);
        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabLayout);
        // 得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // 根据tabId处理相关tabChang事件
                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                if (isOpen) {
                    imm.hideSoftInputFromWindow(mTabHost.getWindowToken(), 0); //强制隐藏键盘
                }
            }
        });
    }

    private View getTabItemView(int index) {
        View view = mInflater.inflate(R.layout.tab_item_view, null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tabIcon);
        tabIcon.setImageResource(mImageViewArray[index]);
        TextView tabTitle = (TextView) view.findViewById(R.id.tabTitle);
        tabTitle.setText(mTextviewArray[index]);
        // 消息item
        if (index == 1) {
            redDotView = (RedDotView) view.findViewById(R.id.redDot);
            unReadCount = messageDao.getAllUnReadMessageCount();
            redDotView.initView(unReadCount);
        }
        return view;
    }

    private void updateVersion() {
        controller.getNewVerison();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MessageSignConstant.NEW_VERSION_SUCCESS:
                final NewVersionRspInfo newVersion = (NewVersionRspInfo) msg.getData().getSerializable("newVersion");
                if (null != newVersion && StringUtils.isNotBlank(newVersion.getVersionCode())) {
                    if (Integer.parseInt(SeeLoveApplication.versionCode) < Integer.parseInt(newVersion.getVersionCode())) {
                        if ("1".equals(newVersion.getIsForced())) {
                            if (NetworkUtil.isWifiConnection(this)) {
                                try {
                                    FileUtil.delAllFile("sdcard/updateDownload/");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                intoDownloadManager(newVersion.getDownloadUrl());
                            }
                        } else {
                            final VersionUpdateDialog updateDialog = new VersionUpdateDialog(this);
                            updateDialog.initData(newVersion.getDes());
                            updateDialog.getSureTV().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateDialog.dismiss();
                                    try {
                                        FileUtil.delAllFile("sdcard/updateDownload/");
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                    intoDownloadManager(newVersion.getDownloadUrl());
                                }
                            });
                            updateDialog.show();
                        }
                    }
                }
                break;
        }
        return false;
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void intoDownloadManager(String url) {
        dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置下载路径和文件名
        request.setDestinationInExternalPublicDir("updateDownload", "seelove.apk");
        request.setDescription(getString(R.string.version_update));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        long refernece = dManager.enqueue(request);
        // 把当前下载的ID保存起来
        SharedPreferences sPreferences = getSharedPreferences("downloadplato", 0);
        sPreferences.edit().putLong("plato", refernece).commit();
        listener(refernece);
    }

    private void listener(final long Id) {
        // 注册广播监听系统的下载完成事件。
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (ID == Id) {
                    Intent dintent = new Intent(Intent.ACTION_VIEW);
                    dintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dintent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/updateDownload/seelove.apk")),
                            "application/vnd.android.package-archive");
                    startActivity(dintent);
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //模拟Home键效果
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
        if (null != broadcastReceiver) {
            unregisterReceiver(broadcastReceiver);
        }
    }
}