package com.tianyu.seelove.model.entity.video;

/**
 * 短视频上传成功实体类
 * @author shisheng.zhao
 * @date 2017-03-31 17:56
 */
public class VideoUploadResponse {
    private String hash; // 短视频id
    private String key; // 用户id

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
