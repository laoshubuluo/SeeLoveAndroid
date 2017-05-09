package com.tianyu.seelove.model.entity.network.request;


import com.tianyu.seelove.model.entity.network.request.base.ActionInfo;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class UserFindDetailActionInfo extends ActionInfo {
    private long userId;// 待查询的用户id
    private long currentLoginUserId; // 当前登录的用户id，未登录填写0

    public UserFindDetailActionInfo(int actionId, long userId, long currentLoginUserId) {
        super(actionId);
        this.userId = userId;
        this.currentLoginUserId = currentLoginUserId;
    }

    public void setCurrentLoginUserId(long currentLoginUserId) {
        this.currentLoginUserId = currentLoginUserId;
    }

    public long getCurrentLoginUserId() {
        return currentLoginUserId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}