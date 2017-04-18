package com.tianyu.seelove.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.ui.activity.video.FullVideoActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现显示自定义adapter
 *
 * @author shisheng.zhao
 * @date 2017-03-31 17:52
 */
public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {
    private Context mContext;
    private List<SLUserDetail> userDetailList;
    String videoUrl = "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4";
    String headUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg";

    // 定义构造方法，默认传入上下文和数据源
    public FollowAdapter(Context context, List<SLUserDetail> slUserDetailList) {
        mContext = context;
        if (null == slUserDetailList) {
            slUserDetailList = new ArrayList<SLUserDetail>();
        }
        userDetailList = slUserDetailList;
    }

    public void updateData(List<SLUserDetail> slUserDetailList, boolean isClean) {
        if (null == slUserDetailList) {
            slUserDetailList = new ArrayList<SLUserDetail>();
        }
        if (isClean) {
            userDetailList.clear();
            userDetailList = slUserDetailList;
        } else {
            userDetailList.addAll(slUserDetailList);
        }
        notifyDataSetChanged();
    }

    @Override  // 将ItemView渲染进来，创建ViewHolder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.follow_item_view, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override  // 将数据源的数据绑定到相应控件上
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        ImageLoader.getInstance().displayImage(headUrl, viewHolder.userIcon, ImageLoaderUtil.getHeadUrlImageOptions());
        ImageLoader.getInstance().displayImage(headUrl, viewHolder.userAvatar, ImageLoaderUtil.getSmallImageOptions());
        viewHolder.itemView.setTag(userDetailList.get(position));
        // viewHolder.mPlayBtnView.setTag(R.id.tag_defaultVideo, defaultVideo);
        viewHolder.mPlayBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 全屏播放
                //SLVideo video = (SLVideo) view.getTag(R.id.tag_defaultVideo);
                Intent intent = new Intent(new Intent(mContext, FullVideoActivity.class));
                intent.putExtra("videoUrl", videoUrl);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userDetailList != null) {
            return userDetailList.size();
        }
        return 0;
    }

    // 定义自己的ViewHolder，将View的控件引用在成员变量上
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userIcon, followStatue;
        public TextView userName, videoTitle, videoTime;
        public View videoView;
        public ImageView mPlayBtnView;
        public ImageView userAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            userIcon = (ImageView) itemView.findViewById(R.id.userIcon);
            followStatue = (ImageView) itemView.findViewById(R.id.followStatue);
            videoTime = (TextView) itemView.findViewById(R.id.videoTime);
            videoTitle = (TextView) itemView.findViewById(R.id.videoTitle);
            userName = (TextView) itemView.findViewById(R.id.userName);
            videoView = itemView.findViewById(R.id.item_video_view);
            userAvatar = (ImageView) videoView.findViewById(R.id.user_avatar);
            mPlayBtnView = (ImageView) videoView.findViewById(R.id.play_btn);
        }
    }
}