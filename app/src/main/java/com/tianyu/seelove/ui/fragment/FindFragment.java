package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.FindUserListAdapter;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.ui.activity.system.ShareActivity;
import com.tianyu.seelove.ui.fragment.base.BaseFragment;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.view.PullToRefreshView;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import java.util.List;

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("FindFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new UserController(getActivity(), handler);
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
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        userGridView = (GridView) view.findViewById(R.id.userGridView);
        mAdapter = new FindUserListAdapter(getActivity(), userList);
        userGridView.setAdapter(mAdapter);
        // 请求服务器
        customProgressDialog = new CustomProgressDialog(getActivity(), getString(R.string.loading));
        customProgressDialog.show();
        // TODO shisheng.zhao 测试数据
        controller.findAll(0, 0, "", "");
//        controller.findAll(AppUtils.getInstance().getStartAge(),AppUtils.getInstance().getEndAge(),
//                AppUtils.getInstance().getSexCode(),AppUtils.getInstance().getCityCode());
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
		/* 上拉加载更多 */
        h.sendEmptyMessage(1);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
		/* 下拉刷新数据 */
        h.sendEmptyMessage(2);
    }

    Handler h = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                h.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.onFooterRefreshComplete();
                    }
                }, SystemClock.uptimeMillis() + 1000);
            } else if (msg.what == 2) {
                h.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.onHeaderRefreshComplete();
                    }
                }, SystemClock.uptimeMillis() + 1000);
            }
        };
    };

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rightBtn:
                intent = IntentManager.createIntent(getActivity(), ShareActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
                break;
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
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.USER_FIND_ALL_SUCCESS:
                userList = (List<SLUserDetail>) msg.getData().getSerializable("userList");
                mAdapter.updateData(userList, true);
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
        LogUtil.d("FindFragment____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("FindFragment____onDetach");
    }
}