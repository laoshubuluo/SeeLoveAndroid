package com.tianyu.seelove.model.entity.network.response;

import com.tianyu.seelove.model.entity.network.response.base.ResponseInfo;
import com.tianyu.seelove.model.entity.user.SLUserDetail;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 响应实体
 */
public class UserFindDetailRspInfo extends ResponseInfo {
    private SLUserDetail userDetail;

    public SLUserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(SLUserDetail userDetail) {
        this.userDetail = userDetail;
    }
}