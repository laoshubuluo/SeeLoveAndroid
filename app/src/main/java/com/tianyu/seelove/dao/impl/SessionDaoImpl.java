package com.tianyu.seelove.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
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
 * @date 2017-04-05 19:42
 */
public class SessionDaoImpl implements SessionDao {
    private static final String GET_LATEST_SESSIONS = "SELECT DISTINCT * FROM SESSIONINFO ORDER BY priority DESC LIMIT ?";
    private static final String GET_SESSION_BY_TARGETID = "SELECT DISTINCT * FROM SESSIONINFO WHERE targetId = ?";
    private static final String DELETE_SESSION = "DELETE FROM SESSIONINFO WHERE targetId = ?";
    private static final String UPDATE_SESSIONNAME_BY_TARGETID = "UPDATE SESSIONINFO SET sessionName = ? WHERE targetId = ?";
    private static final String UPDATE_SESSIONCONTENT_BY_TARGETID = "UPDATE SESSIONINFO SET sessionContent = ? WHERE targetId = ?";
    private static final String UPDATE_SESSIONCONTENT_AND_SESSIONNAME_BY_TARGETID = "UPDATE SESSIONINFO SET sessionName = ?,sessionIcon = ? WHERE targetId = ?";

    @Override
    public synchronized void addSession(SLSession session) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("TargetId", session.getTargetId());
        contentValues.put("SessionType", session.getSessionType().toString());
        contentValues.put("LastMessageId", session.getLastMessageId());
        contentValues.put("Priority", session.getPriority());
        contentValues.put("SessionIcon", session.getSessionIcon());
        contentValues.put("MessageType", session.getMessageType().toString());
        contentValues.put("SessionName", session.getSessionName());
        contentValues.put("SessionContent", session.getSessionContent());
        contentValues.put("SessionIsRead", session.getSessionIsRead());
        DbConnectionManager.getInstance().getConnection().beginTransaction();
        try {
            DbConnectionManager.getInstance().getConnection()
                    .replace("SESSIONINFO", null, contentValues);
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
                .execSQL(UPDATE_SESSIONNAME_BY_TARGETID, new String[]{sessionName, targetId});

    }

    @Override
    public void updateSessionContent(String sessionContent, String targetId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL(UPDATE_SESSIONCONTENT_BY_TARGETID, new String[]{sessionContent, targetId});

    }

    @Override
    public void updateSessionIconAndSessionName(String sessionName, String sessionIcon, String targetId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL(UPDATE_SESSIONCONTENT_AND_SESSIONNAME_BY_TARGETID, new String[]{sessionName, sessionIcon, targetId});
    }

    public SLSession getSessionByCursor(Cursor cursor) {
        SLSession session = new SLSession();
        session.setTargetId(cursor.getString(cursor.getColumnIndex("TargetId")));
        session.setSessionType(SessionType.CHAT);
        session.setLastMessageId(cursor.getString(cursor.getColumnIndex("LastMessageId")));
        session.setPriority(cursor.getInt(cursor.getColumnIndex("Priority")));
        session.setSessionIcon(cursor.getString(cursor.getColumnIndex("SessionIcon")));
        session.setMessageType(MessageType.TEXT);
        session.setSessionName(cursor.getString(cursor.getColumnIndex("SessionName")));
        session.setSessionContent(cursor.getString(cursor.getColumnIndex("SessionContent")));
        session.setSessionIsRead(cursor.getInt(cursor.getColumnIndex("SessionIsRead")));
        return session;
    }
}
