package com.tianyu.seelove.model.entity.network.request.base;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.tianyu.seelove.utils.NetworkUtil;

import java.util.TimeZone;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class SystemInfo extends AbstractRequestInfo {
    private String uidi;//OpenUDID（当前版本可以为空）
    private String imei;//设备唯一标示
    private String imsi;//sim卡串号
    private String idfa;//广告标识符
    private String device;//设备型号
    private String brand;//厂商
    private String osType;//系统类型
    private String osVer;//系统版本
    private String language;//系统语言
    private String apn; // 网络类型
    private String timezone;// 时区

    public SystemInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        TimeZone tz = TimeZone.getDefault();
        this.uidi = null;
        this.imei = tm.getDeviceId();
        this.imsi = tm.getSubscriberId();
        this.idfa = null;
        this.device = Build.MODEL;
        this.brand = Build.BRAND;
        this.osType = "Android";
        this.osVer = Build.VERSION.RELEASE;
        this.language = context.getResources().getConfiguration().locale.getLanguage();
        this.apn = NetworkUtil.getNetWorkType(context);
        this.timezone = tz.getID();
    }
}