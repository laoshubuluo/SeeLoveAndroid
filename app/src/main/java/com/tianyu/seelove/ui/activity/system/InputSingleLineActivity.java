package com.tianyu.seelove.ui.activity.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.ActivityResultConstant;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.StringUtils;

/**
 * 单行输入界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class InputSingleLineActivity extends BaseActivity {
    public final static int USER_INFO_NAME_INPUT = 1;//用户信息：昵称输入(当前用户)
    private int activityType;
    private SLUser user;
    private TextView titleView;
    private EditText editText;
    private ImageView cleanAllImg;
    InputMethodManager imm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_input_single_line);
        activityType = getIntent().getIntExtra("activityType", 0);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initData();
    }

    private void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        editText = (EditText) findViewById(R.id.editText);
        cleanAllImg = (ImageView) findViewById(R.id.cleanAll);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        cleanAllImg.setOnClickListener(this);
    }

    private void initData() {
        switch (activityType) {
            case USER_INFO_NAME_INPUT:
                user = (SLUser) getIntent().getSerializableExtra("user");
                titleView.setText(getString(R.string.nick_name));
                editText.setText(user.getNickName());
                InputFilter[] fName = {new InputFilter.LengthFilter(20)};
                editText.setFilters(fName);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        Intent intent = null;
        switch (v.getId()) {
            case R.id.leftBtn: {
                // 隐藏键盘
                if (isOpen) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                }
                // 修改昵称
                if (activityType == USER_INFO_NAME_INPUT) {
                    String message = editText.getText().toString().trim();
                    if (StringUtils.isNullOrBlank(message)) {
                        Toast.makeText(getApplicationContext(), getString(R.string.nick_name_is_null), Toast.LENGTH_LONG).show();
                        return;
                    }
                    intent = new Intent();
                    intent.putExtra("name", message);
                    setResult(ActivityResultConstant.NAME_INPUT, intent);
                    finish();
                }
                break;
            }
            case R.id.cleanAll:
                editText.setText("");
                break;
            default:
                break;
        }
    }
}