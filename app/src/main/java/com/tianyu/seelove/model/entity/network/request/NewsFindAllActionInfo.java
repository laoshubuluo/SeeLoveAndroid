package com.tianyu.seelove.model.entity.network.request;


import com.tianyu.seelove.model.entity.network.request.base.ActionInfo;
import com.tianyu.seelove.model.entity.user.SLFollow;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class NewsFindAllActionInfo extends ActionInfo {

    private long userId; // 用户id,唯一标示

    public NewsFindAllActionInfo(int actionId, long userId) {
        super(actionId);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }}