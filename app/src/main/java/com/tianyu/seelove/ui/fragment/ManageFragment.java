package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.VideoDao;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.dao.impl.VideoDaoImpl;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.service.MessageSendService;
import com.tianyu.seelove.ui.activity.system.SettingActivity;
import com.tianyu.seelove.ui.activity.user.FollowUserListActivity;
import com.tianyu.seelove.ui.activity.user.MyInfoActivity;
import com.tianyu.seelove.ui.activity.video.VideoListActivity;
import com.tianyu.seelove.ui.fragment.base.BaseFragment;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.view.MyGridView;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import com.tianyu.seelove.wxapi.QQEntryActivity;
import com.tianyu.seelove.wxapi.WXEntryActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragmengt(管理)
 * @author shisheng.zhao
 * @date 2017-03-29 15:03
 */
public class ManageFragment extends BaseFragment {
    List<SLVideo> videoInfos;
    private View view = null;
    private UserController controller;
    private SLUser slUser;
    private Long userId;
    private ImageView bigImage, headUrl;
    private TextView titleView, userName, videoCount, followCount, followedCount;
    private MyGridView videoGridView;
    private LinearLayout loginLayout, userLayout;
    private UserDao userDao;
    private VideoDao videoDao;
    private VideoGridAdapter videoGridAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("ManageFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("ManageFragment____onCreate");
        userDao = new UserDaoImpl();
        videoDao = new VideoDaoImpl();
        controller = new UserController(getActivity(), handler);
        slUser = userDao.getUserByUserId(AppUtils.getInstance().getUserId());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("ManageFragment____onCreateView");
        // 防止onCreateView被多次调用
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_manage, container, false);
            initView(view);
            initData(slUser);
        }
        return view;
    }

    private void initView(View view) {
        titleView = (TextView) view.findViewById(R.id.titleView);
        ImageView rightView = (ImageView) view.findViewById(R.id.rightBtn);
        rightView.setVisibility(View.VISIBLE);
        rightView.setBackgroundResource(R.mipmap.setting_icon);
        rightView.setOnClickListener(this);
        loginLayout = (LinearLayout) view.findViewById(R.id.loginLayout);
        userLayout = (LinearLayout) view.findViewById(R.id.userLayout);
        bigImage = (ImageView) view.findViewById(R.id.bigImage);
        headUrl = (ImageView) view.findViewById(R.id.headUrl);
        userName = (TextView) view.findViewById(R.id.userName);
        LinearLayout videoLayout = (LinearLayout) view.findViewById(R.id.videoLayout);
        LinearLayout followLayout = (LinearLayout) view.findViewById(R.id.followLayout);
        LinearLayout followedLayout = (LinearLayout) view.findViewById(R.id.followedLayout);
        videoCount = (TextView) view.findViewById(R.id.videoCount);
        followCount = (TextView) view.findViewById(R.id.followCount);
        followedCount = (TextView) view.findViewById(R.id.followedCount);
        RelativeLayout userInfoLayout = (RelativeLayout) view.findViewById(R.id.userInfoLayout);
        LinearLayout videoItemLayout = (LinearLayout) view.findViewById(R.id.videoItemLayout);
        userInfoLayout.setOnClickListener(this);
        videoLayout.setOnClickListener(this);
        followLayout.setOnClickListener(this);
        followedLayout.setOnClickListener(this);
        videoItemLayout.setOnClickListener(this);
        titleView.setText(R.string.manager);
        videoGridView = (MyGridView) view.findViewById(R.id.videoGridView);
        videoGridAdapter = new VideoGridAdapter(getActivity());
        videoGridAdapter.updateData(new ArrayList<SLVideo>());
        videoGridView.setAdapter(videoGridAdapter);
        Button qqLoginBtn = (Button) view.findViewById(R.id.qqLoginBtn);
        Button wechatLoginBtn = (Button) view.findViewById(R.id.wechatLoginBtn);
        Button registBtn = (Button) view.findViewById(R.id.registBtn);
        Button loginBtn = (Button) view.findViewById(R.id.loginBtn);
        qqLoginBtn.setOnClickListener(this);
        wechatLoginBtn.setOnClickListener(this);
        registBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    private void initData(SLUser slUser) {
        if (0 == AppUtils.getInstance().getUserId() || null == slUser || 0 == slUser.getUserId()) {
            userLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            return;
        } else {
            userLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(slUser.getHeadUrl(), bigImage, ImageLoaderUtil.getDefaultDisplayOptions());
            ImageLoader.getInstance().displayImage(slUser.getHeadUrl(), headUrl, ImageLoaderUtil.getHeadUrlImageOptions());
            userName.setText(slUser.getNickName());
            videoCount.setText(slUser.getVideoCount() + "");
            followCount.setText(slUser.getFollowCount() + "");
            followedCount.setText(slUser.getFollowedCount() + "");
            videoInfos = videoDao.getVideoListByUserId(AppUtils.getInstance().getUserId());
            videoGridAdapter.updateData(videoInfos);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rightBtn: {
                intent = new Intent();
                intent.setClass(view.getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.userInfoLayout: {
                intent = new Intent();
                intent.setClass(view.getContext(), MyInfoActivity.class);
                intent.putExtra("user",slUser);
                startActivity(intent);
                break;
            }
            case R.id.videoLayout: {
                intent = new Intent();
                intent.setClass(view.getContext(), VideoListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.followLayout: {
                intent = new Intent();
                intent.putExtra("followType",1);
                intent.setClass(view.getContext(), FollowUserListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.followedLayout: {
                intent = new Intent();
                intent.putExtra("followType",2);
                intent.setClass(view.getContext(), FollowUserListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.videoItemLayout: {
                intent = new Intent();
                intent.setClass(view.getContext(), VideoListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.qqLoginBtn: {
                customProgressDialog = new CustomProgressDialog(getActivity(), getString(R.string.loading));
                customProgressDialog.show();
                intent = new Intent(getActivity(), QQEntryActivity.class);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.wechatLoginBtn: {
                customProgressDialog = new CustomProgressDialog(getActivity(), getString(R.string.loading));
                customProgressDialog.show();
                intent = new Intent(getActivity(), WXEntryActivity.class);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.registBtn: {
                customProgressDialog = new CustomProgressDialog(getActivity(), getString(R.string.loading));
                customProgressDialog.show();
                controller.regist("测试帐号", "我是从微信返回的字段");
                break;
            }
            case R.id.loginBtn: {
                customProgressDialog = new CustomProgressDialog(getActivity(), getString(R.string.loading));
                customProgressDialog.show();
                controller.login(userId, "", "");
                break;
            }
            default:
                break;
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
            promptDialog = new PromptDialog(getActivity());
        SLUser user;
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.USER_REGIST_SUCCESS:
                user = (SLUser) msg.getData().getSerializable("user");
                userId = user.getUserId();
                titleView.setText(user.getNickName());
                Toast.makeText(getActivity(), "创建成功：" + user.toString(), Toast.LENGTH_LONG).show();
                break;
            case MessageSignConstant.USER_REGIST_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_create_failure), message);
                promptDialog.show();
                break;
            case MessageSignConstant.USER_LOGIN_SUCCESS:
                user = (SLUser) msg.getData().getSerializable("user");
                initData(user);
                Toast.makeText(getActivity(), "登录成功：" + user.toString(), Toast.LENGTH_LONG).show();
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
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("ManageFragment____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("ManageFragment____onStart");
        slUser = userDao.getUserByUserId(AppUtils.getInstance().getUserId());
        initData(slUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("ManageFragment____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("ManageFragment____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("ManageFragment____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("ManageFragment____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("ManageFragment____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("ManageFragment____onDetach");
    }
}