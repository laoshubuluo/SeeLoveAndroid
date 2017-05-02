package com.tianyu.seelove.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.ActivityResultConstant;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.AgeType;
import com.tianyu.seelove.model.enums.CityType;
import com.tianyu.seelove.model.enums.EducationType;
import com.tianyu.seelove.model.enums.HouseType;
import com.tianyu.seelove.model.enums.MarraryType;
import com.tianyu.seelove.model.enums.SexType;
import com.tianyu.seelove.model.enums.WorkType;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.activity.system.InputListActivity;
import com.tianyu.seelove.ui.activity.system.InputMultiLineActivity;
import com.tianyu.seelove.ui.activity.system.InputSingleLineActivity;
import com.tianyu.seelove.ui.activity.system.SelectHeadActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.view.dialog.CustomProgressDialog;
import com.tianyu.seelove.view.dialog.PromptDialog;

/**
 * 个人信息界面
 * @author shisheng.zhao
 * @date 2017-03-29 22:50
 */
public class MyInfoActivity extends BaseActivity {
    private UserController controller;
    private RelativeLayout headLayout;
    private ImageView userIcon;
    private SLUser slUser;
    private TextView userName, userAge, userWork, userEducation, userAddress, userHouse, userMarriage, userIntroduce, userSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        controller = new UserController(this, handler);
        slUser = (SLUser) getIntent().getSerializableExtra("user");
        initView();
        initData();
    }

    private void initView() {
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(R.string.user_info);
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        ImageView saveView = (ImageView) findViewById(R.id.rightBtn);
        saveView.setBackgroundResource(R.mipmap.submit_btn);
        saveView.setVisibility(View.VISIBLE);
        backView.setVisibility(View.VISIBLE);
        saveView.setOnClickListener(this);
        backView.setOnClickListener(this);
        RelativeLayout userNameLayout = (RelativeLayout) findViewById(R.id.userNameLayout);
        RelativeLayout userSexLayout = (RelativeLayout) findViewById(R.id.userSexLayout);
        RelativeLayout userAgeLayout = (RelativeLayout) findViewById(R.id.userAgeLayout);
        RelativeLayout userWorkLayout = (RelativeLayout) findViewById(R.id.userWorkLayout);
        RelativeLayout userEducationLayout = (RelativeLayout) findViewById(R.id.userEducationLayout);
        RelativeLayout userAddressLayout = (RelativeLayout) findViewById(R.id.userAddressLayout);
        RelativeLayout userHouseLayout = (RelativeLayout) findViewById(R.id.userHouseLayout);
        RelativeLayout userMarriageLayout = (RelativeLayout) findViewById(R.id.userMarriageLayout);
        RelativeLayout userIntroduceLayout = (RelativeLayout) findViewById(R.id.userIntroduceLayout);
        userNameLayout.setOnClickListener(this);
        userSexLayout.setOnClickListener(this);
        userAgeLayout.setOnClickListener(this);
        userWorkLayout.setOnClickListener(this);
        userEducationLayout.setOnClickListener(this);
        userAddressLayout.setOnClickListener(this);
        userHouseLayout.setOnClickListener(this);
        userMarriageLayout.setOnClickListener(this);
        userIntroduceLayout.setOnClickListener(this);
        headLayout = (RelativeLayout) findViewById(R.id.headLayout);
        userIcon = (ImageView) findViewById(R.id.userIcon);
        userName = (TextView) findViewById(R.id.userName);
        userSex = (TextView) findViewById(R.id.userSex);
        userAge = (TextView) findViewById(R.id.userAge);
        userWork = (TextView) findViewById(R.id.userWork);
        userEducation = (TextView) findViewById(R.id.userEducation);
        userAddress = (TextView) findViewById(R.id.userAddress);
        userHouse = (TextView) findViewById(R.id.userHouse);
        userMarriage = (TextView) findViewById(R.id.userMarriage);
        userIntroduce = (TextView) findViewById(R.id.userIntroduce);
        headLayout.setOnClickListener(this);
    }

    private void initData() {
        if (null != slUser) {
            ImageLoader.getInstance().displayImage(slUser.getHeadUrl(), userIcon, ImageLoaderUtil.getHeadUrlImageOptions());
            userName.setText(slUser.getNickName());
            userSex.setText(SexType.parse(slUser.getSex()).getResultMsg());
            userAge.setText(AgeType.parse(String.valueOf(slUser.getAge())).getResultMsg());
            userWork.setText(WorkType.parse(slUser.getWorkCode()).getResultMsg());
            userEducation.setText(EducationType.parse(slUser.getEducationCode()).getResultMsg());
            userAddress.setText(CityType.parse(slUser.getCityCode()).getResultMsg());
            userHouse.setText(HouseType.parse(slUser.getHouseCode()).getResultMsg());
            userMarriage.setText(MarraryType.parse(slUser.getMarriageCode()).getResultMsg());
            userIntroduce.setText(slUser.getIntroduce());
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent = null;
        switch (view.getId()) {
            case R.id.leftBtn: {
                finish();
                break;
            }
            case R.id.rightBtn: {
                // 请求服务器
                customProgressDialog = new CustomProgressDialog(MyInfoActivity.this, getString(R.string.loading));
                customProgressDialog.show();
                controller.update(slUser);
                break;
            }
            case R.id.headLayout: {
                intent = IntentManager.createIntent(getApplicationContext(), SelectHeadActivity.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.up_in, R.anim.up_out);
                break;
            }
            case R.id.userNameLayout: {
                intent = new Intent(getApplicationContext(), InputSingleLineActivity.class);
                intent.putExtra("activityType", InputSingleLineActivity.USER_INFO_NAME_INPUT);
                intent.putExtra("user", slUser);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.userSexLayout: {
                intent = new Intent(getApplicationContext(), InputListActivity.class);
                intent.putExtra("activityType", InputListActivity.USER_INFO_SEX_INPUT);
                intent.putExtra("user", slUser);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.userAgeLayout: {
                intent = new Intent(getApplicationContext(), InputListActivity.class);
                intent.putExtra("activityType", InputListActivity.USER_INFO_AGE_INPUT);
                intent.putExtra("user", slUser);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.userWorkLayout: {
                intent = new Intent(getApplicationContext(), InputListActivity.class);
                intent.putExtra("activityType", InputListActivity.USER_INFO_WORK_INPUT);
                intent.putExtra("user", slUser);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.userEducationLayout: {
                intent = new Intent(getApplicationContext(), InputListActivity.class);
                intent.putExtra("activityType", InputListActivity.USER_INFO_EDUCATION_INPUT);
                intent.putExtra("user", slUser);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.userAddressLayout: {
                intent = new Intent(getApplicationContext(), InputListActivity.class);
                intent.putExtra("activityType", InputListActivity.USER_INFO_CITY_INPUT);
                intent.putExtra("user", slUser);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.userHouseLayout: {
                intent = new Intent(getApplicationContext(), InputListActivity.class);
                intent.putExtra("activityType", InputListActivity.USER_INFO_HOUSE_INPUT);
                intent.putExtra("user", slUser);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.userMarriageLayout: {
                intent = new Intent(getApplicationContext(), InputListActivity.class);
                intent.putExtra("activityType", InputListActivity.USER_INFO_MARRARY_INPUT);
                intent.putExtra("user", slUser);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.userIntroduceLayout: {
                intent = new Intent(getApplicationContext(), InputMultiLineActivity.class);
                intent.putExtra("activityType", InputMultiLineActivity.USER_INFO_INTRODUCE_INPUT);
                intent.putExtra("userIntroduce", slUser.getIntroduce());
                startActivityForResult(intent, 0);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ActivityResultConstant.UPDATE_USER_HEAD: {
                String filePath = data.getExtras().getString("filePath");
                ImageLoader.getInstance().displayImage(filePath, userIcon, ImageLoaderUtil.getHeadUrlImageOptions());
                slUser.setBigImg(filePath);
                slUser.setHeadUrl(filePath);
                break;
            }
            case ActivityResultConstant.NAME_INPUT: {
                String name = data.getExtras().getString("name");
                slUser.setNickName(name);
                userName.setText(name);
                break;
            }
            case ActivityResultConstant.SEX_INPUT: {
                String sexStr = data.getExtras().getString("sexStr");
                slUser.setSex(SexType.parseByMsg(sexStr).getResultCode());
                userSex.setText(sexStr);
                break;
            }
            case ActivityResultConstant.CITY_INPUT: {
                String cityStr = data.getExtras().getString("cityStr");
                slUser.setCityCode(CityType.parseByMsg(cityStr).getResultCode());
                slUser.setCityName(cityStr);
                userAddress.setText(cityStr);
                break;
            }
            case ActivityResultConstant.WORK_INPUT: {
                String workStr = data.getExtras().getString("workStr");
                slUser.setWorkCode(WorkType.parseByMsg(workStr).getResultCode());
                slUser.setWorkName(workStr);
                userWork.setText(workStr);
                break;
            }
            case ActivityResultConstant.AGE_INPUT: {
                String ageStr = data.getExtras().getString("ageStr");
                slUser.setAge(Integer.parseInt(AgeType.parseByMsg(ageStr).getResultCode()));
                userAge.setText(ageStr);
                break;
            }
            case ActivityResultConstant.EDUCATION_INPUT: {
                String educationStr = data.getExtras().getString("educationStr");
                slUser.setEducationCode(EducationType.parseByMsg(educationStr).getResultCode());
                slUser.setEducationName(educationStr);
                userEducation.setText(educationStr);
                break;
            }
            case ActivityResultConstant.HOUSE_INPUT: {
                String houseStr = data.getExtras().getString("houseStr");
                slUser.setHouseCode(HouseType.parseByMsg(houseStr).getResultCode());
                slUser.setHouseName(houseStr);
                userHouse.setText(houseStr);
                break;
            }
            case ActivityResultConstant.MARRIAGE_INPUT: {
                String marrayStr = data.getExtras().getString("marrayStr");
                slUser.setMarriageCode(MarraryType.parseByMsg(marrayStr).getResultCode());
                slUser.setMarriageName(marrayStr);
                userMarriage.setText(marrayStr);
                break;
            }
            case ActivityResultConstant.INTRODUCE_INPUT: {
                String introduce = data.getExtras().getString("userIntroduce");
                slUser.setIntroduce(introduce);
                userIntroduce.setText(introduce);
                break;
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (customProgressDialog != null)
            customProgressDialog.dismiss();
        if (promptDialog == null || promptDialog.isShowing())
            promptDialog = new PromptDialog(this);
        String code;
        String message;
        switch (msg.what) {
            case MessageSignConstant.USER_UPDATE_SUCCESS:
                Toast.makeText(this, R.string.user_update_success, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case MessageSignConstant.USER_UPDATE_FAILURE:
                code = msg.getData().getString("code");
                message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.user_update_success), message);
                promptDialog.show();
                break;
            case MessageSignConstant.SERVER_OR_NETWORK_ERROR:
                promptDialog.initData("", msg.getData().getString("message"));
                promptDialog.show();
                break;
            case MessageSignConstant.UNKNOWN_ERROR:
                promptDialog.initData("", getString(R.string.unknown_error));
                promptDialog.show();
                break;
        }
        return false;
    }
}
