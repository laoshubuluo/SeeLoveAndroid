package com.tianyu.seelove.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.model.entity.video.SLVideo;
import com.tianyu.seelove.ui.activity.video.VideoPlayActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频显示自定义adapter
 *
 * @author shisheng.zhao
 * @date 2017-03-31 17:50
 */
public class VideoGridAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater = null;
    List<SLVideo> slVideoList = new ArrayList<>();
    private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标,true是显示,false是不显示
    private ShowDeleteSignListener signListener;
    private DeleteListener deleteListener;

    public VideoGridAdapter(Context context, ShowDeleteSignListener signListener, DeleteListener deleteListener) {
        this.mContext = context;
        this.signListener = signListener;
        this.deleteListener = deleteListener;
        inflater = LayoutInflater.from(context);
    }

    public VideoGridAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void updateData(List<SLVideo> slVideoList) {
        if (null == slVideoList) {
            slVideoList = new ArrayList<>();
        }
        this.slVideoList.clear();
        this.slVideoList = slVideoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return slVideoList.size();
    }

    @Override
    public Object getItem(int index) {
        return slVideoList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.item_video_grid, null);
            viewHolder = new ViewHolder();
            viewHolder.itemView = (LinearLayout) convertView.findViewById(R.id.itemView);
            viewHolder.videoTitle = (TextView) convertView.findViewById(R.id.videoTitle);
            viewHolder.videoView = convertView.findViewById(R.id.item_video_view);
            viewHolder.videoImg = (ImageView) convertView.findViewById(R.id.video_img);
            viewHolder.playBtn = (ImageView) convertView.findViewById(R.id.play_btn);
            viewHolder.deleteImg = (ImageView) convertView.findViewById(R.id.deleteImg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(slVideoList.get(position).getVideoImg(), viewHolder.videoImg, ImageLoaderUtil.getSmallImageOptions());
        viewHolder.videoTitle.setText(slVideoList.get(position).getVideoTitle());
        viewHolder.deleteImg.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);// 设置删除按钮是否显示
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), VideoPlayActivity.class);
                intent.putExtra("videoPath", slVideoList.get(position).getVideoUrl());
                mContext.startActivity(intent);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != signListener) {
                    signListener.showDeleteSign();
                }
                return true;
            }
        });
        viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != deleteListener) {
                    deleteListener.delete(position);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public LinearLayout itemView;
        public TextView videoTitle;
        public View videoView;
        public ImageView playBtn;
        public ImageView videoImg;
        public ImageView deleteImg;
    }

    public void setIsShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
    }

    public interface ShowDeleteSignListener {
        void showDeleteSign();
    }

    public interface DeleteListener {
        void delete(int position);
    }
}