package com.tianyu.seelove.network.request.base;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * author : L.jinzhu
 * date : 2015/8/21
 * introduce : Volley请求顶级对象
 */
public abstract class BaseVolleyRequest<REQUEST, RESPONSE> {

    protected Response.Listener responseSuccessListener = new Response.Listener() {
        @Override
        public void onResponse(Object response) {
            responseSuccess((RESPONSE) response);
        }
    };

    protected Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            responseError(error);
        }
    };

    public abstract REQUEST getRequest();

    protected abstract String getUrl();

    protected abstract int requestType();

    protected abstract String requestTag();

    /**
     * 正常响应处理逻辑[需要子类重写]
     */
    protected abstract void responseSuccess(RESPONSE response);

    /**
     * 异常响应处理逻辑[需要子类重写]
     */
    protected abstract void responseError(VolleyError error);
}
