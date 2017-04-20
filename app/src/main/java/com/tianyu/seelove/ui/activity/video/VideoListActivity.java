package com.tianyu.seelove.ui.activity.video;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.ui.activity.base.BaseActivity;

/**
 * 个人信息界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class VideoListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        initView();
    }

    private void initView() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText("我的视频");
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
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
