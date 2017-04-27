package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;

import com.tianyu.seelove.model.entity.user.SLFollow;
import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.FollowFindAllByFollowedUserRequest;
import com.tianyu.seelove.network.request.FollowFindAllByUserRequest;
import com.tianyu.seelove.network.request.FollowRequest;

/**
 * author : L.jinzhu
 * date : 2015/8/24
 * introduce : 用户控制
 */
public class FollowController {
    private Handler handler;
    private Context context;

    public FollowController(Context c, Handler h) {
        this.context = c;
        this.handler = h;
    }

    /**
     * 关注、取消关注
     */
    public void follow(SLFollow follow, int type) {
        FollowRequest request = new FollowRequest(handler, context, follow, type);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }

    /**
     * 我关注的
     */
    public void followFindAllByUser(long userId) {
        FollowFindAllByUserRequest request = new FollowFindAllByUserRequest(handler, context, userId);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }

    /**
     * 关注我的
     */
    public void followFindAllByFollowUser(long userId) {
        FollowFindAllByFollowedUserRequest request = new FollowFindAllByFollowedUserRequest(handler, context, userId);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
