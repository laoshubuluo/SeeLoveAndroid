package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;
import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.UserRegistRequest;
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
     * 用户注册
     * @param userName
     * @param dataFromOtherPlatform
     */
    public void regist(String userName, String dataFromOtherPlatform) {
        UserRegistRequest request = new UserRegistRequest(handler, context, userName, dataFromOtherPlatform);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }

    /**
     * 用户登录
     * @param userId
     * @param userName
     * @param password
     */
    public void login(Long userId, String userName, String password) {
        UserLoginRequest request = new UserLoginRequest(handler, context, userId, userName, password);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
