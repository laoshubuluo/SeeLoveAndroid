package com.tianyu.seelove.ui.activity.system;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.manager.RongCloudManager;
import com.tianyu.seelove.service.MessageSendService;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.view.dialog.SureDialog;

/**
 * 设置界面
 * @author shisheng.zhao
 * @date 2017-04-20 11:26
 */
public class SettingActivity extends BaseActivity {
    private SureDialog sureDialog;
    private TextView exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
    }

    private void initView() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(R.string.setting);
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        backView.setVisibility(View.VISIBLE);
        exitBtn = (TextView) findViewById(R.id.exitLogin);
        exitBtn.setOnClickListener(this);
        backView.setOnClickListener(this);
    }

    private void initData() {
        if (AppUtils.getInstance().getUserId() != 0) {
            exitBtn.setVisibility(View.VISIBLE);
        } else {
            exitBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.leftBtn:
                finish();
                break;
            case R.id.exitLogin:
                if (null != sureDialog) {
                    sureDialog.dismiss();
                }
                sureDialog = new SureDialog(SettingActivity.this);
                sureDialog.initData("", getString(R.string.exit_login_tips));
                sureDialog.getSureTV().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitLogin();
                        sureDialog.dismiss();
                        finish();
                    }
                });
                sureDialog.show();
                break;
        }
    }

    private void exitLogin() {
        // 注销通知栏通知
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            notificationManager.cancelAll();
        } catch (Exception ex) {
        }
        // Stop sendMessageService
        Intent intent_message = IntentManager.createIntent(this, MessageSendService.class);
        stopService(intent_message);
        // 断开融云不接收push消息的正确流程应该是先disconnect然后在logOut
        RongCloudManager.getInstance().disconnect();
        RongCloudManager.getInstance().logOut();
        //数据库重定向
        DbConnectionManager.getInstance().reset();
        //userId和token清空
        AppUtils.getInstance().setUserId(0l);
        AppUtils.getInstance().setUserToken("");
        AppUtils.getInstance().setStartAge(18);
        AppUtils.getInstance().setEndAge(50);
        AppUtils.getInstance().setSexCode("0");
        AppUtils.getInstance().setCityCode("00");
        AppUtils.getInstance().reset();
        // 发送广播 通知消息,动态界面已退出登录,引导用户进行登录
        sendBroadcast(new Intent(Actions.ACTION_EXIT_APP));
    }
}
