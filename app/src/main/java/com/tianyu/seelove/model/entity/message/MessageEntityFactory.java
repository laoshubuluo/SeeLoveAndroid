package com.tianyu.seelove.model.entity.message;

import com.tianyu.seelove.model.enums.MessageType;

/**
 * @author shisheng.zhao
 * @Description: 构建MessageEntity工厂模式
 * @date 2015-09-01 下午18:51:27
 */
public class MessageEntityFactory {
    public static MessageEntity getMessageEntity(SLMessage message) {
        if (message.getMessageType() == MessageType.TEXT) {
            return new TextMessageEntity(message);
        } else if (message.getMessageType() == MessageType.AUDIO) {
            return new AudioMessageEntity(message);
        } else if (message.getMessageType() == MessageType.IMAGE) {
            return new ImageMessageEntity(message);
        } else {
            return new TextMessageEntity(message);
        }
    }
}