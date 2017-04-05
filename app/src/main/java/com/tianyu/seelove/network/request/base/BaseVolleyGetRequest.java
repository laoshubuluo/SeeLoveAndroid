package com.tianyu.seelove.network.request.base;

import com.android.volley.Request;

/**
 * author : L.jinzhu
 * date : 2015/8/21
 * introduce : Volley请求顶级对象
 */
public abstract class BaseVolleyGetRequest<REQUEST, RESPONSE> extends BaseVolleyRequest<REQUEST, RESPONSE> {

    @Override
    protected int requestType() {
        return Request.Method.GET;
    }

}
