package com.tianyu.seelove.ui.activity.system;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.common.ActivityResultConstant;
import com.tianyu.seelove.injection.ControlInjection;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.AgeType;
import com.tianyu.seelove.model.enums.CityType;
import com.tianyu.seelove.model.enums.EducationType;
import com.tianyu.seelove.model.enums.HouseType;
import com.tianyu.seelove.model.enums.MarraryType;
import com.tianyu.seelove.model.enums.SexType;
import com.tianyu.seelove.model.enums.WorkType;
import com.tianyu.seelove.ui.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表选择输入界面
 *
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class InputListActivity extends BaseActivity {
    public final static int USER_INFO_SEX_INPUT = 1;// 用户信息：性别选择(当前用户)
    public final static int USER_INFO_AGE_INPUT = 2;// 用户信息：年龄选择(当前用户)
    public final static int USER_INFO_WORK_INPUT = 3;// 用户信息：工作选择(当前用户)
    public final static int USER_INFO_EDUCATION_INPUT = 4;// 用户信息：学历选择(当前用户)
    public final static int USER_INFO_CITY_INPUT = 5;// 用户信息：城市选择(当前用户)
    public final static int USER_INFO_HOUSE_INPUT = 6;// 用户信息：住房情况选择(当前用户)
    public final static int USER_INFO_MARRARY_INPUT = 7;// 用户信息：婚姻状况选择(当前用户)
    private int activityType;
    private ViewHolder viewHolder;
    private SLUser user;
    private TextView titleView;
    @ControlInjection(R.id.listView)
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_input_list);
        activityType = getIntent().getIntExtra("activityType", 0);
        user = (SLUser) getIntent().getSerializableExtra("user");
        initView();
        initData();
    }

    private void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
    }

    private void initData() {
        switch (activityType) {
            // 性别
            case USER_INFO_SEX_INPUT:
                initDataSex();
                break;
            // 年龄
            case USER_INFO_AGE_INPUT:
                initDataAge();
                break;
            // 职业
            case USER_INFO_WORK_INPUT:
                initDataWork();
                break;
            // 学历
            case USER_INFO_EDUCATION_INPUT:
                initDataEducation();
                break;
            // 所在城市
            case USER_INFO_CITY_INPUT:
                initDataCity();
                break;
            // 住房情况
            case USER_INFO_HOUSE_INPUT:
                initDataHouse();
                break;
            // 婚姻状况
            case USER_INFO_MARRARY_INPUT:
                initDataMarrary();
                break;
        }
    }

    private void initDataSex() {
        titleView.setText(R.string.sex);
        final List<String> sexList = new ArrayList<>();
        sexList.add(SexType.SEX_BOY.getResultMsg());
        sexList.add(SexType.SEX_GIRL.getResultMsg());
        sexList.add(SexType.SEX_UNKNOW.getResultMsg());
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return sexList.size();
            }

            @Override
            public Object getItem(int i) {
                return sexList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_list_item_simple, null);
                viewHolder = new ViewHolder();
                viewHolder.contentTV = (TextView) convertView.findViewById(R.id.contentTV);
                viewHolder.checkIV = (ImageView) convertView.findViewById(R.id.checkIV);
                convertView.setTag(viewHolder);
                //显示内容
                String value = sexList.get(position);
                viewHolder.contentTV.setText(value);
                viewHolder.contentTV.setTextSize(17);
                if (value.equals(user.getSex())) {
                    viewHolder.checkIV.setVisibility(View.VISIBLE);
                    viewHolder.contentTV.setTextColor(getResources().getColor(R.color.red_7a));
                }
                return convertView;
            }
        };
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String sexStr = sexList.get(position);
                Intent intent = new Intent();
                intent.putExtra("sexStr", sexStr);
                setResult(ActivityResultConstant.SEX_INPUT, intent);
                finish();
            }
        });
    }

    private void initDataWork() {
        titleView.setText(R.string.work);
        final List<String> workList = new ArrayList<>();
        workList.add(WorkType.WORK_01.getResultMsg());
        workList.add(WorkType.WORK_02.getResultMsg());
        workList.add(WorkType.WORK_03.getResultMsg());
        workList.add(WorkType.WORK_04.getResultMsg());
        workList.add(WorkType.WORK_05.getResultMsg());
        workList.add(WorkType.WORK_06.getResultMsg());
        workList.add(WorkType.WORK_07.getResultMsg());
        workList.add(WorkType.WORK_08.getResultMsg());
        workList.add(WorkType.WORK_09.getResultMsg());
        workList.add(WorkType.WORK_10.getResultMsg());
        workList.add(WorkType.WORK_11.getResultMsg());
        workList.add(WorkType.WORK_12.getResultMsg());
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return workList.size();
            }

            @Override
            public Object getItem(int i) {
                return workList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_list_item_simple, null);
                viewHolder = new ViewHolder();
                viewHolder.contentTV = (TextView) convertView.findViewById(R.id.contentTV);
                viewHolder.checkIV = (ImageView) convertView.findViewById(R.id.checkIV);
                convertView.setTag(viewHolder);
                //显示内容
                String value = workList.get(position);
                viewHolder.contentTV.setText(value);
                viewHolder.contentTV.setTextSize(17);
                if (value.equals(user.getWorkCode())) {
                    viewHolder.checkIV.setVisibility(View.VISIBLE);
                    viewHolder.contentTV.setTextColor(getResources().getColor(R.color.red_7a));
                }
                return convertView;
            }
        };
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String workStr = workList.get(position);
                Intent intent = new Intent();
                intent.putExtra("workStr", workStr);
                setResult(ActivityResultConstant.WORK_INPUT, intent);
                finish();
            }
        });
    }

    private void initDataAge() {
        titleView.setText(R.string.age);
        final List<String> ageList = new ArrayList<>();
        ageList.add(AgeType.AGE_18.getResultMsg());
        ageList.add(AgeType.AGE_19.getResultMsg());
        ageList.add(AgeType.AGE_20.getResultMsg());
        ageList.add(AgeType.AGE_21.getResultMsg());
        ageList.add(AgeType.AGE_22.getResultMsg());
        ageList.add(AgeType.AGE_23.getResultMsg());
        ageList.add(AgeType.AGE_24.getResultMsg());
        ageList.add(AgeType.AGE_25.getResultMsg());
        ageList.add(AgeType.AGE_26.getResultMsg());
        ageList.add(AgeType.AGE_27.getResultMsg());
        ageList.add(AgeType.AGE_28.getResultMsg());
        ageList.add(AgeType.AGE_29.getResultMsg());
        ageList.add(AgeType.AGE_30.getResultMsg());
        ageList.add(AgeType.AGE_31.getResultMsg());
        ageList.add(AgeType.AGE_32.getResultMsg());
        ageList.add(AgeType.AGE_33.getResultMsg());
        ageList.add(AgeType.AGE_34.getResultMsg());
        ageList.add(AgeType.AGE_35.getResultMsg());
        ageList.add(AgeType.AGE_36.getResultMsg());
        ageList.add(AgeType.AGE_37.getResultMsg());
        ageList.add(AgeType.AGE_38.getResultMsg());
        ageList.add(AgeType.AGE_39.getResultMsg());
        ageList.add(AgeType.AGE_40.getResultMsg());
        ageList.add(AgeType.AGE_41.getResultMsg());
        ageList.add(AgeType.AGE_42.getResultMsg());
        ageList.add(AgeType.AGE_43.getResultMsg());
        ageList.add(AgeType.AGE_44.getResultMsg());
        ageList.add(AgeType.AGE_45.getResultMsg());
        ageList.add(AgeType.AGE_46.getResultMsg());
        ageList.add(AgeType.AGE_47.getResultMsg());
        ageList.add(AgeType.AGE_48.getResultMsg());
        ageList.add(AgeType.AGE_49.getResultMsg());
        ageList.add(AgeType.AGE_50.getResultMsg());
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return ageList.size();
            }

            @Override
            public Object getItem(int i) {
                return ageList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_list_item_simple, null);
                viewHolder = new ViewHolder();
                viewHolder.contentTV = (TextView) convertView.findViewById(R.id.contentTV);
                viewHolder.checkIV = (ImageView) convertView.findViewById(R.id.checkIV);
                convertView.setTag(viewHolder);
                //显示内容
                String value = ageList.get(position);
                viewHolder.contentTV.setText(value);
                viewHolder.contentTV.setTextSize(17);
                if (value.equals(user.getAge())) {
                    viewHolder.checkIV.setVisibility(View.VISIBLE);
                    viewHolder.contentTV.setTextColor(getResources().getColor(R.color.red_7a));
                }
                return convertView;
            }
        };
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String ageStr = ageList.get(position);
                Intent intent = new Intent();
                intent.putExtra("ageStr", ageStr);
                setResult(ActivityResultConstant.AGE_INPUT, intent);
                finish();
            }
        });
    }

    private void initDataCity() {
        titleView.setText(R.string.city);
        final List<String> cityList = new ArrayList<>();
        cityList.add(CityType.CITY_01.getResultMsg());
        cityList.add(CityType.CITY_02.getResultMsg());
        cityList.add(CityType.CITY_03.getResultMsg());
        cityList.add(CityType.CITY_04.getResultMsg());
        cityList.add(CityType.CITY_05.getResultMsg());
        cityList.add(CityType.CITY_06.getResultMsg());
        cityList.add(CityType.CITY_07.getResultMsg());
        cityList.add(CityType.CITY_08.getResultMsg());
        cityList.add(CityType.CITY_09.getResultMsg());
        cityList.add(CityType.CITY_10.getResultMsg());
        cityList.add(CityType.CITY_11.getResultMsg());
        cityList.add(CityType.CITY_12.getResultMsg());
        cityList.add(CityType.CITY_13.getResultMsg());
        cityList.add(CityType.CITY_14.getResultMsg());
        cityList.add(CityType.CITY_15.getResultMsg());
        cityList.add(CityType.CITY_16.getResultMsg());
        cityList.add(CityType.CITY_17.getResultMsg());
        cityList.add(CityType.CITY_18.getResultMsg());
        cityList.add(CityType.CITY_19.getResultMsg());
        cityList.add(CityType.CITY_20.getResultMsg());
        cityList.add(CityType.CITY_21.getResultMsg());
        cityList.add(CityType.CITY_22.getResultMsg());
        cityList.add(CityType.CITY_23.getResultMsg());
        cityList.add(CityType.CITY_24.getResultMsg());
        cityList.add(CityType.CITY_25.getResultMsg());
        cityList.add(CityType.CITY_26.getResultMsg());
        cityList.add(CityType.CITY_27.getResultMsg());
        cityList.add(CityType.CITY_28.getResultMsg());
        cityList.add(CityType.CITY_29.getResultMsg());
        cityList.add(CityType.CITY_30.getResultMsg());
        cityList.add(CityType.CITY_31.getResultMsg());
        cityList.add(CityType.CITY_32.getResultMsg());
        cityList.add(CityType.CITY_33.getResultMsg());
        cityList.add(CityType.CITY_34.getResultMsg());
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return cityList.size();
            }

            @Override
            public Object getItem(int i) {
                return cityList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_list_item_simple, null);
                viewHolder = new ViewHolder();
                viewHolder.contentTV = (TextView) convertView.findViewById(R.id.contentTV);
                viewHolder.checkIV = (ImageView) convertView.findViewById(R.id.checkIV);
                convertView.setTag(viewHolder);
                //显示内容
                String value = cityList.get(position);
                viewHolder.contentTV.setText(value);
                viewHolder.contentTV.setTextSize(17);
                if (value.equals(user.getCityCode())) {
                    viewHolder.checkIV.setVisibility(View.VISIBLE);
                    viewHolder.contentTV.setTextColor(getResources().getColor(R.color.red_7a));
                }
                return convertView;
            }
        };
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String cityStr = cityList.get(position);
                Intent intent = new Intent();
                intent.putExtra("cityStr", cityStr);
                setResult(ActivityResultConstant.CITY_INPUT, intent);
                finish();
            }
        });
    }

    private void initDataHouse() {
        titleView.setText(R.string.house);
        final List<String> houseList = new ArrayList<>();
        houseList.add(HouseType.HOUSE_UNKNOW.getResultMsg());
        houseList.add(HouseType.HOUSE_NO.getResultMsg());
        houseList.add(HouseType.HOUSE_OK.getResultMsg());
        houseList.add(HouseType.HOUSE_PARENT.getResultMsg());
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return houseList.size();
            }

            @Override
            public Object getItem(int i) {
                return houseList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_list_item_simple, null);
                viewHolder = new ViewHolder();
                viewHolder.contentTV = (TextView) convertView.findViewById(R.id.contentTV);
                viewHolder.checkIV = (ImageView) convertView.findViewById(R.id.checkIV);
                convertView.setTag(viewHolder);
                //显示内容
                String value = houseList.get(position);
                viewHolder.contentTV.setText(value);
                viewHolder.contentTV.setTextSize(17);
                if (value.equals(user.getHouseCode())) {
                    viewHolder.checkIV.setVisibility(View.VISIBLE);
                    viewHolder.contentTV.setTextColor(getResources().getColor(R.color.red_7a));
                }
                return convertView;
            }
        };
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String houseStr = houseList.get(position);
                Intent intent = new Intent();
                intent.putExtra("houseStr", houseStr);
                setResult(ActivityResultConstant.HOUSE_INPUT, intent);
                finish();
            }
        });
    }

    private void initDataEducation() {
        titleView.setText(R.string.education);
        final List<String> educationList = new ArrayList<>();
        educationList.add(EducationType.EDUCATION_UNKNOW.getResultMsg());
        educationList.add(EducationType.EDUCATION_SENIOR.getResultMsg());
        educationList.add(EducationType.EDUCATION_COLLEGE.getResultMsg());
        educationList.add(EducationType.EDUCATION_UNIVERSITY.getResultMsg());
        educationList.add(EducationType.EDUCATION_GRADUATE.getResultMsg());
        educationList.add(EducationType.EDUCATION_DOCTOR.getResultMsg());
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return educationList.size();
            }

            @Override
            public Object getItem(int i) {
                return educationList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_list_item_simple, null);
                viewHolder = new ViewHolder();
                viewHolder.contentTV = (TextView) convertView.findViewById(R.id.contentTV);
                viewHolder.checkIV = (ImageView) convertView.findViewById(R.id.checkIV);
                convertView.setTag(viewHolder);
                //显示内容
                String value = educationList.get(position);
                viewHolder.contentTV.setText(value);
                viewHolder.contentTV.setTextSize(17);
                if (value.equals(user.getEducationCode())) {
                    viewHolder.checkIV.setVisibility(View.VISIBLE);
                    viewHolder.contentTV.setTextColor(getResources().getColor(R.color.red_7a));
                }
                return convertView;
            }
        };
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String educationStr = educationList.get(position);
                Intent intent = new Intent();
                intent.putExtra("educationStr", educationStr);
                setResult(ActivityResultConstant.EDUCATION_INPUT, intent);
                finish();
            }
        });
    }


    private void initDataMarrary() {
        titleView.setText(R.string.marrary);
        final List<String> marraryList = new ArrayList<>();
        marraryList.add(MarraryType.UNMARRARY.getResultMsg());
        marraryList.add(MarraryType.DIVORCE.getResultMsg());
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return marraryList.size();
            }

            @Override
            public Object getItem(int i) {
                return marraryList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_list_item_simple, null);
                viewHolder = new ViewHolder();
                viewHolder.contentTV = (TextView) convertView.findViewById(R.id.contentTV);
                viewHolder.checkIV = (ImageView) convertView.findViewById(R.id.checkIV);
                convertView.setTag(viewHolder);
                //显示内容
                String value = marraryList.get(position);
                viewHolder.contentTV.setText(value);
                viewHolder.contentTV.setTextSize(17);
                if (value.equals(user.getMarriageCode())) {
                    viewHolder.checkIV.setVisibility(View.VISIBLE);
                    viewHolder.contentTV.setTextColor(getResources().getColor(R.color.red_7a));
                }
                return convertView;
            }
        };
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String marrayStr = marraryList.get(position);
                Intent intent = new Intent();
                intent.putExtra("marrayStr", marrayStr);
                setResult(ActivityResultConstant.MARRIAGE_INPUT, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftBtn:
                finish();
                break;
            default:
                break;
        }
    }

    private class ViewHolder {
        private TextView contentTV;
        private ImageView checkIV;
    }
}