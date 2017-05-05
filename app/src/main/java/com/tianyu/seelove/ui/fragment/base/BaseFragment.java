package com.tianyu.seelove.ui.fragment.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 基类fragment, 实现一些fragment公共方法
 *
 * @author L.jinzhu
 * @date 2017-03-28 16:27
 */
public class BaseFragment extends Fragment implements Handler.Callback, View.OnClickListener {
    public Handler handler;
    public CustomProgressDialog customProgressDialog;
    public PromptDialog promptDialog;
    public ImageLoader imageLoader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this);
        promptDialog = new PromptDialog(getActivity());
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }
}
