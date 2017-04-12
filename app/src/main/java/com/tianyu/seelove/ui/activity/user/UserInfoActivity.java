package com.tianyu.seelove.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.model.entity.video.VideoInfo;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;

import java.util.ArrayList;

/**
 * 个人信息界面
 *
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class UserInfoActivity extends BaseActivity {
    private VideoGridAdapter gridAdapter;
    private ArrayList<VideoInfo> videoInfos;
    private String userName = "";
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        userName = getIntent().getStringExtra("userName");
        initView();
    }

    public void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        ImageView leftBtn = (ImageView) findViewById(R.id.leftBtn);
        ImageView rightBtn = (ImageView) findViewById(R.id.rightBtn);
        leftBtn.setOnClickListener(this);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setOnClickListener(this);
        rightBtn.setBackgroundResource(R.mipmap.share_btn);
        ImageView bigImage = (ImageView) findViewById(R.id.bigImage);
        titleView.setText(userName);
        ImageLoader.getInstance().displayImage("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=673651839,1464649612&fm=111&gp=0.jpg", bigImage, ImageLoaderUtil.getSmallImageOptions());
        GridView gridView = (GridView) findViewById(R.id.videoGridView);
        videoInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setVideoTitle("00" + i);
            videoInfos.add(videoInfo);
        }
        gridAdapter = new VideoGridAdapter(this, videoInfos);
        gridView.setAdapter(gridAdapter);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.leftBtn:
                finish();
                break;
        }
    }
}