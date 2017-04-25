package com.tianyu.seelove.network.request;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.common.RequestCode;
import com.tianyu.seelove.common.ResponseConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.manager.RongCloudManager;
import com.tianyu.seelove.model.entity.network.request.UserLoginActionInfo;
import com.tianyu.seelove.model.entity.network.request.base.RequestInfo;
import com.tianyu.seelove.model.entity.network.response.UserLoginRspInfo;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.service.MessageSendService;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;
import org.json.JSONObject;

/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 用户创建请求request
 */
public class UserLoginRequest extends PostJsonRequest {
    private UserDao userDao;
    private Long userId;
    private String userName;
    private String password;

    public UserLoginRequest(Handler handler, Context context, Long userId, String userName, String password) {
        this.handler = handler;
        this.context = context;
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        userDao = new UserDaoImpl();
    }

    @Override
    protected String getParamsJson() {
        UserLoginActionInfo actionInfo = new UserLoginActionInfo(RequestCode.USER_LOGIN, userId, userName, password);
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
            UserLoginRspInfo info = GsonUtil.fromJson(response.toString(), UserLoginRspInfo.class);
            //响应正常
            if (ResponseConstant.SUCCESS == info.getStatusCode()) {
                b.putSerializable("user", info.getUser());
                saveDataAfterLoginSuccess(info.getUser());
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

    private void saveDataAfterLoginSuccess(SLUser slUser){
        // 初始化用户信息
        AppUtils.getInstance().setUserId(slUser.getUserId());
        AppUtils.getInstance().setUserToken(slUser.getToken4RongCloud());
        // 数据库指向用户自己的数据库
        DbConnectionManager.getInstance().reload();
        userDao.addUser(slUser);
        // 链接融云服务器
        String token = AppUtils.getInstance().getUserToken(); // 当前用户token
        RongCloudManager.getInstance().connect(token);
    }
}
