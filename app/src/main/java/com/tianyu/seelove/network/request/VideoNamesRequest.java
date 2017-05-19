package com.tianyu.seelove.network.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.common.RequestCode;
import com.tianyu.seelove.common.ResponseConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.model.entity.network.request.NewVersionActionInfo;
import com.tianyu.seelove.model.entity.network.request.VideoNamesActionInfo;
import com.tianyu.seelove.model.entity.network.request.base.RequestInfo;
import com.tianyu.seelove.model.entity.network.response.NewVersionRspInfo;
import com.tianyu.seelove.model.entity.network.response.VideoNamesRspInfo;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;

import org.json.JSONObject;


/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 请求request
 */
public class VideoNamesRequest extends PostJsonRequest {

    public VideoNamesRequest(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    protected String getParamsJson() {
        VideoNamesActionInfo actionInfo = new VideoNamesActionInfo(RequestCode.SYSTEM_VIDEO_NAMES);
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
            VideoNamesRspInfo info = GsonUtil.fromJson(response.toString(), VideoNamesRspInfo.class);
            //响应正常
            if (ResponseConstant.SUCCESS == info.getStatusCode()) {
                if (StringUtils.isNotBlank(info.getVideoNames())) {
                    Constant.videoNames = info.getVideoNames().split(",");
                }
                LogUtil.i(requestTag() + " success");
            }
            //响应失败
            else {
                LogUtil.i(requestTag() + " error, code: " + info.getStatusCode() + " message: " + info.getStatusMsg());
            }
        } catch (Throwable e) {
            LogUtil.e(requestTag() + " error", e);
        }
    }
}
