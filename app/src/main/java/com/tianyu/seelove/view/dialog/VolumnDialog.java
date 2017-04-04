package com.tianyu.seelove.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyu.seelove.R;

/**
 * @author shisheng.zhao
 * @Description: 自定义VolumnDialog
 * @date 2015-09-01 上午15:04:27
 */
public class VolumnDialog extends ProgressDialog {
    private ImageView volumn;
    private TextView chanleRecord;

    public void setVolumn(double signalEMA) {
        int[] imgs = new int[]{R.mipmap.amp1, R.mipmap.amp2,
                R.mipmap.amp3, R.mipmap.amp4, R.mipmap.amp5,
                R.mipmap.amp6};
        int index = (int) Math.ceil(signalEMA / 2);
        if (index >= imgs.length) {
            volumn.setImageResource(R.mipmap.amp7);
            return;
        } else {
            volumn.setImageResource(imgs[index]);
        }
    }

    public VolumnDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.translate);//背景
        setContentView(R.layout.voice_rcd_hint_window);
        LinearLayout voicebgLayout = (LinearLayout)findViewById(R.id.voice_bg);
        volumn = (ImageView) findViewById(R.id.volume);
        chanleRecord = (TextView) findViewById(R.id.chanle_record);
        voicebgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setChanleRecord(TextView chanleRecord) {
        this.chanleRecord = chanleRecord;
    }

    public TextView getChanleRecord() {
        return chanleRecord;
    }
}
