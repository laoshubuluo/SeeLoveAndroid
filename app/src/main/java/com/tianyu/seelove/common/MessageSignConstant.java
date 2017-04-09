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

    public static final int USER_CREATE_SUCCESS = 1001;
    public static final int USER_CREATE_FAILURE = 1002;
}
