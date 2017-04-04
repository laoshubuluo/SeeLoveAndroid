package com.tianyu.seelove.model.entity.message;

import java.io.Serializable;

/**
 * @author shisheng.zhao
 * @Description: 轮播图封装实体类对象
 * @date 2016-03-09 下午17:57:25
 */
public class ViewPageImage implements Serializable{
    private String imageUrl; // 原图地址
    private String thumImageUrl; // 缩略图地址

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumImageUrl() {
        return thumImageUrl;
    }

    public void setThumImageUrl(String thumImageUrl) {
        this.thumImageUrl = thumImageUrl;
    }
}
