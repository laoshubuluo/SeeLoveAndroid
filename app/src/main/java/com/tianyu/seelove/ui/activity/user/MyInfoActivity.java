package com.tianyu.seelove.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.ActivityResultConstant;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.activity.system.SelectHeadActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import java.util.ArrayList;

/**
 * 个人信息界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class MyInfoActivity extends BaseActivity {
    String headUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg";
    private RelativeLayout headLayout;
    private ImageView userIcon;
    private TextView userName, userAge, userWork, userEducation, userAddress, userHouse, userMarriage;
    private ArrayList<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        dataList.add("camera_default");
        initView();
        initData();
    }

    private void initView() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText("个人信息");
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        headLayout = (RelativeLayout) findViewById(R.id.headLayout);
        userIcon = (ImageView) findViewById(R.id.userIcon);
        userName = (TextView) findViewById(R.id.userName);
        userAge = (TextView) findViewById(R.id.userAge);
        userWork = (TextView) findViewById(R.id.userWork);
        userEducation = (TextView) findViewById(R.id.userEducation);
        userAddress = (TextView) findViewById(R.id.userAddress);
        userHouse = (TextView) findViewById(R.id.userHouse);
        userMarriage = (TextView) findViewById(R.id.userMarriage);
        headLayout.setOnClickListener(this);
    }

    private void initData() {
        ImageLoader.getInstance().displayImage(headUrl, userIcon, ImageLoaderUtil.getHeadUrlImageOptions());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent = null;
        switch (view.getId()) {
            case R.id.headLayout:
                intent = IntentManager.createIntent(getApplicationContext(), SelectHeadActivity.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.up_in, R.anim.up_out);
                break;
            case R.id.leftBtn:
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ActivityResultConstant.UPDATE_USER_HEAD:
                String filePath = data.getExtras().getString("filePath");
                ImageLoader.getInstance().displayImage(filePath, userIcon, ImageLoaderUtil.getHeadUrlImageOptions());
                break;
        }
    }
}
