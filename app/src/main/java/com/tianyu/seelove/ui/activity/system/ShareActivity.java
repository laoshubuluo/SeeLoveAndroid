package com.tianyu.seelove.ui.activity.system;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.ShareListAdapter;
import com.tianyu.seelove.manager.ShareManager;
import com.tianyu.seelove.model.entity.share.ShareInfo;
import com.tianyu.seelove.model.enums.ShareTo;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * 分享信息界面
 * @author shisheng.zhao
 * @date 2017-04-22 17:23
 */
public class ShareActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private GridView shareGridView;
    private Button btnCancel;
    private ShareListAdapter adapter;
    private boolean isGridViewItemCanClick = true;// GridView条目是否可点击
    private ShareManager shareManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentColor("#00000000");
        setContentView(R.layout.activity_share);
        shareManager = new ShareManager(this);
        initView();
        initData();
    }

    private void initView() {
        shareGridView = (GridView) findViewById(R.id.shareGridView);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        shareGridView.setOnItemClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initData(){
        List<ShareInfo> shareList = new ArrayList<>();
        shareList.add(new ShareInfo(R.mipmap.share_wechat_icon, getString(R.string.share_wechat)));
        shareList.add(new ShareInfo(R.mipmap.share_wechatspace_icon, getString(R.string.share_wechat_space)));
        shareList.add(new ShareInfo(R.mipmap.share_qq_icon, getString(R.string.share_qq)));
        shareList.add(new ShareInfo(R.mipmap.share_qqspace_icon, getString(R.string.share_qq_space)));
//        shareList.add(new ShareInfo(R.mipmap.share_sinaweibo_icon, getString(R.string.share_sina_weibo_space)));
        adapter = new ShareListAdapter(ShareActivity.this, shareList);
        shareGridView.setAdapter(adapter);
        shareGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));// 点击后背景不变
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long _id) {
        // GridView条目是否可点击
        if (isGridViewItemCanClick) {
            isGridViewItemCanClick = false;
        } else {
            return;
        }
        ShareInfo shareInfo = (ShareInfo) parent.getAdapter().getItem(position);
        Intent intent;
        switch (shareInfo.getShareId()) {
            case R.mipmap.share_wechat_icon: // 微信
                shareManager.share(ShareTo.WECHAT);
                break;
            case R.mipmap.share_wechatspace_icon: // 朋友圈
                shareManager.share(ShareTo.WECHAT_CIRCLE);
                break;
            case R.mipmap.share_qq_icon: // QQ
                shareManager.share(ShareTo.QQ);
                break;
            case R.mipmap.share_qqspace_icon: // QQ空间
                shareManager.share(ShareTo.QQ_CIRCLE);
                break;
            case R.mipmap.share_sinaweibo_icon: // 新浪微博
                shareManager.share(ShareTo.SINA_WEIBO);
                break;
            default:
                break;
        }
        finish();
        overridePendingTransition(R.anim.down_in, R.anim.down_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel: // 取消
                break;
            default:
                break;
        }
        finish();
        overridePendingTransition(R.anim.down_in, R.anim.down_out);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        overridePendingTransition(R.anim.down_in, R.anim.down_out);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.down_in, R.anim.down_out);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGridViewItemCanClick = true;
    }
}
