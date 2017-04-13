package com.tianyu.seelove.dao.impl;

import android.database.Cursor;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 会话访问层实现类
 * @date 2017-04-05 19:42
 */
public class SessionDaoImpl implements SessionDao {
    private static final String ADD_SESSION = "INSERT INTO SESSIONINFO(TargetId,SessionType,LastMessageId,Priority," +
            "SessionIcon,MessageType,SessionName,SessionContent,SessionIsRead) VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String GET_LATEST_SESSIONS = "SELECT DISTINCT * FROM SESSIONINFO ORDER BY Priority DESC LIMIT ?";
    private static final String GET_SESSION_BY_TARGETID = "SELECT DISTINCT * FROM SESSIONINFO WHERE TargetId = ?";
    private static final String DELETE_SESSION = "DELETE FROM SESSIONINFO WHERE TargetId = ?";
    private static final String UPDATE_SESSIONNAME_BY_TARGETID = "UPDATE SESSIONINFO SET SessionName = ? WHERE TargetId = ?";
    private static final String UPDATE_SESSIONCONTENT_BY_TARGETID = "UPDATE SESSIONINFO SET SessionContent = ? WHERE TargetId = ?";
    private static final String UPDATE_SESSIONCONTENT_AND_SESSIONNAME_BY_TARGETID = "UPDATE SESSIONINFO SET SessionName = ?,SessionIcon = ? WHERE TargetId = ?";

    @Override
    public void addSession(SLSession session) {
        if (null == session) {
            return;
        }
        if (null == getSessionByTargetId(session.getTargetId())) {
            insertSession(session);
        } else {
            deleteSession(session.getTargetId());
            insertSession(session);
        }
    }

    private boolean insertSession(SLSession session) {
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(ADD_SESSION,
                    new String[]{String.valueOf(session.getTargetId()), session.getSessionType().toString(),
                            session.getLastMessageId(), String.valueOf(session.getPriority()),
                            session.getSessionIcon(), session.getMessageType().toString(),
                            session.getSessionName(), session.getSessionContent(), String.valueOf(session.getSessionIsRead())});
            LogUtil.i("db execute sql success： " + ADD_SESSION);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + ADD_SESSION, ex);
            return false;
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
    public SLSession getSessionByTargetId(long targetId) {
        Cursor cursor = DbConnectionManager.getInstance().getConnection()
                .rawQuery(GET_SESSION_BY_TARGETID,
                        new String[]{String.valueOf(targetId)});
        while (cursor.moveToNext()) {
            return getSessionByCursor(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    @Override
    public void deleteSession(long sessionId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL(DELETE_SESSION, new Object[]{String.valueOf(sessionId)});
    }

    @Override
    public void updateSessionName(String sessionName, String targetId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL(UPDATE_SESSIONNAME_BY_TARGETID, new String[]{sessionName, targetId});

    }

    @Override
    public void updateSessionContent(String sessionContent, long targetId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL(UPDATE_SESSIONCONTENT_BY_TARGETID, new String[]{sessionContent, String.valueOf(targetId)});

    }

    @Override
    public void updateSessionIconAndSessionName(String sessionName, String sessionIcon, String targetId) {
        DbConnectionManager.getInstance().getConnection()
                .execSQL(UPDATE_SESSIONCONTENT_AND_SESSIONNAME_BY_TARGETID, new String[]{sessionName, sessionIcon, targetId});
    }

    public SLSession getSessionByCursor(Cursor cursor) {
        SLSession session = new SLSession();
        session.setTargetId(cursor.getLong(cursor.getColumnIndex("TargetId")));
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
