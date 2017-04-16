package com.tianyu.seelove.model.entity.network.response;


import com.tianyu.seelove.model.entity.network.response.base.ResponseInfo;
import com.tianyu.seelove.model.entity.user.SLUser;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 响应实体
 */
public class UserLoginRspInfo extends ResponseInfo {
    private SLUser user;

    public SLUser getUser() {
        return user;
    }

    public void setUser(SLUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserLoginRspInfo{" +
                "actionId=" + actionId +
                ", statusCode=" + statusCode +
                ", statusMsg='" + statusMsg + '\'' +
                ", user=" + user.toString() +
                '}';
    }
}