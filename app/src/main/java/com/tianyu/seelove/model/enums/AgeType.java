package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户年龄枚举类型
 * @date 2017-04-24 16:08
 */
public enum AgeType {
    AGE_18("18", "18岁"),
    AGE_19("19", "19岁"),
    AGE_20("20", "20岁"),
    AGE_21("21", "21岁"),
    AGE_22("22", "22岁"),
    AGE_23("23", "23岁"),
    AGE_24("24", "24岁"),
    AGE_25("25", "25岁"),
    AGE_26("26", "26岁"),
    AGE_27("27", "27岁"),
    AGE_28("28", "28岁"),
    AGE_29("29", "29岁"),
    AGE_30("30", "30岁"),
    AGE_31("31", "31岁"),
    AGE_32("32", "32岁"),
    AGE_33("33", "33岁"),
    AGE_34("34", "34岁"),
    AGE_35("35", "35岁"),
    AGE_36("36", "36岁"),
    AGE_37("37", "37岁"),
    AGE_38("38", "38岁"),
    AGE_39("39", "39岁"),
    AGE_40("40", "40岁"),
    AGE_41("41", "41岁"),
    AGE_42("42", "42岁"),
    AGE_43("43", "43岁"),
    AGE_44("44", "44岁"),
    AGE_45("45", "45岁"),
    AGE_46("46", "46岁"),
    AGE_47("47", "47岁"),
    AGE_48("48", "48岁"),
    AGE_49("49", "49岁"),
    AGE_50("50", "50岁");

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