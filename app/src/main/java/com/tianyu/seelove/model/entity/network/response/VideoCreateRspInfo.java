package com.tianyu.seelove.model.entity.network.response;

import com.tianyu.seelove.model.entity.network.response.base.ResponseInfo;
import com.tianyu.seelove.model.entity.video.SLVideo;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 响应实体
 */
public class VideoCreateRspInfo extends ResponseInfo {
    private SLVideo video;

    public void setVideo(SLVideo video) {
        this.video = video;
    }

    public SLVideo getVideo() {
        return video;
    }
}