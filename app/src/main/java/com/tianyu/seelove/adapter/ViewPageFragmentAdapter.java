package com.tianyu.seelove.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tianyu.seelove.R;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.message.ViewPageImage;
import com.tianyu.seelove.ui.activity.message.SaveImageActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.view.dialog.TransProgressDialog;
import com.tianyu.seelove.view.photoview.PhotoView;
import com.tianyu.seelove.view.photoview.PhotoViewAttacher;

import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 轮播多张图片Adapter
 * @date 2016-03-09 下午16:19:51
 */
public class ViewPageFragmentAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private List<ViewPageImage> list;

    public ViewPageFragmentAdapter(Context context, FragmentManager fm, List<ViewPageImage> list) {
        super(fm);
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    // 初始化每个页卡选项
    @Override
    public Object instantiateItem(ViewGroup arg0, int position) {
        ArrayFragment ff = (ArrayFragment) super.instantiateItem(arg0, position);
        ff.setThings(context, list.get(position), position, list.size());
        return ff;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }


    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return new ArrayFragment();
    }

    /**
     * 所有的  每个Fragment
     */
    public static class ArrayFragment extends Fragment {
        private ViewPageImage viewPageImage;
        private int position;
        private int count;
        private int width = 480;
        private int height = 800;
        private TransProgressDialog progressDialog;

        public void setThings(Context context, ViewPageImage viewPageImage, int position, int count) {
            this.viewPageImage = viewPageImage;
            this.position = position;
            this.count = count;
            progressDialog = new TransProgressDialog(context, context.getResources().getString(R.string.loading));
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            System.out.println("onCreateView = ");
            //在这里加载每个 fragment的显示的 View
            View view = inflater.inflate(R.layout.viewpage_fragment_item, container, false);
            final PhotoView photoView = (PhotoView) view.findViewById(R.id.imageView);
            final PhotoView thumImageView = (PhotoView) view.findViewById(R.id.thumImageView);
            final TextView imageCount = (TextView) view.findViewById(R.id.imageCount);
            imageCount.setText((position + 1) + "/" + count);
            ImageLoader.getInstance().loadImage(viewPageImage.getImageUrl(), new ImageSize(width, height), ImageLoaderUtil.getDefaultDisplayOptions(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    photoView.setImageBitmap(loadedImage);
                    thumImageView.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    super.onLoadingStarted(imageUri, view);
                    if (StringUtils.isNotBlank(viewPageImage.getThumImageUrl())) {
                        progressDialog.show();
                        ImageLoader.getInstance().displayImage(viewPageImage.getThumImageUrl(), thumImageView);
                    }
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    super.onLoadingFailed(imageUri, view, failReason);
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.image_load_faile), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    super.onLoadingCancelled(imageUri, view);
                    progressDialog.dismiss();
                }
            });
            // 单击退出PhotoView
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    getActivity().finish();
                }
            });

            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = IntentManager.createIntent(getActivity(), SaveImageActivity.class);
                    intent.putExtra("imagePath", viewPageImage.getImageUrl());
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
                    return false;
                }
            });
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
            // 单击退出PhotoView
            thumImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    getActivity().finish();
                }
            });
            thumImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            System.out.println("onActivityCreated = ");
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void onDestroyView() {
            System.out.println("onDestroyView = " + position);
            super.onDestroyView();
        }

        @Override
        public void onDestroy() {
            System.out.println("onDestroy = " + position);
            super.onDestroy();
        }
    }
}