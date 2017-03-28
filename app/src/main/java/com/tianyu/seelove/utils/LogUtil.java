package com.tianyu.seelove.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 日志工具类,对所有的日志文件进行统一管理
 * @author shisheng.zhao
 * @date 2017-03-28 16:12
 */
public class LogUtil {
    private static final String TAG = "com.tianyu.seelove";
    public static boolean enable = true;

    private LogUtil() {
    }

    public static void i(String msg) {
        if (enable) {
            Log.i(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (enable) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (enable) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (enable) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (enable) {
            if (tag == null || TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            Log.e(tag, msg);
        }
    }

    public static void e(String msg, Throwable e) {
        if (enable) {
            Log.e(TAG, msg, e);
        }
    }

    public static void w(String msg) {
        if (enable) {
            Log.w(TAG, msg);
        }
    }

    public static void log2file(Context context, String Tag, String me) {
        if (enable) {
            try {
                Log.d(Tag, me);
                write2SD(context, Tag + ":" + me);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void write2SD(Context context, String msg) throws IOException {
        if (enable) {
            File file = null;
//			if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
//				file = new File(Environment.getExternalStorageDirectory(),
//						"hlog");
//			} else {
//				file = new File(context.getCacheDir(), "hlog");
//			}
            file = new File(context.getCacheDir(), "hlog");
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter writer = new PrintWriter(new FileOutputStream(file, true));
            writer.append("---------------------\n\r");
            writer.append("time:"
                    + new Date(System.currentTimeMillis()).toLocaleString() + "\n\r");
            writer.append("info:" + msg + "\n\r");
            writer.append("---------------------\n\r");
            writer.flush();
            writer.close();
        }
    }
}
