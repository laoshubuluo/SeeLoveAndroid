package com.tianyu.seelove.ui.activity.video;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.video.MyVideoView;

/**
 * 个人信息界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class VideoPlayActivity extends BaseActivity {
    private MyVideoView vv_play;
    private String videoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentColor("#00000000");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);
        videoPath = getIntent().getStringExtra("videoPath");
        customProgressDialog = new CustomProgressDialog(this, getString(R.string.loading_video));
        customProgressDialog.show();
        initView();
    }

    private void initView() {
        ImageView backBtn = (ImageView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        vv_play = (MyVideoView) findViewById(R.id.vv_play);
        vv_play.setVideoPath(videoPath);
        vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                vv_play.setLooping(true);
                vv_play.start();
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.backBtn: {
                finish();
                break;
            }
        }
    }
}
