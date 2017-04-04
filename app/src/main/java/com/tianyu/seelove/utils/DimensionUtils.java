package com.tianyu.seelove.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 和尺寸相关的工具类,主要负责尺寸之间的转换
 *
 * @author shisheng.zhao
 * @date 2015-09-01 17:59:32
 */
public class DimensionUtils {
    /**
     * 将dip转换为pixel
     *
     * @param r        所在的视图
     * @param dipValue 需要转换的dip值
     * @return dip值对应的pixel值
     */
    public static int convertDipToPixels(Resources r, int dipValue) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue, r.getDisplayMetrics());
        return px;
    }
}
