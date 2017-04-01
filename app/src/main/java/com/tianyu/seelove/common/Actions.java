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
     * 群聊消息发生变动时更新窗口的广播消息
     */
    public static final String GROUPMESSAGE_ADD_ACTION = "wmgj.amen.action.groupmessage_add_action";
    /**
     * 发送单聊消息广播
     */
    public static final String ACTION_SNED_SINGLE_MESSAGE = "wmgj.amen.send_single_message";
    /**
     * 发送群组消息广播
     */
    public static final String ACTION_SNED_GROUP_MESSAGE = "wmgj.amen.send_group_message";
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
     * 接收群聊消息广播
     */
    public static final String ACTION_RECEIVER_GROUP_MESSAGE = "wmgj.amen.receiver_group_message";
    /**
     * 群组消息发送融云返回状态码
     */
    public static final String ACTION_GROUP_MESSAGE_RCODE = "wmgj.amen.group_message_rcode";
    /**
     * Session 广播
     */
    public static final String ACTION_SESSION = "wmgj.amen.session";
    /**
     * 代祷Session 广播
     */
    public static final String ACTION_PRAY_SESSION = "wmgj.amen.pray_session";
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
    public static final String ACTION_FRIEND_IS_FRIEND = "wmgj.amen.action.friend_is_friend";//成为好友
    public static final String ACTION_FRIEND_BE_REQUEST = "wmgj.amen.action.friend_be_request";//被对方申请添加为好友
    public static final String ACTION_FRIEND_DELETE = "wmgj.amen.action.friend_delete";//删除好友
    public static final String ACTION_FRIEND_UPDATE = "wmgj.amen.action.friend_update";//好友状态更新
    public static final String ACTION_CLEAN_USER_SESSION = "wmgj.amen.action.clean_user_session";//删除好友刷新会话列表
    public static final String ACTION_BLACKLIST_ADD = "wmgj.amen.action.blacklist_add";//加入黑名单
    public static final String ACTION_BLACKLIST_DELETE = "wmgj.amen.action.blacklist_delete";//移除黑名单

    /**
     * 用户邀请、添加（通讯录好友推荐界面）广播
     */
    public static final String ACTION_USER_ADD = "wmgj.amen.action.user_add";//用户添加
    public static final String ACTION_USER_INVATE = "wmgj.amen.action.user_invate";//用户邀请
    /**
     * 查经讨论：已读未读状态变化
     */
    public static final String ACTION_BIBLE_DISCUSS_READ_CHANGE = "wmgj.amen.action.bible_discuss_read_change";
    /**
     * 软件更新广播
     */
    public static final String ACTION_APP_UPDATE = "wmgj.amen.action.app_update";//软件更新
    public static final String ACTION_TASK_SYSTEM_DEVOTE = "wmgj.amen.action.task_system_devote";//系统task－－支持阿门事工
    public static final String ACTION_TASK_SYSTEM_SHARE = "wmgj.amen.action.task_system_share";//系统task－－分享阿门app
    public static final String ACTION_TASK_SYSTEM_CONTACTS = "wmgj.amen.action.task_system_contacts";//系统task－－邀请通讯录好友
    /**
     * 任务的已读未读状态变化
     */
    public static final String ACTION_TASK_4_FRIEND_READ_CHANGE = "wmgj.amen.action.task_4_friend_read_change";
    public static final String ACTION_TASK_4_MEETING_READ_CHANGE = "wmgj.amen.action.task_4_meeting_read_change";

    /**
     * 聚会
     */
    public static final String ACTION_MEETING_RELATION_CHANGE = "wmgj.amen.action.meeting_relation_change";
    /**
     * 聚会位置更新
     */
    public static final String ACTION_MEETING_UPDATE_POSITION = "wmgj.amen.action.meeting_update_position";
    // 聚会信息发生变更通知广播
    public static final String ACTION_MEETING_UPDATE_MEETING = "wmgj.amen.action.meeting_update_meeting";
    // 聚会群组刷新UI
    public static final String ACTION_MEETING_REFRESH_MEETINGGROUP = "wmgj.amen.action.meeting_refresh_meetinggroup";
    /**
     * 支付结果
     */
    public static final String ACTION_PAY_COMPLETED = "wmgj.amen.action.pay_completed";

    // 代祷删除选中图片
    public static final String ACTION_PRAY_DELETE_IMAGE = "wmgj.amen.action.pray_delete_image";
    // 代祷添加选中图片
    public static final String ACTION_PRAY_ADD_IMAGE = "wmgj.amen.action.pray_add_image";
    // 代祷数据更新
    public static final String ACTION_PRAY_DATA_UPDATE = "wmgj.amen.action.pray_data_update";

    // 互动交流更新数据
    public static final String ACTION_MEETING_INTERACT_DATA_UPDATE = "wmgj.amen.action.meeting_interact_data_update";

    // 信息分享_删除评论
    public static final String ACTION_INFO_SHARE_DEL_COMMENT = "wmgj.amen.action.info_share_del_comment";

    // 信息分享评论_增加评论
    public static final String ACTION_INFO_SHARE_COMMENT_ADD_COMMENT = "wmgj.amen.action.info_share_comment_add_comment";
    // 信息分享评论_删除评论
    public static final String ACTION_INFO_SHARE_COMMENT_DEL_COMMENT = "wmgj.amen.action.info_share_comment_del_comment";
    // 信息分享评论_增加赞
    public static final String ACTION_INFO_SHARE_COMMENT_ADD_AGREE = "wmgj.amen.action.info_share_comment_add_agree";

    // 红点，支持阿门事工
    public static final String ACTION_RED_DOT_SHOW_4_DEVOTE_AMEN = "wmgj.amen.action.red_dot_show_4_devote_amen";

    // 分享成功
    public static final String ACTION_SHARE_SUCCESS = "wmgj.amen.action.share_success";
}
