package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.common.ActivityResultConstant;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.SecurityCodeController;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.VideoDao;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.dao.impl.VideoDaoImpl;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.ui.activity.system.SelectHeadActivity;
import com.tianyu.seelove.ui.activity.system.SettingActivity;
import com.tianyu.seelove.ui.activity.user.FollowUserListActivity;
import com.tianyu.seelove.ui.activity.user.MyInfoActivity;
import com.tianyu.seelove.ui.activity.video.VideoListActivity;
import com.tianyu.seelove.ui.fragment.base.BaseFragment;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;
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
    List<SLVideo> videoInfos = new ArrayList<>();
    private View view = null;
    private UserController controller;
    private SecurityCodeController codeController;
    private SLUser slUser;
    private ImageView bigImage, headUrl;
    private TextView titleView, userName, videoCount, followCount, followedCount;
    private MyGridView videoGridView;
    private LinearLayout loginLayout, userLayout;
    private EditText phoneEdit, codeEdit;
    private UserDao userDao;
    private VideoDao videoDao;
    private VideoGridAdapter videoGridAdapter;
    private TextView getCodeBtn;
    private TimeCount time;//倒计时
    private String filePath = "";

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
        codeController = new SecurityCodeController(getActivity(), handler);
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
        getCodeBtn = (TextView) view.findViewById(R.id.getCodeBtn);
        phoneEdit = (EditText) view.findViewById(R.id.phoneEdit);
        codeEdit = (EditText) view.findViewById(R.id.codeEdit);
        LinearLayout userEditLayout = (LinearLayout) view.findViewById(R.id.userEditLayout);
        ImageView qqLoginBtn = (ImageView) view.findViewById(R.id.qqLoginBtn);
        ImageView wechatLoginBtn = (ImageView) view.findViewById(R.id.wechatLoginBtn);
        Button loginBtn = (Button) view.findViewById(R.id.loginBtn);
        qqLoginBtn.setOnClickListener(this);
        wechatLoginBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        getCodeBtn.setOnClickListener(this);
        bigImage.setOnClickListener(this);
        userEditLayout.setOnClickListener(this);
    }

    private void initData(SLUser slUser) {
        if (0 == AppUtils.getInstance().getUserId() || null == slUser || 0 == slUser.getUserId()) {
            userLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            return;
        } else {
            userLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(slUser.getBigImg(), bigImage, ImageLoaderUtil.getDefaultDisplayOptions());
            ImageLoader.getInstance().displayImage(slUser.getHeadUrl(), headUrl, ImageLoaderUtil.getHeadUrlImageOptions());
            userName.setText(slUser.getNickName());
            videoCount.setText(slUser.getVideoCount() + "");
            followCount.setText(slUser.getFollowCount() + "");
            followedCount.setText(slUser.getFollowedCount() + "");
            videoInfos.clear();
            videoInfos = videoDao.getVideoListByUserId(AppUtils.getInstance().getUserId());
            videoGridAdapter.updateData(videoInfos);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        String phoneNum = phoneEdit.getText().toString().trim();
        String verifyCode = codeEdit.getText().toString().trim();
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
                intent.putExtra("user", slUser);
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
                intent.putExtra("followType", 1);
                intent.setClass(view.getContext(), FollowUserListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.followedLayout: {
                intent = new Intent();
                intent.putExtra("followType", 2);
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
            case R.id.loginBtn: {
                if (StringUtils.isNullOrBlank(phoneNum)) {
                    Toast.makeText(getActivity(), R.string.phone_is_null, Toast.LENGTH_LONG).show();
                    break;
                }
                if (StringUtils.isNullOrBlank(verifyCode)) {
                    Toast.makeText(getActivity(), R.string.code_is_null, Toast.LENGTH_LONG).show();
                    break;
                }
                customProgressDialog = new CustomProgressDialog(getActivity(), getString(R.string.loading));
                customProgressDialog.show();
                controller.login4Phone(SLUser.ACCOUNT_TYPE_PHONE, phoneNum, verifyCode);
                break;
            }
            case R.id.getCodeBtn: {
                if (StringUtils.isNullOrBlank(phoneNum)) {
                    Toast.makeText(getActivity(), R.string.phone_is_null, Toast.LENGTH_LONG).show();
                    break;
                }
                codeController.send(phoneNum);
                //开始倒计时
                time = new TimeCount(60000, 1000);
                time.start();
                break;
            }
            case R.id.bigImage: {
                intent = IntentManager.createIntent(getActivity(), SelectHeadActivity.class);
                startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
                break;
            }
            case R.id.userEditLayout: {
                intent = new Intent();
                intent.setClass(view.getContext(), MyInfoActivity.class);
                intent.putExtra("user", slUser);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 重新发送倒计时
     */
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            reset();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            getCodeBtn.setClickable(false);
            getCodeBtn.setText(millisUntilFinished / 1000 + "s");
        }

        public void reset() {
            this.cancel();
            getCodeBtn.setText(getString(R.string.get_code));
            getCodeBtn.setClickable(true);
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
            promptDialog = new PromptDialog(getActivity());
        SLUser user;
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.USER_UPDATE_SUCCESS:
                ImageLoader.getInstance().displayImage(filePath, bigImage, ImageLoaderUtil.getSmallImageOptions());
                break;
            case MessageSignConstant.USER_UPDATE_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_update_success), message);
                promptDialog.show();
                break;
            case MessageSignConstant.USER_LOGIN_SUCCESS:
                SLUserDetail slUserDetail = (SLUserDetail) msg.getData().getSerializable("userDetail");
                slUser = slUserDetail.getUser();
                initData(slUser);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ActivityResultConstant.UPDATE_USER_HEAD: {
                filePath = data.getExtras().getString("filePath");
                slUser.setBigImg(filePath);
                customProgressDialog = new CustomProgressDialog(getActivity(), getString(R.string.loading));
                customProgressDialog.show();
                controller.update(slUser);
                break;
            }
        }
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