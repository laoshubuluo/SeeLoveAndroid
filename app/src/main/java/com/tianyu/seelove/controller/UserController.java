package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;

import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.UserCreateRequest;


/**
 * author : L.jinzhu
 * date : 2015/8/24
 * introduce : 好友控制器
 */
public class UserController {

    private Handler handler;
    private Context context;

    public UserController(Context c, Handler h) {
        this.context = c;
        this.handler = h;
    }

    /**
     * 好友搜索
     */
    public void create(String userName, String dataFromOtherPlatform) {
        UserCreateRequest request = new UserCreateRequest(handler, context, userName, dataFromOtherPlatform);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
