package com.tianyu.seelove.model.entity.message;

import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.utils.StringUtils;

/**
 * @author shisheng.zhao
 * @Description: 图片消息实体类, 通过重写父类方法展示自定义消息模板
 * @date 2017-03-31 21:50
 */
public class SLImageMessage extends SLMessage {

    public SLImageMessage() {
    }

    private String thumUrl; // 缩略图地址
    private String sendProcess; // 图片消息的上传进度

    @Override
    public MessageType getMessageType() {
        return MessageType.IMAGE;
    }

    public String getThumUrl() {
        return StringUtils.isNullOrBlank(thumUrl) ? "" : thumUrl;
    }

    public void setThumUrl(String thumUrl) {
        this.thumUrl = thumUrl;
    }

    public void setSendProcess(String sendProcess) {
        this.sendProcess = sendProcess;
    }

    public String getSendProcess() {
        return sendProcess;
    }
}