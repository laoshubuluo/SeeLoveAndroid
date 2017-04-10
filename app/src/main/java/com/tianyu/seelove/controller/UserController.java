package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;

import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.UserCreateRequest;
import com.tianyu.seelove.network.request.UserLoginRequest;


/**
 * author : L.jinzhu
 * date : 2015/8/24
 * introduce : 用户控制
 */
public class UserController {

    private Handler handler;
    private Context context;

    public UserController(Context c, Handler h) {
        this.context = c;
        this.handler = h;
    }

    /**
     * 创建
     */
    public void create(String userName, String dataFromOtherPlatform) {
        UserCreateRequest request = new UserCreateRequest(handler, context, userName, dataFromOtherPlatform);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }

    /**
     * 登录
     */
    public void login(Long userId, String userName, String password) {
        UserLoginRequest request = new UserLoginRequest(handler, context, userId, userName, password);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
