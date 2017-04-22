package com.tianyu.seelove.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.ui.activity.video.FullVideoActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频显示自定义adapter
 *
 * @author shisheng.zhao
 * @date 2017-03-31 17:50
 */
public class FindUserListAdapter extends BaseAdapter {
    String videoUrl = "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4";
    private Context mContext;
    private List<SLUserDetail> slUserDetailList;

    public FindUserListAdapter(Context context, List<SLUserDetail> slUserDetailList) {
        this.mContext = context;
        if (null == slUserDetailList) {
            slUserDetailList = new ArrayList<>();
        }
        this.slUserDetailList = slUserDetailList;
    }

    public void updateData(List<SLUserDetail> slUserDetails, boolean isClean) {
        if (null == slUserDetails) {
            slUserDetails = new ArrayList<>();
        }
        if (isClean) {
            slUserDetailList.clear();
            slUserDetailList = slUserDetails;
        } else {
            slUserDetailList.addAll(slUserDetails);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return slUserDetailList.size();
    }

    @Override
    public Object getItem(int index) {
        return slUserDetailList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_find_user, null);
            viewHolder = new ViewHolder();
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.videoView = convertView.findViewById(R.id.item_video_view);
            viewHolder.userAvatar = (ImageView) convertView.findViewById(R.id.user_avatar);
            viewHolder.mPlayBtnView = (ImageView) convertView.findViewById(R.id.play_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SLUserDetail userDetailInfo = slUserDetailList.get(position);
        SLUser userInfo = userDetailInfo.getUser();
        ImageLoader.getInstance().displayImage(userInfo.getHeadUrl(), viewHolder.userAvatar, ImageLoaderUtil.getSmallImageOptions());
        viewHolder.userName.setText(userInfo.getNickName());
        viewHolder.mPlayBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(mContext, FullVideoActivity.class));
                intent.putExtra("videoUrl", videoUrl);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView userName;
        public View videoView;
        public ImageView mPlayBtnView;
        public ImageView userAvatar;
    }
}