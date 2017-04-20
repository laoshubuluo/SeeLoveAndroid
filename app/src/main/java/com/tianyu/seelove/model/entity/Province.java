package com.tianyu.seelove.model.entity;

import java.io.Serializable;

/**
 * @author shisheng.zhao
 * @Description: 省份实体类对象
 * @date 2016-03-17 下午14:57:38
 */
public class Province implements Serializable {
    private String countryId; // 国家ID,1代表中国
    private String provinceId; // 省份ID
    private String provinceName; // 省份全称
    private String provinceShowName; // 省份简称

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceShowName(String provinceShowName) {
        this.provinceShowName = provinceShowName;
    }

    public String getProvinceShowName() {
        return provinceShowName;
    }
}
