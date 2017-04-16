package com.tianyu.seelove.model.entity.network.response;

import com.tianyu.seelove.model.entity.network.response.base.ResponseInfo;
import com.tianyu.seelove.model.entity.user.SLUser;

import java.util.List;


/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 响应实体
 */
public class FollowFindAllRspInfo extends ResponseInfo {
    List<SLUser> userList;

    public List<SLUser> getUserList() {
        return userList;
    }

    public void setUserList(List<SLUser> userList) {
        this.userList = userList;
    }
}