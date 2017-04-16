package com.tianyu.seelove.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.activity.message.SingleChatActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.view.MyGridView;
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
    private UserController controller;
    private VideoGridAdapter gridAdapter;
    private SLUser user;
    private ArrayList<SLVideo> videoList;
    private TextView titleView;
    private ImageView bigImage;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        user = (SLUser) getIntent().getSerializableExtra("user");
        if (null == user) {
            // TODO by L.jinzhu for 待完善
            user = new SLUser(-1, "未成功获取到user信息，本地模拟");
        }
        controller = new UserController(this, handler);

        initView();
        initData();
    }

    public void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        ImageView leftBtn = (ImageView) findViewById(R.id.leftBtn);
        ImageView rightBtn = (ImageView) findViewById(R.id.rightBtn);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        bigImage = (ImageView) findViewById(R.id.bigImage);
        Button sendMessage = (Button) findViewById(R.id.sendMessage);
        titleView.setText(user.getNickName());
        leftBtn.setOnClickListener(this);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setOnClickListener(this);
        rightBtn.setBackgroundResource(R.mipmap.share_btn);
        sendMessage.setOnClickListener(this);

        // 请求服务器
        customProgressDialog = new CustomProgressDialog(this, getString(R.string.loading));
        customProgressDialog.show();
        controller.findDetail(user.getUserId());
    }

    private void initData() {
        ImageLoader.getInstance().displayImage(user.getHeadUrl(), bigImage, ImageLoaderUtil.getSmallImageOptions());
        MyGridView gridView = (MyGridView) findViewById(R.id.videoGridView);
        gridAdapter = new VideoGridAdapter(this, videoList);
        gridView.setAdapter(gridAdapter);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
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
            promptDialog = new PromptDialog(this);
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.USER_FIND_DETAIL_SUCCESS:
                user = (SLUser) msg.getData().getSerializable("user");
                videoList = (ArrayList<SLVideo>) msg.getData().getSerializable("videoList");
                initData();
                break;
            case MessageSignConstant.USER_FIND_DETAIL_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_find_detail_failure), message);
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
        return false;
    }
}