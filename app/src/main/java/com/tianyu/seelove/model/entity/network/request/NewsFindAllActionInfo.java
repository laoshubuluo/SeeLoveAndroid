package com.tianyu.seelove.model.entity.network.request;


import com.tianyu.seelove.model.entity.network.request.base.ActionInfo;
import com.tianyu.seelove.model.entity.user.SLFollow;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class NewsFindAllActionInfo extends ActionInfo {
    private int pageNumber;
    private int dataGetType;

    private long userId; // 用户id,唯一标示

    public NewsFindAllActionInfo(int actionId, int pageNumber, int dataGetType, long userId) {
        super(actionId);
        this.pageNumber = pageNumber;
        this.dataGetType = dataGetType;
        this.userId = userId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getDataGetType() {
        return dataGetType;
    }

    public void setDataGetType(int dataGetType) {
        this.dataGetType = dataGetType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}