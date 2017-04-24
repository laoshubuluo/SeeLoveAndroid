package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户职业枚举类型
 * @date 2017-04-24 16:08
 */
public enum WorkType {
    WORK_01("01", "会计"),
    WORK_02("02", "演员"),
    WORK_03("03", "医生"),
    WORK_04("04", "护士"),
    WORK_05("05", "厨师"),
    WORK_06("06", "工程师"),
    WORK_07("07", "律师"),
    WORK_08("08", "司机"),
    WORK_09("09", "老师"),
    WORK_10("10", "学生"),
    WORK_11("11", "记者"),
    WORK_12("12", "其他");

    private String resultCode;
    private String resultMsg;

    public static WorkType parse(String resultCode) {
        for (WorkType item : values()) {
            if (item.getResultCode().equals(resultCode))
                return item;
        }
        return null;
    }

    public static WorkType parseByMsg(String resultMsg) {
        for (WorkType item : values()) {
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

    WorkType(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}