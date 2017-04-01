package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.VideoGridAdapter;
import com.tianyu.seelove.model.entity.video.VideoInfo;
import com.tianyu.seelove.ui.activity.user.MyInfoActivity;
import com.tianyu.seelove.ui.activity.video.VideoListActivity;
import com.tianyu.seelove.utils.LogUtil;
import java.util.ArrayList;

/**
 * Fragmengt(管理)
 * @author shisheng.zhao
 * @date 2017-03-29 15:03
 */
public class ManageFragment extends Fragment {
    ArrayList<VideoInfo> videoInfos;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("ManageFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("ManageFragment____onCreate");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("ManageFragment____onCreateView");
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        RelativeLayout userInfoLayout = (RelativeLayout) view.findViewById(R.id.userInfoLayout);
        LinearLayout videoLayout = (LinearLayout) view.findViewById(R.id.videoLayout);
        userInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), MyInfoActivity.class);
                startActivity(intent);
            }
        });
        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), VideoListActivity.class);
                startActivity(intent);
            }
        });
        titleView.setText(R.string.manager);
        GridView gridView = (GridView) view.findViewById(R.id.videoGridView);
        videoInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setVideoTitle("00"+i);
            videoInfos.add(videoInfo);
        }
        int size = videoInfos.size();
        int length = 120;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数
        gridView.setAdapter(new VideoGridAdapter(getActivity(), videoInfos));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("ManageFragment____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("ManageFragment____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("ManageFragment____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("ManageFragment____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("ManageFragment____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("ManageFragment____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("ManageFragment____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("ManageFragment____onDetach");
    }
}