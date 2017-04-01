package com.tianyu.seelove.model.entity.message;

import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.utils.StringUtils;

import java.io.Serializable;

/**
 * @author shisheng.zhao
 * @Description: Session实体类
 * @date 2015-09-01 下午18:30:52
 */
public class SLSession implements Serializable {
    private static final long serialVersionUID = 1L;
    private String targetId; // 会话对象的id，这里会根据sessionType的不同而不同
    private SessionType sessionType;// 会话类型
    private String lastMessageId;// 最后一次消息id
    private long priority;// 权重，权重越高，越靠前
    private String sessionIcon; // imageIcon
    private MessageType messageType;
    private String sessionName; // 会话名称
    private String sessionIsRead;
    private String sessionContent;
    private String prayId; // 代祷Id
    private String praySubject; // 代祷主题

    public String getTargetId() {
        return StringUtils.isNullOrBlank(targetId) ? "" : targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public String getLastMessageId() {
        return StringUtils.isNullOrBlank(lastMessageId) ? "" : lastMessageId;
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
        return StringUtils.isNullOrBlank(sessionIcon) ? "" : sessionIcon;
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
        return StringUtils.isNullOrBlank(sessionName) ? "" : sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionIsRead() {
        return StringUtils.isNullOrBlank(sessionIsRead) ? "" : sessionIsRead;
    }

    public void setSessionIsRead(String sessionIsRead) {
        this.sessionIsRead = sessionIsRead;
    }

    public String getSessionContent() {
        return StringUtils.isNullOrBlank(sessionContent) ? "" : sessionContent;
    }

    public void setSessionContent(String sessionContent) {
        this.sessionContent = sessionContent;
    }

    public void setPrayId(String prayId) {
        this.prayId = prayId;
    }

    public String getPrayId() {
        return StringUtils.isNullOrBlank(prayId) ? "" : prayId;
    }

    public void setPraySubject(String praySubject) {
        this.praySubject = praySubject;
    }

    public String getPraySubject() {
        return StringUtils.isNullOrBlank(praySubject) ? "" : praySubject;
    }

    public int getUnreadCount() {
        MessageDao dao = new MessageDaoImpl();
        if (sessionType.equals(SessionType.CHAT)) {
            try {
                return dao.getUnReadMessageCount(targetId);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Session{" +
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
