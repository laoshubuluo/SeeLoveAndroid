package com.tianyu.seelove.view.messageplugin;

import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import com.tianyu.seelove.view.CustomGridLayout;

/**
 * @author shisheng.zhao
 * @Description: 多媒体消息基类 在聊天过程中，允许用户发送图片、表情、语音、视频等。 多媒体消息将以插件的形式集成到聊天发送页面
 * @date 2015-09-01 上午18:14:37
 */
public abstract class MessagePlugin {
    /**
     * 插件选择入口
     */
    protected CustomGridLayout entrance;
    /**
     * 插件管理器引用
     */
    protected PluginManager manager;

    /**
     * 定义有参数构造方法，让子类必须继承
     *
     * @param
     */
    public MessagePlugin(PluginManager manager) {
        this.manager = manager;
        initView();
    }

    public CustomGridLayout getEntrance() {
        return entrance;
    }

    protected void initView() {
        entrance = new CustomGridLayout(manager.getContext());
        entrance.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        entrance.getGrid_imageView().setImageBitmap(
                BitmapFactory.decodeResource(manager.getContext()
                        .getResources(), getEntranceBtnImg()));
        entrance.getGrid_TextView().setText(getEntanceText());
        entrance.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onEntranceClick();
            }
        });
    }

    protected abstract int getEntranceBtnImg();

    public abstract void onEntranceClick();

    protected abstract String getEntanceText();

    protected abstract void reset();
}
