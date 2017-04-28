package com.tianyu.seelove.network.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.common.RequestCode;
import com.tianyu.seelove.common.ResponseConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.model.entity.network.request.UserRegisterLoginActionInfo;
import com.tianyu.seelove.model.entity.network.request.base.RequestInfo;
import com.tianyu.seelove.model.entity.network.response.UserRegisterLoginRspInfo;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;

import org.json.JSONObject;

/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 用户注册请求request
 */
public class UserRegistLoginRequest extends PostJsonRequest {
    private int accountType;// User.accountType

    // 第三方平台快捷登录
    private String dataFromOtherPlatform;

    // 手机号快捷注册登录
    private String phoneNumber;
    private String code;

    public UserRegistLoginRequest(Handler handler, Context context, int accountType, String dataFromOtherPlatform) {
        this.handler = handler;
        this.context = context;
        this.accountType = accountType;
        this.dataFromOtherPlatform = dataFromOtherPlatform;
    }

    public UserRegistLoginRequest(Handler handler, Context context, int accountType, String phoneNumber, String code) {
        this.handler = handler;
        this.context = context;
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    @Override
    protected String getParamsJson() {
        UserRegisterLoginActionInfo actionInfo;
        if (SLUser.ACCOUNT_TYPE_PHONE == accountType) {
            actionInfo = new UserRegisterLoginActionInfo(RequestCode.USER_REGISTER_LOGIN, accountType, phoneNumber, code);
        } else {
            actionInfo = new UserRegisterLoginActionInfo(RequestCode.USER_REGISTER_LOGIN, accountType, dataFromOtherPlatform);
        }
        RequestInfo requestInfo = new RequestInfo(context, actionInfo);
        return GsonUtil.toJson(requestInfo);
    }

    @Override
    protected String getUrl() {
        return WebConstant.BASE_URL;
    }

    @Override
    protected String requestTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected void responseSuccess(JSONObject response) {
        Bundle b = new Bundle();
        Message msg = new Message();
        try {
            LogUtil.i("response success json: [" + requestTag() + "]: " + response.toString());
            UserRegisterLoginRspInfo info = GsonUtil.fromJson(response.toString(), UserRegisterLoginRspInfo.class);
            //响应正常
            if (ResponseConstant.SUCCESS == info.getStatusCode()) {
                b.putSerializable("user", info.getUser());
                msg.what = MessageSignConstant.USER_LOGIN_SUCCESS;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.i(requestTag() + " success");
            }
            //响应失败
            else {
                b.putInt("code", info.getStatusCode());
                b.putString("message", info.getStatusMsg());
                msg.what = MessageSignConstant.USER_LOGIN_FAILURE;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.i(requestTag() + " error, code: " + info.getStatusCode() + " message: " + info.getStatusMsg());
            }
        } catch (Throwable e) {
            handler.sendEmptyMessage(MessageSignConstant.UNKNOWN_ERROR);
            LogUtil.e(requestTag() + " error", e);
        }
    }
}
