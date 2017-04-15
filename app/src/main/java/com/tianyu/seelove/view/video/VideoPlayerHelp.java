package com.tianyu.seelove.view.video;

import android.media.MediaPlayer;

/**
 * 构建一个MediaPlayer单例，对mediaPlayer进行管理
 *
 * @author shisheng.zhao
 */
public class VideoPlayerHelp {
    private static MediaPlayer mPlayer;

    public static MediaPlayer getInstance() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        return mPlayer;
    }

    /**
     * MediaPlayer resume
     */
    public static void resume() {
        try{
            if (mPlayer != null) {
                mPlayer.start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * MediaPlayer pause
     */
    public static void pause() {
        try {
            if (mPlayer != null) {
                mPlayer.pause();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * MediaPlayer release
     */
    public static void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
