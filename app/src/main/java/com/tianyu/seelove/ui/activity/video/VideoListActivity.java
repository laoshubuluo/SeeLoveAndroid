package com.tianyu.seelove.ui.activity.video;

import android.os.Bundle;
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
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText("我的视频");
    }
}
