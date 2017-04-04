package com.tianyu.seelove.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tianyu.seelove.manager.DirectoryManager;
import com.tianyu.seelove.utils.LogUtil;

/**
 * @author shisheng.zhao
 * @Description: SD卡状态发改变时的接收者
 * @date 2015-09-01 下午13:00:12
 */
public class SDCardChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            LogUtil.d("SDCard status changed:" + action);
            DirectoryManager.update(context);
        }
    }
}
