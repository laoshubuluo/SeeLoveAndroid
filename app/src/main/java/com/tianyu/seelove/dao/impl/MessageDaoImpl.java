package com.tianyu.seelove.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.model.entity.message.SLAudioMessage;
import com.tianyu.seelove.model.entity.message.SLImageMessage;
import com.tianyu.seelove.model.entity.message.SLLocationMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.message.SLTextMessage;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 消息数据访问层实现类
 * @date 2017-04-01 10:32
 */
public class MessageDaoImpl implements MessageDao {
    protected static final String ADD_MESSAGE = "insert into messageinfo(messageId," +
            "userFrom,userTo,content,timestamp,groupId,isRead,isVisible,isDelay,state,type,prayId) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?)";
    protected static final String GET_MESSAGE_BY_ID = "select * from messageinfo where messageId=?";
    protected static final String GET_GROUPMESSAGE_BY_PAGE =
            "SELECT * FROM (select * from messageinfo where groupId=? and isVisible != '1' order by _id desc limit ?,?) as t0 order by _id";
    protected static final String GET_GROUPIMAGEMESSAGE =
            "SELECT * FROM (select * from messageinfo where groupId=? and isVisible != '1' and type = 'IMAGE' order by _id desc) as t0 order by _id";
    protected static final String UNREAD_MESSAGE_COUNT = "select count(*) ct from messageinfo " +
            "where isRead=0 and (groupId is null or groupId = 'null') and userFrom=?";
    protected static final String SET_ALL_READ = "update messageinfo set isRead=1 where (userFrom=? and userTo = ?) or (userTo=? and userFrom =?) and (groupId is null or groupId = 'null')";
    protected static final String GET_MESSAGE_BY_PAGE = "SELECT * FROM (select * from messageinfo where ("
            + "userFrom=? and userTo = ?) or (userTo=? and userFrom =?) and (groupId is null or groupId = 'null') and isVisible != 1 order by _id desc limit ?,?) as t1 order by _id";
    protected static final String GET_IMAG_MESSAGE = "SELECT * FROM (select * from messageinfo where (userFrom=? and userTo = ? and type = 'IMAGE') or (userTo=? and userFrom =? and type = 'IMAGE') and (groupId is null or groupId = 'null') and isVisible != '1' order by _id desc) as t1 order by _id";
    protected static final String ALL_UNREAD = "select count(*) ct from messageinfo where isRead=0";
    public static final String sqlInsertMessageInfo = "insert into messageinfo(messageId,userFrom,userTo,content,timestamp," +
            "groupId,isRead,isVisible,isDelay,state,type,audiolength,lng,lat,address,volumeId,chapterId,sectionId,prayId,thumUrl," +
            "articleId,title,imageUrl,url,articleType,userId,userName,headUrl,userTemp) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private HashMap<MessageType, MessageDaoImpl> map =
            new HashMap<MessageType, MessageDaoImpl>();

    public MessageDaoImpl() {
        if (getClass().toString().equals(MessageDaoImpl.class.toString())) {
            if (!map.containsKey(MessageType.TEXT)) {
                map.put(MessageType.TEXT, new TextMessageDaoImpl());
            }
            if (!map.containsKey(MessageType.AUDIO)) {
                map.put(MessageType.AUDIO, new AudioMessageDaoImpl());
            }
            if (!map.containsKey(MessageType.IMAGE)) {
                map.put(MessageType.IMAGE, new ImageMessageDaoImpl());
            }
            if (!map.containsKey(MessageType.LOCATION)) {
                map.put(MessageType.LOCATION, new LocationMessageDaoImpl());
            }
        }
    }

    private MessageDaoImpl getDaoImpl(MessageType messageType) {
        return map.get(messageType);
    }

    @Override
    public void addMessage(SLMessage message) {
        getDaoImpl(message.getMessageType()).addMessage(message);
    }

    @Override
    public List<SLMessage> getMessageByPage(String from, String to, int start, int count) {
        Cursor cursor = DbConnectionManager
                .getInstance()
                .getConnection()
                .rawQuery(GET_MESSAGE_BY_PAGE,
                        new String[]{from, to, from, to, start + "", count + ""});
        List<SLMessage> messages = new ArrayList<SLMessage>();
        try {
            while (cursor.moveToNext()) {
                String messageType = cursor.getString(cursor
                        .getColumnIndex("type"));
                messages.add(getDaoImpl(MessageType.valueOf(messageType))
                        .getMessageByCursor(cursor));
            }
        } finally {
            cursor.close();
        }
        return messages;
    }

    @Override
    public List<SLMessage> getImageMessage(String from, String to) {
        Cursor cursor = DbConnectionManager
                .getInstance()
                .getConnection()
                .rawQuery(GET_IMAG_MESSAGE,
                        new String[]{from, to, from, to});
        List<SLMessage> messages = new ArrayList<SLMessage>();
        try {
            while (cursor.moveToNext()) {
                String messageType = cursor.getString(cursor
                        .getColumnIndex("type"));
                messages.add(getDaoImpl(MessageType.valueOf(messageType))
                        .getMessageByCursor(cursor));
            }
        } finally {
            cursor.close();
        }
        return messages;
    }

    @Override
    public void cleanSingalChatMessage(String from, String to) {
        String sql = "delete from messageinfo where (userFrom=? and userTo = ?) or (" +
                "userTo=? and userFrom =?) and (groupId is null or groupId = 'null')";
        DbConnectionManager.getInstance().getConnection()
                .execSQL(sql, new String[]{from, to, from, to});
    }

    @Override
    public SLMessage getMessageById(String messageId) {
        if (!StringUtils.isNotBlank(messageId)) {
            return null;
        }
        Cursor cursor = null;
        try {
            SQLiteDatabase database = DbConnectionManager.getInstance().getConnection();
            cursor = database
                    .rawQuery("select * from messageinfo where messageId=?", new String[]{messageId});
            while (cursor.moveToNext()) {
                String messageType = cursor.getString(cursor
                        .getColumnIndex("type"));
                return getDaoImpl(MessageType.valueOf(messageType))
                        .getMessageByCursor(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    protected SLMessage getMessageByCursor(Cursor cursor) {
        return null;
    }

    @Override
    public void markAsSusMsg(String messageId) {
        String sql = "update messageinfo set state=" + SLMessage.MessagePropertie.MSG_SENDSUS
                + " where messageId=?";
        DbConnectionManager.getInstance().getConnection()
                .execSQL(sql, new String[]{messageId});
    }

    @Override
    public void markAsFailedMsg(String messageId) {
        String sql = "update messageinfo set state=" + SLMessage.MessagePropertie.MSG_FAIL
                + " where messageId=?";
        DbConnectionManager.getInstance().getConnection()
                .execSQL(sql, new String[]{messageId});
    }

    @Override
    public int getUnReadMessageCount(String from) {
        Cursor cursor = DbConnectionManager.getInstance().getConnection()
                .rawQuery(UNREAD_MESSAGE_COUNT, new String[]{from});
        while (cursor.moveToNext()) {
            int n = cursor.getInt(cursor.getColumnIndex("ct"));
            cursor.close();
            return n;
        }
        cursor.close();
        return 0;
    }

    @Override
    public void setAllRead(String from, String to) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL(SET_ALL_READ, new String[]{from, to, from, to});
    }

    @Override
    public void updateMessageIsReadByMessageId(String messageId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL("update messageinfo set isRead=1 where messageId=?", new String[]{messageId});
    }

    @Override
    public void updateMessageThumUrlByMessageId(String messageId, String thumUrl) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL("update messageinfo set thumUrl=? where messageId=?", new String[]{thumUrl, messageId});
    }

    @Override
    public int getUnreadCount() {
        int unReadCount;
        try {
            Cursor cursor = DbConnectionManager.getInstance().getConnection()
                    .rawQuery(ALL_UNREAD, null);
            while (cursor.moveToNext()) {
                unReadCount = cursor.getInt(cursor.getColumnIndex("ct"));
                cursor.close();
                return unReadCount;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void deleteMessageById(String messageId) {
        String sql = "delete from messageinfo where messageId = ?";
        DbConnectionManager.getInstance().getConnection()
                .execSQL(sql, new String[]{messageId});
    }

    /**
     * 绑定sql的数据
     */
    public static SQLiteStatement bindData(SQLiteStatement stat, SLMessage amMessage) {
        if (MessageType.TEXT.equals(amMessage.getMessageType())) {
            SLTextMessage message = (SLTextMessage) amMessage;
            stat.bindString(1, message.getMessageId());
            stat.bindString(2, message.getUserFrom());
            stat.bindString(3, message.getUserTo());
            stat.bindString(4, message.getMessageContent());
            stat.bindString(5, String.valueOf(message.getTimestamp()));
            stat.bindString(7, String.valueOf(message.getIsRead()));
            stat.bindString(8, String.valueOf(message.getIsVisible()));
            stat.bindString(9, String.valueOf(message.getIsDelay()));
            stat.bindString(10, String.valueOf(message.getSendStatue()));
            stat.bindString(11, message.getMessageType().toString());
        } else if (MessageType.AUDIO.equals(amMessage.getMessageType())) {
            SLAudioMessage message = (SLAudioMessage) amMessage;
            stat.bindString(1, message.getMessageId());
            stat.bindString(2, message.getUserFrom());
            stat.bindString(3, message.getUserTo());
            stat.bindString(4, message.getMessageContent());
            stat.bindString(5, String.valueOf(message.getTimestamp()));
            stat.bindString(7, String.valueOf(message.getIsRead()));
            stat.bindString(8, String.valueOf(message.getIsVisible()));
            stat.bindString(9, String.valueOf(message.getIsDelay()));
            stat.bindString(10, String.valueOf(message.getSendStatue()));
            stat.bindString(11, message.getMessageType().toString());
            stat.bindString(12, String.valueOf(message.getAudioLength()));
        } else if (MessageType.IMAGE.equals(amMessage.getMessageType())) {
            SLImageMessage message = (SLImageMessage) amMessage;
            stat.bindString(1, message.getMessageId());
            stat.bindString(2, message.getUserFrom());
            stat.bindString(3, message.getUserTo());
            stat.bindString(4, message.getMessageContent());
            stat.bindString(5, String.valueOf(message.getTimestamp()));
            stat.bindString(7, String.valueOf(message.getIsRead()));
            stat.bindString(8, String.valueOf(message.getIsVisible()));
            stat.bindString(9, String.valueOf(message.getIsDelay()));
            stat.bindString(10, String.valueOf(message.getSendStatue()));
            stat.bindString(11, message.getMessageType().toString());
            stat.bindString(20, message.getThumUrl());
        } else if (MessageType.LOCATION.equals(amMessage.getMessageType())) {
            SLLocationMessage message = (SLLocationMessage) amMessage;
            stat.bindString(1, message.getMessageId());
            stat.bindString(2, message.getUserFrom());
            stat.bindString(3, message.getUserTo());
            stat.bindString(4, message.getMessageContent());
            stat.bindString(5, String.valueOf(message.getTimestamp()));
            stat.bindString(7, String.valueOf(message.getIsRead()));
            stat.bindString(8, String.valueOf(message.getIsVisible()));
            stat.bindString(9, String.valueOf(message.getIsDelay()));
            stat.bindString(10, String.valueOf(message.getSendStatue()));
            stat.bindString(11, message.getMessageType().toString());
            stat.bindString(13, String.valueOf(message.getLng()));
            stat.bindString(14, String.valueOf(message.getLat()));
            stat.bindString(15, message.getAddress());
        }
        return stat;
    }
}
