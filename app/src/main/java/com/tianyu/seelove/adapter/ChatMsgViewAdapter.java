package com.tianyu.seelove.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.manager.MediaPlayerManager;
import com.tianyu.seelove.model.entity.message.MessageEntity;

import java.util.Map;

public class ChatMsgViewAdapter extends BaseAdapter {
    private Map<String, MessageEntity> msglist;
    private Context mContext;
    MessageDao messageDao = new MessageDaoImpl();

    public ChatMsgViewAdapter(Context mContext, Map<String, MessageEntity> msglist) {
        super();
        this.mContext = mContext;
        this.msglist = msglist;
    }


    public void addData(Map<String, MessageEntity> msglist) {
        this.msglist = msglist;
        notifyDataSetChanged();
    }

    public void addData(String messageId, MessageEntity entity) {
        msglist.put(messageId, entity);
        notifyDataSetChanged();
    }

    public void cleanData(String messageId, MessageEntity entity) {
        msglist.put(messageId, entity);
        notifyDataSetChanged();
    }

    public void deleteData(String messageId) {
        msglist.remove(messageId);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return msglist.size();
    }

    @Override
    public Object getItem(int position) {
        return msglist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup group) {
        MessageEntity entity = msglist.values().toArray(new MessageEntity[]{})[position];
        return entity.getConvertView(mContext, convertView, group);
    }

    static class ViewHolder {
        public ImageView imgUserHeader;
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public TextView tvTime;
        public ProgressBar mcProgressBar;
        public boolean isComMsg = true;
        public ImageView mc_sendfail;
    }

    public void stopVoice() {
        MediaPlayerManager.getInstance().stopVoice();
    }
}
