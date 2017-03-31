package com.tianyu.seelove.ui.activity.video;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tianyu.seelove.R;

/**
 * 个人信息界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class VideoListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        showFullScreen();
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText("我的视频");
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
