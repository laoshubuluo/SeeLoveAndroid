package com.tianyu.seelove.dao;

import com.tianyu.seelove.model.entity.message.SLSession;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 和会话相关的数据访问层接口
 * @date 2017-04-01 10:33
 */
public interface SessionDao {

    /**
     * 向本地数据库中加入新的会话
     * @param session
     */
    public void addSession(SLSession session);

    /**
     * 通过id从本地数据库中删除对应的会话
     * @param sessionId
     */
    public void deleteSession(String sessionId);

    /**
     * 获取最近的会话列表
     * @param count 需要获取的会话的数量
     * @return
     */
    public List<SLSession> getLatestSessions(int count);

    /**
     * 获取代祷会话列表
     * @param count
     * @return
     */
    public List<SLSession> getPraySessions(int count);

    /**
     * 获取聚会互动信息会话列表
     * @param count
     * @return
     */
    public List<SLSession> getMeetingInteractSessions(int count);
    public List<SLSession> getMeetSessions(int count);

    public SLSession getSessionByTargetId(String targetId);

    public void updateSessionName(String sessionName, String targetId);

    public void updateSessionContent(String sessionContent, String targetId);

    /**
     * 更新会话状态--目前主要是针对系统文章
     * @param sessionStatus: 0代表未读,1代表已读
     * @param targetId
     */
    public void updateSessionStatus(String sessionStatus, String targetId);

    public void updateSessionIconAndSessionName(String sessionName, String sessionIcon, String targetId);
}
