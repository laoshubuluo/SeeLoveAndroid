package com.tianyu.seelove.ui.activity.system;

import android.os.Bundle;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.injection.ControlInjection;
import com.tianyu.seelove.ui.activity.base.BaseActivity;

/**
 * @Description: 网络连接失败Activity
 * @date 2017-04-05 15:02
 */
public class NetworkConnectActivity extends BaseActivity {
    @ControlInjection(R.id.titleView)
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_connect);
        initView();
    }

    private void initView() {
        titleView.setText(getString(R.string.network_connect));
    }
}