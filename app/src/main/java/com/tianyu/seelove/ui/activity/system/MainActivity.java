package com.tianyu.seelove.ui.activity.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.service.MessageSendService;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.fragment.FindFragment;
import com.tianyu.seelove.ui.fragment.FollowFragment;
import com.tianyu.seelove.ui.fragment.ManageFragment;
import com.tianyu.seelove.ui.fragment.MessageFragment;
import com.tianyu.seelove.view.RedDotView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageDao = new MessageDaoImpl();
        mTextviewArray = this.getResources().getStringArray(R.array.frag_text);
        initIntent();
        initView();
        initService();
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
    }
}