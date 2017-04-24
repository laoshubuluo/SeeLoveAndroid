package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户学历枚举类型
 * @date 2017-04-24 16:08
 */
public enum EducationType {
    EDUCATION_UNKNOW("0", "未知"),
    EDUCATION_SENIOR("1", "高中"),
    EDUCATION_COLLEGE("2", "大专"),
    EDUCATION_UNIVERSITY("3", "大学本科"),
    EDUCATION_GRADUATE("4", "研究生"),
    EDUCATION_DOCTOR("5", "博士");

    private String resultCode;
    private String resultMsg;

    public static EducationType parse(String resultCode) {
        for (EducationType item : values()) {
            if (item.getResultCode().equals(resultCode))
                return item;
        }
        return null;
    }

    public static EducationType parseByMsg(String resultMsg) {
        for (EducationType item : values()) {
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

    EducationType(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}