package com.tianyu.seelove.model.entity.network.request;


import com.tianyu.seelove.model.entity.network.request.base.ActionInfo;
import com.tianyu.seelove.model.entity.user.SLUser;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class UserUpdateActionInfo extends ActionInfo {
    private SLUser user;

    public UserUpdateActionInfo(int actionId, SLUser user) {
        super(actionId);
        this.user = user;
    }

    public SLUser getUser() {
        return user;
    }

    public void setUser(SLUser user) {
        this.user = user;
    }
}