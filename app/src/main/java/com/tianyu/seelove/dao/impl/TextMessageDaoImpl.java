package com.tianyu.seelove.dao.impl;

import android.database.Cursor;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.message.SLTextMessage;

/**
 * @author shisheng.zhao
 * @Description: 处理文字消息的数据库访问层实现
 * @date 2017-04-01 10:33
 */
public class TextMessageDaoImpl extends MessageDaoImpl {
    private static final String sqlAddMessage = "INSERT INTO MESSAGEINFO(MessageId,UserFrom,UserTo,MessageContent," +
            "TimeStamp,IsRead,IsVisable,IsDelay,SendStatue,MessageType) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?)";

    public void addMessage(SLMessage message) {
        DbConnectionManager.getInstance().getConnection().execSQL(sqlAddMessage,
                new Object[]{message.getMessageId(), message.getUserFrom(), message.getUserTo(),
                        message.getMessageContent(), message.getTimestamp(), message.getIsRead(),
                        message.getIsVisible(), message.getIsDelay(), message.getSendStatue(),
                        message.getMessageType().toString()});
    }

    public SLMessage getMessageByCursor(Cursor cursor) {
        SLTextMessage message = new SLTextMessage();
        message.setMessageId(cursor.getString(cursor.getColumnIndexOrThrow("MessageId")));
        message.setUserFrom(cursor.getLong(cursor.getColumnIndexOrThrow("UserFrom")));
        message.setUserTo(cursor.getLong(cursor.getColumnIndexOrThrow("UserTo")));
        message.setMessageContent(cursor.getString(cursor.getColumnIndexOrThrow("MessageContent")));
        message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("TimeStamp")));
        message.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow("IsRead")));
        message.setIsVisible(cursor.getInt(cursor.getColumnIndexOrThrow("IsVisable")));
        message.setIsDelay(cursor.getInt(cursor.getColumnIndexOrThrow("IsDelay")));
        message.setSendStatue(cursor.getInt(cursor.getColumnIndexOrThrow("SendStatue")));
        return message;
    }
}
