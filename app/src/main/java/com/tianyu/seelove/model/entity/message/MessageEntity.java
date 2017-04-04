package com.tianyu.seelove.model.entity.message;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * author : L.jinzhu
 * date : 2015/9/10
 * introduce : 消息实体类基类
 */
public abstract class MessageEntity{
    public MessageEntity(SLMessage message) {
    }
    /**
     * 获取该消息对应的展示模板
     */
    protected abstract View inflateView(Context context);

    /**
     * 获得该消息对应的展示形式
     */
    public abstract View getConvertView(Context context, View convertView, ViewGroup group);
}
