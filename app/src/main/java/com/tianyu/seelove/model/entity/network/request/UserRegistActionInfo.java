package com.tianyu.seelove.model.entity.network.request;

import com.tianyu.seelove.model.entity.network.request.base.ActionInfo;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class UserRegistActionInfo extends ActionInfo {
    private String userName;
    private String dataFromOtherPlatform;// 第三方平台返回的用户信息体

    public UserRegistActionInfo(int actionId, String userName, String dataFromOtherPlatform) {
        super(actionId);
        this.userName = userName;
        this.dataFromOtherPlatform = dataFromOtherPlatform;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDataFromOtherPlatform() {
        return dataFromOtherPlatform;
    }

    public void setDataFromOtherPlatform(String dataFromOtherPlatform) {
        this.dataFromOtherPlatform = dataFromOtherPlatform;
    }
}