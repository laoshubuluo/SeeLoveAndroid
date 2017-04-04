package com.tianyu.seelove.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.utils.StringUtils;

/**
 * @author shisheng.zhao
 * @Description: 自定义Dialog
 * @date 2015-10-28 上午11:17:26
 */
public class SureDialog extends Dialog {
    private TextView titleTV;
    private TextView contentTV;
    public TextView sureTV;
    public TextView cancelTV;
    public CheckBox isCheck;
    public View lineView;

    public SureDialog(Context context) {
        super(context, R.style.WinDialog);
        setContentView(R.layout.view_dialog_sure);
        titleTV = (TextView) findViewById(R.id.titleTV);
        contentTV = (TextView) findViewById(R.id.contentTV);
        sureTV = (TextView) findViewById(R.id.sureTV);
        cancelTV = (TextView) findViewById(R.id.cancelTV);
        isCheck = (CheckBox) findViewById(R.id.isChexkBox);
        lineView = findViewById(R.id.line_layout);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SureDialog.this.dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void initData(String title, String content) {
        if (!StringUtils.isNullOrBlank(title)) {
            titleTV.setText(title);
            titleTV.setVisibility(View.VISIBLE);
        }
        contentTV.setText(content);
        contentTV.setVisibility(View.VISIBLE);
    }

    public TextView getTitleTV() {
        return titleTV;
    }

    public TextView getContentTV() {
        return contentTV;
    }

    public TextView getSureTV() {
        return sureTV;
    }

    public TextView getCancelTV() {
        return cancelTV;
    }

    public CheckBox getIsCheck() {
        return isCheck;
    }

    public View getLineView() {
        return lineView;
    }

    public void setSureTVData(String data) {
        sureTV.setText(data);
    }
}
