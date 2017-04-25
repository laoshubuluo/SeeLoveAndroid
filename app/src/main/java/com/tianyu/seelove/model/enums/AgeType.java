package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户年龄枚举类型
 * @date 2017-04-24 16:08
 */
public enum AgeType {
    AGE_18("0", "18岁"),
    AGE_19("1", "19岁"),
    AGE_20("2", "20岁"),
    AGE_21("3", "21岁"),
    AGE_22("4", "22岁"),
    AGE_23("5", "23岁"),
    AGE_24("6", "24岁"),
    AGE_25("7", "25岁"),
    AGE_26("8", "26岁"),
    AGE_27("9", "27岁"),
    AGE_28("10", "28岁"),
    AGE_29("11", "29岁"),
    AGE_30("12", "30岁"),
    AGE_31("13", "31岁"),
    AGE_32("14", "32岁"),
    AGE_33("15", "33岁"),
    AGE_34("16", "34岁"),
    AGE_35("17", "35岁"),
    AGE_36("18", "36岁"),
    AGE_37("19", "37岁"),
    AGE_38("20", "38岁"),
    AGE_39("21", "39岁"),
    AGE_40("22", "40岁"),
    AGE_41("23", "41岁"),
    AGE_42("24", "42岁"),
    AGE_43("25", "43岁"),
    AGE_44("26", "44岁"),
    AGE_45("27", "45岁"),
    AGE_46("28", "46岁"),
    AGE_47("29", "47岁"),
    AGE_48("30", "48岁"),
    AGE_49("31", "49岁"),
    AGE_50("32", "50岁");

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