package com.tianyu.seelove.dao;

import com.tianyu.seelove.model.entity.message.SLMessage;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 消息数据访问层接口
 * @date 2017-04-01 08:11
 */
public interface MessageDao {

    /**
     * 向本地数据库中添加一条消息记录
     * @param message
     */
    public void addMessage(SLMessage message);

    /**
     * 根据用户ID获取所有消息列表
     * @return
     */
    public List<SLMessage> getMessageByPage(String from, String to, int start, int count);

    /**
     * 單聊中清空兩者之間的消息來往
     * @param from
     * @param to
     */
    public void cleanSingalChatMessage(String from, String to);

    /**
     * 通过消息id获取消息
     * @param messageId
     * @return
     */
    public SLMessage getMessageById(String messageId);

    /**
     * 通过消息id删除消息
     * @param messageId
     * @return
     */
    public void deleteMessageById(String messageId);

    /**
     * 更新消息状态为成功
     * @param messageId
     */
    public void markAsSusMsg(String messageId);

    /**
     * 更新消息状态为失败
     * @param messageId
     */
    public void markAsFailedMsg(String messageId);

    /**
     * 获取和某人聊天过程中的未读消息总数
     * @param from 需要查询的人
     * @return
     */
    public int getUnReadMessageCount(String from);

    /**
     * 把某人的消息全部置成已读 点对点
     * @param from
     */
    public void setAllRead(String from, String to);

    /**
     * 获取所有未读的消息总数 点对点消息
     * @return
     */
    public int getUnreadCount();

    /**
     * 根据MessageId 更新消息状态为已读
     * @param messageId
     */
    public void updateMessageIsReadByMessageId(String messageId);

    /**
     * 更新消息缩略图
     * @param messageId
     * @param thumUrl
     */
    public void updateMessageThumUrlByMessageId(String messageId, String thumUrl);

    /**
     * 获取单聊图片消息
     * @param from
     * @param to
     * @return
     */
    public List<SLMessage> getImageMessage(String from, String to);
}