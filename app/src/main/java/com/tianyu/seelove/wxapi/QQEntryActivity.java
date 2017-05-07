package com.tianyu.seelove.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.model.enums.AccountType;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.LogUtil;

import org.json.JSONObject;

/**
 * @author shisheng.zhao
 * @Description: QQ登录
 * @date 2017-05-05 19:38
 */
public class QQEntryActivity extends BaseActivity {
    private static Tencent mTencent;
    private UserInfo mInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        //注册到QQ
        regist2QQ();
        qqLogin();
    }

    private void regist2QQ() {
        mTencent = Tencent.createInstance(Constant.QQ_APP_ID, this.getApplicationContext());
    }

    /**
     * QQ登录
     */
    private void qqLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            LogUtil.i("call qq open platform to login: login now");
        } else {
            mTencent.logout(this);
            mTencent.login(this, "all", loginListener);
            LogUtil.i("call qq open platform to login: logout and login");
            return;
        }
    }

    // 第三方应用发送到QQ的请求处理后的响应结果
    IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            LogUtil.i("call qq open platform to login: success");
            initOpenIdAndToken(o);
            getUserInfo();
        }

        @Override
        public void onError(UiError e) {
            LogUtil.i("call qq open platform to login: errorCode: " + e.errorCode + " errorMessage: " + e.errorMessage);
            promptDialog.initData(getString(R.string.qq_code_error), e.errorMessage);
            promptDialog.show();
            loginCancleAndFinish();
        }

        @Override
        public void onCancel() {
            LogUtil.i("call qq open platform to login: cancel");
            loginCancleAndFinish();
        }
    };

    /**
     * 初始化openId和token
     */
    private void initOpenIdAndToken(Object object) {
        try {
            JSONObject jsonObject = (JSONObject) object;
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
            LogUtil.e(this.getClass().getName(), "call qq open platform to login: read token|openId error");
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(getUserInfoListener);
        }
    }

    // 第三方应用发送到QQ的请求处理后的响应结果
    IUiListener getUserInfoListener = new IUiListener() {
        @Override
        public void onComplete(final Object response) {
            LogUtil.i("call qq open platform to getUserInfo: success");
            // QQ登录
            UserController controller = new UserController(getApplication(), Constant.loginHandler);
            controller.login4Platform(AccountType.QQ.getCode(), mTencent.getOpenId(), response.toString());
            finish();
        }

        @Override
        public void onCancel() {
            LogUtil.i("call qq open platform to getUserInfo: cancel");
            loginCancleAndFinish();
        }

        @Override
        public void onError(UiError e) {
            LogUtil.i("call qq open platform to getUserInfo: errorCode: " + e.errorCode + " errorMessage: " + e.errorMessage);
            promptDialog.initData(getString(R.string.qq_code_error), "errorCode: " + e.errorCode + " errorMessage: " + e.errorMessage);
            promptDialog.show();
            loginCancleAndFinish();
        }
    };

    // 应用调用Andriod_SDK接口时，如果要成功接收到回调，需要增加此方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_LOGIN:
                mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 通知上层界面（登录界面），因错误或主动操作，第三方登录流程退出
     */
    private void loginCancleAndFinish() {
        Constant.loginOpenPlatformIng = false;
        finish();
    }
}