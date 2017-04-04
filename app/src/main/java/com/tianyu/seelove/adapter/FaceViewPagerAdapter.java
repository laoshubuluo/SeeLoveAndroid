package com.tianyu.seelove.adapter;

import java.util.List;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author shisheng.zhao
 * @Description: 数据填充器
 * @date 2015-09-01 下午17:54:42
 */
public class FaceViewPagerAdapter extends PagerAdapter {
    private List<View> pageViews;

    public FaceViewPagerAdapter(List<View> pageViews) {
        super();
        this.pageViews = pageViews;
    }

    @Override
    public int getCount() {
        return pageViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(pageViews.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(pageViews.get(position));
        return pageViews.get(position);
    }
}
