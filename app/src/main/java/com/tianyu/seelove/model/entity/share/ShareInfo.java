package com.tianyu.seelove.model.entity.share;

import java.io.Serializable;

/**
 * 分享model
 * @author shisheng.zhao
 * @date 2017-04-22 17:23
 */
public class ShareInfo implements Serializable {
    private int shareId;
    private String shareTitle;

    public ShareInfo(int shareId, String shareTitle) {
        this.shareId = shareId;
        this.shareTitle = shareTitle;
    }

    public void setShareId(int shareId) {
        this.shareId = shareId;
    }

    public int getShareId() {
        return shareId;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareTitle() {
        return shareTitle;
    }
}
