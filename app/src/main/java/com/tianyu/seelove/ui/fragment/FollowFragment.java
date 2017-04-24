package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.FollowListAdapter;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.ui.activity.video.VideoRecordActivity;
import com.tianyu.seelove.ui.fragment.base.BaseFragment;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import java.util.List;

/**
 * Fragmengt(动态)
 * @author shisheng.zhao
 * @date 2017-03-29 15:15
 */
public class FollowFragment extends BaseFragment {
    private FollowListAdapter adapter;
    private ListView followListView;
    private View view = null;
    private UserController controller;
    private List<SLUserDetail> userList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("FollowFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("FollowFragment____onCreate");
        controller = new UserController(getActivity(), handler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 防止onCreateView被多次调用
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_follow, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        titleView.setText(R.string.follow);
        ImageView rightView = (ImageView) view.findViewById(R.id.rightBtn);
        rightView.setVisibility(View.VISIBLE);
        rightView.setBackgroundResource(R.mipmap.create_video_cion);
        rightView.setOnClickListener(this);
        followListView = (ListView) view.findViewById(R.id.followListView);
        adapter = new FollowListAdapter(getActivity(), userList);
        followListView.setAdapter(adapter);
        // 请求服务器
        customProgressDialog = new CustomProgressDialog(getActivity(), getString(R.string.loading));
        customProgressDialog.show();
        controller.findAll(1, 33, "1", "111");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rightBtn: {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), VideoRecordActivity.class);
                view.getContext().startActivity(intent);
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
            promptDialog = new PromptDialog(getActivity());
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.USER_FIND_ALL_SUCCESS:
                userList = (List<SLUserDetail>) msg.getData().getSerializable("userList");
                adapter.updateData(userList, true);
                adapter.notifyDataSetChanged();
                break;
            case MessageSignConstant.USER_FIND_ALL_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_find_all_failure), message);
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
        LogUtil.d("FollowFragment____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("FollowFragment____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("FollowFragment____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("FollowFragment____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("FollowFragment____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("FollowFragment____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("FollowFragment____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("FollowFragment____onDetach");
    }
}