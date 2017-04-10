package com.tianyu.seelove.model.entity.network.request;


import com.tianyu.seelove.model.entity.network.request.base.ActionInfo;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class UserLoginActionInfo extends ActionInfo {
    private Long userId;
    private String userName;
    private String password;

    public UserLoginActionInfo(int actionId, Long userId, String userName, String password) {
        super(actionId);
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    public UserLoginActionInfo(int actionId) {
        super(actionId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}