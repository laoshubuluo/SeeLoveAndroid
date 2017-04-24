package com.tianyu.seelove.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.tianyu.seelove.R;

/**
 * @author shisheng.zhao
 * @Description: 自定义Dialog
 * @date 2015-10-28 上午11:17:26
 */
public class VideoTitleDialog extends Dialog {
    private TextView titleTV;
    private EditText contentTV;
    public TextView sureTV;
    public TextView cancelTV;
    public View lineView;

    public VideoTitleDialog(Context context) {
        super(context, R.style.WinDialog);
        setContentView(R.layout.view_dialog_video);
        titleTV = (TextView) findViewById(R.id.titleTV);
        contentTV = (EditText) findViewById(R.id.contentTV);
        sureTV = (TextView) findViewById(R.id.sureTV);
        cancelTV = (TextView) findViewById(R.id.cancelTV);
        lineView = findViewById(R.id.line_layout);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoTitleDialog.this.dismiss();
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

    public TextView getSureTV() {
        return sureTV;
    }

    public EditText getContentTV() {
        return contentTV;
    }
}
