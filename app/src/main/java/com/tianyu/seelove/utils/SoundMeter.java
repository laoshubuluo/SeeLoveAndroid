package com.tianyu.seelove.utils;

import java.io.File;
import java.io.IOException;
import android.media.MediaRecorder;

import com.tianyu.seelove.manager.DirectoryManager;

/**
 * @author shisheng.zhao
 * @Description: 录音工具类，主要负责录音并返回录音的存放路劲
 * @date 2015-09-01 上午11:23:26
 */
public class SoundMeter {
    static final private double EMA_FILTER = 0.6;
    public MediaRecorder mRecorder = null;
    private double mEMA = 0.0;
    private String finalpath = null;
    private boolean isRecording = false;

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public void start(String name) throws Exception {
        finalpath = null;
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        String pathString = DirectoryManager.getDirectory(DirectoryManager.DIR.VOICE)
                + File.separator + name;
        mRecorder.setOutputFile(pathString);
        mRecorder.setOnErrorListener(errorListener);
        mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                LogUtil.i("MediaRecorder" + "====================");
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    mRecorder.stop();
                }
            }
        });
        mRecorder.prepare();
        mRecorder.start();
        mEMA = 0.0;
        LogUtil.i("MediaRecorder录音成功:" + pathString);
        finalpath = pathString;
    }

    public String stop() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mRecorder = null;
        }
        return finalpath;
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude() / 2700.0);
        else
            return 0;
    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    public MediaRecorder getmRecorder() {
        return mRecorder;
    }

    public void setmRecorder(MediaRecorder mRecorder) {
        this.mRecorder = mRecorder;
    }

    MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        public void onError(MediaRecorder mp, int what, int extra) {
            switch (what) {
                case MediaRecorder.MEDIA_ERROR_SERVER_DIED:
                    LogUtil.i("MediaRecorder" + "====================");
                    break;
                default:
                    LogUtil.i("MediaRecorder" + "====================");
                    break;
            }
        }
    };
}
