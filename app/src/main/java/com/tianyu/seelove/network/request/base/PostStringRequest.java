package com.tianyu.seelove.network.request.base;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.tianyu.seelove.utils.LogUtil;

import java.io.UnsupportedEncodingException;

/**
 * author : L.jinzhu
 * date : 2015/8/21
 * introduce : Volley请求string父对象
 */
public abstract class PostStringRequest extends BaseVolleyPostRequest<StringRequest, String> {
    /**
     * 请求参数json[需要子类重写]
     */
    protected abstract String getParamsJson();

    @SuppressWarnings("unchecked")
    @Override
    public StringRequest getRequest() {
        StringRequest request = null;
        try {
            request = new StringRequest(requestType(), getUrl(),
                    responseSuccessListener, responseErrorListener) {
                @Override
                public byte[] getBody() {
                    return addRequestPrefixData(getParamsJson());
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    //转换编码，解决中文乱码问题
                    try {
                        String object = new String(response.data, "UTF-8");
                        return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (Exception je) {
                        return Response.error(new ParseError(je));
                    }
                }
            };
            if (requestTag() != null && "".equals(requestTag()))
                request.setTag(requestTag());
            LogUtil.i("request json: " + new String(request.getBody()).substring(20));
        } catch (Throwable e) {
            LogUtil.e("request json error", e);
        }
        return request;
    }
}
