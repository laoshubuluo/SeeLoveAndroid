package com.tianyu.seelove.ui.activity.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 基类activity, 实现一些activity公共方法、开关动画
 * @author shisheng.zhao
 * @date 2017-03-28 16:27
 */
public class BaseActivity extends FragmentActivity implements Handler.Callback, View.OnClickListener {
    public String currentColor = "#f5537a";
    public Handler handler;
    public CustomProgressDialog customProgressDialog;
    public PromptDialog promptDialog;
    public ImageLoader imageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDisplay();
        handler = new Handler(this);
        promptDialog = new PromptDialog(BaseActivity.this);
        imageLoader = ImageLoader.getInstance();
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
    public boolean handleMessage(Message message) {
        return false;
    }

    public void getDisplay() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Constant.screenWidth = display.getWidth();
        Constant.screenHeight = display.getHeight();
    }

    @Override
    protected void onDestroy() {
        if (null != customProgressDialog) {
            customProgressDialog.dismiss();
        }
        if (null != promptDialog) {
            promptDialog.dismiss();
        }
        super.onDestroy();
    }

    private AlertDialog progressDialog;

    public TextView showProgressDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = View.inflate(this, R.layout.dialog_loading, null);
        builder.setView(view);
        ProgressBar pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        TextView tv_hint = (TextView) view.findViewById(R.id.tv_hint);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            pb_loading.setIndeterminateTintList(ContextCompat.getColorStateList(this, R.color.dialog_pro_color));
//        }
        tv_hint.setText("视频编译中");
        progressDialog = builder.create();
        progressDialog.show();

        return tv_hint;
    }

    public void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
