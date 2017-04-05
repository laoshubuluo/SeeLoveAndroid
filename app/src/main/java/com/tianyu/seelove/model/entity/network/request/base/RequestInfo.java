package com.tianyu.seelove.model.entity.network.request.base;

import android.content.Context;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class RequestInfo extends AbstractRequestInfo {
    private SystemInfo systemInfo;
    private AppInfo appInfo;
    private ActionInfo actionInfo;

    public RequestInfo(Context context, ActionInfo actionInfo) {
        this.systemInfo = new SystemInfo(context);
        this.appInfo = new AppInfo(context);
        this.actionInfo = actionInfo;
    }
}