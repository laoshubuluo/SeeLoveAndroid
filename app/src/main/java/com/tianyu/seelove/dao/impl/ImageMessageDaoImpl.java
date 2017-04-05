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

    public void addMessage(SLMessage message) {
        SLImageMessage amImageMessage = (SLImageMessage) message;
        DbConnectionManager.getInstance().getConnection().execSQL(
                "insert into messageinfo(messageId,userFrom,userTo,content,timestamp,groupId,isRead,isVisible,isDelay,state,type,thumUrl) values(?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{amImageMessage.getMessageId(),
                        amImageMessage.getUserFrom(), amImageMessage.getUserTo(),
                        amImageMessage.getMessageContent(), amImageMessage.getTimestamp(), amImageMessage.getIsRead(),
                        amImageMessage.getIsVisible(), amImageMessage.getIsDelay(),
                        amImageMessage.getSendStatue(),
                        amImageMessage.getMessageType().toString(), amImageMessage.getThumUrl()});
    }

    public SLMessage getMessageByCursor(Cursor cursor) {
        SLImageMessage message = new SLImageMessage();
        message.setMessageId(cursor.getString(cursor.getColumnIndexOrThrow("messageId")));
        message.setUserFrom(cursor.getString(cursor.getColumnIndexOrThrow("userFrom")));
        message.setUserTo(cursor.getString(cursor.getColumnIndexOrThrow("userTo")));
        message.setMessageContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
        message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("timestamp")));
        message.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow("isRead")));
        message.setIsVisible(cursor.getInt(cursor.getColumnIndexOrThrow("isVisible")));
        message.setIsDelay(cursor.getInt(cursor.getColumnIndexOrThrow("isDelay")));
        message.setSendStatue(cursor.getInt(cursor.getColumnIndexOrThrow("state")));
        message.setThumUrl(cursor.getString(cursor.getColumnIndexOrThrow("thumUrl")));
        return message;
    }
}
