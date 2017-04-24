package com.tianyu.seelove.network.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.common.RequestCode;
import com.tianyu.seelove.common.ResponseConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.dao.VideoDao;
import com.tianyu.seelove.dao.impl.VideoDaoImpl;
import com.tianyu.seelove.model.entity.network.request.VideoCreateActionInfo;
import com.tianyu.seelove.model.entity.network.request.base.RequestInfo;
import com.tianyu.seelove.model.entity.network.response.VideoCreateRspInfo;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;

import org.json.JSONObject;

/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 获取所有用户请求request
 */
public class VideoCreateRequest extends PostJsonRequest {
    private VideoDao videoDao;
    private SLVideo video;

    public VideoCreateRequest(Handler handler, Context context, SLVideo video) {
        this.handler = handler;
        this.context = context;
        this.video = video;
        videoDao = new VideoDaoImpl();
    }

    @Override
    protected String getParamsJson() {
        VideoCreateActionInfo actionInfo = new VideoCreateActionInfo(RequestCode.VIDEO_CREATE, video);
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
            VideoCreateRspInfo info = GsonUtil.fromJson(response.toString(), VideoCreateRspInfo.class);
            //响应正常
            if (ResponseConstant.SUCCESS == info.getStatusCode()) {
                videoDao.addVideo(video);
                msg.what = MessageSignConstant.VIDEO_CREATE_SUCCESS;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.i(requestTag() + " success");
            }
            //响应失败
            else {
                b.putInt("code", info.getStatusCode());
                b.putString("message", info.getStatusMsg());
                msg.what = MessageSignConstant.VIDEO_CREATE_FAILURE;
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
