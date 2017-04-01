package com.tianyu.seelove.model.entity.video;

/**
 * 短视频实体类
 * @author shisheng.zhao
 * @date 2017-03-31 17:56
 */
public class VideoInfo {
    private long videoId; // 短视频id
    private long userId; // 用户id
    private String videoTitle; // 短视频标题
    private String videoTime; // 短视频时长
    private int isDefault; // 是否为默认展示短视频 1是;0否
    private String videoImg; // 短视频封皮
    private String videoUrl; // 短视频播放地址
    private String remark; // 保留域

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "videoId=" + videoId +
                ", userId=" + userId +
                ", videoTitle='" + videoTitle + '\'' +
                ", videoTime='" + videoTime + '\'' +
                ", isDefault=" + isDefault +
                ", videoImg='" + videoImg + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
