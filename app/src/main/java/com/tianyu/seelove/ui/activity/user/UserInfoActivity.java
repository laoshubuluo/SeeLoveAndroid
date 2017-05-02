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
import com.tianyu.seelove.controller.FollowController;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.network.request.FollowActionInfo;
import com.tianyu.seelove.model.entity.user.SLFollow;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.activity.message.SingleChatActivity;
import com.tianyu.seelove.ui.activity.system.ShareActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.view.MyGridView;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人信息界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class UserInfoActivity extends BaseActivity {
    private FollowController followController;
    private UserController controller;
    private VideoGridAdapter videoGridAdapter;
    private SLUser slUser;
    private TextView titleView, userName, userAge, cityName, userDescript;
    private ImageView leftBtn, rightBtn, bigImage, followBtn;
    private ScrollView scrollView;
    private Button sendMessage;
    private MyGridView videoGridView;
    private SLUserDetail slUserDetail;
    List<SLVideo> slVideoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        controller = new UserController(this, handler);
        followController = new FollowController(this, handler);
        slUser = (SLUser) getIntent().getSerializableExtra("user");
        initView();
        initData();
    }

    public void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        leftBtn = (ImageView) findViewById(R.id.leftBtn);
        rightBtn = (ImageView) findViewById(R.id.rightBtn);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        bigImage = (ImageView) findViewById(R.id.bigImage);
        sendMessage = (Button) findViewById(R.id.sendMessage);
        userName = (TextView) findViewById(R.id.userName);
        followBtn = (ImageView) findViewById(R.id.followBtn);
        userAge = (TextView) findViewById(R.id.userAge);
        cityName = (TextView) findViewById(R.id.cityName);
        userDescript = (TextView) findViewById(R.id.userDescript);
        videoGridView = (MyGridView) findViewById(R.id.videoGridView);
        leftBtn.setOnClickListener(this);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setOnClickListener(this);
        rightBtn.setBackgroundResource(R.mipmap.share_btn);
        sendMessage.setOnClickListener(this);
        followBtn.setOnClickListener(this);
        videoGridAdapter = new VideoGridAdapter(this);
        videoGridAdapter.updateData(new ArrayList<SLVideo>());
        videoGridView.setAdapter(videoGridAdapter);
        // 请求服务器
        customProgressDialog = new CustomProgressDialog(this, getString(R.string.loading));
        customProgressDialog.show();
        controller.findDetail(slUser.getUserId());
    }

    private void initData() {
        if (null != slUser) {
            titleView.setText(slUser.getNickName());
            userName.setText(slUser.getNickName());
            userAge.setText(slUser.getAge() + "岁");
            userDescript.setText(StringUtils.isNotBlank(slUser.getIntroduce()) ? slUser.getIntroduce() : "我是一句话介绍！！");
            cityName.setText(StringUtils.isNotBlank(slUser.getCityName()) ? "/" + slUser.getCityName() : "/北京");
            ImageLoader.getInstance().displayImage(slUser.getBigImg(), bigImage, ImageLoaderUtil.getSmallImageOptions());
            if (slVideoList.size() > 0) {
                videoGridAdapter.updateData(slVideoList);
            }
        }
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
            case R.id.rightBtn: {
                intent = IntentManager.createIntent(this, ShareActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.up_in, R.anim.up_out);
                break;
            }
            case R.id.followBtn: {
                // TODO shisheng.zhao 需要通过一种方式获得关系状态已关注/未关注
                SLFollow slFollow = new SLFollow();
                slFollow.setUserId(AppUtils.getInstance().getUserId());
                slFollow.setFollowUserId(slUser.getUserId());
                customProgressDialog = new CustomProgressDialog(this, getString(R.string.add_follow));
                customProgressDialog.show();
                followController.follow(slFollow, FollowActionInfo.FOLLOW_TYPE_OK);
                break;
            }
            case R.id.sendMessage: {
                if (0l != AppUtils.getInstance().getUserId()) {
                    new UserDaoImpl().addUser(slUser);
                }
                intent = new Intent();
                intent.setClass(UserInfoActivity.this, SingleChatActivity.class);
                intent.putExtra("userId", slUser.getUserId());
                intent.putExtra("userName", slUser.getNickName());
                startActivity(intent);
                break;
            }
        }
    }

    /**
     * Handler发送message的逻辑处理方法
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
                slUserDetail = (SLUserDetail) msg.getData().getSerializable("userDetail");
                slUser = slUserDetail.getUser();
                if (null != slUserDetail.getVideoList() && slUserDetail.getVideoList().size() > 0) {
                    slVideoList = slUserDetail.getVideoList();
                }
                initData();
                break;
            case MessageSignConstant.USER_FIND_DETAIL_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_find_detail_failure), message);
                promptDialog.show();
                break;
            case MessageSignConstant.FOLLOW_SUCCESS:
                followBtn.setBackgroundResource(R.mipmap.followed_icon);
                break;
            case MessageSignConstant.FOLLOW_FAILURE:
                promptDialog.initData("", msg.getData().getString("message"));
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