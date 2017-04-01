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

    public void addMessage(SLMessage message) {
        SLAudioMessage audioMessage = (SLAudioMessage) message;
        String addSql = "insert into messageinfo(messageId,userFrom,userTo,content," +
                "timestamp,groupId,isRead,isVisible,isDelay,state,type,audiolength) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?)";
        DbConnectionManager.getInstance().getConnection().execSQL(
                addSql,
                new Object[]{audioMessage.getMessageId(),
                        audioMessage.getUserFrom(), audioMessage.getUserTo(),
                        audioMessage.getContent(), audioMessage.getTimestamp(),
                        audioMessage.getIsRead(),
                        audioMessage.getIsVisible(), audioMessage.getIsDelay(),
                        audioMessage.getState(),
                        audioMessage.getMessageType().toString(),
                        audioMessage.getAudioLength()});
    }

    public SLMessage getMessageByCursor(Cursor cursor) {
        SLAudioMessage message = new SLAudioMessage();
        message.setMessageId(cursor.getString(cursor.getColumnIndexOrThrow("messageId")));
        message.setUserFrom(cursor.getString(cursor.getColumnIndexOrThrow("userFrom")));
        message.setUserTo(cursor.getString(cursor.getColumnIndexOrThrow("userTo")));
        message.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
        message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("timestamp")));
        message.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow("isRead")));
        message.setIsVisible(cursor.getInt(cursor.getColumnIndexOrThrow("isVisible")));
        message.setIsDelay(cursor.getInt(cursor.getColumnIndexOrThrow("isDelay")));
        message.setState(cursor.getInt(cursor.getColumnIndexOrThrow("state")));
        message.setAudioLength(Integer.parseInt(cursor.getString(cursor
                .getColumnIndex("audiolength"))));
        return message;
    }
}
