package com.tianyu.seelove.model.entity.message;

import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.utils.StringUtils;

/**
 * @author shisheng.zhao
 * @Description: 位置消息实体类, 通过重写父类方法展示自定义消息模板
 * @date 2017-03-31 21:50
 */
public class SLLocationMessage extends SLMessage {
    public SLLocationMessage() {
    }

    private double lng; // 经度
    private double lat; // 维度
    private String address; // 位置信息

    @Override
    public MessageType getMessageType() {
        return MessageType.LOCATION;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return StringUtils.isNullOrBlank(address) ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
