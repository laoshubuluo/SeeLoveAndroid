package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户婚姻状态枚举类型
 * @date 2017-04-24 17:05
 */
public enum MarraryType {
    UNMARRARY("0", "未婚"),
    DIVORCE("1", "离异");

    private String resultCode;
    private String resultMsg;

    public static MarraryType parse(String resultCode) {
        for (MarraryType item : values()) {
            if (item.getResultCode().equals(resultCode))
                return item;
        }
        return null;
    }

    public static MarraryType parseByMsg(String resultMsg) {
        for (MarraryType item : values()) {
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

    MarraryType(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}