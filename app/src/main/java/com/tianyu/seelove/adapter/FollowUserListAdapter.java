package com.tianyu.seelove.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.ui.activity.user.UserInfoActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频显示自定义adapter
 * @author shisheng.zhao
 * @date 2017-03-31 17:50
 */
public class FollowUserListAdapter extends BaseAdapter {
    private Context mContext;
    private List<SLUser> slUserList;
    private int followType = 1;

    public FollowUserListAdapter(Context context, List<SLUser> slUserList, int followType) {
        this.mContext = context;
        this.slUserList = slUserList;
        this.followType = followType;
    }

    public void updateData(List<SLUser> slUserList, boolean isClean) {
        if (null == slUserList) {
            slUserList = new ArrayList<>();
        }
        if (isClean) {
            this.slUserList.clear();
            this.slUserList = slUserList;
        } else {
            this.slUserList.addAll(slUserList);
        }
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        ImageLoader.getInstance().displayImage(slUserList.get(position).getHeadUrl(), viewHolder.headUrl, ImageLoaderUtil.getHeadUrlImageOptions());
        viewHolder.userName.setText(slUserList.get(position).getNickName());
        if (1 == followType) {
            viewHolder.followBtn.setBackgroundResource(R.mipmap.followed_icon);
        } else if (2 == followType) {
            viewHolder.followBtn.setBackgroundResource(R.mipmap.follow_add_icon);
        }
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra("user", slUserList.get(position));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        RelativeLayout itemLayout;
        ImageView headUrl;
        TextView userName;
        ImageView followBtn;
    }
}