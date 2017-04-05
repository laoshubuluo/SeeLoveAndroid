package com.tianyu.seelove.model.entity.message;

import com.tianyu.seelove.model.enums.MessageType;
import java.io.Serializable;

/**
 * @author shisheng.zhao
 * @Description: 消息实体类
 * @date 2017-03-31 21:43
 */
public abstract class SLMessage implements Serializable {

    public interface MessagePropertie {
        int MSG_SENDING = 0;
        /**
         * 发送中,默认为初始状态
         */
        int MSG_SENDSUS = 1;
        /**
         * 发送成功
         */
        int MSG_FAIL = 2;
        /** 发送失败 */
    }
    private String messageId;// 消息ID
    private String userFrom; // 消息来源Uid
    private String userTo; // 消息目标Uid
    private String messageContent; // 消息主体：可以使文本内容或者是文件的路径，根据getMessageType来判断
    private long timestamp; // 时间戳
    private int isRead; // 0 为未读 1 为已读
    private int isVisible; // 0可见 1不可见删除其实更新标志
    private int isDelay; // 0历史消息 1非历史消息
    private int sendStatue; // 消息发送状态;0:发送中(默认状态),1:发送成功;2:发送失败;
    private Boolean isShowTime = false;
    public static final int msgUnread = 0;
    public static final int msgRead = 1;

    public abstract MessageType getMessageType();

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getSendStatue() {
        return sendStatue;
    }

    public void setSendStatue(int sendStatue) {
        this.sendStatue = sendStatue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getIsDelay() {
        return isDelay;
    }

    public void setIsDelay(int isDelay) {
        this.isDelay = isDelay;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public Boolean getIsShowTime() {
        return isShowTime;
    }

    public void setIsShowTime(Boolean isShowTime) {
        this.isShowTime = isShowTime;
    }

    @Override
    public String toString() {
        return "SLMessage{" +
                "messageId='" + messageId + '\'' +
                ", userFrom='" + userFrom + '\'' +
                ", userTo='" + userTo + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                ", isVisible=" + isVisible +
                ", isDelay=" + isDelay +
                ", sendStatue=" + sendStatue +
                '}';
    }
}
