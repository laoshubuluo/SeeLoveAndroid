package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 用户所在城市枚举类型
 * @date 2017-04-24 16:08
 */
public enum CityType {
    CITY_01("01", "北京"),
    CITY_02("02", "天津"),
    CITY_03("03", "河北"),
    CITY_04("04", "山西"),
    CITY_05("05", "内蒙古"),
    CITY_06("06", "辽宁"),
    CITY_07("07", "吉林"),
    CITY_08("08", "黑龙江"),
    CITY_09("09", "上海"),
    CITY_10("10", "江苏"),
    CITY_11("11", "浙江"),
    CITY_12("12", "安徽"),
    CITY_13("13", "福建"),
    CITY_14("14", "江西"),
    CITY_15("15", "山东"),
    CITY_16("16", "河南"),
    CITY_17("17", "湖北"),
    CITY_18("18", "湖南"),
    CITY_19("19", "广东"),
    CITY_20("20", "广西"),
    CITY_21("21", "海南"),
    CITY_22("22", "重庆"),
    CITY_23("23", "四川"),
    CITY_24("24", "贵州"),
    CITY_25("25", "云南"),
    CITY_26("26", "西藏"),
    CITY_27("27", "陕西"),
    CITY_28("28", "甘肃"),
    CITY_29("29", "青海"),
    CITY_30("30", "宁夏"),
    CITY_31("31", "新疆"),
    CITY_32("32", "台湾"),
    CITY_33("33", "香港"),
    CITY_34("34", "澳门");

    private String resultCode;
    private String resultMsg;

    public static CityType parse(String resultCode) {
        for (CityType item : values()) {
            if (item.getResultCode().equals(resultCode))
                return item;
        }
        return null;
    }

    public static CityType parseByMsg(String resultMsg) {
        for (CityType item : values()) {
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

    CityType(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}