package com.tianyu.seelove.ui.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.dao.VideoDao;
import com.tianyu.seelove.dao.impl.VideoDaoImpl;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.view.MyGridView;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的视频界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class VideoListActivity extends BaseActivity {
    private VideoDao videoDao;
    private MyGridView videoGridView;
    private VideoGridAdapter adapter;
    private List<SLVideo> slVideoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        videoDao = new VideoDaoImpl();
        adapter = new VideoGridAdapter(this);
        adapter.updateData(slVideoList);
        initView();
        initData();
    }

    private void initView() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText("我的视频");
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        ImageView rightView = (ImageView)findViewById(R.id.rightBtn);
        rightView.setBackgroundResource(R.mipmap.create_video_cion);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        rightView.setVisibility(View.VISIBLE);
        rightView.setOnClickListener(this);
        videoGridView = (MyGridView) findViewById(R.id.videoGridView);
        videoGridView.setAdapter(adapter);
    }

    private void initData() {
        slVideoList = videoDao.getVideoListByUserId(AppUtils.getInstance().getUserId());
        adapter.updateData(slVideoList);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.leftBtn: {
                finish();
                break;
            }
            case R.id.rightBtn:{
                Intent intent = new Intent();
                intent.setClass(view.getContext(), VideoRecordActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
