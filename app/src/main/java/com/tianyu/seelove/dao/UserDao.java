package com.tianyu.seelove.dao;

import com.tianyu.seelove.model.entity.user.SLUser;

import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 用户数据访问层接口
 * @date 2017-04-02 08:41
 */
public interface UserDao {

    /**
     * 添加用户
     * @param user
     * @param status User.STATUS_*
     * @return
     */
    public boolean add(SLUser user, String status);

    /**
     * 添加用户
     *
     * @param userList
     * @return
     */
    public boolean add(List<SLUser> userList);

    /**
     * 添加用户，如果不存在
     *
     * @param user
     * @return
     */
    public boolean addIfNotExist(SLUser user);

    /**
     * 根据Uid获取User信息
     *
     * @param uid
     * @return
     */
    public SLUser getUserByUid(String uid);

    /**
     * 根据phoneNo获取User信息
     *
     * @param phoneNo
     * @return
     */
    public SLUser getUserByPhoneNo(String phoneNo);

    /**
     * 根据uid获取好友类型
     *
     * @param userId
     * @return
     */
    public String getUserStatusById(long userId);

    /**
     * 根据状态获取User信息
     *
     * @param status
     * @return
     */
    public List<SLUser> getUserByStatus(String status);

    /**
     * 获取好友列表
     *
     * @param
     * @return
     */
    public List<SLUser> getFriendList();

    /**
     * 获取所有
     *
     * @param
     * @return
     */
    public List<SLUser> getAll();

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public boolean updateUser(SLUser user);


    /**
     * 根据Uid更新用户头像
     *
     * @param uid
     * @return
     */
    public boolean updateUserHeadByUid(String uid, String headUrl);

    /**
     * 根据status更新用户信仰等级
     *
     * @param status User.STATUS_*
     * @return
     */
    public boolean updateFaithLevelByStatus(String status, int level);

    /**
     * 根据uid更新用户状态
     *
     * @param status User.STATUS_*
     * @return
     */
    public boolean updateStatusByUid(String uid, String status);

    /**
     * 根据uid更新免打扰状态
     *
     * @param bnb
     * @return
     */
    public boolean updateBnbByUid(String uid, String bnb);

    /**
     * 删除用户信息
     *
     * @param userId
     * @return
     */
    public boolean deleteUser(long userId);

    /**
     * 删除“旧”的“好友”数据
     *
     * @param timestampNew
     * @return
     */
    public boolean deleteOldFriendData(long timestampNew);
}
