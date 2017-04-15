package com.tianyu.seelove.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.ViewPageFragmentAdapter;
import com.tianyu.seelove.model.entity.message.ViewPageImage;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 用于轮播多张图片
 * @date 2017-04-13 23:19
 */
public class ViewPageFragment extends BaseActivity {
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
        initView();
        initData();
    }

    private void initView() {
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
    }

    private void initData() {
        viewPageFragmentAdapter = new ViewPageFragmentAdapter(ViewPageFragment.this, getSupportFragmentManager(), list);
        viewPager.setAdapter(viewPageFragmentAdapter);
        if (null != selectImage) {
            for (int i = 0, size = list.size(); i < size; i++) {
                if (selectImage.equals(list.get(i).getImageUrl())) {
                    viewPager.setCurrentItem(i);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.leftBtn:
                finish();
                break;
        }
    }
}
