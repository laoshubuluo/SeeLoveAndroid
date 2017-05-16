package com.tianyu.seelove.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.common.Constant;
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
import com.tianyu.seelove.model.enums.FollowStatus;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.activity.message.SingleChatActivity;
import com.tianyu.seelove.ui.activity.system.ShareActivity;
import com.tianyu.seelove.ui.activity.video.VideoImageActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.view.MyGridView;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;

import java.util.ArrayList;
import java.util.List;

import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.model.BaseMediaBitrateConfig;
import mabeijianxi.camera.model.CBRMode;
import mabeijianxi.camera.model.MediaRecorderConfig;

/**
 * 个人信息界面
 *
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class UserInfoActivity extends BaseActivity {
    private FollowController followController;
    private UserController controller;
    private VideoGridAdapter videoGridAdapter;
    private SLUser slUser;
    private TextView titleView, userName, userAge, cityName, userDescript;
    private ImageView leftBtn, rightBtn, bigImage, headImage, followBtn;
    private ScrollView scrollView;
    private Button sendMessage;
    private MyGridView videoGridView;
    private SLUserDetail slUserDetail;
    List<SLVideo> slVideoList = new ArrayList<>();
    int followStatus = 0;
    private LinearLayout videoEmptyLayout;

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
        headImage = (ImageView) findViewById(R.id.headUrl);
        sendMessage = (Button) findViewById(R.id.sendMessage);
        userName = (TextView) findViewById(R.id.userName);
        followBtn = (ImageView) findViewById(R.id.followBtn);
        userAge = (TextView) findViewById(R.id.userAge);
        cityName = (TextView) findViewById(R.id.cityName);
        userDescript = (TextView) findViewById(R.id.userDescript);
        videoEmptyLayout = (LinearLayout) findViewById(R.id.videoEmptyLayout);
        videoGridView = (MyGridView) findViewById(R.id.videoGridView);
        leftBtn.setOnClickListener(this);
        leftBtn.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.GONE);
        rightBtn.setOnClickListener(this);
        videoEmptyLayout.setOnClickListener(this);
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
            userDescript.setText(StringUtils.isNotBlank(slUser.getIntroduce()) ? slUser.getIntroduce() : "这位童鞋很懒，什么都没留下～～");
            cityName.setText(StringUtils.isNotBlank(slUser.getCityName()) ? "/" + slUser.getCityName() : "/北京");
            ImageLoader.getInstance().displayImage(slUser.getHeadUrl(), headImage, ImageLoaderUtil.getHeadUrlImageOptions());
            ImageLoader.getInstance().displayImage(StringUtils.isNotBlank(slUser.getBigImg()) ? slUser.getBigImg() : slUser.getHeadUrl(),
                    bigImage, ImageLoaderUtil.getSmallImageOptions());
            if (slUser.getUserId() == AppUtils.getInstance().getUserId()) {
                followBtn.setVisibility(View.GONE);
            } else {
                followBtn.setVisibility(View.VISIBLE);
                if (followStatus == FollowStatus.NONE.getCode() || followStatus == FollowStatus.FOLLOW_LOGIN_USER.getCode()) {
                    followBtn.setBackgroundResource(R.mipmap.follow_add_icon);
                } else if (followStatus == FollowStatus.FOLLOWED_BY_LOGIN_USER.getCode() || followStatus == FollowStatus.EACH_OTHER.getCode()) {
                    followBtn.setBackgroundResource(R.mipmap.followed_icon);
                }
            }
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
            case R.id.videoEmptyLayout: {
                // 录制设置压缩
                BaseMediaBitrateConfig recordMode = null;
                recordMode = new CBRMode(Constant.cbrBufSize, Constant.cbrBitrate);
                recordMode.setVelocity(Constant.velocity);
                BaseMediaBitrateConfig compressMode = null;
                compressMode = new CBRMode(Constant.cbrBufSize, Constant.cbrBitrate);
                compressMode.setVelocity(Constant.velocity);
                MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
//                        .doH264Compress(compressMode)
                        .setMediaBitrateConfig(recordMode)
                        .smallVideoWidth(Constant.videoWidth)
                        .smallVideoHeight(Constant.videHeight)
                        .recordTimeMax(Constant.maxRecordTime)
                        .maxFrameRate(Constant.maxFrameRate)
                        .captureThumbnailsTime(1)
                        .recordTimeMin(Constant.minRecordTime)
                        .build();
                MediaRecorderActivity.goSmallVideoRecorder(UserInfoActivity.this, VideoImageActivity.class.getName(), config);
                break;
            }
            case R.id.rightBtn: {
                intent = IntentManager.createIntent(this, ShareActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.up_in, R.anim.up_out);
                break;
            }
            case R.id.followBtn: {
                if (0l == AppUtils.getInstance().getUserId()) {
                    intent = IntentManager.createIntent(getApplicationContext(), UserLoginActivity.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.up_in, R.anim.up_out);
                    Toast.makeText(UserInfoActivity.this, R.string.login_tips, Toast.LENGTH_LONG).show();
                    break;
                }
                int followType = 1;
                if (followStatus == FollowStatus.NONE.getCode() || followStatus == FollowStatus.FOLLOW_LOGIN_USER.getCode()) {
                    followType = FollowActionInfo.FOLLOW_TYPE_OK;
                } else if (followStatus == FollowStatus.FOLLOWED_BY_LOGIN_USER.getCode() || followStatus == FollowStatus.EACH_OTHER.getCode()) {
                    followType = FollowActionInfo.FOLLOW_TYPE_CANCLE;
                }
                SLFollow slFollow = new SLFollow();
                slFollow.setUserId(AppUtils.getInstance().getUserId());
                slFollow.setFollowUserId(slUser.getUserId());
                customProgressDialog = new CustomProgressDialog(this, getString(R.string.add_follow));
                customProgressDialog.show();
                followController.follow(slFollow, followType);
                break;
            }
            case R.id.sendMessage: {
                if (0l == AppUtils.getInstance().getUserId()) {
                    intent = IntentManager.createIntent(getApplicationContext(), UserLoginActivity.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.up_in, R.anim.up_out);
                    Toast.makeText(UserInfoActivity.this, R.string.login_tips, Toast.LENGTH_LONG).show();
                    break;
                } else {
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
                if (null != slUserDetail) {
                    followStatus = slUserDetail.getFollowStatus();
                }
                initData();
                if (slVideoList.size() > 0) {
                    videoEmptyLayout.setVisibility(View.GONE);
                } else {
                    videoEmptyLayout.setVisibility(View.VISIBLE);
                }
                break;
            case MessageSignConstant.USER_FIND_DETAIL_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_find_detail_failure), message);
                promptDialog.show();
                break;
            case MessageSignConstant.FOLLOW_SUCCESS:
                if (followStatus == FollowStatus.NONE.getCode() || followStatus == FollowStatus.FOLLOW_LOGIN_USER.getCode()) {
                    followBtn.setBackgroundResource(R.mipmap.followed_icon);
                    followStatus = FollowStatus.FOLLOWED_BY_LOGIN_USER.getCode();
                } else if (followStatus == FollowStatus.FOLLOWED_BY_LOGIN_USER.getCode() || followStatus == FollowStatus.EACH_OTHER.getCode()) {
                    followBtn.setBackgroundResource(R.mipmap.follow_add_icon);
                    followStatus = FollowStatus.NONE.getCode();
                }
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