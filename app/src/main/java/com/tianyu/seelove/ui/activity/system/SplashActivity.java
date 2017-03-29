package com.tianyu.seelove.ui.activity.system;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.tianyu.seelove.R;
import com.tianyu.seelove.ui.activity.base.BaseActivity;

/**
 * splash界面
 * @author shisheng.zhao
 * @date 2017-03-29 12:16
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
