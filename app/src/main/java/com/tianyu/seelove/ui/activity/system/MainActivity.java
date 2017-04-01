package com.tianyu.seelove.ui.activity.system;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.FragmentTabAdapter;
import com.tianyu.seelove.ui.fragment.FindFragment;
import com.tianyu.seelove.ui.fragment.FollowFragment;
import com.tianyu.seelove.ui.fragment.ManageFragment;
import com.tianyu.seelove.ui.fragment.MessageFragment;
import com.tianyu.seelove.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 主页－统一对fragment进行管理
 * @author shisheng.zhao
 * @date 2017-03-28 16:27
 */
public class MainActivity extends FragmentActivity {
    private RadioGroup radioGroup;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFullScreen();
        fragments.add(new FindFragment());
        fragments.add(new MessageFragment());
        fragments.add(new FollowFragment());
        fragments.add(new ManageFragment());
        radioGroup = (RadioGroup) findViewById(R.id.tabs_radioGroup);
        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, radioGroup);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                LogUtil.d("index = " + index);
            }
        });
    }

    protected void showFullScreen() {
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            FrameLayout view = (FrameLayout) this.findViewById(android.R.id.content);
            View rootView = view.getChildAt(0);
            rootView.setFitsSystemWindows(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // 自定义颜色
            tintManager.setTintColor(Color.parseColor("#f5537a"));
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}