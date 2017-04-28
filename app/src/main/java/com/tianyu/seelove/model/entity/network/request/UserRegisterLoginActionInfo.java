package com.tianyu.seelove.model.entity.network.request;


import com.tianyu.seelove.model.entity.network.request.base.ActionInfo;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class UserRegisterLoginActionInfo extends ActionInfo {
    private int accountType;// User.accountType

    // 第三方平台快捷登录
    private String dataFromOtherPlatform;//

    // 手机号快捷注册登录
    private String phoneNumber;
    private String code;

    public UserRegisterLoginActionInfo(int actionId, int accountType, String dataFromOtherPlatform) {
        super(actionId);
        this.accountType = accountType;
        this.dataFromOtherPlatform = dataFromOtherPlatform;
    }

    public UserRegisterLoginActionInfo(int actionId, int accountType, String phoneNumber, String code) {
        super(actionId);
        this.accountType = accountType;
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getDataFromOtherPlatform() {
        return dataFromOtherPlatform;
    }

    public void setDataFromOtherPlatform(String dataFromOtherPlatform) {
        this.dataFromOtherPlatform = dataFromOtherPlatform;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
