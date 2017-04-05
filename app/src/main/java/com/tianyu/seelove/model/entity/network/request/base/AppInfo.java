package com.tianyu.seelove.model.entity.network.request.base;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.tianyu.seelove.utils.LogUtil;


/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class AppInfo extends AbstractRequestInfo {

    private String language;
    private int versionCode;
    private String versionName;
    private String bundleId;
    private String channelId;// 渠道号

    public AppInfo(Context context) {
        String packageName = context.getPackageName();
        this.language = "zh";
        this.bundleId = packageName;
        try {
            this.versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
            this.versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            this.channelId = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e("package version get error: ", e);
        }
    }
}