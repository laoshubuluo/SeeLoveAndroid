package com.tianyu.seelove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.model.entity.share.ShareInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * 分享 List自定义适配器，用于展示Item信息，可以通过自定义View展示多种差异化消息结构体
 * @author shisheng.zhao
 * @date 2017-04-22 17:23
 */
public class ShareListAdapter extends BaseAdapter {
    private Context context;
    private List<ShareInfo> shareInfoList;
    private ViewHolder viewHolder;

    public ShareListAdapter(Context context, List<ShareInfo> shareInfos) {
        this.context = context;
        if (null == shareInfos)
            shareInfos = new ArrayList<>();
        this.shareInfoList = shareInfos;
    }

    public int getCount() {
        return shareInfoList.size();
    }

    public Object getItem(int position) {
        return shareInfoList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ShareInfo share = shareInfoList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_share_list, null);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setBackgroundResource(R.color.translate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.icon.setBackgroundResource(share.getShareId());
        viewHolder.title.setText(share.getShareTitle());
        return convertView;
    }

    private class ViewHolder {
        private ImageView icon;
        private TextView title;
    }
}