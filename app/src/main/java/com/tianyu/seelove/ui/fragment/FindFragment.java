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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.FindUserListAdapter;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.model.enums.DataGetType;
import com.tianyu.seelove.ui.activity.system.ShareActivity;
import com.tianyu.seelove.ui.activity.user.UserLoginActivity;
import com.tianyu.seelove.ui.activity.video.VideoImageActivity;
import com.tianyu.seelove.ui.fragment.base.BaseFragment;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.view.PullToRefreshView;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import java.util.ArrayList;
import java.util.List;
import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.model.BaseMediaBitrateConfig;
import mabeijianxi.camera.model.CBRMode;
import mabeijianxi.camera.model.MediaRecorderConfig;

/**
 * Fragmengt(发现)
 * @author shisheng.zhao
 * @date 2017-03-29 15:15
 */
public class FindFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
    private View view = null;
    private GridView userGridView;
    private FindUserListAdapter mAdapter;
    private UserController controller;
    private List<SLUserDetail> userList;
    private PullToRefreshView mPullToRefreshView;
    private int pageNumber = 0;
    private int dataGetType = 0;
    private int isEndPage = 0;
    private FindReciver reciver;
    private View emptyView;
    private TextView errorContent;
    private boolean isError = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("FindFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new UserController(getActivity(), handler);
        reciver = new FindReciver();
        initIntent();
        LogUtil.d("FindFragment____onCreate");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("FindFragment____onCreateView");
        // 防止onCreateView被多次调用
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_find, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        ImageView rightView = (ImageView) view.findViewById(R.id.rightBtn);
        titleView.setText(R.string.find);
        rightView.setVisibility(View.GONE);
        rightView.setBackgroundResource(R.mipmap.share_btn);
        rightView.setOnClickListener(this);
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_view);
        emptyView = view.findViewById(R.id.emptyLayout);
        LinearLayout errorLayout = (LinearLayout) emptyView.findViewById(R.id.errorLayout);
        errorContent = (TextView) emptyView.findViewById(R.id.errorContent);
        errorLayout.setOnClickListener(this);
        emptyView.setVisibility(View.GONE);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        userGridView = (GridView) view.findViewById(R.id.userGridView);
        mAdapter = new FindUserListAdapter(getActivity(), userList);
        userGridView.setAdapter(mAdapter);
        userGridView.setVisibility(View.VISIBLE);
        initData();
    }

    private void initData() {
        // 请求服务器
        customProgressDialog = new CustomProgressDialog(getActivity(), getString(R.string.loading));
        customProgressDialog.show();
        updateData(DataGetType.DOWN);
        dataGetType = 0;
    }

    private void initIntent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(1100);
        intentFilter.addAction(Actions.ACTION_LOGIN_SUCCESS);
        intentFilter.addAction(Actions.ACTION_EXIT_APP);
        getActivity().registerReceiver(reciver, intentFilter);
    }

    private class FindReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Actions.ACTION_EXIT_APP) || intent.getAction().equals(Actions.ACTION_LOGIN_SUCCESS)) {
                dataGetType = 0;
                pageNumber = 0;
                updateData(DataGetType.DOWN);
            }
        }
    }

    private void updateData(final DataGetType dataGetType) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                controller.findAll(pageNumber, dataGetType.getCode(), 0, 0, "", "");
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
            Toast.makeText(getActivity(), R.string.no_next_page_data, Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rightBtn:
                intent = IntentManager.createIntent(getActivity(), ShareActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
                break;
            case R.id.errorLayout: {
                if (isError) { // 更新数据
                    initData();
                } else { // 引导用户去发布视频
                    if (0l == AppUtils.getInstance().getUserId() && !Constant.loginActivityIng) {
                        intent = IntentManager.createIntent(getActivity(), UserLoginActivity.class);
                        startActivityForResult(intent, 0);
                        getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
                        Toast.makeText(getActivity(), R.string.login_tips, Toast.LENGTH_LONG).show();
                        return;
                    } else {
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
                        MediaRecorderActivity.goSmallVideoRecorder(getActivity(), VideoImageActivity.class.getName(), config);
                    }
                }
                break;
            }
            default:
                break;
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
            case MessageSignConstant.USER_FIND_ALL_SUCCESS:
                pageNumber = msg.getData().getInt("currentPage");
                isEndPage = msg.getData().getInt("isEndPage");
                userList = (List<SLUserDetail>) msg.getData().getSerializable("userList");
                if (null == userList) {
                    userList = new ArrayList<>();
                }
                if (userList.size() <= 0) {
                    isError = false;
                    userGridView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    errorContent.setText(R.string.find_no_data);
                } else {
                    userGridView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                if (dataGetType == 0) {
                    mAdapter.updateData(userList, true);
                } else {
                    mAdapter.updateData(userList, false);
                }
                break;
            case MessageSignConstant.USER_FIND_ALL_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_find_all_failure), message);
                promptDialog.show();
                if (userList.size() <= 0) {
                    isError = true;
                    emptyView.setVisibility(View.VISIBLE);
                    userGridView.setVisibility(View.GONE);
                    errorContent.setText(R.string.user_find_all_failure);
                } else {
                    userGridView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                break;
            case MessageSignConstant.SERVER_OR_NETWORK_ERROR:
                promptDialog.initData("", msg.getData().getString("message"));
                promptDialog.show();
                break;
            case MessageSignConstant.UNKNOWN_ERROR:
                promptDialog.initData("", getString(R.string.unknown_error));
                promptDialog.show();
                if (userList.size() <= 0) {
                    isError = true;
                    emptyView.setVisibility(View.VISIBLE);
                    userGridView.setVisibility(View.GONE);
                    errorContent.setText(R.string.unknown_error);
                } else {
                    userGridView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                break;
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("FindFragment____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("FindFragment____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("FindFragment____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("FindFragment____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("FindFragment____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("FindFragment____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != reciver) {
            getActivity().unregisterReceiver(reciver);
            reciver = null;
        }
        LogUtil.d("FindFragment____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("FindFragment____onDetach");
    }
}