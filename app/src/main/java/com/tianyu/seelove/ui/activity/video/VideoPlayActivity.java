package com.tianyu.seelove.ui.activity.video;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;
import com.tianyu.seelove.R;
import com.tianyu.seelove.model.entity.video.VideoUploadResponse;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.view.video.MyVideoView;
import org.json.JSONObject;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 个人信息界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class VideoPlayActivity extends BaseActivity {
    private Button uploadBtn;
    private MyVideoView vv_play;
    String AccessKey = "8Pu-IcVd7xcMrLdLSubKgT8EWVfKtZOrCkQ0aSox";
    String SecretKey = "MVgYeiasQigSxm5pK0kM94O_1q_DKLoa1lQYlIQ6";
    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";
    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);
        uploadBtn = (Button) findViewById(R.id.uploadBtn);
        vv_play = (MyVideoView) findViewById(R.id.vv_play);
        path = getIntent().getStringExtra("path");
        vv_play.setVideoPath(path);
        vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_play.setLooping(true);
                vv_play.start();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    private void uploadFile() {
        try {
            // 1 构造上传策略
            JSONObject _json = new JSONObject();
            long _dataline = System.currentTimeMillis() / 1000 + 3600;
            _json.put("deadline", _dataline);// 有效时间为一个小时
            _json.put("scope", "laoshubuluo");
            String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json.toString().getBytes());
            byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, SecretKey);
            String _encodedSign = UrlSafeBase64.encodeToString(_sign);
            String _uploadToken = AccessKey + ':' + _encodedSign + ':'
                    + _encodedPutPolicy;
            String SAVE_FILE_DIRECTORY = Environment
                    .getExternalStorageDirectory() + "/SeeLoveRecordedDemo/1492313332882/finish.mp4";
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(path, null, _uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            VideoUploadResponse videoUploadResponse = GsonUtil.fromJson(response.toString(), VideoUploadResponse.class);
                            String videoUrl = "http://7xrjck.com1.z0.glb.clouddn.com/" + videoUploadResponse.getKey();
                            Toast.makeText(VideoPlayActivity.this, videoUrl, Toast.LENGTH_SHORT).show();
                        }
                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 这个签名方法找了半天 一个个对出来的、、、、程序猿辛苦啊、、、 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
            throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }
}
