package com.tianyu.seelove.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.tianyu.seelove.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 视频封面自定义adapter
 * @author shisheng.zhao
 * @date 2017-04-25 16:05
 */
public class VideoImageAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater = null;
    List<Bitmap> bitmapList = new ArrayList<>();
    private int lastPosition = -1;   //lastPosition 记录上一次选中的图片位置，-1表示未选中
    private Vector<Boolean> vector = new Vector<>();// 定义一个向量作为选中与否容器

    public VideoImageAdapter(Context context, List<Bitmap> bitmapList) {
        this.mContext = context;
        this.bitmapList = bitmapList;
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < bitmapList.size(); i++) {
            vector.add(false);
        }
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public Object getItem(int index) {
        return bitmapList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.item_video_image, null);
            viewHolder = new ViewHolder();
            viewHolder.videoImg = (ImageView) convertView.findViewById(R.id.videoImg);
            viewHolder.signImg = (ImageView) convertView.findViewById(R.id.signImg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.videoImg.setImageBitmap(bitmapList.get(position));
        if (vector.elementAt(position) == true) {
            viewHolder.signImg.setVisibility(View.VISIBLE);
        } else {
            viewHolder.signImg.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView videoImg;
        public ImageView signImg;
    }

    /**
     * 修改选中时的状态
     * @param position
     */
    public void changeState(int position) {
        if (lastPosition != -1)
            vector.setElementAt(false, lastPosition);                   //取消上一次的选中状态
        vector.setElementAt(!vector.elementAt(position), position);     //直接取反即可
        lastPosition = position;                                        //记录本次选中的位置
        notifyDataSetChanged();                                         //通知适配器进行更新
    }
}