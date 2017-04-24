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
import com.tianyu.seelove.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频显示自定义adapter
 * @author shisheng.zhao
 * @date 2017-03-31 17:50
 */
public class VideoGridAdapter extends BaseAdapter {
    String imageUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg";
    String videoUrl = "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4";
    Context mContext;
    LayoutInflater inflater = null;
    List<SLVideo> slVideoList = new ArrayList<>();

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.item_video_grid, null);
            viewHolder = new ViewHolder();
            viewHolder.itemView = (LinearLayout) convertView.findViewById(R.id.itemView);
            viewHolder.videoTitle = (TextView) convertView.findViewById(R.id.videoTitle);
            viewHolder.videoView = convertView.findViewById(R.id.item_video_view);
            viewHolder.videoImg = (ImageView) convertView.findViewById(R.id.video_img);
            viewHolder.playBtn = (ImageView) convertView.findViewById(R.id.play_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(imageUrl, viewHolder.videoImg, ImageLoaderUtil.getSmallImageOptions());
        viewHolder.videoTitle.setText(StringUtils.isNotBlank(slVideoList.get(position).getVideoTitle())
                ? slVideoList.get(position).getVideoTitle() : "我的爱情观!");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), VideoPlayActivity.class);
                intent.putExtra("videoPath", videoUrl);
                mContext.startActivity(intent);
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
    }
}