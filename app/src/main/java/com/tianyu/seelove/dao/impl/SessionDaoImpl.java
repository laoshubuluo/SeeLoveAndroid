package com.tianyu.seelove.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.model.enums.SessionType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 会话访问层实现类
 * @date 2015-09-11 下午15:54:22
 */
public class SessionDaoImpl implements SessionDao {
    private static final String GET_LATEST_SESSIONS = "select distinct targetId,sessionType,lastMessageId," +
            "priority,sessionIcon,messageType,sessionName,sessionContent,sessionIsRead,prayId,praySubject from sessioninfo where sessionType != 'PRAYMESSAGE' and sessionType != 'MEETCOMMENT' and sessionType != 'MEETARTICLE' and sessionType != 'MEETINTERACT_MESSAGE' order by priority desc limit ?";
    private static final String GET_PRAY_SESSIONS = "select distinct targetId,sessionType,lastMessageId," +
            "priority,sessionIcon,messageType,sessionName,sessionContent,sessionIsRead,prayId,praySubject from sessioninfo where sessionType = 'PRAYMESSAGE'" +
            "order by priority desc limit ?";
    private static final String GET_MEETING_INTERACT_SESSIONS = "select distinct targetId,sessionType,lastMessageId," +
            "priority,sessionIcon,messageType,sessionName,sessionContent,sessionIsRead,prayId,praySubject from sessioninfo where sessionType = 'MEETINTERACT_MESSAGE'" +
            "order by priority desc limit ?";
    private static final String GET_MEET_SESSIONS = "select distinct targetId,sessionType,lastMessageId," +
            "priority,sessionIcon,messageType,sessionName,sessionContent,sessionIsRead,prayId,praySubject from sessioninfo where sessionType = 'MEETARTICLE' or sessionType ='MEETCOMMENT'" +
            "order by priority desc limit ?";
    private static final String DELETE_SESSION = "delete from sessioninfo where targetId=?";
    private static final String UPDATE_SESSIONNAME = "update sessioninfo set sessionName=? where targetId=?";
    private static final String GET_SESSION_BY_TARGETID = "select distinct targetId,sessionType,lastMessageId," +
            "priority,sessionIcon,messageType,sessionName,sessionContent,sessionIsRead,prayId,praySubject from sessioninfo " +
            "where targetId=?";
    public static final String ADD_SESSION = "insert into sessioninfo(targetId,sessionType,lastMessageId,priority,sessionIcon,messageType,sessionName,sessionContent," +
            "sessionIsRead,prayId,praySubject) values(?,?,?,?,?,?,?,?,?,?,?)";

    @Override
    public synchronized void addSession(SLSession session) {
        ContentValues cv = new ContentValues();
        cv.put("targetId", session.getTargetId());
        cv.put("sessionType", session.getSessionType().toString());
        cv.put("lastMessageId", session.getLastMessageId());
        cv.put("priority", session.getPriority());
        cv.put("sessionIcon", session.getSessionIcon());
        cv.put("messageType", session.getMessageType().toString());
        cv.put("sessionName", session.getSessionName());
        cv.put("sessionContent", session.getSessionContent());
        cv.put("sessionIsRead", session.getSessionIsRead());
        DbConnectionManager.getInstance().getConnection().beginTransaction();
        try {
            DbConnectionManager.getInstance().getConnection()
                    .replace("sessioninfo", null, cv);
            DbConnectionManager.getInstance().getConnection()
                    .setTransactionSuccessful();
        } finally {
            DbConnectionManager.getInstance().getConnection().endTransaction();
        }
    }

    @Override
    public List<SLSession> getLatestSessions(int count) {
        if (DbConnectionManager.getInstance().getConnection() == null) {
            return new ArrayList<SLSession>();
        }
        Cursor cursor = DbConnectionManager.getInstance().getConnection()
                .rawQuery(GET_LATEST_SESSIONS,
                        new String[]{String.valueOf(count)});
        List<SLSession> sessions = new ArrayList<SLSession>();
        while (cursor.moveToNext()) {
            sessions.add(getSessionByCursor(cursor));
        }
        cursor.close();
        return sessions;
    }

    @Override
    public List<SLSession> getPraySessions(int count) {
        if (DbConnectionManager.getInstance().getConnection() == null) {
            return new ArrayList<SLSession>();
        }
        Cursor cursor = DbConnectionManager.getInstance().getConnection()
                .rawQuery(GET_PRAY_SESSIONS,
                        new String[]{String.valueOf(count)});
        List<SLSession> sessions = new ArrayList<SLSession>();
        while (cursor.moveToNext()) {
            sessions.add(getSessionByCursor(cursor));
        }
        cursor.close();
        return sessions;
    }

    @Override
    public List<SLSession> getMeetingInteractSessions(int count) {
        if (DbConnectionManager.getInstance().getConnection() == null) {
            return new ArrayList<SLSession>();
        }
        Cursor cursor = DbConnectionManager.getInstance().getConnection()
                .rawQuery(GET_MEETING_INTERACT_SESSIONS,
                        new String[]{String.valueOf(count)});
        List<SLSession> sessions = new ArrayList<SLSession>();
        while (cursor.moveToNext()) {
            sessions.add(getSessionByCursor(cursor));
        }
        cursor.close();
        return sessions;
    }

    @Override
    public List<SLSession> getMeetSessions(int count) {
        if (DbConnectionManager.getInstance().getConnection() == null) {
            return new ArrayList<SLSession>();
        }
        Cursor cursor = DbConnectionManager.getInstance().getConnection()
                .rawQuery(GET_MEET_SESSIONS,
                        new String[]{String.valueOf(count)});
        List<SLSession> sessions = new ArrayList<SLSession>();
        while (cursor.moveToNext()) {
            sessions.add(getSessionByCursor(cursor));
        }
        cursor.close();
        return sessions;
    }

    @Override
    public SLSession getSessionByTargetId(String targetId) {
        Cursor cursor = DbConnectionManager.getInstance().getConnection()
                .rawQuery(GET_SESSION_BY_TARGETID,
                        new String[]{targetId});
        while (cursor.moveToNext()) {
            return getSessionByCursor(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    @Override
    public void deleteSession(String sessionId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL(DELETE_SESSION, new Object[]{sessionId});
    }

    @Override
    public void updateSessionName(String sessionName, String targetId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL(UPDATE_SESSIONNAME, new String[]{sessionName, targetId});

    }

    @Override
    public void updateSessionContent(String sessionContent, String targetId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL("update sessioninfo set sessionContent=? where targetId=?", new String[]{sessionContent, targetId});

    }

    @Override
    public void updateSessionStatus(String sessionStatus, String targetId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL("update sessioninfo set sessionIsRead=? where targetId=?", new String[]{sessionStatus, targetId});

    }

    @Override
    public void updateSessionIconAndSessionName(String sessionName, String sessionIcon, String targetId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL("update sessioninfo set sessionName=?,sessionIcon=? where targetId=?", new String[]{sessionName, sessionIcon, targetId});
    }

    /**
     * 绑定sql的数据
     */
    public static SQLiteStatement bindData(SQLiteStatement stat, SLSession session) {
        stat.bindString(1, session.getTargetId());
        stat.bindString(2, session.getSessionType().toString());
        stat.bindString(3, session.getLastMessageId());
        stat.bindString(4, String.valueOf(session.getPriority()));
        stat.bindString(5, session.getSessionIcon());
        stat.bindString(6, session.getMessageType().toString());
        stat.bindString(7, session.getSessionName());
        stat.bindString(8, session.getSessionContent());
        stat.bindString(9, session.getSessionIsRead()+"");
        return stat;
    }

    public SLSession getSessionByCursor(Cursor cursor) {
        SLSession session = new SLSession();
        session.setSessionIcon(cursor.getString(cursor.getColumnIndex("sessionIcon")));
        session.setLastMessageId(cursor.getString(cursor
                .getColumnIndex("lastMessageId")));
        session.setPriority(cursor.getLong(cursor
                .getColumnIndex("priority")));
        session.setSessionType(SessionType.valueOf(cursor.getString(cursor
                .getColumnIndex("sessionType"))));
        session.setTargetId(cursor.getString(cursor.getColumnIndex("targetId")));
        session.setSessionName(cursor.getString(cursor
                .getColumnIndexOrThrow("sessionName")));
        session.setSessionContent(cursor.getString(cursor
                .getColumnIndex("sessionContent")));
        session.setSessionIsRead(cursor.getInt(cursor
                .getColumnIndex("sessionIsRead")));
        try {
            session.setMessageType(MessageType.valueOf(cursor
                    .getString(cursor.getColumnIndex("messageType"))));
        } catch (Exception ex) {
            session.setMessageType(MessageType.TEXT);
        }
        return session;
    }
}
