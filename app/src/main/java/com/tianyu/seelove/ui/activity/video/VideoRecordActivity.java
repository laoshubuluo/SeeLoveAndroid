package com.tianyu.seelove.ui.activity.video;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;
import com.tianyu.seelove.R;
import com.tianyu.seelove.application.SeeLoveApplication;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.controller.VideoController;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.model.entity.video.VideoUploadResponse;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import com.tianyu.seelove.view.dialog.VideoTitleDialog;
import com.tianyu.seelove.view.video.FocusSurfaceView;
import com.tianyu.seelove.view.video.MyVideoView;
import com.tianyu.seelove.view.video.RecordedButton;
import com.yixia.camera.MediaRecorderBase;
import com.yixia.camera.MediaRecorderNative;
import com.yixia.camera.VCamera;
import com.yixia.camera.model.MediaObject;
import com.yixia.videoeditor.adapter.UtilityAdapter;
import org.json.JSONObject;
import java.io.File;
import java.util.LinkedList;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 录制视频界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class VideoRecordActivity extends BaseActivity implements MediaRecorderBase.OnEncodeListener {
    private static final int REQUEST_KEY = 100;
    private MediaRecorderNative mMediaRecorder;
    private MediaObject mMediaObject;
    private FocusSurfaceView sv_ffmpeg;
    private RecordedButton rb_start;
    private int maxDuration = 10000;
    private boolean recordedOver;
    private MyVideoView vv_play;
    private ImageView iv_finish;
    private ImageView iv_back;
    private float dp100;
    private TextView tv_hint;
    private float backX = -1;
    private TextView textView;
    private VideoController controller;
    private String videoTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentColor("#00000000");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_record);
        controller = new VideoController(this, handler);
        sv_ffmpeg = (FocusSurfaceView) findViewById(R.id.sv_ffmpeg);
        rb_start = (RecordedButton) findViewById(R.id.rb_start);
        vv_play = (MyVideoView) findViewById(R.id.vv_play);
        iv_finish = (ImageView) findViewById(R.id.iv_finish);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        dp100 = getResources().getDimension(R.dimen.dp100);
        initMediaRecorder();
        sv_ffmpeg.setTouchFocus(mMediaRecorder);
        rb_start.setMax(maxDuration);
        rb_start.setOnGestureListener(new RecordedButton.OnGestureListener() {
            @Override
            public void onLongClick() {
                mMediaRecorder.startRecord();
                myHandler.sendEmptyMessageDelayed(0, 50);
            }

            @Override
            public void onClick() {

            }

            @Override
            public void onLift() {
                recordedOver = true;
                videoFinish();
            }

            @Override
            public void onOver() {
                recordedOver = true;
                rb_start.closeButton();
                videoFinish();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 初始化视频拍摄状态
     */
    private void initMediaRecorderState() {
        vv_play.setVisibility(View.GONE);
        vv_play.pause();
        iv_back.setX(backX);
        iv_finish.setX(backX);
        tv_hint.setVisibility(View.VISIBLE);
        rb_start.setVisibility(View.VISIBLE);
        lastValue = 0;
    }

    private void videoFinish() {
        textView = showProgressDialog();
        mMediaRecorder.stopRecord();
        //开始合成视频, 异步
        mMediaRecorder.startEncoding();
    }

    float lastValue;

    private void startAnim() {
        rb_start.setVisibility(View.GONE);
        ValueAnimator va = ValueAnimator.ofFloat(0, dp100).setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float dis = value - lastValue;
                iv_back.setX(iv_back.getX() - dis);
                iv_finish.setX(iv_finish.getX() + dis);
                lastValue = value;
            }
        });
        va.start();
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!recordedOver) {
                rb_start.setProgress(mMediaObject.getDuration());
                myHandler.sendEmptyMessageDelayed(0, 50);
                tv_hint.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 初始化录制对象
     */
    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorderNative();
        mMediaRecorder.setOnEncodeListener(this);
        String key = String.valueOf(System.currentTimeMillis());
        //设置缓存文件夹
        mMediaObject = mMediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath());
        //设置视频预览源
        mMediaRecorder.setSurfaceHolder(sv_ffmpeg.getHolder());
        //准备
        mMediaRecorder.prepare();
        //滤波器相关
        UtilityAdapter.freeFilterParser();
        UtilityAdapter.initFilterParser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaRecorder.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaRecorder.stopPreview();
    }

    @Override
    public void onBackPressed() {
        if (rb_start.getVisibility() != View.VISIBLE) {
            initMediaRecorderState();
            LinkedList<MediaObject.MediaPart> medaParts = mMediaObject.getMedaParts();
            for (MediaObject.MediaPart part : medaParts) {
                mMediaObject.removePart(part, true);
            }
            mMediaRecorder.startPreview();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (backX == -1) {
            backX = iv_back.getX();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_KEY) {
                LinkedList<MediaObject.MediaPart> medaParts = mMediaObject.getMedaParts();
                for (MediaObject.MediaPart part : medaParts) {
                    mMediaObject.removePart(part, true);
                }
                deleteDir(SeeLoveApplication.VIDEO_PATH);
            }
        }
    }

    /**
     * 删除文件夹下所有文件
     */
    public static void deleteDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File f : files) {
                deleteDir(f.getAbsolutePath());
            }
        } else if (dir.exists()) {
            dir.delete();
        }
    }

    @Override
    public void onEncodeStart() {
        Log.i("Log.i", "onEncodeStart");
    }

    @Override
    public void onEncodeProgress(int progress) {
        if (textView != null) {
            textView.setText("视频处理中 " + progress + "%");
        }
    }

    /**
     * 视频编辑完成
     */
    @Override
    public void onEncodeComplete() {
        closeProgressDialog();
        final String path = mMediaObject.getOutputTempVideoPath();
        if (!TextUtils.isEmpty(path)) {
            iv_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final VideoTitleDialog videoTitleDialog = new VideoTitleDialog(v.getContext());
                    videoTitleDialog.show();
                    videoTitleDialog.getSureTV().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            videoTitle = videoTitleDialog.getContentTV().getText().toString().trim();
                            // 请求服务器
                            customProgressDialog = new CustomProgressDialog(VideoRecordActivity.this, getString(R.string.loading));
                            customProgressDialog.show();
                            videoTitleDialog.dismiss();
                            uploadFile(path);
                        }
                    });
//                    Intent intent = new Intent(VideoRecordActivity.this, EditVideoActivity.class);
//                    intent.putExtra("videoPath", path);
//                    startActivity(intent);
//                    finish();
//                    startActivityForResult(intent, REQUEST_KEY);
//                    initMediaRecorderState();
                }
            });
            vv_play.setVisibility(View.VISIBLE);
            vv_play.setVideoPath(path);
            vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    vv_play.setLooping(true);
                    vv_play.start();
                }
            });
            vv_play.start();
            recordedOver = false;
            startAnim();
        }
    }

    @Override
    public void onEncodeError() {
        Log.i("Log.i", "onEncodeError");
    }

    private void uploadFile(String videoPath) {
        try {
            // 1 构造上传策略
            JSONObject _json = new JSONObject();
            long _dataline = System.currentTimeMillis() / 1000 + 3600;
            _json.put("deadline", _dataline);// 有效时间为一个小时
            _json.put("scope", "laoshubuluo");
            String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json.toString().getBytes());
            byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, WebConstant.SecretKey);
            String _encodedSign = UrlSafeBase64.encodeToString(_sign);
            String _uploadToken = WebConstant.AccessKey + ':' + _encodedSign + ':'
                    + _encodedPutPolicy;
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(videoPath, null, _uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            VideoUploadResponse videoUploadResponse = GsonUtil.fromJson(response.toString(), VideoUploadResponse.class);
                            String videoUrl = "http://7xrjck.com1.z0.glb.clouddn.com/" + videoUploadResponse.getKey();
                            SLVideo slVideo = new SLVideo();
                            slVideo.setVideoId(System.currentTimeMillis());
                            slVideo.setUserId(AppUtils.getInstance().getUserId());
                            slVideo.setVideoTitle(videoTitle);
                            slVideo.setVideoTime(maxDuration + "");
                            slVideo.setIsDefault("0");
                            slVideo.setVideoTime("");
                            slVideo.setVideoUrl(videoUrl);
                            controller.create(slVideo);
                        }
                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 这个签名方法找了半天 一个个对出来的、程序猿辛苦啊、 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
            throws Exception {
        byte[] data = encryptKey.getBytes(WebConstant.ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, WebConstant.MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(WebConstant.MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(WebConstant.ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
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
            case MessageSignConstant.VIDEO_CREATE_SUCCESS:
                Toast.makeText(this, "发布成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case MessageSignConstant.VIDEO_CREATE_FAILURE:
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
}
