package com.tianyu.seelove.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.utils.StringUtils;

public class PromptDialog extends Dialog {
    private TextView titleTV;
    private TextView contentTV;
    public TextView sureTV;

    public PromptDialog(Context context) {
        super(context, R.style.WinDialog);
        setContentView(R.layout.view_dialog_prompt);
        titleTV = (TextView) findViewById(R.id.titleTV);
        contentTV = (TextView) findViewById(R.id.contentTV);
        sureTV = (TextView) findViewById(R.id.sureTV);
        sureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptDialog.this.dismiss();
            }
        });
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void initData(String title, String content) {
        title = "";
        if (!StringUtils.isNullOrBlank(title)) {
            titleTV.setText(title);
            titleTV.setVisibility(View.VISIBLE);
        }
        contentTV.setText(content);
        contentTV.setVisibility(View.VISIBLE);
    }

    public void setSureTV(TextView sureTV) {
        this.sureTV = sureTV;
    }

    public TextView getSureTV() {
        return sureTV;
    }
}
