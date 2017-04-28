package com.tianyu.seelove.ui.activity.user;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.FollowUserListAdapter;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.FollowController;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户关注和被关注界面
 * @author shisheng.zhao
 * @date 2017-03-29 12:16
 */
public class FollowUserListActivity extends BaseActivity {
    private FollowController controller;
    private TextView titleView;
    private FollowUserListAdapter adapter;
    private List<SLUser> slUserList = new ArrayList<>();
    private ListView userListView;
    private int followType = 0;
    private List<SLUser> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        followType = getIntent().getIntExtra("followType", 1);
        controller = new FollowController(this, handler);
        initView();
        initData();
    }

    private void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        userListView = (ListView) findViewById(R.id.userListView);
    }

    private void initData() {
        // 请求服务器
        customProgressDialog = new CustomProgressDialog(this, getString(R.string.loading));
        customProgressDialog.show();
        if (1 == followType) {
            titleView.setText(R.string.my_follow);
            controller.followFindAllByUser(AppUtils.getInstance().getUserId());
        } else if (2 == followType) {
            titleView.setText(R.string.follow_my);
            controller.followFindAllByFollowUser(AppUtils.getInstance().getUserId());
        }
        adapter = new FollowUserListAdapter(this, slUserList, followType);
        userListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.leftBtn:
                finish();
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
            promptDialog = new PromptDialog(this);
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.FOLLOW_FIND_ALL_BY_USER_SUCCESS:
                userList = (List<SLUser>) msg.getData().getSerializable("userList");
                adapter.updateData(userList, true);
                adapter.notifyDataSetChanged();
                break;
            case MessageSignConstant.FOLLOW_FIND_ALL_BY_USER_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.find_all_failure), message);
                promptDialog.show();
                break;
            case MessageSignConstant.FOLLOW_FIND_ALL_BY_FOLLOWED_USER_SUCCESS:
                userList = (List<SLUser>) msg.getData().getSerializable("userList");
                adapter.updateData(userList, true);
                adapter.notifyDataSetChanged();
                break;
            case MessageSignConstant.FOLLOW_FIND_ALL_BY_FOLLOWED_USER_FAILURE:
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

}
