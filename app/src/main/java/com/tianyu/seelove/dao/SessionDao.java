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
     * 根据sessionId获取session详情
     * @param targetId
     * @return
     */
    public SLSession getSessionByTargetId(String targetId);

    /**
     * 根据sessionId更新sessionName
     * @param sessionName
     * @param targetId
     */
    public void updateSessionName(String sessionName, String targetId);

    /**
     * 根据sessionId更新sessionContent
     * @param sessionContent
     * @param targetId
     */
    public void updateSessionContent(String sessionContent, String targetId);

    /**
     * 根据sessionId更新sessionName和sessionIcon
     * @param sessionName
     * @param sessionIcon
     * @param targetId
     */
    public void updateSessionIconAndSessionName(String sessionName, String sessionIcon, String targetId);
}
