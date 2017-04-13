package com.tianyu.seelove.view.messageplugin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.tianyu.seelove.R;
import com.tianyu.seelove.manager.DirectoryManager;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.ui.activity.message.ImageSendActivity;
import com.tianyu.seelove.utils.BitmapUtils;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;
import java.io.File;
import java.util.ArrayList;

/**
 * @author shisheng.zhao
 * @Description: 图片消息Plugin
 * @date 2017-04-13 22:28
 */
public class CameraMessagePlugin extends MessagePlugin {
    public static File file = null;
    public static final int IMAGE_FROM_CAMERA = 1401;

    public CameraMessagePlugin(final PluginManager manager) {
        super(manager);
        manager.addResultChangedNotifier(new PluginManager.ResultChangedNotifier() {
            @Override
            public void notifyResultChanged(int requestCode, int resultCode,
                                            Intent data) {
                switch (requestCode) {
                    case IMAGE_FROM_CAMERA:
                        String sdStatus = Environment.getExternalStorageState();
                        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                            LogUtil.d("TestFile SD card is not avaiable/writeable right now.");
                            return;
                        }
                        try {
                            if (file != null) {
                                ArrayList<String> list_camera = new ArrayList<String>();
                                list_camera.add(file
                                        .getAbsolutePath());
                                String pathString = list_camera.get(0);
                                try {
                                    pathString = BitmapUtils.saveBitmapToFile(BitmapUtils
                                            .getImageFromFileWithHighResolution(
                                                    list_camera.get(0), 1000, 1000));
                                } catch (OutOfMemoryError ex) {
                                    try {
                                        pathString = BitmapUtils.saveBitmapToFile(BitmapUtils
                                                .getImageFromFileWithHighResolution(
                                                        list_camera.get(0), 500,
                                                        500));
                                    } catch (OutOfMemoryError error) {
                                        ex.printStackTrace();
                                    }
                                }
                                if (StringUtils.isNotBlank(pathString)) {
                                    Intent intent = IntentManager.createIntent(manager.getContext(), ImageSendActivity.class);
                                    intent.putExtra("images", pathString);
                                    intent.putExtra("target", manager.getTarget());
                                    manager.getContext().startActivity(intent);
                                } else {
//                                    ((Activity) manager.getContext()).finish();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected int getEntranceBtnImg() {
        return R.mipmap.chat_tool_camera;
    }

    @Override
    public void onEntranceClick() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(DirectoryManager.getDirectory(DirectoryManager.DIR.IMAGE),
                StringUtils.generateGUID()
                        + "c.jpg");
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        ((Activity) manager.getContext()).startActivityForResult(intent,
                IMAGE_FROM_CAMERA);
    }

    @Override
    protected void reset() {
    }

    @Override
    protected String getEntanceText() {
        return "拍照";
    }
}
