package com.tianyu.seelove.view.messageplugin;

import android.app.Activity;
import android.content.Intent;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.ui.activity.message.AlbumImageActivity;

/**
 * @author shisheng.zhao
 * @Description: 图片消息Plugin
 * @date 2017-04-12 18:35
 */
public class ImageMessagePlugin extends MessagePlugin {
    public static final int IMAGE_FROM_ALBUM = 1400;

    public ImageMessagePlugin(final PluginManager manager) {
        super(manager);
        manager.addResultChangedNotifier(new PluginManager.ResultChangedNotifier() {
            @Override
            public void notifyResultChanged(int requestCode, int resultCode,
                                            Intent data) {
                switch (requestCode) {
                    case IMAGE_FROM_ALBUM:
                        break;
                }
            }
        });
    }

    @Override
    protected int getEntranceBtnImg() {
        return R.mipmap.chat_tool_photo;
    }

    @Override
    public void onEntranceClick() {
        Constant.imageCount = 4; // 聊天发送消息限制图片数量不大于4
        Intent intent = new Intent(manager.getContext(), AlbumImageActivity.class);
        intent.putExtra("target", manager.getTarget());
        ((Activity) manager.getContext()).startActivityForResult(intent, IMAGE_FROM_ALBUM);
    }

    @Override
    protected void reset() {
    }

    @Override
    protected String getEntanceText() {
        return "图片";
    }
}
