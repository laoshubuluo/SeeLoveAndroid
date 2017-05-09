package com.tianyu.seelove.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.tianyu.seelove.manager.CrashHandlerManager;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.manager.DirectoryManager;
import com.tianyu.seelove.manager.RongCloudManager;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.yixia.camera.VCamera;

import java.io.File;

import cn.sharesdk.framework.ShareSDK;
import io.rong.imlib.RongIMClient;

/**
 * 系统初始化application
 *
 * @author shisheng.zhao
 * @date 2016-11-14 18:06:11
 */
public class SeeLoveApplication extends Application {
    public static String deviceMode = "";
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
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(this);
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
        initVCamera();
    }

    /**
     * 初始化VCamera
     */
    private void initVCamera() {
        VIDEO_PATH += String.valueOf(System.currentTimeMillis());
        File file = new File(VIDEO_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 设置视频缓存路径
        VCamera.setVideoCachePath(VIDEO_PATH);
        // 开启log输出,ffmpeg输出logcat
        VCamera.setDebugMode(true);
        // 初始化拍摄SDK,必须进行初始化
        VCamera.initialize(this);
    }

    private String getDeviceModel() {
        return android.os.Build.MODEL; // 手机型号
    }
}
