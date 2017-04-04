package com.tianyu.seelove.view.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.PopupWindow;

public class ResourcePopupWindow extends PopupWindow {
    private View mView;

    public ResourcePopupWindow(Context context, int resId) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(resId, null);
        caculateView(mView);
        this.setContentView(mView);
        float scrrenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int height = mView.getMeasuredHeight();
        this.setWidth((int) (scrrenWidth * 5 / 5));
        this.setHeight(height);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ColorDrawable mDrawable = new ColorDrawable(00000000);
        this.setBackgroundDrawable(mDrawable);
    }

    public View getView() {
        return mView;
    }

    private void caculateView(View view) {
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }
}
