package com.tianyu.seelove.utils;

import android.content.Context;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Constant;
import java.io.File;

/**
 * ImageLoader配置工具类
 * @author shisheng.zhao
 * @date 2017-03-28 16:19
 */
public class ImageLoaderUtil {
    private static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true).build();

    // 初始化imageLoader
    public static void initImageLoader(Context context) {
        File cacheDir = new File(StorageUtil.getCacheDirectory(context));
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3) // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())//建议内存设在5-10M,可以有比较好的表现
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiscCache(cacheDir))
                // 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout 5s readTimeout 30s
                .build();// 开始构建
        ImageLoader.getInstance().init(config);
    }

    // 获取图片信息-小
    public static DisplayImageOptions getSmallImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.ic_launcher)
                .imageScaleType(ImageScaleType.EXACTLY).showImageOnFail(R.mipmap.ic_launcher).cacheInMemory(true).cacheOnDisk(true).build();
        return options;
    }

    // 获取头像图片信息
    public static DisplayImageOptions getHeadUrlImageOptions() {
        RoundedBitmapDisplayer bitmapDisplayer = new RoundedBitmapDisplayer(Constant.corners); // 设定圆角
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.default_head).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(bitmapDisplayer).showImageOnFail(R.mipmap.default_head).cacheInMemory(true).cacheOnDisk(true).build();
        return options;
    }

    public static String getAcceptableUri(String file) {
        if (StringUtils.isNullOrBlank(file)) {
            return "";
        }
        if (file.startsWith("http")) {
            return file;
        }
        return "file://" + file;
    }

    public static String getSmallPic(String pic) {
        return "file://" + pic;
    }


    public static String getSmallPic2(String pic) {
        return pic;
    }

    public static DisplayImageOptions getDefaultDisplayOptions() {
        return options;
    }

}