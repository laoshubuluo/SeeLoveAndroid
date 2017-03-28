package com.tianyu.seelove.ui.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 基类activity, 实现一些activity公共方法、开关动画
 * @author shisheng.zhao
 * @date 2017-03-28 16:27
 */
public class BaseActivity extends Activity implements View.OnClickListener {
    public String currentColor = "#000000";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        showFullScreen();
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
            tintManager.setTintColor(Color.parseColor(currentColor));
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

    @Override
    public void onClick(View view) {
    }

    public void setCurrentColor(String currentColor) {
        this.currentColor = currentColor;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
