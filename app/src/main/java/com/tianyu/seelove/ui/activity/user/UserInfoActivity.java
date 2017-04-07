package com.tianyu.seelove.ui.activity.user;

import android.os.Bundle;
import android.os.Message;
import android.widget.GridView;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.FriendController;
import com.tianyu.seelove.model.entity.video.VideoInfo;
import com.tianyu.seelove.model.enums.DataGetType;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;

import java.util.ArrayList;

/**
 * 个人信息界面
 *
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class UserInfoActivity extends BaseActivity {
    private VideoGridAdapter gridAdapter;
    private ArrayList<VideoInfo> videoInfos;
    private String userName = "";

    private FriendController controller;

    private TextView titleView;
    private TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        userName = getIntent().getStringExtra("userName");

        initView();
    }

    public void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        nameView = (TextView) findViewById(R.id.name);
        titleView.setText(userName);
        GridView gridView = (GridView) findViewById(R.id.videoGridView);
        videoInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setVideoTitle("00" + i);
            videoInfos.add(videoInfo);
        }
        gridAdapter = new VideoGridAdapter(this, videoInfos);
        gridView.setAdapter(gridAdapter);

        customProgressDialog = new CustomProgressDialog(UserInfoActivity.this, getString(R.string.loading));
        customProgressDialog.show();
        controller = new FriendController(getApplication(), handler);
        controller.search("1212", 111, 11123123, DataGetType.UPDATE);
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
            promptDialog = new PromptDialog(UserInfoActivity.this);
        switch (msg.what) {
            case MessageSignConstant.FRIEND_SEARCH_SUCCESS:
                int totalPage = msg.getData().getInt("totalPage");
                int currentPage = msg.getData().getInt("currentPage");
                String userName = msg.getData().getString("userName", "");
                titleView.setText(userName);
                nameView.setText(userName);
                break;
            case MessageSignConstant.FRIEND_SEARCH_FAILURE:
                String code = msg.getData().getString("code");
                String message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.friend_search_failure), message);
                promptDialog.show();
                break;
            case MessageSignConstant.SERVER_OR_NETWORK_ERROR:
                promptDialog.initData(getString(R.string.friend_search_error), msg.getData().getString("message"));
                promptDialog.show();
                break;
            case MessageSignConstant.UNKNOWN_ERROR:
                promptDialog.initData(getString(R.string.friend_search_error), getString(R.string.unknown_error));
                promptDialog.show();
                break;
        }
        // 加载效果关闭
        return false;
    }
}