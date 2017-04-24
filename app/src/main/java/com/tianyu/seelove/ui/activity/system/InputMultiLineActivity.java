package com.tianyu.seelove.ui.activity.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.ActivityResultConstant;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.StringUtils;

/**
 * @author shisheng.zhao
 * @Description: 多行输入Activity
 * @date 2015-12-30 下午17:00:25
 */
public class InputMultiLineActivity extends BaseActivity {
    public final static int USER_INFO_INTRODUCE_INPUT = 1;// 用户信息：一句话介绍(当前用户)
    private int activityType;
    private TextView titleView;
    private EditText multiLineET;
    private TextView numberTV;
    InputMethodManager imm = null;
    private int maxNum = 100;// 最大编辑文字数目
    private int num; // 剩余可编辑文字数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_input_multi_line);
        activityType = getIntent().getIntExtra("activityType", 0);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initData();
    }

    private void initView() {
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        titleView = (TextView) findViewById(R.id.titleView);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        multiLineET = (EditText) findViewById(R.id.multiLineET);
        numberTV = (TextView) findViewById(R.id.numberTV);
    }

    private void initData() {
        switch (activityType) {
            case USER_INFO_INTRODUCE_INPUT: {
                titleView.setText(getString(R.string.introduce));
                multiLineET.setHint(getString(R.string.introduce_hint));
                String userIntroduce = getIntent().getStringExtra("userIntroduce");
                if (StringUtils.isNotBlank(userIntroduce)) {
                    multiLineET.setText(userIntroduce);
                    num = maxNum - userIntroduce.length();
                    numberTV.setText(String.valueOf(num));
                }
                break;
            }
        }
        //计数器
        multiLineET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                num = maxNum - s.length();
                numberTV.setText(String.valueOf(num));
            }
        });
    }

    @Override
    public void onClick(View v) {
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        Intent intent = null;
        switch (v.getId()) {
            case R.id.leftBtn:
                // 隐藏键盘
                if (isOpen) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                }
                if (activityType == USER_INFO_INTRODUCE_INPUT) {
                    intent = new Intent();
                    intent.putExtra("userIntroduce", multiLineET.getText().toString().trim());
                    setResult(ActivityResultConstant.INTRODUCE_INPUT, intent);
                    finish();
                }
            default:
                break;
        }
    }
}