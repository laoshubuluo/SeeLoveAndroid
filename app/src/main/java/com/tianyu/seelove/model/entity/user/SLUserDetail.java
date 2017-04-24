package com.tianyu.seelove.model.entity.user;

import com.tianyu.seelove.model.entity.video.SLVideo;
import java.io.Serializable;
import java.util.List;

/**
 * 短视频和用户实体类
 * @author shisheng.zhao
 * @date 2017-03-31 17:56
 */
public class SLUserDetail implements Serializable{
    private SLUser user;
    private SLVideo defultVideo;// 默认视频
    private List<SLVideo> videoList; // 用户视频列表
    private List<SLUser> followUserList; // 用户关注列表

    public SLUser getUser() {
        return user;
    }

    public void setUser(SLUser user) {
        this.user = user;
    }

    public SLVideo getDefultVideo() {
        return defultVideo;
    }

    public void setDefultVideo(SLVideo defultVideo) {
        this.defultVideo = defultVideo;
    }

    public List<SLVideo> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<SLVideo> videoList) {
        this.videoList = videoList;
    }

    public List<SLUser> getFollowUserList() {
        return followUserList;
    }

    public void setFollowUserList(List<SLUser> followUserList) {
        this.followUserList = followUserList;
    }
}
