package com.tianyu.seelove.ui.activity.user;

import android.os.Bundle;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.ui.activity.base.BaseActivity;

/**
 * 个人信息界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class MyInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText("个人信息");
    }
}
