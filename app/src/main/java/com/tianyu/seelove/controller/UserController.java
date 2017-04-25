package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.UserFindAllRequest;
import com.tianyu.seelove.network.request.UserFindDetailRequest;
import com.tianyu.seelove.network.request.UserLoginRequest;
import com.tianyu.seelove.network.request.UserRegistRequest;
import com.tianyu.seelove.network.request.UserUpdateRequest;

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

    /**
     * 获取所有用户
     */
    public void findAll(int ageStart, int ageEnd, String sex, String cityCode) {
        UserFindAllRequest request = new UserFindAllRequest(handler, context, ageStart, ageEnd, sex, cityCode);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }

    /**
     * 更新用户
     */
    public void update(SLUser user) {
        UserUpdateRequest request = new UserUpdateRequest(handler, context, user);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }

    /**
     * 获取用户信息
     */
    public void findDetail(long userId) {
        UserFindDetailRequest request = new UserFindDetailRequest(handler, context, userId);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
