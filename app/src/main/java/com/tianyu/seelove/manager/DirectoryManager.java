package com.tianyu.seelove.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Environment;

import com.tianyu.seelove.receiver.SDCardChangeReceiver;
import com.tianyu.seelove.utils.LogUtil;

/**
 * @author shisheng.zhao
 * @Description: 文件目录管理基类，负责管理所有的文件目录信息，并且监听SD卡发生的变化
 * @date 2015-09-01 下午14:55:35
 */
public class DirectoryManager {
    static List<SdcardStatusListener> listeners;
    private static File cacheDirectory;
    private static File dataDirectory;

    /**
     * 注册SD卡变化监听器
     * @param paramSdcardStatusListener
     */
    public static void addSdCardListener(
            SdcardStatusListener paramSdcardStatusListener) {
        listeners.add(paramSdcardStatusListener);
    }

    /**
     * 清空指定文件夹下的所有文件
     * @param path 需要清空的路径
     */
    public static void clear(String path) {
        File localFile = new File(path);
        localFile.mkdirs();
        if (localFile.exists()) {
            File[] arrayOfFile = new File(path).listFiles();
            if ((arrayOfFile != null) && (arrayOfFile.length > 0)) {
                int i = arrayOfFile.length;
                for (int j = 0; j < i; j++)
                    arrayOfFile[j].delete();
            }
        }
    }

    /**
     * 获取某个存储目录的绝对路径
     * @param dir
     * @return
     */
    public static File getDirectory(DIR dir) {
        if (dir == DIR.CACHE) {
            return new File(cacheDirectory, dir.toString());
        } else {
            return new File(dataDirectory, dir.toString());
        }
    }

    /**
     * 初始化
     * @param paramContext
     */
    public static void init(Context paramContext) {
        listeners = new ArrayList<SdcardStatusListener>();
        update(paramContext);
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        localIntentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        localIntentFilter.addAction("android.intent.action.MEDIA_REMOVED");
        localIntentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        localIntentFilter.addDataScheme("file");
        paramContext.registerReceiver(new SDCardChangeReceiver(),
                localIntentFilter);
    }

    /**
     * 判断当前存储环境，并创建对应的文件夹
     * @param context
     */
    public static void update(Context context) {
        int i = 0;
        String packageName = context.getPackageName();
        // 判断是否已经挂载SD卡
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            dataDirectory = new File(Environment.getExternalStorageDirectory(),
                    "Android" + File.separator + "data" + File.separator
                            + packageName + File.separator + "files"
                            + File.separator);
            cacheDirectory = new File(
                    Environment.getExternalStorageDirectory(), "Android"
                    + File.separator + "data" + File.separator
                    + packageName + File.separator + "cache"
                    + File.separator);
            for (SdcardStatusListener listener : listeners) {
                listener.onChange(SdcardStatusListener.SDCARD_STATUS.MEDIA_MOUNTED);
            }
        } else {
            dataDirectory = context.getFilesDir();
            cacheDirectory = context.getCacheDir();
        }
        DIR[] arrayOfDIR = DIR.values();
        int j = arrayOfDIR.length;
        while (i < j) {
            DIR localDIR = arrayOfDIR[i];
            if (localDIR.equals(DIR.CACHE)) {
                File localFile = new File(cacheDirectory, localDIR.toString());
                if (localFile.exists()) {
                    i++;
                    continue;
                }
                localFile.mkdirs();
            } else {
                File localFile = new File(dataDirectory, localDIR.toString());
                if (localFile.exists()) {
                    i++;
                    continue;
                }
                localFile.mkdirs();
            }
            i++;
        }
        LogUtil.d("log directory:" + dataDirectory.getAbsolutePath());
        LogUtil.d("cache dorectory:" + cacheDirectory.getAbsolutePath());
    }

    /**
     * 目录枚举
     */
    public enum DIR {
        APK("apk"), DATA("data"), VOICE("voice"), VIDEO("video"), IMAGE("image"), TMP(
                "tmp"), LOG("log"), CACHE("");
        private String locate;
        private DIR(String locate) {
            this.locate = locate;
        }
        public String toString() {
            return this.locate;
        }
    }

    /**
     * SD卡状态发生改变时的监听器
     */
    public abstract interface SdcardStatusListener {
        /**
         * SD卡状态发生变化回调方法
         * @param sdCard_status 当前的SD卡状态
         */
        public abstract void onChange(SDCARD_STATUS sdCard_status);
        public enum SDCARD_STATUS {
            MEDIA_MOUNTED, // 已加载SD卡
            MEDIA_REMOVED, // SD卡被移除
            MEDIA_UNMOUNTED, // SD卡被卸载
            MEDIA_BAD_REMOVAL// SD卡被不正确移除
        }
    }
}
