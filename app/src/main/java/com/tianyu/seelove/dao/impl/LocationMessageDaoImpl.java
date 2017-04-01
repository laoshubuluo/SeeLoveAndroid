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

    public void addMessage(SLMessage message) {
        SLLocationMessage locationMessage = (SLLocationMessage) message;
        String addSql = "insert into messageinfo(messageId,userFrom,userTo,content," +
                "timestamp,groupId,isRead,isVisible,isDelay,state,type,lng,lat,address) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        DbConnectionManager.getInstance().getConnection().execSQL(
                addSql,
                new Object[]{locationMessage.getMessageId(),
                        locationMessage.getUserFrom(), locationMessage.getUserTo(),
                        locationMessage.getContent(), locationMessage.getTimestamp(), locationMessage.getIsRead(),
                        locationMessage.getIsVisible(), locationMessage.getIsDelay(),
                        locationMessage.getState(),
                        locationMessage.getMessageType().toString(),
                        locationMessage.getLng(), locationMessage.getLat(),locationMessage.getAddress()});
    }

    public SLLocationMessage getMessageByCursor(Cursor cursor) {
        SLLocationMessage message = new SLLocationMessage();
        message.setMessageId(cursor.getString(cursor.getColumnIndexOrThrow("messageId")));
        message.setUserFrom(cursor.getString(cursor.getColumnIndexOrThrow("userFrom")));
        message.setUserTo(cursor.getString(cursor.getColumnIndexOrThrow("userTo")));
        message.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
        message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("timestamp")));
        message.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow("isRead")));
        message.setIsVisible(cursor.getInt(cursor.getColumnIndexOrThrow("isVisible")));
        message.setIsDelay(cursor.getInt(cursor.getColumnIndexOrThrow("isDelay")));
        message.setState(cursor.getInt(cursor.getColumnIndexOrThrow("state")));
        message.setLng(Double.parseDouble(cursor.getString(cursor
                .getColumnIndex("lng"))));
        message.setLat(Double.parseDouble(cursor.getString(cursor
                .getColumnIndex("lat"))));
        message.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
        return message;
    }
}
