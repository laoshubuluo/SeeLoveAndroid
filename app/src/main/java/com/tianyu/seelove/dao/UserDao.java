package com.tianyu.seelove.dao;

import com.tianyu.seelove.model.entity.user.SLUser;

/**
 * @author shisheng.zhao
 * @Description: 用户数据访问层接口
 * @date 2017-04-02 08:41
 */
public interface UserDao {

    /**
     * 根据userId获取user信息
     * @param userId
     * @return
     */
    public SLUser getUserByUserId(String userId);

    /**
     * 根据userId更新用户信息
     * @param slUser
     * @return
     */
    public boolean updateUserByUserId(SLUser slUser);

    /**
     * 根据userId更新用户头像
     * @param userId
     * @param headUrl
     * @return
     */
    public boolean updateUserHeadUrlByUserId(String userId, String headUrl);

    /**
     * 根据userId删除用户信息
     * @param userId
     * @return
     */
    public boolean deleteUserByUserId(long userId);
}
