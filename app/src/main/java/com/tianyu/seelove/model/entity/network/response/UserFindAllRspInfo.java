package com.tianyu.seelove.model.entity.network.response;

import com.tianyu.seelove.model.entity.network.response.base.ResponseInfo;
import com.tianyu.seelove.model.entity.user.SLUserDetail;

import java.util.List;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 响应实体
 */
public class UserFindAllRspInfo extends ResponseInfo {
    private List<SLUserDetail> userDetailList;

    public List<SLUserDetail> getUserDetailList() {
        return userDetailList;
    }

    public void setUserDetailList(List<SLUserDetail> userDetailList) {
        this.userDetailList = userDetailList;
    }
}