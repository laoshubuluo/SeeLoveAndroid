package com.tianyu.seelove.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import com.tianyu.seelove.manager.DirectoryManager;

/**
 * @author shisheng.zhao
 * @Description: 相机工具
 * @date 2017-04-20 16:59
 */
public class CameraUtils {
    /**
     * 保存文件 并返回文件路径
     * @param context
     * @param file
     * @return file 路径
     */
    public static String getPathByCameraFile(Context context, File file) {
        if (file == null) {
            return null;
        }
        FileOutputStream outputStream = null;
        String cameraPicpath = "";
        try {
            Bitmap bmp = BitmapUtils.decodeStream(context.getContentResolver(),
                    Uri.fromFile(file), 400, 600);
            File file1 = new File(DirectoryManager.getDirectory(DirectoryManager.DIR.IMAGE),
                    StringUtils.generateGUID()
                            + ".jpg");
            outputStream = new FileOutputStream(file1);
            if (bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                cameraPicpath = file1.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return cameraPicpath;
    }

    public static String getPathByCameraFile(Context context, Uri uri) {
        FileOutputStream outputStream = null;
        String cameraPicpath = "";
        try {
            Bitmap bmp = BitmapUtils.decodeStream(context.getContentResolver(),
                    uri, 500, 500);
            File file1 = new File(DirectoryManager.getDirectory(DirectoryManager.DIR.IMAGE),
                    StringUtils.generateGUID()
                            + ".jpg");
            outputStream = new FileOutputStream(file1);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            cameraPicpath = file1.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return cameraPicpath;
    }

    /**
     * 获取剪切的intent
     * @param cameraPath
     * @return
     */
    public static Intent getCropIntent(String cameraPath, int outputX,
                                       int outputY) {
        Bitmap zoomBitmap = BitmapUtils
                .getImageFromFile(cameraPath, 800f, 800f);
        OutputStream fOutputStream = null;
        try {
            fOutputStream = new FileOutputStream(cameraPath);
            zoomBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOutputStream);
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
        Intent intents = new Intent("com.android.camera.action.CROP");
        intents.setDataAndType(Uri.fromFile(new File(cameraPath)), "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intents.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intents.putExtra("aspectX", 1);
        intents.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intents.putExtra("outputX", outputX);
        intents.putExtra("outputY", outputY);
        intents.putExtra("return-data", true);
        return intents;
    }

    public static Intent getCropIntent(Context context, Uri uri, int outputX,
                                       int outputY) throws Exception {
        Bitmap zoomBitmap = BitmapUtils.decodeStream(
                context.getContentResolver(), uri, 400, 600);
        OutputStream fOutputStream = null;
        File destFile = new File(DirectoryManager.getDirectory(DirectoryManager.DIR.IMAGE),
                new Date().getTime() + "");
        try {
            fOutputStream = new FileOutputStream(destFile);
            zoomBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);
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
        Intent intents = new Intent("com.android.camera.action.CROP");
        intents.setDataAndType(Uri.fromFile(destFile), "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intents.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intents.putExtra("aspectX", 1);
        intents.putExtra("aspectY", 1);
        intents.putExtra("scale", true);
        // outputX outputY 是裁剪图片宽高
        intents.putExtra("outputX", outputX);
        intents.putExtra("outputY", outputY);
        intents.putExtra("return-data", true);
        return intents;
    }
}
