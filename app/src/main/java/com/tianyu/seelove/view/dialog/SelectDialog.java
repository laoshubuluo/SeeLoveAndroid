package com.tianyu.seelove.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.view.RangeSeekBar;
import com.tianyu.seelove.view.SpinerPopWindow;

import java.util.ArrayList;
import java.util.List;



/**
 * @author shisheng.zhao
 * @Description: 自定义Dialog
 * @date 2015-10-28 上午11:17:26
 */
public class SelectDialog extends Dialog{
    private Context mContext;
    private TextView titleTV;
    private TextView contentTV;
    public TextView sureTV;
    public TextView cancelTV;
    public CheckBox isCheck;
    public View lineView;
    private RangeSeekBar seekBar;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> list;
    private TextView cityName;

    public SelectDialog(Context context) {
        super(context, R.style.WinDialog);
        setContentView(R.layout.view_dialog_select);
        mContext = context;
        titleTV = (TextView) findViewById(R.id.titleTV);
        contentTV = (TextView) findViewById(R.id.contentTV);
        seekBar = (RangeSeekBar) findViewById(R.id.seekBar);
        sureTV = (TextView) findViewById(R.id.sureTV);
        cancelTV = (TextView) findViewById(R.id.cancelTV);
        isCheck = (CheckBox) findViewById(R.id.isChexkBox);
        lineView = findViewById(R.id.line_layout);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDialog.this.dismiss();
            }
        });
        seekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(float lowerRange, float upperRange) {

            }
        });
        initData();
        cityName = (TextView) findViewById(R.id.cityName);
        cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpinerPopWindow.setWidth(cityName.getWidth());
                mSpinerPopWindow.showAsDropDown(cityName);
                setTextImage(R.mipmap.icon_up);
            }
        });
        mSpinerPopWindow = new SpinerPopWindow<String>(context, list,itemClickListener);
        mSpinerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTextImage(R.mipmap.icon_down);
            }
        });
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            cityName.setText(list.get(position));
        }
    };


    /**
     * 初始化数据
     */
    private void initData() {
        list = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            list.add("test:" + i);
        }
    }

    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = mContext.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        cityName.setCompoundDrawables(null, null, drawable, null);
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
