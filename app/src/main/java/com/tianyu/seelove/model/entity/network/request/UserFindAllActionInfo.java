package com.tianyu.seelove.model.entity.network.request;


import com.tianyu.seelove.model.entity.network.request.base.ActionInfo;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class UserFindAllActionInfo extends ActionInfo {
    private int ageStart = 0;
    private int ageEnd = 0;
    private String sex;
    private String cityCode;

    public UserFindAllActionInfo(int actionId, int ageStart, int ageEnd, String sex, String cityCode) {
        super(actionId);
        this.ageStart = ageStart;
        this.ageEnd = ageEnd;
        this.sex = sex;
        this.cityCode = cityCode;
    }

    public int getAgeStart() {
        return ageStart;
    }

    public void setAgeStart(int ageStart) {
        this.ageStart = ageStart;
    }

    public int getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(int ageEnd) {
        this.ageEnd = ageEnd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}