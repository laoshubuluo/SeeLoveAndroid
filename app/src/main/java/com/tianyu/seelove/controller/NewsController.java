package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;

import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.NewsFindAllRequest;

/**
 * author : L.jinzhu
 * date : 2015/8/24
 * introduce : 动态控制
 */
public class NewsController {
    private Handler handler;
    private Context context;

    public NewsController(Context c, Handler h) {
        this.context = c;
        this.handler = h;
    }

    /**
     * 获取所有动态
     *
     * @param userId
     */
    public void findAll(long userId) {
        NewsFindAllRequest request = new NewsFindAllRequest(handler, context, userId);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
