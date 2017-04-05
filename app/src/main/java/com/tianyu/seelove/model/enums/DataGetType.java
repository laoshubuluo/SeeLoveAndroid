package com.tianyu.seelove.model.enums;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 数据获取类型
 */
public enum DataGetType {
    PAGE_DOWN("down"),
    PAGE_UP("up"),
    UPDATE("update");

    private String type;

    DataGetType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}