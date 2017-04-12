package com.tianyu.seelove.model.entity.user;

import com.tianyu.seelove.model.enums.SexType;

import java.io.Serializable;

/**
 * 用户实体类
 * @author shisheng.zhao
 * @date 2017-03-31 18:07
 */
public class SLUser implements Serializable{
    // 用户基本信息
    private long userId; // 用户id,唯一标示
    private String headUrl; // 用户头像
    private String nickName; // 用户昵称
    private int accountType = 0; // 0:未知;1:微信;2:QQ
    private int age = 0; // 用户年龄
    private String sex ;//性别 0:未知;1男;2女
    private String bigImg; // 用户信息默认大图
    private int cityCode; // 城市编号
    private String cityName; // 城市名称
    private int workCode; // 职业编号
    private String workName; // 职业名称
    private int educationCode; // 学历编号
    private String educationName; // 学历名称
    private int houseCode; // 住房状况编号
    private String houseName; // 住房状况名称
    private int marriageCode; // 婚姻状况编号
    private String marriageName; // 婚姻状况名称
    private String introduce; // 一句话介绍
    private String remark; // 保留域
    // 其他参数
    private String token4RongCloud;// 融云token

    // 显示额外信息
    private int videoCount; // 视频数
    private int followCount; // 关注数
    private int followedCount; // 被关注数

    public int getFollowedCount() {
        return followedCount;
    }

    public void setFollowedCount(int followedCount) {
        this.followedCount = followedCount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBigImg() {
        return bigImg;
    }

    public void setBigImg(String bigImg) {
        this.bigImg = bigImg;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getWorkCode() {
        return workCode;
    }

    public void setWorkCode(int workCode) {
        this.workCode = workCode;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public int getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(int educationCode) {
        this.educationCode = educationCode;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public int getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(int houseCode) {
        this.houseCode = houseCode;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public int getMarriageCode() {
        return marriageCode;
    }

    public void setMarriageCode(int marriageCode) {
        this.marriageCode = marriageCode;
    }

    public String getMarriageName() {
        return marriageName;
    }

    public void setMarriageName(String marriageName) {
        this.marriageName = marriageName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public String getToken4RongCloud() {
        return token4RongCloud;
    }

    public void setToken4RongCloud(String token4RongCloud) {
        this.token4RongCloud = token4RongCloud;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", token4RongCloud='" + token4RongCloud + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
