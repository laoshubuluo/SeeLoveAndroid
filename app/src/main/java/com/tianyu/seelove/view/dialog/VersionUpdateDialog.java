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
 * @Description: 版本更新dialog
 * @date 2017-05-09 16:04
 */
public class VersionUpdateDialog extends Dialog {
    private TextView titleTV;
    private TextView contentTV;
    public TextView sureTV;
    public TextView cancelTV;
    public CheckBox isCheck;
    public View lineView;

    public VersionUpdateDialog(Context context) {
        super(context, R.style.WinDialog);
        setContentView(R.layout.dialog_version_update);
        titleTV = (TextView) findViewById(R.id.titleTV);
        contentTV = (TextView) findViewById(R.id.contentTV);
        sureTV = (TextView) findViewById(R.id.sureTV);
        cancelTV = (TextView) findViewById(R.id.cancelTV);
        lineView = findViewById(R.id.line_layout);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersionUpdateDialog.this.dismiss();
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

    public void initData(String content) {
        if (!StringUtils.isNullOrBlank(content)) {
            contentTV.setText(content);
            contentTV.setVisibility(View.VISIBLE);
        }
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
