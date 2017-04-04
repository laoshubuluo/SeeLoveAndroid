package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户性别枚举类型
 * @date 2017-04-02 08:55
 */
public enum AccountType {
    QQ("1", "QQ"),
    WECHAT("2", "微信"),
    UNKNOW("0", "未知");

    private String resultCode;
    private String resultMsg;

    public static AccountType parse(String resultCode) {
        for (AccountType item : values()) {
            if (item.getResultCode().equals(resultCode))
                return item;
        }
        return null;
    }

    public static AccountType parseByMsg(String resultMsg) {
        for (AccountType item : values()) {
            if (item.getResultMsg().equals(resultMsg))
                return item;
        }
        return null;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    AccountType(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}