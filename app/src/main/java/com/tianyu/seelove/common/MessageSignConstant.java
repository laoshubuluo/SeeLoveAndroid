package com.tianyu.seelove.common;

/**
 * author : L.jinzhu
 * date : 2015/8/1
 * introduce : Message标记常量
 */
public class MessageSignConstant {
    private MessageSignConstant() {
    }

    public static final int SERVER_OR_NETWORK_ERROR = 500; // 服务器or网络错误
    public static final int UNKNOWN_ERROR = 9999; // 未知错误、内部错误

    public static final int USER_REGIST_SUCCESS = 1001;
    public static final int USER_REGIST_FAILURE = 1002;

    public static final int USER_LOGIN_SUCCESS = 1003;
    public static final int USER_LOGIN_FAILURE = 1004;

    public static final int USER_FIND_ALL_SUCCESS = 1005;
    public static final int USER_FIND_ALL_FAILURE = 1006;

    public static final int USER_UPDATE_SUCCESS = 1007;
    public static final int USER_UPDATE_FAILURE = 1008;

    public static final int USER_FIND_DETAIL_SUCCESS = 1009;
    public static final int USER_FIND_DETAIL_FAILURE = 1010;

    public static final int VIDEO_CREATE_SUCCESS = 1011;
    public static final int VIDEO_CREATE_FAILURE = 1012;

    public static final int FOLLOW_SUCCESS = 1013;
    public static final int FOLLOW_FAILURE = 1014;

    public static final int FOLLOW_FIND_ALL_BY_USER_SUCCESS = 1015;
    public static final int FOLLOW_FIND_ALL_BY_USER_FAILURE = 1016;

    public static final int FOLLOW_FIND_ALL_BY_FOLLOWED_USER_SUCCESS = 1017;
    public static final int FOLLOW_FIND_ALL_BY_FOLLOWED_USER_FAILURE = 1018;

    public static final int NEWS_FIND_ALL_SUCCESS = 1019;
    public static final int NEWS_FIND_ALL_FAILURE = 1020;
}