package com.tianyu.seelove.view.messageplugin;


import com.tianyu.seelove.R;
import com.tianyu.seelove.view.VideoSelector;

/**
 * @author shisheng.zhao
 * @Description: 自定义消息语音消息Plugin
 * @date 2015-09-01 上午18:15:26
 */
public class VideoMessagePlugin extends MessagePlugin {
    public VideoMessagePlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    protected int getEntranceBtnImg() {
        return R.mipmap.chat_tool_camera;
    }

    @Override
    public void onEntranceClick() {
        VideoSelector selector = new VideoSelector(manager.getContext());
        selector.show();
    }

    @Override
    protected void reset() {
    }

    @Override
    protected String getEntanceText() {

        return "录像";
    }
}
