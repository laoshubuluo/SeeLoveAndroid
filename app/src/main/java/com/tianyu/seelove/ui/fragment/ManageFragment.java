package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.ui.activity.user.UserInfoActivity;
import com.tianyu.seelove.ui.activity.video.VideoListActivity;

/**
 * Fragmengt(管理)
 *
 * @author shisheng.zhao
 * @date 2017-03-29 15:03
 */
public class ManageFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        System.out.println("BBBBBBBBBBBBB___onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("BBBBBBBBBBBBB____onCreate");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("BBBBBBBBBBBBB____onCreateView");
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        RelativeLayout userInfoLayout = (RelativeLayout) view.findViewById(R.id.userInfoLayout);
        LinearLayout videoLayout = (LinearLayout) view.findViewById(R.id.videoLayout);
        userInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), UserInfoActivity.class);
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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("BBBBBBBBBBBBB____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("BBBBBBBBBBBBB____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("BBBBBBBBBBBBB____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("BBBBBBBBBBBBB____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("BBBBBBBBBBBBB____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("BBBBBBBBBBBBB____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("BBBBBBBBBBBBB____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("BBBBBBBBBBBBB____onDetach");
    }
}