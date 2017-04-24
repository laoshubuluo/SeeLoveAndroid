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
 * 用户关注和被关注界面
 * @author shisheng.zhao
 * @date 2017-03-29 12:16
 */
public class FollowUserListActivity extends BaseActivity {
    String headUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg";
    private TextView titleView;
    private FollowUserListAdapter adapter;
    private List<SLUser> slUserList = new ArrayList<>();
    private ListView userListView;
    private int followType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        followType = getIntent().getIntExtra("followType", 1);
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
        if (1 == followType) {
            titleView.setText(R.string.my_follow);
        } else if (2 == followType) {
            titleView.setText(R.string.follow_my);
        }
        for (int i = 0; i < 10; i++) {
            SLUser slUser = new SLUser();
            slUser.setHeadUrl(headUrl);
            slUser.setUserId(i + 1);
            slUser.setNickName("Test" + i);
            slUserList.add(slUser);
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
}
