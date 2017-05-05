package com.tianyu.seelove.dao;

import com.tianyu.seelove.model.entity.video.SLVideo;

import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 短视屏数据访问层接口
 * @date 2017-04-23 04:36
 */
public interface VideoDao {

    /**
     * 添加短视屏信息
     * @param slVideo
     */
    void addVideo(SLVideo slVideo);

    /**
     * 根据视频ID获取视频详情
     * @param videoId
     * @return
     */
    SLVideo getVideoByVideoId(long videoId);

    /**
     * 根据视频ID删除视频信息
     * @param videoId
     * @return
     */
    boolean deleteVideoByVideoId(long videoId);

    /**
     * 获取当前用户所有短视屏详情
     * @param userId
     * @return
     */
    List<SLVideo> getVideoListByUserId(long userId);
}
