package com.tianyu.seelove.common;

/**
 * Created by liangjinzhu on 17/4/10.
 * 请求消息码
 */
public class RequestCode {

    public static final int TOKEN_SUCCESS = 200;

    public static final int USER_CREATE = 1001;
    public static final int USER_LOGIN = 1002;
    public static final int USER_FIND_ALL = 1003;
    public static final int USER_FIND_DETAIL = 1004;
    public static final int USER_UPDATE = 1005;

    public static final int VIDEO_CREATE = 1006;

    public static final int FOLLOW = 1010;
    public static final int FOLLOW_FIND_BY_USER = 1011;// 我关注的
    public static final int FOLLOW_FIND_BY_FOLLOWED_USER = 1012;// 关注我的

    public static final int NEWS_FIND_ALL = 1015;// 动态
}
