package com.tianyu.seelove.model.entity.message;

import com.tianyu.seelove.model.enums.MessageType;

/**
 * @author shisheng.zhao
 * @Description: 语音消息实体类, 通过重写父类方法展示自定义消息模板
 * @date 2017-03-31 21:50
 */
public class SLAudioMessage extends SLMessage {

    public SLAudioMessage() {
    }

    private int audioLength;

    @Override
    public MessageType getMessageType() {
        return MessageType.AUDIO;
    }

    public int getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(int audioLength) {
        this.audioLength = audioLength;
    }
}
