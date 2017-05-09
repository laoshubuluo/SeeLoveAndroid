package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;

import com.tianyu.seelove.model.entity.network.request.TokenFromWeiXinRequest;
import com.tianyu.seelove.model.entity.network.request.UserInfoFromWeiXinRequest;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.UserFindAllRequest;
import com.tianyu.seelove.network.request.UserFindDetailRequest;
import com.tianyu.seelove.network.request.UserRegistLoginRequest;
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
     * 用户登录
     */
    public void login4Platform(int accountType, String openId, String dataFromOtherPlatform) {
        UserRegistLoginRequest request = new UserRegistLoginRequest(handler, context, openId, accountType, dataFromOtherPlatform);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }

    /**
     * 用户登录
     */
    public void login4Phone(int accountType, String phoneNumber, String code) {
        UserRegistLoginRequest request = new UserRegistLoginRequest(handler, context, accountType, phoneNumber, code);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }

    /**
     * 获取所有用户
     */
    public void findAll(int pageNumber, int dataGetType, int ageStart, int ageEnd, String sex, String cityCode) {
        UserFindAllRequest request = new UserFindAllRequest(handler, context, pageNumber, dataGetType, ageStart, ageEnd, sex, cityCode);
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

    /**
     * 微信开放平台登录步骤二：通过code获取token
     */
    public void getTokenByCodeFromWeiXin(String code) {
        TokenFromWeiXinRequest request = new TokenFromWeiXinRequest(handler, context, code);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }

    /**
     * 微信开放平台登录步骤三：通过token获取用户信息
     */
    public void getUserInfoByTokenFromWeiXin(String accessToken, String openId) {
        UserInfoFromWeiXinRequest request = new UserInfoFromWeiXinRequest(handler, context, accessToken, openId);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
