package com.tianyu.seelove.ui.activity.video;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.tianyu.seelove.R;
import com.tianyu.seelove.view.video.VideoPlayer;
import com.tianyu.seelove.view.video.VideoPlayerController;
import com.tianyu.seelove.view.video.VideoPlayerHelp;

/**
 * 全屏播放
 * @author shisheng.zhao
 * @date 2017-04-15 10:37
 */
public class FullVideoActivity extends Activity {
    private VideoPlayer mVideo;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_full);
        videoUrl = getIntent().getExtras().getString("videoUrl");
        initView();
        initData();
    }

    private void initView() {
        mVideo = (VideoPlayer) findViewById(R.id.videoSuperPlayer);
    }

    private void initData() {
        VideoPlayerHelp.release();
        mVideo.loadAndPlay(VideoPlayerHelp.getInstance(), videoUrl, 0, true, true);
        mVideo.setPageType(VideoPlayerController.PageType.EXPAND);
        mVideo.setVideoPlayCallback(new VideoPlayer.VideoPlayCallbackImpl() {
            @Override
            public void onSwitchPageType() {
                finish();
            }

            @Override
            public void onPlayFinish() {
                finish();
            }

            @Override
            public void onPlayError() {

            }

            @Override
            public void onCloseVideo() {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        VideoPlayerHelp.pause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        VideoPlayerHelp.resume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        VideoPlayerHelp.release();
        super.onDestroy();
    }
}
