package com.tianyu.seelove.network.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.common.RequestCode;
import com.tianyu.seelove.common.ResponseConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.model.entity.network.request.UserCreateActionInfo;
import com.tianyu.seelove.model.entity.network.request.base.RequestInfo;
import com.tianyu.seelove.model.entity.network.response.UserCreateRspInfo;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;

import org.json.JSONObject;

/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 用户创建请求request
 */
public class UserCreateRequest extends PostJsonRequest {
    private String userName;
    private String dataFromOtherPlatform;// 第三方平台返回的用户信息体

    public UserCreateRequest(Handler handler, Context context, String userName, String dataFromOtherPlatform) {
        this.handler = handler;
        this.context = context;
        this.userName = userName;
        this.dataFromOtherPlatform = dataFromOtherPlatform;
    }

    @Override
    protected String getParamsJson() {
        UserCreateActionInfo actionInfo = new UserCreateActionInfo(RequestCode.USER_CREATE, userName, dataFromOtherPlatform);
        RequestInfo r = new RequestInfo(context, actionInfo);
        return GsonUtil.toJson(r);
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
            UserCreateRspInfo info = GsonUtil.fromJson(response.toString(), UserCreateRspInfo.class);
            //响应正常
            if (ResponseConstant.SUCCESS == info.getStatusCode()) {
                b.putSerializable("user", info.getUser());
                msg.what = MessageSignConstant.USER_CREATE_SUCCESS;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.i(requestTag() + " success");
            }
            //响应失败
            else {
                b.putInt("code", info.getStatusCode());
                b.putString("message", info.getStatusMsg());
                msg.what = MessageSignConstant.USER_CREATE_FAILURE;
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
