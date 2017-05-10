package com.tianyu.seelove.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import com.tianyu.seelove.manager.CrashHandlerManager;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.manager.DirectoryManager;
import com.tianyu.seelove.manager.RongCloudManager;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import java.io.File;
import cn.sharesdk.framework.ShareSDK;
import io.rong.imlib.RongIMClient;
import mabeijianxi.camera.VCamera;
import mabeijianxi.camera.util.DeviceUtils;

/**
 * 系统初始化application
 * @author shisheng.zhao
 * @date 2016-11-14 18:06:11
 */
public class SeeLoveApplication extends Application {
    public static String deviceMode = "";
    public static String versionCode = "";
    public static String VIDEO_PATH = "/sdcard/SeeLoveRecordedDemo/";

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(this);
        // 初始化ImageLoader框架
        ImageLoaderUtil.initImageLoader(this);
        // 添加运行时异常和未处理非运行时异常捕捉器，用于错误收集于上报
        CrashHandlerManager crashHandler = CrashHandlerManager.getInstance();
        crashHandler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        // 初始化目录管理
        DirectoryManager.init(this);
        // 初始化本地数据库连接管理
        DbConnectionManager.init(this);
        DbConnectionManager.getInstance().getConnection();
        // 初始化融云SDK
        RongIMClient.init(this);
        RongCloudManager.getInstance().init(this);
        // 初始化分享SDK
        ShareSDK.initSDK(this);
        deviceMode = getDeviceModel();
        versionCode = getVersionCode(this);
        initSmallVideo(this);
    }

    public static void initSmallVideo(Context context) {
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
            } else {
                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/");
            }
        } else {
            VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        VCamera.setDebugMode(true);
        VCamera.initialize(context);
    }

    private String getDeviceModel() {
        return android.os.Build.MODEL; // 手机型号
    }

    public String getVersionCode(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode=packageInfo.versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
