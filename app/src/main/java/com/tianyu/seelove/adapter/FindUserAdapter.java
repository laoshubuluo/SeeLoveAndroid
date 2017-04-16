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
import com.tianyu.seelove.ui.activity.video.FullVideoActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.view.video.VideoPlayer;
import java.util.ArrayList;
import java.util.List;

/**
 * 发现显示自定义adapter
 * @author shisheng.zhao
 * @date 2017-03-31 17:52
 */
public class FindUserAdapter extends RecyclerView.Adapter<FindUserAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<SLUser> mData; //定义数据源
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    String videoUrl = "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4";

    // 自定义interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, SLUser data);
    }

    // 定义构造方法，默认传入上下文和数据源
    public FindUserAdapter(Context context, List<SLUser> data) {
        mContext = context;
        if (null == data) {
            data = new ArrayList<SLUser>();
        }
        mData = data;
    }

    public void updateData(List<SLUser> data, boolean isClean) {
        if (null == data) {
            data = new ArrayList<SLUser>();
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
        SLUser userInfo = mData.get(position);
        ImageLoader.getInstance().displayImage(userInfo.getHeadUrl(), viewHolder.userAvatar, ImageLoaderUtil.getSmallImageOptions());
        viewHolder.userAvatar.getLayoutParams().height = 550; // 从数据源中获取图片高度，动态设置到控件上
        viewHolder.userName.setText(userInfo.getNickName());
        viewHolder.itemView.setTag(mData.get(position));
        viewHolder.mPlayBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 全屏播放
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
            mOnItemClickListener.onItemClick(view, (SLUser) view.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}