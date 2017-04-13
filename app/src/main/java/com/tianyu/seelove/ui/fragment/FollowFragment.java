package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.ui.fragment.base.BaseFragment;
import com.tianyu.seelove.utils.LogUtil;

/**
 * Fragmengt(关注)
 *
 * @author shisheng.zhao
 * @date 2017-03-29 15:15
 */
public class FollowFragment extends BaseFragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("FollowFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("FollowFragment____onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("FollowFragment____onCreateView");
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        titleView.setText(R.string.follow);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("FollowFragment____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("FollowFragment____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("FollowFragment____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("FollowFragment____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("FollowFragment____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("FollowFragment____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("FollowFragment____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("FollowFragment____onDetach");
    }
}