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
import com.tianyu.seelove.model.entity.network.request.UserFindDetailActionInfo;
import com.tianyu.seelove.model.entity.network.request.base.RequestInfo;
import com.tianyu.seelove.model.entity.network.response.UserFindDetailRspInfo;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;
import org.json.JSONObject;

/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 获取用户请求request
 */
public class UserFindDetailRequest extends PostJsonRequest {
    private long userId = 0;
    private UserDao userDao;
    private VideoDao videoDao;

    public UserFindDetailRequest(Handler handler, Context context, long userId) {
        this.handler = handler;
        this.context = context;
        this.userId = userId;
        userDao = new UserDaoImpl();
        videoDao = new VideoDaoImpl();
    }

    @Override
    protected String getParamsJson() {
        UserFindDetailActionInfo actionInfo = new UserFindDetailActionInfo(RequestCode.USER_FIND_DETAIL, userId);
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
            UserFindDetailRspInfo info = GsonUtil.fromJson(response.toString(), UserFindDetailRspInfo.class);
            //响应正常
            if (ResponseConstant.SUCCESS == info.getStatusCode()) {
                userDao.addUser(info.getUserDetail().getUser());
                if (null != info.getUserDetail().getVideoList() && info.getUserDetail().getVideoList().size() > 0) {
                    for (SLVideo slVideo : info.getUserDetail().getVideoList()) {
                        slVideo.setUserId(userId);
                        videoDao.addVideo(slVideo);
                    }
                }
                b.putSerializable("userDetail", info.getUserDetail());
                msg.what = MessageSignConstant.USER_FIND_DETAIL_SUCCESS;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.i(requestTag() + " success");
            }
            //响应失败
            else {
                b.putInt("code", info.getStatusCode());
                b.putString("message", info.getStatusMsg());
                msg.what = MessageSignConstant.USER_FIND_DETAIL_FAILURE;
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
