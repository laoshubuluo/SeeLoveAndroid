package com.tianyu.seelove.model.entity.message;

import com.tianyu.seelove.model.enums.MessageType;

/**
 * @author shisheng.zhao
 * @Description: 文本消息实体类, 通过重写父类构造方法展示自定义消息模版
 * @date 2017-03-31 21:43
 */
public class SLTextMessage extends SLMessage {

    public SLTextMessage() {
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.TEXT;
    }
}
