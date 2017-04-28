package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;

import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.SecurityCodeSendRequest;
import com.tianyu.seelove.network.request.UserFindAllRequest;
import com.tianyu.seelove.network.request.UserFindDetailRequest;
import com.tianyu.seelove.network.request.UserRegistLoginRequest;
import com.tianyu.seelove.network.request.UserUpdateRequest;

/**
 * author : L.jinzhu
 * date : 2015/8/24
 * introduce : 验证码控制
 */
public class SecurityCodeController {
    private Handler handler;
    private Context context;

    public SecurityCodeController(Context c, Handler h) {
        this.context = c;
        this.handler = h;
    }

    /**
     * 用户登录
     */
    public void send(String phoneNumber) {
        SecurityCodeSendRequest request = new SecurityCodeSendRequest(handler, context, phoneNumber, "1");
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
