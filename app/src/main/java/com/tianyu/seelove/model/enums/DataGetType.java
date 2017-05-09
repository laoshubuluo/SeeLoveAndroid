package com.tianyu.seelove.model.enums;

/**
 * Created by liangjinzhu on 17/4/10.
 */
public enum DataGetType {
    UP(1),
    DOWN(2),
    OTHER(0);

    private int code;

    DataGetType(int code) {
        this.code = code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
