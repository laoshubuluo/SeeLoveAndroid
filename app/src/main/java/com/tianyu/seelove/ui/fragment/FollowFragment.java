package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.FollowListAdapter;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.NewsController;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.model.enums.DataGetType;
import com.tianyu.seelove.ui.activity.user.UserLoginActivity;
import com.tianyu.seelove.ui.activity.video.VideoRecordActivity;
import com.tianyu.seelove.ui.fragment.base.BaseFragment;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.view.PullToRefreshView;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragmengt(动态)
 * @author shisheng.zhao
 * @date 2017-03-29 15:15
 */
public class FollowFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener {
    private FollowListAdapter adapter;
    private ListView followListView;
    private FollowReciver reciver;
    private View view = null;
    private NewsController controller;
    private List<SLUserDetail> userList;
    private PullToRefreshView mPullToRefreshView;
    private int pageNumber = 0;
    private int dataGetType = 0;
    private int isEndPage = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("FollowFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("FollowFragment____onCreate");
        controller = new NewsController(getActivity(), handler);
        reciver = new FollowReciver();
        initIntent();
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
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_view);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        followListView = (ListView) view.findViewById(R.id.followListView);
        adapter = new FollowListAdapter(getActivity(), userList);
        followListView.setAdapter(adapter);
        if (0l != AppUtils.getInstance().getUserId()) {
            initData();
        }
    }

    private void initData() {
        // 请求服务器
        customProgressDialog = new CustomProgressDialog(getActivity(), getString(R.string.loading));
        customProgressDialog.show();
        updateData(DataGetType.DOWN);
        dataGetType = 0;
    }

    private void updateData(final DataGetType dataGetType) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                controller.findAll(pageNumber, dataGetType.getCode(), AppUtils.getInstance().getUserId());
            }
        }, 100);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (isEndPage == 0) {
            dataGetType = 1;
            updateData(DataGetType.DOWN);
        } else if (isEndPage == 1) {
            mPullToRefreshView.onFooterRefreshComplete();
            Toast.makeText(getActivity(), "没有下一页了!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 下拉刷新
     * @param view
     */
    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        dataGetType = 0;
        pageNumber = 0;
        updateData(DataGetType.DOWN);
    }

    private void initIntent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(1100);
        intentFilter.addAction(Actions.ACTION_UPDATE_FOLLOW_LIST);
        getActivity().registerReceiver(reciver, intentFilter);
    }

    private class FollowReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Actions.ACTION_UPDATE_FOLLOW_LIST)) {
                initData();
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rightBtn: {
                if (0l != AppUtils.getInstance().getUserId()) {
                    intent = new Intent();
                    intent.setClass(view.getContext(), VideoRecordActivity.class);
                    view.getContext().startActivity(intent);
                } else {
                    intent = IntentManager.createIntent(getActivity(), UserLoginActivity.class);
                    startActivityForResult(intent, 0);
                    getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
                    Toast.makeText(getActivity(), R.string.login_tips, Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (customProgressDialog != null)
            customProgressDialog.dismiss();
        if (promptDialog == null || promptDialog.isShowing())
            promptDialog = new PromptDialog(getActivity());
        if (null != mPullToRefreshView) {
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
        }
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.NEWS_FIND_ALL_SUCCESS:
                pageNumber = msg.getData().getInt("currentPage");
                isEndPage = msg.getData().getInt("isEndPage");
                userList = (List<SLUserDetail>) msg.getData().getSerializable("userList");
                if (null == userList) {
                    userList = new ArrayList<>();
                }
                if (dataGetType == 0) {
                    adapter.updateData(userList, true);
                } else {
                    adapter.updateData(userList, false);
                }
                break;
            case MessageSignConstant.NEWS_FIND_ALL_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.find_all_failure), message);
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
        if (0l == AppUtils.getInstance().getUserId()) {
            Intent intent = IntentManager.createIntent(getActivity(), UserLoginActivity.class);
            startActivityForResult(intent, 0);
            getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
            Toast.makeText(getActivity(), R.string.login_tips, Toast.LENGTH_LONG).show();
            return;
        }
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
        if (null != reciver) {
            getActivity().unregisterReceiver(reciver);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("FollowFragment____onDetach");
    }
}