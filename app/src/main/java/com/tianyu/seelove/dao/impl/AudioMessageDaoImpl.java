package com.tianyu.seelove.dao.impl;

import android.database.Cursor;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.model.entity.message.SLAudioMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;

/**
 * @author shisheng.zhao
 * @Description: 处理语音消息的数据库访问层实现
 * @date 2017-04-01 10:30
 */
public class AudioMessageDaoImpl extends MessageDaoImpl {
    private static final String sqlAddMessage = "INSERT INTO MESSAGEINFO(MessageId,UserFrom,UserTo,MessageContent," +
            "TimeStamp,IsRead,IsVisable,IsDelay,SendStatue,MessageType,AudioLength) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

    public void addMessage(SLMessage message) {
        SLAudioMessage audioMessage = (SLAudioMessage) message;
        DbConnectionManager.getInstance().getConnection().execSQL(
                sqlAddMessage,
                new Object[]{audioMessage.getMessageId(),
                        audioMessage.getUserFrom(), audioMessage.getUserTo(),
                        audioMessage.getMessageContent(), audioMessage.getTimestamp(),
                        audioMessage.getIsRead(), audioMessage.getIsVisible(), audioMessage.getIsDelay(),
                        audioMessage.getSendStatue(), audioMessage.getMessageType().toString(), audioMessage.getAudioLength()});
    }

    public SLMessage getMessageByCursor(Cursor cursor) {
        SLAudioMessage message = new SLAudioMessage();
        message.setMessageId(cursor.getString(cursor.getColumnIndexOrThrow("MessageId")));
        message.setUserFrom(cursor.getLong(cursor.getColumnIndexOrThrow("UserFrom")));
        message.setUserTo(cursor.getLong(cursor.getColumnIndexOrThrow("UserTo")));
        message.setMessageContent(cursor.getString(cursor.getColumnIndexOrThrow("MessageContent")));
        message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("TimeStamp")));
        message.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow("IsRead")));
        message.setIsVisible(cursor.getInt(cursor.getColumnIndexOrThrow("IsVisable")));
        message.setIsDelay(cursor.getInt(cursor.getColumnIndexOrThrow("IsDelay")));
        message.setSendStatue(cursor.getInt(cursor.getColumnIndexOrThrow("SendStatue")));
        message.setAudioLength(Integer.parseInt(cursor.getString(cursor.getColumnIndex("AudioLength"))));
        return message;
    }
}
