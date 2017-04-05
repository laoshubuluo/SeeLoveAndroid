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
        contentValues.put("targetId", session.getTargetId());
        contentValues.put("sessionType", session.getSessionType().toString());
        contentValues.put("lastMessageId", session.getLastMessageId());
        contentValues.put("priority", session.getPriority());
        contentValues.put("sessionIcon", session.getSessionIcon());
        contentValues.put("messageType", session.getMessageType().toString());
        contentValues.put("sessionName", session.getSessionName());
        contentValues.put("sessionContent", session.getSessionContent());
        contentValues.put("sessionIsRead", session.getSessionIsRead());
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
        session.setTargetId(cursor.getString(cursor.getColumnIndex("targetId")));
        session.setSessionType(SessionType.valueOf(cursor.getString(cursor.getColumnIndex("sessionType"))));
        session.setLastMessageId(cursor.getString(cursor.getColumnIndex("lastMessageId")));
        session.setPriority(cursor.getInt(cursor.getColumnIndex("priority")));
        session.setSessionIcon(cursor.getString(cursor.getColumnIndex("sessionIcon")));
        session.setMessageType(MessageType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("messageType"))));
        session.setSessionName(cursor.getString(cursor.getColumnIndex("sessionName")));
        session.setSessionContent(cursor.getString(cursor.getColumnIndex("sessionContent")));
        session.setSessionIsRead(cursor.getInt(cursor.getColumnIndex("sessionIsRead")));
        return session;
    }
}
