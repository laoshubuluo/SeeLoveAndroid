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

    public void addMessage(SLMessage message) {
        DbConnectionManager.getInstance().getConnection().execSQL(
                "insert into messageinfo(messageId,userFrom,userTo,content,timestamp,groupId,isRead,isVisible,isDelay,state,type,prayId,userTemp) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{message.getMessageId(),
                        message.getUserFrom(), message.getUserTo(),
                        message.getMessageContent(), message.getTimestamp(), message.getIsRead(),
                        message.getIsVisible(), message.getIsDelay(),
                        message.getSendStatue(),
                        message.getMessageType().toString()});
    }

    public SLMessage getMessageByCursor(Cursor cursor) {
        SLTextMessage message = new SLTextMessage();
        message.setMessageId(cursor.getString(cursor.getColumnIndexOrThrow("messageId")));
        message.setUserFrom(cursor.getString(cursor.getColumnIndexOrThrow("userFrom")));
        message.setUserTo(cursor.getString(cursor.getColumnIndexOrThrow("userTo")));
        message.setMessageContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
        message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("timestamp")));
        message.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow("isRead")));
        message.setIsVisible(cursor.getInt(cursor.getColumnIndexOrThrow("isVisible")));
        message.setIsDelay(cursor.getInt(cursor.getColumnIndexOrThrow("isDelay")));
        message.setSendStatue(cursor.getInt(cursor.getColumnIndexOrThrow("state")));
        return message;
    }
}
