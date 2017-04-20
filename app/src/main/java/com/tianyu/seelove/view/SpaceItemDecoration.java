package com.tianyu.seelove.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
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
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildPosition(view) != 0)
            outRect.top = space;
    }
}