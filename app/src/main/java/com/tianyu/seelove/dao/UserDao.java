package com.tianyu.seelove.dao;

import com.tianyu.seelove.model.entity.user.SLUser;

/**
 * @author shisheng.zhao
 * @Description: 用户数据访问层接口
 * @date 2017-04-02 08:41
 */
public interface UserDao {

    /**
     * 添加用户信息
     *
     * @param slUser
     */
    void addUser(SLUser slUser);

    /**
     * 根据userId获取user信息
     *
     * @param userId
     * @return
     */
    SLUser getUserByUserId(long userId);

    /**
     * 根据userId更新用户信息
     *
     * @param slUser
     * @return
     */
    boolean updateUserByUserId(SLUser slUser);

    /**
     * 根据userId更新用户头像
     *
     * @param userId
     * @param headUrl
     * @return
     */
    boolean updateUserHeadUrlByUserId(String userId, String headUrl);

    /**
     * 根据userId更新关注数
     * @param userId
     * @param followCount
     * @return
     */
    boolean updateUserFollowCountByUserId(long userId,int followCount);

    /**
     * 根据userId更新被关注数
     * @param userId
     * @param followedCount
     * @return
     */
    boolean updateUserFollowedCountByUserId(long userId,int followedCount);

    /**
     * 根据userId删除用户信息
     *
     * @param userId
     * @return
     */
    boolean deleteUserByUserId(String userId);
}
