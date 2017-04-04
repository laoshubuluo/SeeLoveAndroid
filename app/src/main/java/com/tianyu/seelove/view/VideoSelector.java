package com.tianyu.seelove.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.tianyu.seelove.R;

/**
 * @author shisheng.zhao
 * @Description: videoMessage选择器，可以通过录像和从手机选择两种方式进行选择
 * @date 2015-09-01 上午18:18:37
 */
public class VideoSelector {
    public static final int VIDEO_FROM_ALBUM = 1500;
    public static final int VIDEO_FROM_CAMERA = 1501;
    private Context context;

    public VideoSelector(Context context) {
        this.context = context;
    }

    public void show() {
        LayoutInflater layoutIn = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutIn.inflate(R.layout.popmenu, null);
        final PopupWindow window_video = new PopupWindow(view,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        Button btn_camera = (Button) view.findViewById(R.id.pop_addGroup);
        btn_camera.setText("录像");
        Button btn_album = (Button) view.findViewById(R.id.pop_addPatient);
        btn_album.setText("从手机相册中选择");
        Button pop_cancel = (Button) view.findViewById(R.id.pop_cancel);
        btn_camera.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                // 设置视频大小
                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,
                        768000);
                // 设置视频时间 毫秒单位
                intent.putExtra(
                        MediaStore.EXTRA_DURATION_LIMIT, 45000);
                ((Activity) context).startActivityForResult(intent, VIDEO_FROM_CAMERA);
                window_video.dismiss();
            }
        });
        btn_album.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("video/*");
                ((Activity) context).startActivityForResult(
                        Intent.createChooser(intent, "选择录像"), VIDEO_FROM_ALBUM);
                window_video.dismiss();
            }
        });
        pop_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                window_video.dismiss();
            }
        });

        window_video.setAnimationStyle(R.style.popuStyle);
        window_video.setBackgroundDrawable(null);
        window_video.showAtLocation(
                ((Activity) context).findViewById(R.id.message_layout),
                Gravity.BOTTOM, 0, 0);
    }
}
