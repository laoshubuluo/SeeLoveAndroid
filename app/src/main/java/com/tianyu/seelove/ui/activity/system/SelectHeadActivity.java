package com.tianyu.seelove.ui.activity.system;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.ActivityResultConstant;
import com.tianyu.seelove.common.WebConstant;
import com.tianyu.seelove.manager.DirectoryManager;
import com.tianyu.seelove.model.entity.video.VideoUploadResponse;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.BitmapUtils;
import com.tianyu.seelove.utils.CameraUtils;
import com.tianyu.seelove.utils.DimensionUtils;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.StringUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author shisheng.zhao
 * @Description: 選擇頭像
 * @date 2017-04-20 16:59
 */
public class SelectHeadActivity extends BaseActivity {
    Bitmap photo = null;
    private Button btnTakePhoto;
    private Button btnSelectImage;
    private Button btn_cancel;
    private ArrayList<String> dataList = new ArrayList<>();
    private File photeFile = null;
    private String header_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentColor("#00000000");
        setContentView(R.layout.activity_selecthead_choice);
        dataList.add("camera_default");
        initView();
    }

    private void initView() {
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnSelectImage = (Button) findViewById(R.id.btnSelectImage);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btnTakePhoto.setOnClickListener(this);
        btnSelectImage.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnTakePhoto:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photeFile = new File(DirectoryManager.getDirectory(DirectoryManager.DIR.IMAGE), StringUtils.generateGUID() + ".jpg");
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photeFile));
                startActivityForResult(intent, ActivityResultConstant.SELECT_CAMER);
                break;
            case R.id.btnSelectImage:
                intent = new Intent(SelectHeadActivity.this, SignleAlbumActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("dataList", getIntentArrayList(dataList));
                intent.putExtras(bundle);
                startActivityForResult(intent, ActivityResultConstant.SELECT_PICTURE);
                break;
            case R.id.btn_cancel:
                finish();
                overridePendingTransition(R.anim.down_in, R.anim.down_out);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ActivityResultConstant.SELECT_CAMER:
                if (photeFile == null) {
                    return;
                }
                header_path = CameraUtils.getPathByCameraFile(SelectHeadActivity.this, photeFile);
                if (StringUtils.isNotBlank(header_path)) {
                    Intent intents = CameraUtils.getCropIntent(header_path, 300, 300);
                    startActivityForResult(intents, ActivityResultConstant.SELECT_CUT);
                }
                break;
            case ActivityResultConstant.SELECT_PICTURE:
                if (data == null) {
                    return;
                }
                Bundle bundle = data.getExtras();
                Bitmap bitmap = null;
                ArrayList<String> tDataList = (ArrayList<String>) bundle.getSerializable("dataList");
                if (tDataList != null) {
                    for (String path : tDataList) {
                        bitmap = BitmapUtils.getImageFromFile(path, DimensionUtils.convertDipToPixels(getResources(), 400),
                                DimensionUtils.convertDipToPixels(getResources(), 600));
                    }
                }
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                try {
                    Intent intent = CameraUtils.getCropIntent(SelectHeadActivity.this, uri, 300, 300);
                    startActivityForResult(intent, ActivityResultConstant.SELECT_CUT);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case ActivityResultConstant.SELECT_CUT:
                if (data == null) {
                    return;
                }
                OutputStream fOutputStream = null;
                try {
                    photo = data.getExtras().getParcelable("data");
                    header_path = DirectoryManager.getDirectory(DirectoryManager.DIR.IMAGE) + File.separator + "pselfheader.jpg";
                    fOutputStream = new FileOutputStream(header_path);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);
                    uploadFile(header_path); // 上传图片到文件服务器
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fOutputStream.flush();
                        fOutputStream.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        overridePendingTransition(R.anim.down_in, R.anim.down_out);
        return true;
    }

    private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {
        ArrayList<String> tDataList = new ArrayList<String>();
        for (String s : dataList) {
            if (!s.contains("default")) {
                tDataList.add(s);
            }
        }
        return tDataList;
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

    private void uploadFile(String filePath) {
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
            uploadManager.put(filePath, null, _uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            VideoUploadResponse videoUploadResponse = GsonUtil.fromJson(response.toString(), VideoUploadResponse.class);
                            String videoUrl = "http://7xrjck.com1.z0.glb.clouddn.com/" + videoUploadResponse.getKey();
                            // 返回内容至上一级界面
                            Intent i = new Intent();
                            i.putExtra("filePath", videoUrl);
                            setResult(ActivityResultConstant.UPDATE_USER_HEAD, i);
                            finish();
                            overridePendingTransition(R.anim.down_in, R.anim.down_out);
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
}
