package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;

import com.tianyu.seelove.model.entity.user.SLFollow;
import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.FollowFindAllByFollowedUserRequest;
import com.tianyu.seelove.network.request.FollowFindAllByUserRequest;
import com.tianyu.seelove.network.request.FollowRequest;
import com.tianyu.seelove.network.request.NewVersionRequest;

/**
 * author : L.jinzhu
 * date : 2015/8/24
 * introduce : 用户控制
 */
public class SystemController {
    private Handler handler;
    private Context context;

    public SystemController(Context c, Handler h) {
        this.context = c;
        this.handler = h;
    }

    /**
     * 新版本
     */
    public void getNewVerison() {
        NewVersionRequest request = new NewVersionRequest(handler, context);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
