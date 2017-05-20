package com.tianyu.seelove.ui.activity.video;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.SignGridAdapter;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.common.Constant;
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
import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.model.BaseMediaBitrateConfig;
import mabeijianxi.camera.model.CBRMode;
import mabeijianxi.camera.model.MediaRecorderConfig;

/**
 * 我的视频界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class VideoListActivity extends BaseActivity implements VideoGridAdapter.ShowDeleteSignListener, VideoGridAdapter.DeleteListener {
    private VideoController controller;
    private VideoDao videoDao;
    private MyGridView videoGridView, signGridView;
    private VideoGridAdapter adapter;
    private List<SLVideo> slVideoList = new ArrayList<>();
    private boolean isShowDelete = false;
    private int currentPosition = 0;
    private LinearLayout videoEmptyLayout;
    private SignGridAdapter signGridAdapter;

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
        videoEmptyLayout = (LinearLayout) findViewById(R.id.videoEmptyLayout);
        videoGridView = (MyGridView) findViewById(R.id.videoGridView);
        signGridView = (MyGridView) findViewById(R.id.signGridView);
        videoGridView.setAdapter(adapter);
        videoEmptyLayout.setOnClickListener(this);
    }


    private void initData() {
        slVideoList = videoDao.getVideoListByUserId(AppUtils.getInstance().getUserId());
        adapter.updateData(slVideoList);
        if (null != slVideoList && slVideoList.size() > 0) {
            videoEmptyLayout.setVisibility(View.GONE);
        } else {
            signGridAdapter = new SignGridAdapter(this, Constant.videoNames,1);
            signGridView.setAdapter(signGridAdapter);
            videoEmptyLayout.setVisibility(View.VISIBLE);
        }
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
            case R.id.videoEmptyLayout: {
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
                MediaRecorderActivity.goSmallVideoRecorder(this, VideoImageActivity.class.getName(), config);
                break;
            }
            case R.id.rightBtn: {
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
                MediaRecorderActivity.goSmallVideoRecorder(this, VideoImageActivity.class.getName(), config);
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
                if (slVideoList.size() <= 0) {
                    signGridAdapter = new SignGridAdapter(this, Constant.videoNames, 1);
                    signGridView.setAdapter(signGridAdapter);
                    videoEmptyLayout.setVisibility(View.VISIBLE);
                }
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
