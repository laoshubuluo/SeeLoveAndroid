package com.tianyu.seelove.dao.impl;

import android.database.Cursor;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.model.entity.message.SLLocationMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;

/**
 * @author shisheng.zhao
 * @Description: 处理位置消息的数据库访问层实现
 * @date 2017-04-01 10:31
 */
public class LocationMessageDaoImpl extends MessageDaoImpl {
    private static final String sqlAddMessage = "INSERT INTO MESSAGEINFO(MessageId,UserFrom,UserTo,MessageContent," +
            "TimeStamp,IsRead,IsVisable,IsDelay,SendStatue,MessageType,Lng,Lat,Address) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public void addMessage(SLMessage message) {
        SLLocationMessage locationMessage = (SLLocationMessage) message;
        DbConnectionManager.getInstance().getConnection().execSQL(
                sqlAddMessage,
                new Object[]{locationMessage.getMessageId(),
                        locationMessage.getUserFrom(), locationMessage.getUserTo(),
                        locationMessage.getMessageContent(), locationMessage.getTimestamp(), locationMessage.getIsRead(),
                        locationMessage.getIsVisible(), locationMessage.getIsDelay(), locationMessage.getSendStatue(),
                        locationMessage.getMessageType().toString(), locationMessage.getLng(),
                        locationMessage.getLat(),locationMessage.getAddress()});
    }

    public SLLocationMessage getMessageByCursor(Cursor cursor) {
        SLLocationMessage message = new SLLocationMessage();
        message.setMessageId(cursor.getString(cursor.getColumnIndexOrThrow("MessageId")));
        message.setUserFrom(cursor.getLong(cursor.getColumnIndexOrThrow("UserFrom")));
        message.setUserTo(cursor.getLong(cursor.getColumnIndexOrThrow("UserTo")));
        message.setMessageContent(cursor.getString(cursor.getColumnIndexOrThrow("MessageContent")));
        message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("TimeStamp")));
        message.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow("IsRead")));
        message.setIsVisible(cursor.getInt(cursor.getColumnIndexOrThrow("IsVisable")));
        message.setIsDelay(cursor.getInt(cursor.getColumnIndexOrThrow("IsDelay")));
        message.setSendStatue(cursor.getInt(cursor.getColumnIndexOrThrow("SendStatue")));
        message.setLng(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Lng"))));
        message.setLat(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Lat"))));
        message.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("Address")));
        return message;
    }
}
