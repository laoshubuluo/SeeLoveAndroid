package com.tianyu.seelove.ui.activity.user;

import android.os.Bundle;
import com.tianyu.seelove.R;
import com.tianyu.seelove.ui.activity.base.BaseActivity;

/**
 * 用户登录界面
 * @author shisheng.zhao
 * @date 2017-04-29 09:30
 */
public class UserLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentColor("#00000000");
        setContentView(R.layout.activity_user_login);
    }
}
