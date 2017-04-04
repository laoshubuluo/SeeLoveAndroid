package com.tianyu.seelove.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.model.entity.ImageViewHolder;
import com.tianyu.seelove.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class AlbumImageAdapter extends CommonAdapter<String> {
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static ArrayList<String> mSelectedImage = new ArrayList<String>();

    /**
     * 文件夹路径
     */
    private String mDirPath;
    private Context context;

    public AlbumImageAdapter(Context context, List<String> mDatas, int itemLayoutId,
                             String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        this.context = context;
    }

    @Override
    public void convert(final ImageViewHolder helper, final String item) {
        //设置no_pic
        helper.setImageResource(R.id.id_item_image, R.mipmap.pictures_no);
        //设置no_selected
        helper.setImageResource(R.id.id_item_select,
                R.mipmap.picture_unselected);
        //设置图片
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);
        mImageView.setColorFilter(null);
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                Intent intent = null;
                // 已经选择过该图片
                if (mSelectedImage.contains(mDirPath + "/" + item)) {
                    mSelectedImage.remove(mDirPath + "/" + item);
                    mSelect.setImageResource(R.mipmap.picture_unselected);
                    mImageView.setColorFilter(null);
                } else
                // 未选择该图片
                {
                    try {
                        Bitmap bitmap = BitmapUtils.getImageFromFile(mDirPath + "/" + item);
                        if (null != bitmap) {
                            if (mSelectedImage.size() >= Constant.imageCount) {
                                Toast.makeText(context, "最多可以选择" + Constant.imageCount + "张", Toast.LENGTH_SHORT).show();
                            } else {
                                mSelectedImage.add(mDirPath + "/" + item);
                                mSelect.setImageResource(R.mipmap.pictures_selected);
                                mImageView.setColorFilter(Color.parseColor("#77000000"));
                            }
                        } else {
                            Toast.makeText(context, "未知类型图片", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "未知类型图片", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item)) {
            mSelect.setImageResource(R.mipmap.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }
}