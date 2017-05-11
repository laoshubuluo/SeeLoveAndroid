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
import com.tianyu.seelove.ui.activity.user.UserInfoActivity;
import com.tianyu.seelove.ui.activity.video.VideoPlayActivity;
import com.tianyu.seelove.utils.DateUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频显示自定义adapter
 * @author shisheng.zhao
 * @date 2017-03-31 17:50
 */
public class FollowListAdapter extends BaseAdapter {
    private Context mContext;
    private List<SLUserDetail> slUserDetailList;

    public FollowListAdapter(Context context, List<SLUserDetail> slUserDetailList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_follow_view, null);
            viewHolder = new ViewHolder();
            viewHolder.userIcon = (ImageView) convertView.findViewById(R.id.userIcon);
            viewHolder.bgImage = (ImageView) convertView.findViewById(R.id.bgImage);
            viewHolder.followStatue = (ImageView) convertView.findViewById(R.id.followStatue);
            viewHolder.videoTime = (TextView) convertView.findViewById(R.id.videoTime);
            viewHolder.videoTitle = (TextView) convertView.findViewById(R.id.videoTitle);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.videoView = convertView.findViewById(R.id.item_video_view);
            viewHolder.videoImg = (ImageView) convertView.findViewById(R.id.video_img);
            viewHolder.playBtn = (ImageView) convertView.findViewById(R.id.play_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.videoTime.setText(DateUtils.getPastDate(Long.parseLong(slUserDetailList.get(position).getDefultVideo().getVideoTime())));
        viewHolder.userName.setText(slUserDetailList.get(position).getUser().getNickName());
        viewHolder.videoTitle.setText(slUserDetailList.get(position).getDefultVideo().getVideoTitle());
        ImageLoader.getInstance().displayImage(slUserDetailList.get(position).getUser().getHeadUrl(), viewHolder.userIcon, ImageLoaderUtil.getHeadUrlImageOptions());
        ImageLoader.getInstance().displayImage(slUserDetailList.get(position).getDefultVideo().getVideoImg(), viewHolder.videoImg, ImageLoaderUtil.getSmallImageOptions());
        ImageLoader.getInstance().displayImage(slUserDetailList.get(position).getDefultVideo().getVideoImg(), viewHolder.bgImage, ImageLoaderUtil.getSmallImageOptions());
        viewHolder.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SLUser slUser = slUserDetailList.get(position).getUser();
                Intent intent = new Intent();
                intent.setClass(mContext, UserInfoActivity.class);
                intent.putExtra("user", slUser);
                mContext.startActivity(intent);
            }
        });
        viewHolder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, VideoPlayActivity.class);
                intent.putExtra("videoPath", slUserDetailList.get(position).getDefultVideo().getVideoUrl());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public ImageView userIcon, followStatue;
        public TextView userName, videoTitle, videoTime;
        public View videoView;
        public ImageView playBtn;
        public ImageView videoImg;
        public ImageView bgImage;
    }
}