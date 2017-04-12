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


    public static final String sqlInsertMessageInfo = "insert into messageinfo(messageId,userFrom,userTo,content,timestamp," +
            "groupId,isRead,isVisible,isDelay,state,type,audiolength,lng,lat,address,volumeId,chapterId,sectionId,prayId,thumUrl," +
            "articleId,title,imageUrl,url,articleType,userId,userName,headUrl,userTemp) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


    // 分页查询单聊消息
    private final static String GET_MESSAGE_BY_PAGE = "SELECT * FROM (SELECT * FROM MESSAGEINFO WHERE (UserFrom = ? AND UserTo = ?) OR (UserTo = ? AND UserFrom = ?) " +
            "AND IsVisable !=1 ORDER BY _ID DESC LIMIT ?,?) AS T1 ORDER BY _ID";
    // 设置单聊消息为已读
    private final static String SET_ALL_READ = "UPDATE MESSAGEINFO SET IsRead = '1' WHERE (UserFrom = ? AND UserTo = ?) OR (UserTo = ? AND UserFrom = ?)";
    // 获取单聊图片消息
    private final static String GET_IMAG_MESSAGE = "SELECT * FROM (SELECT * FROM MESSAGEINFO WHERE (UserFrom = ? AND UserTo = ? AND MessageType = 'IMAGE') " +
            "OR (UserTo = ? AND UserFrom = ? AND MessageType = 'IMAGE') AND IsVisable != '1' ORDER BY _ID DESC) AS T1 ORDER BY _ID";
    // 清空两个人之间的单聊消息
    private final static String DELETE_CHAT_MESSAGE = "DELETE FROM MESSAGEINFO WHERE (UserFrom = ? AND UserTo = ?) OR (UserTo = ? AND UserFrom = ?)";
    // 根据消息ID获取消息详情
    private final static String SELECT_MESSAGE_BY_MESSAGEID = "SELECT * FROM MESSAGEINFO WHERE MessageId = ?";
    // 根据消息ID更新sendStatue为发送成功
    private final static String UPDATE_SENDSTATUE_SUCCESS = "UPDATE MESSAGEINFO SET SendStatue = " + SLMessage.MessagePropertie.MSG_SENDSUS + " WHERE MessageId = ?";
    // 根据消息ID更新sendStatue为发送失败
    private final static String UPDATE_SENDSTATUE_FAIL = "UPDATE MESSAGEINFO SET SendStatue = " + SLMessage.MessagePropertie.MSG_FAIL + " WHERE MessageId = ?";
    // 查询当前用户未读消息数量
    private final static String SELECT_UNREAD_MESSAGE_COUNT = "SELECT COUNT(*) CT FROM MESSAGEINFO WHERE IsRead = '0' AND UserFrom = ?";
    // 根据消息ID更新当前消息为已读
    private final static String UPDATE_MESSAGE_ISREAD_BY_MESSAGEID = "UPDATE MESSAGEINFO SET IsRead = '1' WHERE MessageId = ?";
    // 根据消息ID更新缩略图地址
    private final static String UPDATE_THUMURL_BY_MESSAGEID = "UPDATE MESSAGEINFO SET ThumUrl = ? WHERE MessageId = ?";
    // 获取所有唯独消息数量
    private final static String SELECT_ALL_UNREAD_MESSAGE_COUNT = "SELECT COUNT(*) CT FROM MESSAGEINFO WHERE IsRead = 0";
    // 根据消息ID删除当前消息
    private final static String DELETE_MESSAGE_BY_MESSAGEID = "DELETE FROM MESSAGEINFO WHERE MessageId = ?";


    private HashMap<MessageType, MessageDaoImpl> map = new HashMap<MessageType, MessageDaoImpl>();

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
    public List<SLMessage> getMessageByPage(long userFrom, long userTo, int start, int count) {
        Cursor cursor = DbConnectionManager.getInstance().getConnection().rawQuery(GET_MESSAGE_BY_PAGE,
                new String[]{String.valueOf(userFrom), String.valueOf(userTo), String.valueOf(userFrom), String.valueOf(userTo), start + "", count + ""});
        List<SLMessage> messages = new ArrayList<SLMessage>();
        try {
            while (cursor.moveToNext()) {
                String messageType = cursor.getString(cursor.getColumnIndex("MessageType"));
                messages.add(getDaoImpl(MessageType.valueOf(messageType)).getMessageByCursor(cursor));
            }
        } finally {
            cursor.close();
        }
        return messages;
    }

    @Override
    public List<SLMessage> getImageMessage(long userFrom, long userTo) {
        Cursor cursor = DbConnectionManager.getInstance().getConnection().rawQuery(GET_IMAG_MESSAGE,
                new String[]{String.valueOf(userFrom), String.valueOf(userTo), String.valueOf(userFrom), String.valueOf(userTo)});
        List<SLMessage> messages = new ArrayList<SLMessage>();
        try {
            while (cursor.moveToNext()) {
                String messageType = cursor.getString(cursor.getColumnIndex("MessageType"));
                messages.add(getDaoImpl(MessageType.valueOf(messageType)).getMessageByCursor(cursor));
            }
        } finally {
            cursor.close();
        }
        return messages;
    }

    @Override
    public void cleanSingalChatMessage(long userFrom, long userTo) {
        DbConnectionManager.getInstance().getConnection().execSQL(DELETE_CHAT_MESSAGE,
                new String[]{String.valueOf(userFrom), String.valueOf(userTo), String.valueOf(userFrom), String.valueOf(userTo)});
    }

    @Override
    public SLMessage getMessageById(String messageId) {
        if (!StringUtils.isNotBlank(messageId)) {
            return null;
        }
        Cursor cursor = null;
        try {
            SQLiteDatabase database = DbConnectionManager.getInstance().getConnection();
            cursor = database.rawQuery(SELECT_MESSAGE_BY_MESSAGEID, new String[]{messageId});
            while (cursor.moveToNext()) {
                String messageType = cursor.getString(cursor.getColumnIndex("MessageType"));
                return getDaoImpl(MessageType.valueOf(messageType)).getMessageByCursor(cursor);
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
    public void updateSendStatueSuccessByMessageId(String messageId) {
        DbConnectionManager.getInstance().getConnection().execSQL(UPDATE_SENDSTATUE_SUCCESS, new String[]{messageId});
    }

    @Override
    public void updateSendStatueFailByMessageId(String messageId) {
        DbConnectionManager.getInstance().getConnection().execSQL(UPDATE_SENDSTATUE_FAIL, new String[]{messageId});
    }

    @Override
    public int getUnReadMessageCount(String from) {
        Cursor cursor = DbConnectionManager.getInstance().getConnection().rawQuery(SELECT_UNREAD_MESSAGE_COUNT, new String[]{from});
        while (cursor.moveToNext()) {
            int n = cursor.getInt(cursor.getColumnIndex("CT"));
            cursor.close();
            return n;
        }
        cursor.close();
        return 0;
    }

    @Override
    public void setAllRead(long userFrom, long userTo) {
        DbConnectionManager.getInstance().getConnection().execSQL(SET_ALL_READ, new String[]{String.valueOf(userFrom), String.valueOf(userTo), String.valueOf(userFrom), String.valueOf(userTo)});
    }

    @Override
    public void updateMessageIsReadByMessageId(String messageId) {
        DbConnectionManager.getInstance().getConnection().execSQL(UPDATE_MESSAGE_ISREAD_BY_MESSAGEID, new String[]{messageId});
    }

    @Override
    public void updateMessageThumUrlByMessageId(String messageId, String thumUrl) {
        DbConnectionManager.getInstance().getConnection().execSQL(UPDATE_THUMURL_BY_MESSAGEID, new String[]{thumUrl, messageId});
    }

    @Override
    public int getAllUnReadMessageCount() {
        int unReadCount;
        try {
            Cursor cursor = DbConnectionManager.getInstance().getConnection().rawQuery(SELECT_ALL_UNREAD_MESSAGE_COUNT, null);
            while (cursor.moveToNext()) {
                unReadCount = cursor.getInt(cursor.getColumnIndex("CT"));
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
    public void deleteMessageByMessageId(String messageId) {
        DbConnectionManager.getInstance().getConnection().execSQL(DELETE_MESSAGE_BY_MESSAGEID, new String[]{messageId});
    }

    /**
     * 绑定sql的数据
     */
    private final static String sqlCreateMessageInfo = "CREATE TABLE MESSAGEINFO(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "MessageId,UserFrom,UserTo,MessageContent,TimeStamp,IsRead DEFAULT '0',IsVisable DEFAULT '0',IsDelay DEFALUT '1'," +
            "SendStatue DEFAULT '0',MessageType,ThumUrl,AudioLength,Lng,Lat,Address)";
    public static SQLiteStatement bindData(SQLiteStatement stat, SLMessage slMessage) {
        if (MessageType.TEXT.equals(slMessage.getMessageType())) {
            SLTextMessage message = (SLTextMessage) slMessage;
            stat.bindString(1, message.getMessageId());
            stat.bindString(2, String.valueOf(message.getUserFrom()));
            stat.bindString(3, String.valueOf(message.getUserTo()));
            stat.bindString(4, message.getMessageContent());
            stat.bindString(5, String.valueOf(message.getTimestamp()));
            stat.bindString(7, String.valueOf(message.getIsRead()));
            stat.bindString(8, String.valueOf(message.getIsVisible()));
            stat.bindString(9, String.valueOf(message.getIsDelay()));
            stat.bindString(10, String.valueOf(message.getSendStatue()));
            stat.bindString(11, message.getMessageType().toString());
        } else if (MessageType.AUDIO.equals(slMessage.getMessageType())) {
            SLAudioMessage message = (SLAudioMessage) slMessage;
            stat.bindString(1, message.getMessageId());
            stat.bindString(2, String.valueOf(message.getUserFrom()));
            stat.bindString(3, String.valueOf(message.getUserTo()));
            stat.bindString(4, message.getMessageContent());
            stat.bindString(5, String.valueOf(message.getTimestamp()));
            stat.bindString(7, String.valueOf(message.getIsRead()));
            stat.bindString(8, String.valueOf(message.getIsVisible()));
            stat.bindString(9, String.valueOf(message.getIsDelay()));
            stat.bindString(10, String.valueOf(message.getSendStatue()));
            stat.bindString(11, message.getMessageType().toString());
            stat.bindString(13, String.valueOf(message.getAudioLength()));
        } else if (MessageType.IMAGE.equals(slMessage.getMessageType())) {
            SLImageMessage message = (SLImageMessage) slMessage;
            stat.bindString(1, message.getMessageId());
            stat.bindString(2, String.valueOf(message.getUserFrom()));
            stat.bindString(3, String.valueOf(message.getUserTo()));
            stat.bindString(4, message.getMessageContent());
            stat.bindString(5, String.valueOf(message.getTimestamp()));
            stat.bindString(7, String.valueOf(message.getIsRead()));
            stat.bindString(8, String.valueOf(message.getIsVisible()));
            stat.bindString(9, String.valueOf(message.getIsDelay()));
            stat.bindString(10, String.valueOf(message.getSendStatue()));
            stat.bindString(11, message.getMessageType().toString());
            stat.bindString(12, message.getThumUrl());
        } else if (MessageType.LOCATION.equals(slMessage.getMessageType())) {
            SLLocationMessage message = (SLLocationMessage) slMessage;
            stat.bindString(1, message.getMessageId());
            stat.bindString(2, String.valueOf(message.getUserFrom()));
            stat.bindString(3, String.valueOf(message.getUserTo()));
            stat.bindString(4, message.getMessageContent());
            stat.bindString(5, String.valueOf(message.getTimestamp()));
            stat.bindString(7, String.valueOf(message.getIsRead()));
            stat.bindString(8, String.valueOf(message.getIsVisible()));
            stat.bindString(9, String.valueOf(message.getIsDelay()));
            stat.bindString(10, String.valueOf(message.getSendStatue()));
            stat.bindString(11, message.getMessageType().toString());
            stat.bindString(14, String.valueOf(message.getLng()));
            stat.bindString(15, String.valueOf(message.getLat()));
            stat.bindString(16, message.getAddress());
        }
        return stat;
    }
}
