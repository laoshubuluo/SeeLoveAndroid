package com.tianyu.seelove.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.tianyu.seelove.R;

/**
 * @author shisheng.zhao
 * @Description: View的开关Controll
 * @date 2015-09-01 下午18:07:16
 */
public class ViewUtils {
    private static View focusView;// 正在落焦view

    /**
     * View落焦
     */
    public static void viewFocus(Context context, View view) {
        viewCleanFocus(context);
        if (null == view)
            return;
        focusView = view;
        focusView.setBackgroundColor(context.getResources().getColor(R.color.bg_grey_d6));
    }

    /**
     * View取消落焦
     */
    public static void viewCleanFocus(Context context) {
        if (null == focusView)
            return;
        focusView.setBackgroundColor(context.getResources().getColor(R.color.translate));
    }

    /**
     * 开关view
     *
     * @param view
     */
    public static void toggleView(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
    }

    public static void toggleView(View view, View parent) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            LayoutParams params = parent.getLayoutParams();
            params.height = DimensionUtils.convertDipToPixels(
                    parent.getResources(), 50);
            parent.setLayoutParams(params);
            parent.postInvalidate();
            return;
        }
        view.setVisibility(View.VISIBLE);
        LayoutParams params = parent.getLayoutParams();
        params.height = DimensionUtils.convertDipToPixels(
                parent.getResources(), 220);
        parent.setLayoutParams(params);
        parent.postInvalidate();
    }

    /**
     * 关闭 view
     *
     * @param view
     */
    public static void shutView(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            return;
        }
    }

    public static void shutView(View view, View parent) {
        if (view.getVisibility() == View.VISIBLE) {
            LayoutParams params = parent.getLayoutParams();
            params.height = DimensionUtils.convertDipToPixels(
                    parent.getResources(), 50);
            parent.setLayoutParams(params);
            view.setVisibility(View.GONE);
            return;
        }
        LayoutParams params = parent.getLayoutParams();
        params.height = DimensionUtils.convertDipToPixels(
                parent.getResources(), 50);
        parent.setLayoutParams(params);
    }
}
