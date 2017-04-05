package com.tianyu.seelove.network.request.base;

import com.android.volley.toolbox.StringRequest;

/**
 * author : L.jinzhu
 * date : 2015/8/21
 * introduce : Volley请求string父对象
 */
public abstract class GetStringRequest extends BaseVolleyGetRequest<StringRequest, String> {

    @SuppressWarnings("unchecked")
    @Override
    public StringRequest getRequest() {
        return new StringRequest(requestType(), getUrl(),
                responseSuccessListener, responseErrorListener);
    }

}
