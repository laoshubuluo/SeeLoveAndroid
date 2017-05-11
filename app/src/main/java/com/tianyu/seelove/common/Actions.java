package com.tianyu.seelove.common;

/**
 * @author shisheng.zhao
 * @Description: 定义广播Action
 * @date 2017-04-01 10:27
 */
public class Actions {
    /**
     * 单聊消息发生变动时更新窗口的广播消息
     */
    public static final String SINGLEMESSAGE_ADD_ACTION = "wmgj.amen.action.singlemessage_add_action";
    /**
     * 发送单聊消息广播
     */
    public static final String ACTION_SNED_SINGLE_MESSAGE = "wmgj.amen.send_single_message";
    /**
     * 发送广播通知刷新动态列表
     */
    public static final String ACTION_UPDATE_FOLLOW_LIST = "see.love.update_follow_list";
    /**
     * 更新消息状态广播
     */
    public static final String ACTION_UPDATE_MESSAGE_STATUE = "wmgj.amen.update_message_status";
    /**
     * 更新图片消息上传进度广播
     */
    public static final String ACTION_UPDATE_IMGMESSAGE_PROCESS = "wmgj.amen.update_imgmessage_process";
    /**
     * 删除消息广播
     */
    public static final String ACTION_DELETE_MESSAGE_BYMESSAGEID = "wmgj.amen.delete_message_bymessageid";
    /**
     * 接收单聊消息广播
     */
    public static final String ACTION_RECEIVER_SINGLE_MESSAGE = "wmgj.amen.receiver_single_message";
    /**
     * Session 广播
     */
    public static final String ACTION_SESSION = "wmgj.amen.session";
    /**
     * 消息的已读未读状态变化
     */
    public static final String MESSAGE_READ_CHANGE = "wmgj.amen.action.meesage_read_change";
    /**
     * 融云链接状态监听
     */
    public static final String CONNECTION_SUCCESS = "wmgj.amen.action.CONNECTION_SUCCESS"; // 链接成功
    public static final String CONNECTION_FAILED = "wmgj.amen.action.CONNECTION_FAILED"; // 断开链接
    public static final String CONNECTION_LINKING = "wmgj.amen.action.CONNECTION_LINGING"; // 连接中...
    public static final String CONNECTION_UNAVAILABLE = "wmgj.amen.action.CONNECTION_UNAVAILABLE"; // 网络不可用
    /**
     * 好友关系 广播
     */
    public static final String ACTION_CLEAN_USER_SESSION = "wmgj.amen.action.clean_user_session";//删除好友刷新会话列表
    // 退出帐号广播通知
    public static final String ACTION_EXIT_APP = "see.love.action.exit_app";
    // 登录成功广播通知
    public static final String ACTION_LOGIN_SUCCESS = "see.love.action.login_success";
}
