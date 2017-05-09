package com.tianyu.seelove.ui.activity.system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.tianyu.seelove.R;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.service.MessageSendService;
import com.tianyu.seelove.ui.activity.base.BaseActivity;

public class TranslateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent;
        // 启动发送消息Service
        intent = IntentManager.createIntent(getApplicationContext(), MessageSendService.class);
        startService(intent);
        finish();
    }
}