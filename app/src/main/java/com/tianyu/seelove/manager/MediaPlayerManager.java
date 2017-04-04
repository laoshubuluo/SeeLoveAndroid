package com.tianyu.seelove.manager;

import java.io.IOException;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class MediaPlayerManager {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private AnimationDrawable anima;
    private static MediaPlayerManager mediaManager;

    private MediaPlayerManager() {
        super();
    }

    public static MediaPlayerManager getInstance() {
        if (mediaManager == null) {
            mediaManager = new MediaPlayerManager();
        }
        return mediaManager;
    }

    public void stopVoice() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }


    // 播放录音
    public void playVoice(String name) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            } else {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(name);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                    }
                });
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 语音播放结束时，结束语音动画播放
     *
     * @param ad 语音播放动画
     */
    public void playVoice(String name, final AnimationDrawable ad) {
        if (null != anima && anima.isRunning()) {
            anima.stop();
            anima.selectDrawable(0);//设置停止的时候，显示第一帧图片
        }
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                ad.stop();//随着语音播放的结束语音动画也结束。
                ad.selectDrawable(0);//设置停止的时候，显示第一帧图片
                if (null != anima && anima != ad) { // 代表点击的是同一段语音,不同的语音需要结束上一段语音播放的同时开启下一段语音的播放逻辑
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(name);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    ad.start();
                    mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            ad.stop();//随着语音播放的结束语音动画也结束。
                            ad.selectDrawable(0);//设置停止的时候，显示第一帧图片
                        }
                    });
                }
            } else {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(name);
                mediaPlayer.prepare();
                mediaPlayer.start();
                ad.start();
                mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        ad.stop();//随着语音播放的结束语音动画也结束。
                        ad.selectDrawable(0);//设置停止的时候，显示第一帧图片
                    }
                });
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        anima = ad;
    }
}
