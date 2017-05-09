package com.tianyu.seelove.model.entity.network.response;

import com.tianyu.seelove.model.entity.network.response.base.ResponseInfo;
import com.tianyu.seelove.model.entity.user.SLUserDetail;

import java.util.List;


/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 响应实体
 */
public class NewsFindAllRspInfo extends ResponseInfo {
    private int currentPage;
    private int isEndPage; // 是否是最后一页 1：是 0：否
    private List<SLUserDetail> userDetailList;

    public List<SLUserDetail> getUserDetailList() {
        return userDetailList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getIsEndPage() {
        return isEndPage;
    }

    public void setIsEndPage(boolean isEndPage) {
        this.isEndPage = isEndPage ? 1 : 0;
    }

    public void setUserDetailList(List<SLUserDetail> userDetailList) {
        this.userDetailList = userDetailList;
    }
}