package com.tianyu.seelove.model.entity.network.response;

import com.tianyu.seelove.model.entity.network.response.base.ResponseInfo;
import com.tianyu.seelove.model.entity.video.SLVideo;

import java.util.List;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 响应实体
 */
public class VideoFindByUserRspInfo extends ResponseInfo {
    private List<SLVideo> videoList;

    public List<SLVideo> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<SLVideo> videoList) {
        this.videoList = videoList;
    }
}