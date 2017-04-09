package com.tianyu.seelove.model.entity.network.request.base;

/**
 * author : L.jinzhu
 * date : 2015/8/12
 * introduce : 请求实体
 */
public class ActionInfo extends AbstractRequestInfo {

    int actionId;

    public ActionInfo(int actionId) {
        this.actionId = actionId;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }
}