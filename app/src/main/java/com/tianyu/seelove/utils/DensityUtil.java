package com.tianyu.seelove.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {
    private static int[] deviceWidthHeight = new int[2];

    public static int[] getDeviceInfo(Context context) {
        if ((deviceWidthHeight[0] == 0) && (deviceWidthHeight[1] == 0)) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(metrics);

            deviceWidthHeight[0] = metrics.widthPixels;
            deviceWidthHeight[1] = metrics.heightPixels;
        }
        return deviceWidthHeight;
    }

    public static final float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static final float getHeightInPx(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public static final float getWidthInPx(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static final int getHeightInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int heightInDp = px2dip(context, height);
        return heightInDp;
    }

    public static final int getWidthInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int widthInDp = px2dip(context, height);
        return widthInDp;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

}
