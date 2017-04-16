package com.tianyu.seelove.controller;

import android.content.Context;
import android.os.Handler;

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

//    /**
//     * 获取用户视频
//     */
//    public void findByUser() {
//        VideoFindByUserRequest request = new VideoFindByUserRequest(handler, context);
//        VolleyManager.getInstance(context).add2RequestQueue(request.getRequest());
//    }
}
