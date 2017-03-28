package com.tianyu.seelove.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import java.io.File;

/**
 * 存储地址获取工具
 * @author shisheng.zhao
 * @date 2017-03-28 16:15
 */
public class StorageUtil {
    private static final String CACHEDIR = "cacheSeeLove";
    private static final String LOGDIR = "logSeeLove";
    private static final String DOWNLOADDIR = "download";
    private static final String SETTINGDIR = "settingSeeLove";
    private static final String UNZIPDIR="Android";

    private StorageUtil() {
    }

    /**
     * 获得缓存目录(如不存在，会自动创建)
     */
    public static String getCacheDirectory(Context context) {
        StringBuilder dirSb = new StringBuilder(getStorageBasePath(context))
                .append(File.separator)
                .append(context.getPackageName())
                .append(File.separator)
                .append(CACHEDIR);
        String dir = dirSb.toString();
        // 检查路径是否存在
        File file = new File(dir);
        if (!file.exists())
            file.mkdirs();
        try {
            String command = "chmod 777 " + dir;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
        } catch (Throwable e) {
            LogUtil.e("get cache directory error", e);
        }
        return dir;
    }

    /**
     * 获取错误日志目录(如不存在，会自动创建)
     * @param context
     * @return
     */
    public static String getLogDirectory(Context context) {
        StringBuilder dirSb = new StringBuilder(getStorageBasePath(context))
                .append(File.separator)
                .append(context.getPackageName())
                .append(File.separator)
                .append(LOGDIR);
        String dir = dirSb.toString();
        // 检查路径是否存在
        File file = new File(dir);
        if (!file.exists())
            file.mkdirs();
        try {
            String command = "chmod 777 " + dir;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
        } catch (Throwable e) {
            LogUtil.e("get log directory error", e);
        }
        return dir;
    }

    /**
     * 获取下载目录(如不存在，会自动创建)
     * @param context
     * @return
     */
    public static String getDownloadDirectory(Context context) {
        StringBuilder dirSb = new StringBuilder(getStorageBasePath(context))
                .append(File.separator)
                .append(context.getPackageName())
                .append(File.separator)
                .append(DOWNLOADDIR);
        String dir = dirSb.toString();
        // 检查路径是否存在
        File file = new File(dir);
        if (!file.exists())
            file.mkdirs();
        try {
            String command = "chmod 777 " + dir;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
        } catch (Throwable e) {
            LogUtil.e("get download directory error", e);
        }
        return dir;
    }

    public static String getSettingDirectory(Context context) {
        StringBuilder dirSb = new StringBuilder(getStorageBasePath(context))
                .append(File.separator)
                .append(context.getPackageName())
                .append(File.separator)
                .append(SETTINGDIR);
        String dir = dirSb.toString();
        // 检查路径是否存在
        File file = new File(dir);
        if (!file.exists())
            file.mkdirs();
        try {
            String command = "chmod 777 " + dir;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
        } catch (Throwable e) {
            LogUtil.e("get setting directory error", e);
        }
        return dir;
    }

    /**
     * 获取SD卡或者内存路径
     * @return
     */
    public static String getStorageBasePath(Context context) {
        String storagePath;
        // 检查SDcard存储空间是否充足
        if (checkSDcardSpace(10)) {
            storagePath = Environment.getExternalStorageDirectory().toString();
        }
        // 检查Rom存储空间是否充足
        else if (checkRomSpace(10)) {
            storagePath = context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
        // Rom存储及SDcard存储均无可用空间，开始释放资源
        else {
            // 删除缓存资源
            deleteCache(context);
            storagePath = context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
        return storagePath;
    }

    /**
     * 检查Rom存储空间是否充足
     * @return
     */
    public static boolean checkRomSpace(int sizeMb) {
        boolean ishasSpace = false;
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();
        long availableSpace = (availCount * blockSize) / (1024 * 1024);
        LogUtil.i("ROM available space = " + availableSpace);
        if (availableSpace > sizeMb) {
            ishasSpace = true;
        }
        return ishasSpace;
    }

    /**
     * 检查SDcard存储空间是否充足
     * @return
     */
    public static boolean checkSDcardSpace(int sizeMb) {
        boolean ishasSpace = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            long availableSpace = (blocks * blockSize) / (1024 * 1024);
            LogUtil.i("sd card available space = " + availableSpace);
            if (availableSpace > sizeMb) {
                ishasSpace = true;
            }
        }
        return ishasSpace;
    }

    /**
     * 删除缓存
     */
    public static void deleteCache(Context context) {
        File bootDir = new File(getCacheDirectory(context));
        if (!bootDir.exists()) {
            return;
        }
        File[] files = bootDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    /**
     * 获取大文件解压缩目录
     * @param context
     * @return
     */
    public static String getUnzipDirectory(Context context){
        StringBuilder dirSb = new StringBuilder(getStorageBasePath(context))
                .append(File.separator)
                .append(UNZIPDIR);
        String dir = dirSb.toString();
        // 检查路径是否存在
        File file = new File(dir);
        if (!file.exists())
            file.mkdirs();
        try {
            String command = "chmod 777 " + dir;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
        } catch (Throwable e) {
            LogUtil.e("get download directory error", e);
        }
        return dir;
    }
}