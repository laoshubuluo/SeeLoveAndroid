package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户性别枚举类型
 * @date 2017-04-02 08:55
 */
public enum AccountType {
    WECHAT(1, "微信"),
    QQ(2, "QQ"),
    PHONE(3, "手机号"),
    UNKNOW(0, "未知");

    private int code;
    private String msg;

    public static AccountType parse(int resultCode) {
        for (AccountType item : values()) {
            if (item.getCode() == resultCode)
                return item;
        }
        return null;
    }

    public static AccountType parseByMsg(String resultMsg) {
        for (AccountType item : values()) {
            if (item.getMsg().equals(resultMsg))
                return item;
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    AccountType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}