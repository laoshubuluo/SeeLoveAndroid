package com.tianyu.seelove.ui.activity.user;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.model.entity.ImageViewHolder;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.video.VideoInfo;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.ImageUtil;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;

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
    private Long userId;

    private UserController controller;

    private TextView titleView;
    private TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        userName = getIntent().getStringExtra("userName");

        initView();
    }

    public void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        nameView = (TextView) findViewById(R.id.name);
        titleView.setText(userName);
        GridView gridView = (GridView) findViewById(R.id.videoGridView);
        videoInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setVideoTitle("00" + i);
            videoInfos.add(videoInfo);
        }
        gridAdapter = new VideoGridAdapter(this, videoInfos);
        gridView.setAdapter(gridAdapter);


        // TODO by L.jinzhu for test
        controller = new UserController(getApplication(), handler);

        findViewById(R.id.testBtn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog = new CustomProgressDialog(UserInfoActivity.this, getString(R.string.loading));
                customProgressDialog.show();
                controller.create(userName, "我是从微信返回的字段");
            }
        });
        findViewById(R.id.testBtn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog = new CustomProgressDialog(UserInfoActivity.this, getString(R.string.loading));
                customProgressDialog.show();
                controller.login(userId, "", "");
            }
        });
    }

    /**
     * Handler发送message的逻辑处理方法
     *
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Message msg) {
        if (customProgressDialog != null)
            customProgressDialog.dismiss();
        if (promptDialog == null || promptDialog.isShowing())
            promptDialog = new PromptDialog(UserInfoActivity.this);
        SLUser user;
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.USER_CREATE_SUCCESS:
                user = (SLUser) msg.getData().getSerializable("user");
                userId = user.getUserId();
                titleView.setText(user.getNickName());
                nameView.setText("创建token:" + user.getToken4RongCloud());
                Toast.makeText(getApplicationContext(), "创建成功：" + user.toString(), Toast.LENGTH_LONG).show();
                break;
            case MessageSignConstant.USER_CREATE_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_create_failure), message);
                promptDialog.show();
                break;
            case MessageSignConstant.USER_LOGIN_SUCCESS:
                user = (SLUser) msg.getData().getSerializable("user");
                titleView.setText(user.getNickName());
                nameView.setText("登录获取token:" + user.getToken4RongCloud());
                ImageView view = (ImageView) findViewById(R.id.headView);
                ImageLoader.getInstance().displayImage(user.getHeadUrl(), view, ImageUtil.getImageOptions());
                Toast.makeText(getApplicationContext(), "登录成功：" + user.toString(), Toast.LENGTH_LONG).show();
                break;
            case MessageSignConstant.USER_LOGIN_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_login_failure), message);
                promptDialog.show();
                break;
            case MessageSignConstant.SERVER_OR_NETWORK_ERROR:
                promptDialog.initData("", msg.getData().getString("message"));
                promptDialog.show();
                break;
            case MessageSignConstant.UNKNOWN_ERROR:
                promptDialog.initData("", getString(R.string.unknown_error));
                promptDialog.show();
                break;
        }
        // 加载效果关闭
        return false;
    }
}