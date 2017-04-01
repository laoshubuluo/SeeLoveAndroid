package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.utils.LogUtil;

/**
 * Fragmengt(交流)
 * @author shisheng.zhao
 * @date 2017-03-29 15:15
 */
public class MessageFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("MessageFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("MessageFragment____onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("MessageFragment____onCreateView");
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        titleView.setText(R.string.message);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("MessageFragment____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("MessageFragment____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("MessageFragment____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("MessageFragment____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("MessageFragment____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("MessageFragment____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("MessageFragment____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("MessageFragment____onDetach");
    }
}