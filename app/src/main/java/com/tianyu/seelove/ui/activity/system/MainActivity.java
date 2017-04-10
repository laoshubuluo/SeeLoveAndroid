package com.tianyu.seelove.ui.activity.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.service.MessageSendService;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.fragment.FindFragment;
import com.tianyu.seelove.ui.fragment.FollowFragment;
import com.tianyu.seelove.ui.fragment.ManageFragment;
import com.tianyu.seelove.ui.fragment.MessageFragment;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextviewArray = this.getResources().getStringArray(R.array.frag_text);
        initView();
        initService();
        DbConnectionManager.getInstance().reload();
    }

    private void initService(){
        Intent intent;
        // 启动发送消息Service
        intent = IntentManager.createIntent(getApplicationContext(), MessageSendService.class);
        startService(intent);
    }

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
        return view;
    }
}