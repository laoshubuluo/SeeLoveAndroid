package com.tianyu.seelove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.model.entity.user.SLUser;
import java.util.List;

/**
 * 视频显示自定义adapter
 * @author shisheng.zhao
 * @date 2017-03-31 17:50
 */
public class FollowUserListAdapter extends BaseAdapter {
    private Context mContext;
    private List<SLUser> slUserList;

    public FollowUserListAdapter(Context context, List<SLUser> slUserList) {
        this.mContext = context;
        this.slUserList = slUserList;
    }

    @Override
    public int getCount() {
        return slUserList.size();
    }

    @Override
    public Object getItem(int index) {
        return slUserList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_follow_user, null);
            viewHolder = new ViewHolder();
            viewHolder.itemLayout = (RelativeLayout) convertView.findViewById(R.id.itemLayout);
            viewHolder.headUrl = (ImageView) convertView.findViewById(R.id.headUrl);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.followBtn = (ImageView) convertView.findViewById(R.id.followBtn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder {
        RelativeLayout itemLayout;
        ImageView headUrl;
        TextView userName;
        ImageView followBtn;
    }
}