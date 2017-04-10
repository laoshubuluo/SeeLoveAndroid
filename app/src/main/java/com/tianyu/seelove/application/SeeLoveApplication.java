package com.tianyu.seelove.application;

import android.app.Application;
import com.tianyu.seelove.manager.CrashHandlerManager;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.manager.RongCloudManager;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import io.rong.imlib.RongIMClient;

/**
 * 系统初始化application
 * @author shisheng.zhao
 * @date 2016-11-14 18:06:11
 */
public class SeeLoveApplication extends Application {
    public static String deviceMode = "";

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
        // 初始化本地数据库连接管理
        DbConnectionManager.init(this);
        DbConnectionManager.getInstance().getConnection();
        // 初始化融云SDK
        RongIMClient.init(this);
        RongCloudManager.getInstance().init(this);
        deviceMode = getDeviceModel();
    }

    private String getDeviceModel() {
        return android.os.Build.MODEL; // 手机型号
    }
}
