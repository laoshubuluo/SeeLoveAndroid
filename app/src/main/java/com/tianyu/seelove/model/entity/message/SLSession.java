package com.tianyu.seelove.model.entity.message;

import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.model.enums.SessionType;
import java.io.Serializable;

/**
 * @author shisheng.zhao
 * @Description: Session实体类
 * @date 2017-04-05 08:43
 */
public class SLSession implements Serializable {
    private static final long serialVersionUID = 1L;
    private long targetId; // 会话对象的id，这里会根据sessionType的不同而不同
    private SessionType sessionType;// 会话类型
    private String lastMessageId;// 最后一次消息id
    private long priority;// 权重，权重越高，越靠前
    private String sessionIcon; // imageIcon
    private MessageType messageType; // 消息类型
    private String sessionName; // 会话名称
    private int sessionIsRead; // 会话已读未读; 0 未读,1 已读
    private String sessionContent; // 会话内容

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public long getTargetId() {
        return targetId;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public String getSessionIcon() {
        return sessionIcon;
    }

    public void setSessionIcon(String sessionIcon) {
        this.sessionIcon = sessionIcon;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public void setSessionIsRead(int sessionIsRead) {
        this.sessionIsRead = sessionIsRead;
    }

    public int getSessionIsRead() {
        return sessionIsRead;
    }

    public String getSessionContent() {
        return sessionContent;
    }

    public void setSessionContent(String sessionContent) {
        this.sessionContent = sessionContent;
    }

    @Override
    public String toString() {
        return "SLSession{" +
                "targetId='" + targetId + '\'' +
                ", sessionType=" + sessionType +
                ", lastMessageId='" + lastMessageId + '\'' +
                ", priority=" + priority +
                ", sessionIcon='" + sessionIcon + '\'' +
                ", messageType=" + messageType +
                ", sessionName='" + sessionName + '\'' +
                ", sessionIsRead='" + sessionIsRead + '\'' +
                ", sessionContent='" + sessionContent + '\'' +
                '}';
    }
}
