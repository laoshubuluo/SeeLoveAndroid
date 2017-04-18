package com.tianyu.seelove.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;

/**
 * 个人信息界面
 *
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class MyInfoActivity extends BaseActivity {
    String headUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg";
    private ImageView userIcon;
    private TextView userName, userAge, userWork, userEducation, userAddress, userHouse, userMarriage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        initView();
        initData();
    }

    private void initView() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText("个人信息");
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        userIcon = (ImageView) findViewById(R.id.userIcon);
        userName = (TextView) findViewById(R.id.userName);
        userAge = (TextView) findViewById(R.id.userAge);
        userWork = (TextView) findViewById(R.id.userWork);
        userEducation = (TextView) findViewById(R.id.userEducation);
        userAddress = (TextView) findViewById(R.id.userAddress);
        userHouse = (TextView) findViewById(R.id.userHouse);
        userMarriage = (TextView) findViewById(R.id.userMarriage);
    }

    private void initData() {
        ImageLoader.getInstance().displayImage(headUrl, userIcon, ImageLoaderUtil.getHeadUrlImageOptions());
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
