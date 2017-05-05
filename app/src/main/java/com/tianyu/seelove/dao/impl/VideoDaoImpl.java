package com.tianyu.seelove.dao.impl;

import android.database.Cursor;

import com.tianyu.seelove.dao.VideoDao;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 短视屏数据访问层实现类
 * @date 2017-04-23 16:38
 */
public class VideoDaoImpl implements VideoDao {
    // 插入短视屏数据
    public final static String sqlInsertVideoInfo = "INSERT INTO VIDEOINFO(VideoId,UserId,VideoTitle,VideoTime," +
            "IsDefault,VideoImg,VideoUrl,Remark) VALUES(?,?,?,?,?,?,?,?)";
    // 根据视频ID获取视频详情
    public final static String sqlSelectVideoByVideoId = "SELECT * FROM VIDEOINFO WHERE VideoId = ?";
    // 根据视频ID删除视频详情
    public final static String sqlDeleteVideoByVideoId = "DELETE FROM VIDEOINFO WHERE VideoId = ?";
    // 根据用户ID获取视频列表
    public final static String sqlSelectVideoListByUserId = "SELECT * FROM VIDEOINFO WHERE UserId = ?";

    @Override
    public void addVideo(SLVideo slVideo) {
        if (null == slVideo) {
            return;
        }
        if (null == getVideoByVideoId(slVideo.getVideoId())) {
            insertVideo(slVideo);
        } else {
            deleteVideoByVideoId(slVideo.getVideoId());
            insertVideo(slVideo);
        }
    }

    private boolean insertVideo(SLVideo slVideo) {
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    sqlInsertVideoInfo,
                    new String[]{String.valueOf(slVideo.getVideoId()), String.valueOf(slVideo.getUserId()), slVideo.getVideoTitle(),
                            slVideo.getVideoTime(), slVideo.getIsDefault(), slVideo.getVideoImg(),
                            slVideo.getVideoUrl(), slVideo.getRemark()});
            LogUtil.i("db execute sql success： " + sqlInsertVideoInfo);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + sqlInsertVideoInfo, ex);
            return false;
        }
    }

    @Override
    public SLVideo getVideoByVideoId(long videoId) {
        Cursor cursor = null;
        SLVideo slVideo = new SLVideo();
        try {
            cursor = DbConnectionManager.getInstance().getConnection().rawQuery(sqlSelectVideoByVideoId, new String[]{String.valueOf(videoId)});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                return parseUserFromCursor(cursor);
            } else
                return slVideo;
        } catch (Throwable ex) {
            LogUtil.e("db execute sql error： " + sqlSelectVideoByVideoId, ex);
            return slVideo;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public boolean deleteVideoByVideoId(long videoId) {
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    sqlDeleteVideoByVideoId,
                    new Object[]{String.valueOf(videoId)});
            LogUtil.i("db execute sql success： " + sqlDeleteVideoByVideoId);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + sqlDeleteVideoByVideoId, ex);
            return false;
        }
    }

    @Override
    public List<SLVideo> getVideoListByUserId(long userId) {
        Cursor cursor = DbConnectionManager.getInstance().getConnection().rawQuery(sqlSelectVideoListByUserId,
                new String[]{String.valueOf(userId)});
        List<SLVideo> slVideoList = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                slVideoList.add(parseUserFromCursor(cursor));
            }
        } finally {
            cursor.close();
        }
        return slVideoList;
    }

    /**
     * 从cursor中获取视频信息
     *
     * @param cursor
     */

    private SLVideo parseUserFromCursor(Cursor cursor) {
        SLVideo slVideo = new SLVideo();
        slVideo.setVideoId(cursor.getLong(cursor.getColumnIndexOrThrow("VideoId")));
        slVideo.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow("UserId")));
        slVideo.setVideoTitle(cursor.getString(cursor.getColumnIndexOrThrow("VideoTitle")));
        slVideo.setVideoTime(cursor.getString(cursor.getColumnIndexOrThrow("VideoTime")));
        slVideo.setIsDefault(cursor.getString(cursor.getColumnIndexOrThrow("IsDefault")));
        slVideo.setVideoImg(cursor.getString(cursor.getColumnIndexOrThrow("VideoImg")));
        slVideo.setVideoUrl(cursor.getString(cursor.getColumnIndexOrThrow("VideoUrl")));
        slVideo.setRemark(cursor.getString(cursor.getColumnIndexOrThrow("Remark")));
        return slVideo;
    }
}
