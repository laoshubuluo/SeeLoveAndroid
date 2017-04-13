package com.tianyu.seelove.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.ViewPageFragmentAdapter;
import com.tianyu.seelove.model.entity.message.ViewPageImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 用于轮播多张图片
 * @date 2017-04-13 23:19
 */
public class ViewPageFragment extends FragmentActivity {
    private ViewPager viewPager;
    private List<ViewPageImage> list = new ArrayList<ViewPageImage>();
    private ViewPageFragmentAdapter viewPageFragmentAdapter;
    private String selectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 统一去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpage_fragment);
        list = (List<ViewPageImage>) getIntent().getSerializableExtra("viewPageImages");
        selectImage = getIntent().getStringExtra("selectImage");
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        viewPageFragmentAdapter = new ViewPageFragmentAdapter(ViewPageFragment.this,getSupportFragmentManager(), list);
        viewPager.setAdapter(viewPageFragmentAdapter);
        if (null != selectImage) {
            for (int i = 0, size = list.size(); i < size; i++) {
                if (selectImage.equals(list.get(i).getImageUrl())) {
                    viewPager.setCurrentItem(i);
                }
            }
        }
    }
}
