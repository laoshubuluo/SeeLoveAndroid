package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户性别枚举类型
 * @date 2017-04-02 08:55
 */
public enum SexType {
    SEX_BOY("1", "男"),
    SEX_GIRL("2", "女"),
    SEX_UNKNOW("0", "未知");

    private String resultCode;
    private String resultMsg;

    public static SexType parse(String resultCode) {
        for (SexType item : values()) {
            if (item.getResultCode().equals(resultCode))
                return item;
        }
        return null;
    }

    public static SexType parseByMsg(String resultMsg) {
        for (SexType item : values()) {
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

    SexType(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}