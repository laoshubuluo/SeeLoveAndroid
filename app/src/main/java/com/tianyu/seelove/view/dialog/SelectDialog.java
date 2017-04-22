package com.tianyu.seelove.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.dao.SeeLoveDao;
import com.tianyu.seelove.model.entity.Province;
import com.tianyu.seelove.view.RangeSeekBar;
import com.tianyu.seelove.view.SpinerPopWindow;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 自定义Dialog
 * @date 2015-10-28 上午11:17:26
 */
public class SelectDialog extends Dialog implements View.OnClickListener ,RangeSeekBar.OnRangeChangedListener{
    private Context mContext;
    private Button allSexBtn, manSexBtn, womenSexBtn;
    public TextView sureTV, cancelTV, cityName;
    private RangeSeekBar seekBar;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<Province> provinceList;

    public SelectDialog(Context context) {
        super(context, R.style.WinDialog);
        setContentView(R.layout.view_dialog_select);
        mContext = context;
        initView();
        initData();
    }

    private void initView() {
        allSexBtn = (Button) findViewById(R.id.allSex);
        manSexBtn = (Button) findViewById(R.id.manSex);
        womenSexBtn = (Button) findViewById(R.id.womenSex);
        seekBar = (RangeSeekBar) findViewById(R.id.seekBar);
        cityName = (TextView) findViewById(R.id.cityName);
        cancelTV = (TextView) findViewById(R.id.cancelTV);
        sureTV = (TextView) findViewById(R.id.sureTV);
        allSexBtn.setOnClickListener(this);
        manSexBtn.setOnClickListener(this);
        womenSexBtn.setOnClickListener(this);
        cityName.setOnClickListener(this);
        cancelTV.setOnClickListener(this);
        sureTV.setOnClickListener(this);
        seekBar.setOnRangeChangedListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        allSexBtn.setBackgroundResource(R.drawable.shape_corners_button_red_7a);
        allSexBtn.setTextColor(mContext.getResources().getColor(R.color.white));
        manSexBtn.setBackgroundResource(R.drawable.shape_corners_button_white);
        manSexBtn.setTextColor(mContext.getResources().getColor(R.color.black_37));
        womenSexBtn.setBackgroundResource(R.drawable.shape_corners_button_white);
        womenSexBtn.setTextColor(mContext.getResources().getColor(R.color.black_37));
        provinceList = new SeeLoveDao(mContext).getProvinceList();
        mSpinerPopWindow = new SpinerPopWindow<String>(mContext, provinceList, itemClickListener);
        mSpinerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTextImage(R.mipmap.icon_down);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.allSex:
                allSexBtn.setBackgroundResource(R.drawable.shape_corners_button_red_7a);
                allSexBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                manSexBtn.setBackgroundResource(R.drawable.shape_corners_button_white);
                manSexBtn.setTextColor(mContext.getResources().getColor(R.color.black_37));
                womenSexBtn.setBackgroundResource(R.drawable.shape_corners_button_white);
                womenSexBtn.setTextColor(mContext.getResources().getColor(R.color.black_37));
                break;
            case R.id.manSex:
                allSexBtn.setBackgroundResource(R.drawable.shape_corners_button_white);
                allSexBtn.setTextColor(mContext.getResources().getColor(R.color.black_37));
                manSexBtn.setBackgroundResource(R.drawable.shape_corners_button_red_7a);
                manSexBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                womenSexBtn.setBackgroundResource(R.drawable.shape_corners_button_white);
                womenSexBtn.setTextColor(mContext.getResources().getColor(R.color.black_37));
                break;
            case R.id.womenSex:
                allSexBtn.setBackgroundResource(R.drawable.shape_corners_button_white);
                allSexBtn.setTextColor(mContext.getResources().getColor(R.color.black_37));
                manSexBtn.setBackgroundResource(R.drawable.shape_corners_button_white);
                manSexBtn.setTextColor(mContext.getResources().getColor(R.color.black_37));
                womenSexBtn.setBackgroundResource(R.drawable.shape_corners_button_red_7a);
                womenSexBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case R.id.cityName:
                mSpinerPopWindow.setWidth(cityName.getWidth());
                mSpinerPopWindow.showAsDropDown(cityName);
                setTextImage(R.mipmap.icon_up);
                break;
            case R.id.sureTV:
                SelectDialog.this.dismiss();
                break;
            case R.id.cancelTV:
                SelectDialog.this.dismiss();
                break;
        }
    }

    @Override
    public void onRangeChanged(float lowerRange, float upperRange) {

    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            cityName.setText(provinceList.get(position).getProvinceShowName());
        }
    };


    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = mContext.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
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
}
