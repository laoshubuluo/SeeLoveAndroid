package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;

import com.tianyu.seelove.model.enums.DataGetType;
import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.FriendSearchRequest;


/**
 * author : L.jinzhu
 * date : 2015/8/24
 * introduce : 好友控制器
 */
public class FriendController {

    private Handler handler;
    private Context context;

    public FriendController(Context c, Handler h) {
        this.context = c;
        this.handler = h;
    }

    /**
     * 好友搜索
     */
    public void search(String keyWord, int totalPage, int currentPage, DataGetType dataGetType) {
        FriendSearchRequest request = new FriendSearchRequest(handler, context, keyWord, totalPage, currentPage, dataGetType);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
