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
import com.tianyu.seelove.model.entity.network.request.VideoDeleteActionInfo;
import com.tianyu.seelove.model.entity.network.request.base.RequestInfo;
import com.tianyu.seelove.model.entity.network.response.VideoCreateRspInfo;
import com.tianyu.seelove.model.entity.network.response.VideoDeleteRspInfo;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 删除视频请求request
 */
public class VideoDeleteRequest extends PostJsonRequest {
    private Long userId;
    private List<Long> videoIdList;

    public VideoDeleteRequest(Handler handler, Context context, Long userId, List<Long> videoIdList) {
        this.handler = handler;
        this.context = context;
        this.userId = userId;
        this.videoIdList = videoIdList;
    }

    @Override
    protected String getParamsJson() {
        VideoDeleteActionInfo actionInfo = new VideoDeleteActionInfo(RequestCode.VIDEO_DELETE, userId, videoIdList);
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
            VideoDeleteRspInfo info = GsonUtil.fromJson(response.toString(), VideoDeleteRspInfo.class);
            //响应正常
            if (ResponseConstant.SUCCESS == info.getStatusCode()) {
                msg.what = MessageSignConstant.VIDEO_DELETE_SUCCESS;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.i(requestTag() + " success");
            }
            //响应失败
            else {
                b.putInt("code", info.getStatusCode());
                b.putString("message", info.getStatusMsg());
                b.putSerializable("videoIdList", (Serializable) info.getVideoIdList());
                msg.what = MessageSignConstant.VIDEO_DELETE_FAILURE;
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
