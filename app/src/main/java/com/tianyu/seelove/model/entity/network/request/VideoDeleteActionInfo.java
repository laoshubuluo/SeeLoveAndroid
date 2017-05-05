package com.tianyu.seelove.model.entity.network.request;


import com.tianyu.seelove.model.entity.network.request.base.ActionInfo;

import java.util.List;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class VideoDeleteActionInfo extends ActionInfo {
    private Long userId;
    private List<Long> videoIdList;

    public VideoDeleteActionInfo(int actionId, Long userId, List<Long> videoIdList) {
        super(actionId);
        this.userId = userId;
        this.videoIdList = videoIdList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getVideoIdList() {
        return videoIdList;
    }

    public void setVideoIdList(List<Long> videoIdList) {
        this.videoIdList = videoIdList;
    }
}