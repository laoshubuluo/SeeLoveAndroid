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
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.model.entity.video.SLVideo;
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
public class FindUserAdapter extends RecyclerView.Adapter<FindUserAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<SLUserDetail> mData; //定义数据源
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    String videoUrl = "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4";

    // 自定义interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, SLUserDetail data);
    }

    // 定义构造方法，默认传入上下文和数据源
    public FindUserAdapter(Context context, List<SLUserDetail> data) {
        mContext = context;
        if (null == data) {
            data = new ArrayList<SLUserDetail>();
        }
        mData = data;
    }

    public void updateData(List<SLUserDetail> data, boolean isClean) {
        if (null == data) {
            data = new ArrayList<SLUserDetail>();
        }
        if (isClean) {
            mData.clear();
            mData = data;
        } else {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override  // 将ItemView渲染进来，创建ViewHolder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.find_card_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override  // 将数据源的数据绑定到相应控件上
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        SLUserDetail userDetailInfo = mData.get(position);
        SLUser userInfo = userDetailInfo.getUser();
        final SLVideo defaultVideo = userDetailInfo.getDefultVideo();

        ImageLoader.getInstance().displayImage(userInfo.getHeadUrl(), viewHolder.userAvatar, ImageLoaderUtil.getSmallImageOptions());
        viewHolder.userAvatar.getLayoutParams().height = 550; // 从数据源中获取图片高度，动态设置到控件上
        viewHolder.userName.setText(userInfo.getNickName());
        viewHolder.itemView.setTag(mData.get(position));
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
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    // 定义自己的ViewHolder，将View的控件引用在成员变量上
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public View videoView;
        public ImageView mPlayBtnView;
        public ImageView userAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            videoView = itemView.findViewById(R.id.item_video_view);
            userAvatar = (ImageView) videoView.findViewById(R.id.user_avatar);
            mPlayBtnView = (ImageView) videoView.findViewById(R.id.play_btn);
        }
    }

    @Override
    public void onClick(View view) {
        if (null != mOnItemClickListener) {
            mOnItemClickListener.onItemClick(view, (SLUserDetail) view.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}