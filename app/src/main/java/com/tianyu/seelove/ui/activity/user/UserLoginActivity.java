package com.tianyu.seelove.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.SecurityCodeController;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import com.tianyu.seelove.wxapi.QQEntryActivity;
import com.tianyu.seelove.wxapi.WXEntryActivity;

/**
 * @author shisheng.zhao
 * @Description: 用户登录界面
 * @date 2017-05-06 09:22
 */
public class UserLoginActivity extends BaseActivity {
    private TextView getCodeBtn;
    private TimeCount time;//倒计时
    private EditText phoneEdit, codeEdit;
    private ImageView qqLogin, wechatLogin;
    private UserController controller;
    private SecurityCodeController codeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentColor("#00000000");
        setContentView(R.layout.activity_user_login);
        controller = new UserController(this, handler);
        codeController = new SecurityCodeController(this, handler);
        initView();
    }

    private void initView() {
        TextView closeBtn = (TextView) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(this);
        getCodeBtn = (TextView) findViewById(R.id.getCodeBtn);
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        codeEdit = (EditText) findViewById(R.id.codeEdit);
        qqLogin = (ImageView) findViewById(R.id.qqLoginBtn);
        wechatLogin = (ImageView) findViewById(R.id.wechatLoginBtn);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        qqLogin.setOnClickListener(this);
        wechatLogin.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        getCodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        String phoneNum = phoneEdit.getText().toString().trim();
        String verifyCode = codeEdit.getText().toString().trim();
        switch (view.getId()) {
            case R.id.closeBtn:
                finish();
                break;
            case R.id.qqLoginBtn: {
                customProgressDialog = new CustomProgressDialog(UserLoginActivity.this, getString(R.string.loading));
                customProgressDialog.show();
                intent = new Intent(UserLoginActivity.this, QQEntryActivity.class);
                startActivityForResult(intent, 0);
                qqLogin.setClickable(false);
                Constant.loginOpenPlatformIng = true;
                break;
            }
            case R.id.wechatLoginBtn: {
                customProgressDialog = new CustomProgressDialog(UserLoginActivity.this, getString(R.string.loading));
                customProgressDialog.show();
                intent = new Intent(UserLoginActivity.this, WXEntryActivity.class);
                startActivityForResult(intent, 0);
                wechatLogin.setClickable(false);
                Constant.loginOpenPlatformIng = true;
                break;
            }
            case R.id.loginBtn: {
                if (StringUtils.isNullOrBlank(phoneNum)) {
                    Toast.makeText(UserLoginActivity.this, R.string.phone_is_null, Toast.LENGTH_LONG).show();
                    break;
                }
                if (StringUtils.isNullOrBlank(verifyCode)) {
                    Toast.makeText(UserLoginActivity.this, R.string.code_is_null, Toast.LENGTH_LONG).show();
                    break;
                }
                customProgressDialog = new CustomProgressDialog(UserLoginActivity.this, getString(R.string.loading));
                customProgressDialog.show();
                controller.login4Phone(SLUser.ACCOUNT_TYPE_PHONE, phoneNum, verifyCode);
                break;
            }
            case R.id.getCodeBtn: {
                if (StringUtils.isNullOrBlank(phoneNum)) {
                    Toast.makeText(UserLoginActivity.this, R.string.phone_is_null, Toast.LENGTH_LONG).show();
                    break;
                }
                codeController.send(phoneNum);
                //开始倒计时
                time = new TimeCount(60000, 1000);
                time.start();
                break;
            }
            default:
                break;
        }
    }

    /**
     * 重新发送倒计时
     */
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            reset();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            getCodeBtn.setClickable(false);
            getCodeBtn.setText(millisUntilFinished / 1000 + "s");
        }

        public void reset() {
            this.cancel();
            getCodeBtn.setText(getString(R.string.get_code));
            getCodeBtn.setClickable(true);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (customProgressDialog != null)
            customProgressDialog.dismiss();
        if (promptDialog == null || promptDialog.isShowing())
            promptDialog = new PromptDialog(UserLoginActivity.this);
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.USER_LOGIN_SUCCESS:
                Toast.makeText(this, R.string.login_success, Toast.LENGTH_LONG).show();
                finish();
                break;
            case MessageSignConstant.USER_LOGIN_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_login_failure), message);
                promptDialog.show();
                break;
            case MessageSignConstant.SERVER_OR_NETWORK_ERROR:
                promptDialog.initData("", msg.getData().getString("message"));
                promptDialog.show();
                break;
            case MessageSignConstant.UNKNOWN_ERROR:
                promptDialog.initData("", getString(R.string.unknown_error));
                promptDialog.show();
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 微信界面无法正常返回resultCode,故采用全局标识方式处理
        if (!Constant.loginOpenPlatformIng) {
            LogUtil.i("login from open platform cancel");
            if (customProgressDialog != null)
                customProgressDialog.dismiss();
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.down_in, R.anim.down_out);
    }
}
