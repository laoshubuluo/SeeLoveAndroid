package com.tianyu.seelove.ui.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.VideoController;
import com.tianyu.seelove.dao.VideoDao;
import com.tianyu.seelove.dao.impl.VideoDaoImpl;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.view.MyGridView;
import com.tianyu.seelove.view.dialog.PromptDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的视频界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class VideoListActivity extends BaseActivity implements VideoGridAdapter.ShowDeleteSignListener, VideoGridAdapter.DeleteListener {
    private VideoController controller;
    private VideoDao videoDao;
    private MyGridView videoGridView;
    private VideoGridAdapter adapter;
    private List<SLVideo> slVideoList = new ArrayList<>();
    private boolean isShowDelete = false;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        controller = new VideoController(this, handler);
        videoDao = new VideoDaoImpl();
        adapter = new VideoGridAdapter(this, this, this);
        adapter.updateData(slVideoList);
        initView();
        initData();
    }

    private void initView() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(R.string.my_video);
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        ImageView rightView = (ImageView) findViewById(R.id.rightBtn);
        rightView.setBackgroundResource(R.mipmap.create_video_cion);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        rightView.setVisibility(View.VISIBLE);
        rightView.setOnClickListener(this);
        videoGridView = (MyGridView) findViewById(R.id.videoGridView);
        videoGridView.setAdapter(adapter);
    }


    private void initData() {
        slVideoList = videoDao.getVideoListByUserId(AppUtils.getInstance().getUserId());
        adapter.updateData(slVideoList);
    }

    @Override
    public void showDeleteSign() {
        if (isShowDelete) {
            isShowDelete = false;
        } else {
            isShowDelete = true;
            adapter.setIsShowDelete(isShowDelete);
        }
        adapter.setIsShowDelete(isShowDelete);//setIsShowDelete()方法用于传递isShowDelete值
    }

    @Override
    public void delete(int position) {
        currentPosition = position;
        List<Long> videoIdList = new ArrayList<>();
        videoIdList.add(slVideoList.get(position).getVideoId());
        controller.delete(AppUtils.getInstance().getUserId(), videoIdList);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.leftBtn: {
                finish();
                break;
            }
            case R.id.rightBtn: {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), VideoRecordActivity.class);
                view.getContext().startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (customProgressDialog != null)
            customProgressDialog.dismiss();
        if (promptDialog == null || promptDialog.isShowing())
            promptDialog = new PromptDialog(this);
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.VIDEO_DELETE_SUCCESS:
                new VideoDaoImpl().deleteVideoByVideoId(slVideoList.get(currentPosition).getVideoId());
                slVideoList.remove(currentPosition);
                adapter.notifyDataSetChanged();
                break;
            case MessageSignConstant.VIDEO_DELETE_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.delete_video_failure), message);
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
