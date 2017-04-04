package com.tianyu.seelove.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianyu.seelove.R;

/**
 * @author shisheng.zhao
 * @Description: 自定义多媒体插件入口
 * @date 2015-09-01 上午14:44:32
 */
public class CustomGridLayout extends RelativeLayout {
    private ImageView grid_imageView;
    private TextView grid_TextView;

    public CustomGridLayout(Context context) {
        super(context);
        initView();
    }

    public CustomGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageView getGrid_imageView() {
        return grid_imageView;
    }

    public TextView getGrid_TextView() {
        return grid_TextView;
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.widgetgridlayout, this);
        grid_imageView = (ImageView) view.findViewById(R.id.grid_imageView);
        grid_TextView = (TextView) view.findViewById(R.id.grid_textView);
    }
}
