package com.tianyu.seelove.dao.impl;

import android.database.Cursor;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.model.entity.message.SLImageMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;

/**
 * @author shisheng.zhao
 * @Description: 处理图片消息的数据库访问层实现
 * @date 2017-04-01 10:30
 */
public class ImageMessageDaoImpl extends MessageDaoImpl {
    private static final String sqlAddMessage = "INSERT INTO MESSAGEINFO(MessageId,UserFrom,UserTo,MessageContent," +
            "TimeStamp,IsRead,IsVisable,IsDelay,SendStatue,MessageType,ThumUrl) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

    public void addMessage(SLMessage message) {
        SLImageMessage amImageMessage = (SLImageMessage) message;
        DbConnectionManager.getInstance().getConnection().execSQL(sqlAddMessage,
                new Object[]{amImageMessage.getMessageId(),
                        amImageMessage.getUserFrom(), amImageMessage.getUserTo(),
                        amImageMessage.getMessageContent(), amImageMessage.getTimestamp(), amImageMessage.getIsRead(),
                        amImageMessage.getIsVisible(), amImageMessage.getIsDelay(), amImageMessage.getSendStatue(),
                        amImageMessage.getMessageType().toString(), amImageMessage.getThumUrl()});
    }

    public SLMessage getMessageByCursor(Cursor cursor) {
        SLImageMessage message = new SLImageMessage();
        message.setMessageId(cursor.getString(cursor.getColumnIndexOrThrow("MessageId")));
        message.setUserFrom(cursor.getString(cursor.getColumnIndexOrThrow("UserFrom")));
        message.setUserTo(cursor.getString(cursor.getColumnIndexOrThrow("UserTo")));
        message.setMessageContent(cursor.getString(cursor.getColumnIndexOrThrow("MessageContent")));
        message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("TimeStamp")));
        message.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow("IsRead")));
        message.setIsVisible(cursor.getInt(cursor.getColumnIndexOrThrow("IsVisable")));
        message.setIsDelay(cursor.getInt(cursor.getColumnIndexOrThrow("IsDelay")));
        message.setSendStatue(cursor.getInt(cursor.getColumnIndexOrThrow("SendStatue")));
        message.setThumUrl(cursor.getString(cursor.getColumnIndexOrThrow("ThumUrl")));
        return message;
    }
}
