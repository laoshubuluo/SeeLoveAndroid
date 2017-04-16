package com.tianyu.seelove.model.entity.user;

import java.io.Serializable;

/**
 * 关注实体类
 *
 * @author L.jinzhu
 * @date 2017-03-31 18:07
 */
public class SLFollow implements Serializable {
    private long userId; // 用户id,唯一标示
    private long followUserId; // 关注的用户id,唯一标示

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFollowUserId() {
        return followUserId;
    }

    public void setFollowUserId(long followUserId) {
        this.followUserId = followUserId;
    }
}
