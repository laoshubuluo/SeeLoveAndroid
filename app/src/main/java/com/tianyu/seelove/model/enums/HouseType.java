package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户住房情况枚举类型
 * @date 2017-04-24 16:08
 */
public enum HouseType {
    HOUSE_UNKNOW("0", "未知"),
    HOUSE_NO("1", "未购房"),
    HOUSE_OK("2", "已购房"),
    HOUSE_PARENT("3", "父母合住");

    private String resultCode;
    private String resultMsg;

    public static HouseType parse(String resultCode) {
        for (HouseType item : values()) {
            if (item.getResultCode().equals(resultCode))
                return item;
        }
        return null;
    }

    public static HouseType parseByMsg(String resultMsg) {
        for (HouseType item : values()) {
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

    HouseType(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}