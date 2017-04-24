package com.tianyu.seelove.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.FollowUserListAdapter;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.ui.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * splash界面
 * @author shisheng.zhao
 * @date 2017-03-29 12:16
 */
public class FollowUserListActivity extends BaseActivity {
    private FollowUserListAdapter adapter;
    private List<SLUser> slUserList = new ArrayList<>();
    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        initView();
        initData();
    }

    private void initView() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText("关注");
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        userListView = (ListView) findViewById(R.id.userListView);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            SLUser slUser = new SLUser();
            slUser.setUserId(i + 1);
            slUser.setNickName("Test" + i);
            slUserList.add(slUser);
        }
        adapter = new FollowUserListAdapter(this,slUserList);
        userListView.setAdapter(adapter);
    }
}
