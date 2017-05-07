package com.tianyu.seelove.model.enums;

/**
 * @author shisheng.zhao
 * @Description: 分享消息类型
 * @date 2017-05-07 17:27
 */
public enum ShareTo {
    WECHAT(1),//微信朋友
    WECHAT_CIRCLE(2),//微信朋友圈
    QQ(3),//QQ
    QQ_CIRCLE(4),//QQ朋友圈
    SINA_WEIBO(5),//新浪微博
    UNKONWN(0);//未知

    private int type;

    ShareTo(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ShareTo parse(int type) {
        for (ShareTo item : values()) {
            if (item.getType() == type)
                return item;
        }
        return UNKONWN;
    }
}