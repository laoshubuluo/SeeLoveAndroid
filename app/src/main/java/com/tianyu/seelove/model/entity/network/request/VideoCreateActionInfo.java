package com.tianyu.seelove.model.entity.network.request;


import com.tianyu.seelove.model.entity.network.request.base.ActionInfo;
import com.tianyu.seelove.model.entity.video.SLVideo;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class VideoCreateActionInfo extends ActionInfo {
    private SLVideo video;

    public VideoCreateActionInfo(int actionId, SLVideo video) {
        super(actionId);
        this.video = video;
    }

    public SLVideo getVideo() {
        return video;
    }

    public void setVideo(SLVideo video) {
        this.video = video;
    }
}