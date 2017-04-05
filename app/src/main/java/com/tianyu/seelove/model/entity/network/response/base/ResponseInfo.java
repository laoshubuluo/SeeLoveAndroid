package com.tianyu.seelove.model.entity.network.response.base;

import com.google.gson.annotations.SerializedName;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 响应实体
 */
public class ResponseInfo extends AbstractResponseInfo{
    private int actionId;
    @SerializedName("statusCode")
    private String code;
    @SerializedName("statusMsg")
    private String message;

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}