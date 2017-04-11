package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
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
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.video.VideoInfo;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.activity.user.MyInfoActivity;
import com.tianyu.seelove.ui.activity.user.UserInfoActivity;
import com.tianyu.seelove.ui.activity.video.VideoListActivity;
import com.tianyu.seelove.utils.ImageUtil;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import com.tianyu.seelove.wxapi.QQEntryActivity;
import com.tianyu.seelove.wxapi.WXEntryActivity;

import java.util.ArrayList;

/**
 * Fragmengt(管理)
 *
 * @author shisheng.zhao
 * @date 2017-03-29 15:03
 */
public class ManageFragment extends Fragment implements View.OnClickListener, Handler.Callback {
    ArrayList<VideoInfo> videoInfos;
    public CustomProgressDialog customProgressDialog;
    public PromptDialog promptDialog;
    private View view = null;
    private UserController controller;
    public Handler handler;
    private String userName = "天宇";
    private Long userId;
    private ImageView headView;
    private TextView titleView, nameView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("ManageFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("ManageFragment____onCreate");
        handler = new Handler(this);
        promptDialog = new PromptDialog(getActivity());
        controller = new UserController(getActivity(), handler);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("ManageFragment____onCreateView");
        // 防止onCreateView被多次调用
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent)
                parent.removeView(view);
        } else {
            view = inflater.inflate(R.layout.fragment_manage, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        titleView = (TextView) view.findViewById(R.id.titleView);
        RelativeLayout userInfoLayout = (RelativeLayout) view.findViewById(R.id.userInfoLayout);
        LinearLayout videoLayout = (LinearLayout) view.findViewById(R.id.videoLayout);
        userInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), MyInfoActivity.class);
                startActivity(intent);
            }
        });
        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), VideoListActivity.class);
                startActivity(intent);
            }
        });
        titleView.setText(R.string.manager);
        GridView gridView = (GridView) view.findViewById(R.id.videoGridView);
        videoInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setVideoTitle("00" + i);
            videoInfos.add(videoInfo);
        }
        int size = videoInfos.size();
        int length = 120;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数
        gridView.setAdapter(new VideoGridAdapter(getActivity(), videoInfos));
        Button qqLoginBtn = (Button) view.findViewById(R.id.qqLoginBtn);
        Button wechatLoginBtn = (Button) view.findViewById(R.id.wechatLoginBtn);
        Button registBtn = (Button) view.findViewById(R.id.registBtn);
        Button loginBtn = (Button) view.findViewById(R.id.loginBtn);
        headView = (ImageView) view.findViewById(R.id.headView);
        nameView = (TextView) view.findViewById(R.id.nameView);
        qqLoginBtn.setOnClickListener(this);
        wechatLoginBtn.setOnClickListener(this);
        registBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
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
                controller.create(userName, "我是从微信返回的字段");
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
            case MessageSignConstant.USER_CREATE_SUCCESS:
                user = (SLUser) msg.getData().getSerializable("user");
                userId = user.getUserId();
                titleView.setText(user.getNickName());
                nameView.setText("创建token:" + user.getToken4RongCloud());
                Toast.makeText(getActivity(), "创建成功：" + user.toString(), Toast.LENGTH_LONG).show();
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
                ImageLoader.getInstance().displayImage(user.getHeadUrl(), headView, ImageUtil.getImageOptions());
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
        // 加载效果关闭
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