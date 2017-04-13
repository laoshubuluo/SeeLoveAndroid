package com.tianyu.seelove.view.messageplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.tianyu.seelove.R;
import com.tianyu.seelove.ui.activity.message.MapMessageActivity;
import com.tianyu.seelove.utils.ViewUtils;

/**
 * @author shisheng.zhao
 * @Description: 自定义消息常用语Plugin
 * @date 2017-04-13 22:35
 */
public class LocationMessaePlugin extends MessagePlugin {
    private Context mContext;

    public LocationMessaePlugin(final PluginManager manager) {
        super(manager);
        mContext = manager.getContext();
        manager.addResultChangedNotifier(new PluginManager.ResultChangedNotifier() {

            @Override
            public void notifyResultChanged(int requestCode, int resultCode, Intent data) {
                ViewUtils.shutView(manager.getPluginbox());
                manager.resetAllPlugins();
                if (data == null) {
                    return;
                }
            }
        });
    }

    @Override
    protected int getEntranceBtnImg() {
        return R.mipmap.chat_tool_paint;
    }

    @Override
    public void onEntranceClick() {
        Intent intent = new Intent(mContext, MapMessageActivity.class);
        intent.putExtra("target", manager.getTarget());
        ((Activity) mContext).startActivityForResult(intent, 10086);
    }

    @Override
    protected void reset() {
    }

    @Override
    protected String getEntanceText() {
        return "位置";
    }
}
