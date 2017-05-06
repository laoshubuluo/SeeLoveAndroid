package com.tianyu.seelove.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.utils.DimensionUtils;

/**
 * author : L.jinzhu
 * date : 2016/01/11
 * introduce : 红点（新消息提醒）view
 */
public class RedDotView extends TextView {
    final int w_10 = DimensionUtils.convertDipToPixels(getResources(), 10);
    final int w_20 = DimensionUtils.convertDipToPixels(getResources(), 15);
    final int w_24 = DimensionUtils.convertDipToPixels(getResources(), 20);
    final int h_18 = DimensionUtils.convertDipToPixels(getResources(), 14);

    public RedDotView(Context context) {
        super(context);
    }

    public RedDotView(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个属性不能在XML里面定义
        super(context, attrs);
    }

    private void initData() {
    }

    public void initView(int unreadCount) {
        if (unreadCount <= 0) {
            this.setText("");
            this.setVisibility(GONE);
            return;
        }
        if (unreadCount > 0 && unreadCount <= 9) {
            this.setWidth(w_20);
            this.setHeight(w_20);
            this.setBackgroundResource(R.drawable.shape_badeview);
            this.setTextSize(10);
            this.setText(String.valueOf(unreadCount));
            this.setVisibility(View.VISIBLE);
        } else if (unreadCount > 9 && unreadCount < 100) {
            this.setWidth(w_24);
            this.setHeight(h_18);
            this.setBackgroundResource(R.mipmap.badeview_icon);
            this.setTextSize(10);
            this.setText(String.valueOf(unreadCount));
            this.setVisibility(View.VISIBLE);
        } else if (unreadCount >= 100) {
            this.setWidth(w_24);
            this.setHeight(h_18);
            this.setBackgroundResource(R.mipmap.badeview_icon);
            this.setTextSize(10);
            this.setText("···");
            this.setVisibility(View.VISIBLE);
        } else {
            this.setText("");
            this.setVisibility(GONE);
        }
    }

    public void initView(String unreadCount, boolean isShowCount) {
        // 红点
        if (!isShowCount) {
            this.setWidth(w_10);
            this.setHeight(w_10);
            this.setVisibility(View.VISIBLE);
            this.setText("");
            this.setBackgroundResource(R.drawable.shape_badeview);
            return;
        }
        // 显示数字
        if (unreadCount.length() < 2 && unreadCount.equals("n")) {
            this.setWidth(w_24);
            this.setHeight(h_18);
            this.setBackgroundResource(R.mipmap.badeview_icon);
            this.setTextSize(10);
            this.setText("···");
            this.setVisibility(View.VISIBLE);
        } else if (unreadCount.length() < 2 && !unreadCount.equals("n")) {
            this.setWidth(w_20);
            this.setHeight(w_20);
            this.setBackgroundResource(R.drawable.shape_badeview);
            this.setTextSize(10);
            this.setText(unreadCount);
            this.setVisibility(View.VISIBLE);
        } else {
            this.setWidth(w_24);
            this.setHeight(h_18);
            this.setBackgroundResource(R.mipmap.badeview_icon);
            this.setTextSize(10);
            this.setText(unreadCount);
            this.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.setTextColor(getContext().getResources().getColor(R.color.white));
    }
}