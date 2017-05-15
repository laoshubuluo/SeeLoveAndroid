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
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.ShareListAdapter;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.model.entity.share.ShareInfo;
import com.tianyu.seelove.ui.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享信息界面
 *
 * @author shisheng.zhao
 * @date 2017-04-22 17:23
 */
public class ShareActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private GridView shareGridView;
    private Button btnCancel;
    private ShareListAdapter adapter;
    private boolean isGridViewItemCanClick = true;// GridView条目是否可点击
    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentColor("#00000000");
        setContentView(R.layout.activity_share);
        mTencent = Tencent.createInstance(Constant.QQ_APP_ID, this.getApplicationContext());
        initView();
        initData();
    }

    private void initView() {
        shareGridView = (GridView) findViewById(R.id.shareGridView);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        shareGridView.setOnItemClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initData() {
        List<ShareInfo> shareList = new ArrayList<>();
//        shareList.add(new ShareInfo(R.mipmap.share_wechat_icon, getString(R.string.share_wechat)));
//        shareList.add(new ShareInfo(R.mipmap.share_wechatspace_icon, getString(R.string.share_wechat_space)));
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
                break;
            case R.mipmap.share_wechatspace_icon: // 朋友圈
                break;
            case R.mipmap.share_qq_icon: // QQ
                onClickShare();
                break;
            case R.mipmap.share_qqspace_icon: // QQ空间
                shareToQzone();
                break;
            default:
                break;
        }
        finish();
        overridePendingTransition(R.anim.down_in, R.anim.down_out);
    }

    private void onClickShare() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "比图文更真实，短视屏相亲，就在视爱App");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "视爱－让你看见最真实的那个他/她");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://shiai360.com");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://resource.shiai360.com/image/icon.jpg");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "视爱");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 1);
        mTencent.publishToQzone(ShareActivity.this, params, new BaseUiListener());
    }

    private void shareToQzone() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "比图文更真实，短视屏相亲，就在视爱App");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "视爱－让你看见最真实的那个他/她");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://shiai360.com");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://resource.shiai360.com/image/icon.jpg");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "视爱");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 1);
        mTencent.shareToQQ(ShareActivity.this, params, new BaseUiListener());
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            //V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
            Toast.makeText(ShareActivity.this, "onComplete", Toast.LENGTH_LONG).show();
            doComplete(response);
        }

        protected void doComplete(Object values) {

        }

        @Override
        public void onError(UiError e) {
            Toast.makeText(ShareActivity.this, "onError", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(ShareActivity.this, "onCancel", Toast.LENGTH_LONG).show();
        }
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
