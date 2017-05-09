package com.tianyu.seelove.network.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.common.RequestCode;
import com.tianyu.seelove.common.ResponseConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.model.entity.network.request.NewsFindAllActionInfo;
import com.tianyu.seelove.model.entity.network.request.base.RequestInfo;
import com.tianyu.seelove.model.entity.network.response.NewsFindAllRspInfo;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 获取所有动态请求request
 */
public class NewsFindAllRequest extends PostJsonRequest {
    private int pageNumber = 0;
    private int dataGetType = 0;
    private long userId;

    public NewsFindAllRequest(Handler handler, Context context, int pageNumber, int dataGetType, long userId) {
        this.handler = handler;
        this.context = context;
        this.pageNumber = pageNumber;
        this.dataGetType = dataGetType;
        this.userId = userId;
    }

    @Override
    protected String getParamsJson() {
        NewsFindAllActionInfo actionInfo = new NewsFindAllActionInfo(RequestCode.NEWS_FIND_ALL, pageNumber, dataGetType, userId);
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
            NewsFindAllRspInfo info = GsonUtil.fromJson(response.toString(), NewsFindAllRspInfo.class);
            //响应正常
            if (ResponseConstant.SUCCESS == info.getStatusCode()) {
                b.putInt("currentPage", info.getCurrentPage());
                b.putInt("isEndPage", info.getIsEndPage());
                b.putSerializable("userList", (Serializable) info.getUserDetailList());
                msg.what = MessageSignConstant.NEWS_FIND_ALL_SUCCESS;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.i(requestTag() + " success");
            }
            //响应失败
            else {
                b.putInt("code", info.getStatusCode());
                b.putString("message", info.getStatusMsg());
                msg.what = MessageSignConstant.NEWS_FIND_ALL_FAILURE;
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
