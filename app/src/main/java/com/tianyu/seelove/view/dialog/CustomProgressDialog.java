package com.tianyu.seelove.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.tianyu.seelove.R;


/**
 * @author shisheng.zhao
 * @Description: 自定义ProgressDialog
 * @date 2015-09-06 下午17:57:26
 */
public class CustomProgressDialog extends ProgressDialog {
//    private ImageView image;
//    private TextView loadingText;
//
//    private final int LEFT = 0;
//    private final int RIGHT = 1;
//    private final int START = 0;
//
//    private Context context;
//    private RotateAnimation rotateAnim = null;
//    private String text;

    public CustomProgressDialog(Context context, String text) {
        super(context);
//        this.context = context;
//        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.translate);//背景
        setContentView(R.layout.loading);
//        loadingText = (TextView) findViewById(R.id.tv_loading);
//        image = (ImageView) findViewById(R.id.image);
//        loadingText.setText(text);
//        handler.sendEmptyMessageDelayed(LEFT, 100);
    }

//    public void leftStart() {
//        float cX = image.getWidth() / 2.0f;
//        float cY = image.getHeight() / 2.0f;
//        rotateAnim = new RotateAnimation(cX, cY, RotateAnimation.ROTATE_DECREASE);
//        if (rotateAnim != null) {
//            rotateAnim.setFillAfter(true);
//            image.startAnimation(rotateAnim);
//            loadingText.setText(loadingText.getText() + "......");
//        }
//    }
//
//    public void rightStart() {
//        float cX = image.getWidth() / 2.0f;
//        float cY = image.getHeight() / 2.0f;
//        rotateAnim = new RotateAnimation(cX, cY,
//                RotateAnimation.ROTATE_INCREASE);
//        if (rotateAnim != null) {
//            rotateAnim.setFillAfter(true);
//            image.startAnimation(rotateAnim);
//            loadingText.setText(loadingText.getText().toString().substring(0, loadingText.getText().toString().length() - 6));
//        }
//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case LEFT:
//                    leftStart();
//                    sendEmptyMessageDelayed(RIGHT, 800);
//                    break;
//                case RIGHT:
//                    rightStart();
//                    sendEmptyMessageDelayed(LEFT, 800);
//                    break;
//            }
//        }
//    };
}
