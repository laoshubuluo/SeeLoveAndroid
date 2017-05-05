package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.network.VolleyManager;
import com.tianyu.seelove.network.request.VideoCreateRequest;
import com.tianyu.seelove.network.request.VideoDeleteRequest;
import java.util.List;

/**
 * author : L.jinzhu
 * date : 2015/8/24
 * introduce : 视频控制
 */
public class VideoController {
    private Handler handler;
    private Context context;

    public VideoController(Context c, Handler h) {
        this.context = c;
        this.handler = h;
    }

    /**
     * 保存用户视频
     */
    public void create(SLVideo video) {
        VideoCreateRequest request = new VideoCreateRequest(handler, context, video);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }

    /**
     * 删除用户视频
     */
    public void delete(Long userId, List<Long> videoIdList) {
        VideoDeleteRequest request = new VideoDeleteRequest(handler, context, userId, videoIdList);
        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
    }
}
