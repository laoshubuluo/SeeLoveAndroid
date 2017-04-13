package com.tianyu.seelove.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.activity.message.SingleChatActivity;
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
    private ArrayList<SLVideo> videoInfos;
    private SLUser user;
    private TextView titleView;
    private ImageView bigImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        user = (SLUser) getIntent().getSerializableExtra("user");
        if (null == user) {
            user = new SLUser(-1, "未成功获取到user信息，本地模拟");
        }
        initView();
        initData();
    }

    public void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        ImageView leftBtn = (ImageView) findViewById(R.id.leftBtn);
        ImageView rightBtn = (ImageView) findViewById(R.id.rightBtn);
        bigImage = (ImageView) findViewById(R.id.bigImage);
        Button sendMessage = (Button) findViewById(R.id.sendMessage);
        titleView.setText(user.getNickName());
        leftBtn.setOnClickListener(this);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setOnClickListener(this);
        rightBtn.setBackgroundResource(R.mipmap.share_btn);
        sendMessage.setOnClickListener(this);
    }

    private void initData() {
        ImageLoader.getInstance().displayImage(user.getHeadUrl(), bigImage, ImageLoaderUtil.getSmallImageOptions());
        GridView gridView = (GridView) findViewById(R.id.videoGridView);
        videoInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SLVideo videoInfo = new SLVideo();
            videoInfo.setVideoTitle("00" + i);
            videoInfos.add(videoInfo);
        }
        gridAdapter = new VideoGridAdapter(this, videoInfos);
        gridView.setAdapter(gridAdapter);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent = null;
        switch (view.getId()) {
            case R.id.leftBtn: {
                finish();
                break;
            }
            case R.id.sendMessage: {
                intent = new Intent();
                intent.setClass(UserInfoActivity.this, SingleChatActivity.class);
                intent.putExtra("userId", 6634l);
                startActivity(intent);
                break;
            }
        }
    }
}