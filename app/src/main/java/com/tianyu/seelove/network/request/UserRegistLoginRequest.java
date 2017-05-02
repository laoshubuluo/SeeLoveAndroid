package com.tianyu.seelove.network.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.common.RequestCode;
import com.tianyu.seelove.common.ResponseConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.VideoDao;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.dao.impl.VideoDaoImpl;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.manager.RongCloudManager;
import com.tianyu.seelove.model.entity.network.request.UserRegisterLoginActionInfo;
import com.tianyu.seelove.model.entity.network.request.base.RequestInfo;
import com.tianyu.seelove.model.entity.network.response.UserRegisterLoginRspInfo;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;
import org.json.JSONObject;

/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 用户注册请求request
 */
public class UserRegistLoginRequest extends PostJsonRequest {
    private UserDao userDao;
    private VideoDao videoDao;
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
        userDao = new UserDaoImpl();
        videoDao = new VideoDaoImpl();
    }

    public UserRegistLoginRequest(Handler handler, Context context, int accountType, String phoneNumber, String code) {
        this.handler = handler;
        this.context = context;
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
        this.code = code;
        userDao = new UserDaoImpl();
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
                b.putSerializable("userDetail", info.getUserDetail());
                saveDataAfterLoginSuccess(info.getUserDetail());
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

    private void saveDataAfterLoginSuccess(SLUserDetail slUserDetail) {
        // 初始化用户信息
        AppUtils.getInstance().setUserId(slUserDetail.getUser().getUserId());
        AppUtils.getInstance().setUserToken(slUserDetail.getUser().getToken4RongCloud());
        // 数据库指向用户自己的数据库
        DbConnectionManager.getInstance().reload();
        userDao.addUser(slUserDetail.getUser());
        if (null != slUserDetail.getVideoList() && slUserDetail.getVideoList().size() > 0) {
            for (SLVideo slVideo : slUserDetail.getVideoList()) {
                videoDao.addVideo(slVideo);
            }
        }
        // 链接融云服务器
        String token = AppUtils.getInstance().getUserToken(); // 当前用户token
        RongCloudManager.getInstance().connect(token);
    }
}
