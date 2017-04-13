package com.tianyu.seelove.adapter;

import java.util.ArrayList;
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

/**
 * 视频显示自定义adapter
 * @author shisheng.zhao
 * @date 2017-03-31 17:50
 */
public class VideoGridAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    ArrayList<SLVideo> listInfo;

    public VideoGridAdapter(Context context, ArrayList<SLVideo> listInfo) {
        inflater = LayoutInflater.from(context);
        this.listInfo = listInfo;
    }

    @Override
    public int getCount() {
        return listInfo.size();
    }

    @Override
    public Object getItem(int index) {
        return listInfo.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.video_grid_item, null);
            holder = new ViewHolder();
            holder.gridViewItem = (LinearLayout) convertView.findViewById(R.id.gridViewItem);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.textView = (TextView) convertView.findViewById(R.id.textView1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SLVideo videoInfo = listInfo.get(position);
        ImageLoader.getInstance().displayImage(videoInfo.getVideoImg(), holder.imageView, ImageLoaderUtil.getSmallImageOptions());
        holder.textView.setText(videoInfo.getVideoTitle());
        holder.gridViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), VideoPlayActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private LinearLayout gridViewItem;
        ImageView imageView;
        TextView textView;
    }
}