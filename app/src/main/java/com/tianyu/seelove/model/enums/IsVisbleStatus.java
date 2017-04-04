package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 是否可见枚举类型
 * @date 2016-02-19 下午15:43:28
 */
public enum IsVisbleStatus {
    VISBLE(0, "可见"),
    INVISBLE(1, "不可见");

    private int resultCode;
    private String resultMsg;

    public static IsVisbleStatus parse(int resultCode) {
        for (IsVisbleStatus item : values()) {
            if (item.getResultCode() == resultCode)
                return item;
        }
        return null;
    }

    public static IsVisbleStatus parseByMsg(String resultMsg) {
        for (IsVisbleStatus item : values()) {
            if (item.getResultMsg().equals(resultMsg))
                return item;
        }
        return null;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    IsVisbleStatus(int resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}