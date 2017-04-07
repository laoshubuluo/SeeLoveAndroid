package com.tianyu.seelove.network.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.common.ResponseConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.model.entity.network.request.FriendSearchActionInfo;
import com.tianyu.seelove.model.entity.network.request.base.RequestInfo;
import com.tianyu.seelove.model.entity.network.response.FriendSearchInfo;
import com.tianyu.seelove.model.enums.DataGetType;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;

import org.json.JSONObject;

/**
 * author : L.jinzhu
 * date : 2015/9/11
 * introduce : 好友搜索请求request
 */
public class FriendSearchRequest extends PostJsonRequest {
    private String keyWord;
    private DataGetType dataGetType;// 数据获取类型
    private int totalPage;
    private int currentPage;
    private String pageType;

    public FriendSearchRequest(Handler handler, Context context, String keyWord, int totalPage, int currentPage, DataGetType dataGetType) {
        this.handler = handler;
        this.context = context;
        this.keyWord = keyWord;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.dataGetType = dataGetType;
        if (dataGetType.equals(DataGetType.UPDATE)) {
            this.totalPage = 0;
            this.currentPage = 0;
            this.pageType = DataGetType.PAGE_DOWN.getType();
        } else
            this.pageType = dataGetType.getType();
    }

    @Override
    protected String getParamsJson() {
        FriendSearchActionInfo actionInfo = new FriendSearchActionInfo(27, keyWord, totalPage, currentPage, pageType);
        RequestInfo r = new RequestInfo(context, actionInfo);
        return GsonUtil.toJson(r);
    }

    @Override
    protected String getUrl() {
        return WebConstant.BASE_URL;
    }

    @Override
    protected String requestTag() {
        return "friendSearch";
    }

    @Override
    protected void responseSuccess(JSONObject response) {
        Bundle b = new Bundle();
        Message msg = new Message();
        try {
            LogUtil.i("response success json: [" + requestTag() + "]: " + response.toString());
            FriendSearchInfo info = GsonUtil.fromJson(response.toString(), FriendSearchInfo.class);
            //响应正常
            if (ResponseConstant.SUCCESS == info.getCode()) {
                b.putInt("totalPage", info.getTotalPage());
                b.putInt("currentPage", info.getCurrentPage());
                b.putString("dataGetType", dataGetType == null ? "" : dataGetType.getType());
                msg.what = MessageSignConstant.FRIEND_SEARCH_SUCCESS;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.i("friend search success");
            }
            //响应失败
            else {
                b.putInt("code", info.getCode());
                b.putString("message", info.getMessage());
                msg.what = MessageSignConstant.FRIEND_SEARCH_FAILURE;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.e("friend search failure: code: " + info.getCode() + ",message: " + info.getMessage());
            }
        } catch (Throwable e) {
            handler.sendEmptyMessage(MessageSignConstant.UNKNOWN_ERROR);
            LogUtil.e("friend search error", e);
        }
    }
}
