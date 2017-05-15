package com.tianyu.seelove.ui.activity.video;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoImageAdapter;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.controller.VideoController;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.model.entity.video.VideoUploadResponse;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.BitmapUtils;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.view.MyGridView;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;

import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import mabeijianxi.camera.MediaRecorderActivity;

/**
 * 选择视频封面功能
 *
 * @author shisheng.zhao
 * @date 2017-04-25 15:53
 */
public class VideoImageActivity extends BaseActivity {
    private VideoController controller;
    private VideoImageAdapter adapter;
    private String videoPath = "";
    private String imagePath = "";
    private String uploadVideoUrl = "";
    private String uploadImageUrl = "";
    private MyGridView videoGridView;
    private EditText editVideoTitle;
    private List<Bitmap> bitmapList = new ArrayList<>();
    private String videoTitle = "";
    private int maxDuration = 10000;
    private int selectPosition = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_image);
        controller = new VideoController(this, handler);
        Intent intent = getIntent();
        videoPath = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
//        videoPath = getIntent().getStringExtra("videoPath");
        initView();
        getVideoThumbnail();
        initData();
    }

    private void initView() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(R.string.video_image);
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        ImageView submitBtn = (ImageView) findViewById(R.id.rightBtn);
        editVideoTitle = (EditText) findViewById(R.id.editVideoTitle);
        backView.setVisibility(View.VISIBLE);
        submitBtn.setBackgroundResource(R.mipmap.submit_btn);
        submitBtn.setVisibility(View.VISIBLE);
        submitBtn.setOnClickListener(this);
        backView.setOnClickListener(this);
        videoGridView = (MyGridView) findViewById(R.id.videoGridView);
    }

    private void initData() {
        adapter = new VideoImageAdapter(this, bitmapList);
        videoGridView.setAdapter(adapter);
        adapter.changeState(0);
        videoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.changeState(position);
                selectPosition = position;
            }
        });
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
                videoTitle = editVideoTitle.getText().toString().trim();
                if (StringUtils.isNullOrBlank(videoTitle)) {
                    Toast.makeText(this, R.string.video_title_is_null, Toast.LENGTH_LONG).show();
                    break;
                }
                // 请求服务器
                customProgressDialog = new CustomProgressDialog(VideoImageActivity.this, getString(R.string.loading));
                customProgressDialog.show();
                Bitmap bitmap = bitmapList.get(selectPosition);
                imagePath = BitmapUtils.saveBitmapToFile(bitmap);
                uploadVideoFile(videoPath);
                break;
            }
        }
    }

    public void getVideoThumbnail() {
        Uri uri = Uri.parse(videoPath);
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(this, uri);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int seconds = Integer.valueOf(time) / 1000;
            int timeS = Integer.valueOf(time) / 10;
            for (int i = 1; i <= 6; i++) {
                bitmap = retriever.getFrameAtTime(i * timeS * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
                bitmapList.add(bitmap);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传视频封面
     *
     * @param imagePath
     */
    private void uploadImageFile(String imagePath) {
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
            uploadManager.put(imagePath, null, _uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            if (StringUtils.isNotBlank(response.toString())) {
                                VideoUploadResponse videoUploadResponse = GsonUtil.fromJson(response.toString(), VideoUploadResponse.class);
                                if (null != videoUploadResponse) {
                                    uploadImageUrl = Constant.videosNameSpace + videoUploadResponse.getKey();
                                    SLVideo slVideo = new SLVideo();
                                    slVideo.setUserId(AppUtils.getInstance().getUserId());
                                    slVideo.setVideoTitle(videoTitle);
                                    slVideo.setIsDefault("0");
                                    slVideo.setVideoTime(String.valueOf(new Date().getTime()));
                                    slVideo.setVideoUrl(uploadVideoUrl);
                                    slVideo.setVideoImg(uploadImageUrl);
                                    controller.create(slVideo);
                                } else {
                                    Toast.makeText(VideoImageActivity.this, R.string.upload_faile, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(VideoImageActivity.this, R.string.upload_faile, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadVideoFile(String videoPath) {
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
                            if (StringUtils.isNotBlank(response.toString())) {
                                VideoUploadResponse videoUploadResponse = GsonUtil.fromJson(response.toString(), VideoUploadResponse.class);
                                if (null != videoUploadResponse) {
                                    uploadVideoUrl = Constant.videosNameSpace + videoUploadResponse.getKey();
                                    uploadImageFile(imagePath);
                                } else {
                                    Toast.makeText(VideoImageActivity.this, R.string.upload_faile, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(VideoImageActivity.this, R.string.upload_faile, Toast.LENGTH_LONG).show();
                            }
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
                // 发布成功之后删除文件目录
                File dcim = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                deleteAllFiles(new File(dcim + "/seelove/"));
                // 发送广播 通知动态刷新数据
                sendBroadcast(new Intent(Actions.ACTION_UPDATE_FOLLOW_LIST));
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

    private void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }
}