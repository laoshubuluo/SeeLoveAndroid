package com.tianyu.seelove.ui.activity.message;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.tianyu.seelove.R;
import com.tianyu.seelove.injection.ControlInjection;
import com.tianyu.seelove.manager.ThreadManager;
import com.tianyu.seelove.task.GetImageTask;
import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 保存图片到相册
 * @date 2016-03-10 下午15:42:22
 */
public class SaveImageActivity extends BaseActivity {
    Bitmap photo = null;
    @ControlInjection(R.id.btnSaveImage)
    private Button btnSaveImage;
    @ControlInjection(R.id.btn_cancel)
    private Button btn_cancel;
    private String imagePath = "";
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saveimage_choice);
        imagePath = getIntent().getStringExtra("imagePath");
        initView();
    }

    private void initView() {
        btnSaveImage.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnSaveImage:
                GetImageTask task = new GetImageTask();
                task.setOnPostExecuteHandler(new BaseTask.OnPostExecuteHandler<Bitmap>() {

                    @Override
                    public void handle(Bitmap bitmap) {
                        if (null != bitmap) {
                            BitmapUtils.saveImageToGallery(SaveImageActivity.this, bitmap);
                        } else {
                            Toast.makeText(SaveImageActivity.this, R.string.save_image_faile, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    task.execute(imagePath);
                } else {
                    task.executeOnExecutor(ThreadManager.getInstance().getThreadPool(), imagePath);
                }
                finish();
                overridePendingTransition(R.anim.down_in, R.anim.down_out);
                break;
            case R.id.btn_cancel:
                finish();
                overridePendingTransition(R.anim.down_in, R.anim.down_out);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.down_in, R.anim.down_out);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
