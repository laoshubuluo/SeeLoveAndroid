package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户年龄枚举类型
 * @date 2017-04-24 16:08
 */
public enum AgeType {
    AGE_18("18", "18岁"),
    AGE_19("18", "19岁"),
    AGE_20("18", "20岁"),
    AGE_21("18", "21岁"),
    AGE_22("18", "22岁"),
    AGE_23("18", "23岁"),
    AGE_24("18", "24岁"),
    AGE_25("18", "25岁"),
    AGE_26("18", "26岁"),
    AGE_27("18", "27岁"),
    AGE_28("18", "28岁"),
    AGE_29("18", "29岁"),
    AGE_30("18", "30岁"),
    AGE_31("18", "31岁"),
    AGE_32("18", "32岁"),
    AGE_33("18", "33岁"),
    AGE_34("18", "34岁"),
    AGE_35("18", "35岁"),
    AGE_36("18", "36岁"),
    AGE_37("18", "37岁"),
    AGE_38("18", "38岁"),
    AGE_39("18", "39岁"),
    AGE_40("18", "40岁"),
    AGE_41("18", "41岁"),
    AGE_42("18", "42岁"),
    AGE_43("18", "43岁"),
    AGE_44("18", "44岁"),
    AGE_45("18", "45岁"),
    AGE_46("18", "46岁"),
    AGE_47("18", "47岁"),
    AGE_48("18", "48岁"),
    AGE_49("18", "49岁"),
    AGE_50("18", "50岁");

    private String resultCode;
    private String resultMsg;

    public static AgeType parse(String resultCode) {
        for (AgeType item : values()) {
            if (item.getResultCode().equals(resultCode))
                return item;
        }
        return null;
    }

    public static AgeType parseByMsg(String resultMsg) {
        for (AgeType item : values()) {
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

    AgeType(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}