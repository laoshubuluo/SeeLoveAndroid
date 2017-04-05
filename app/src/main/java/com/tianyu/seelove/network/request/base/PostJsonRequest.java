package com.tianyu.seelove.network.request.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.utils.LogUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * author : L.jinzhu
 * date : 2015/8/21
 * introduce : Volley请求json父对象
 */
public abstract class PostJsonRequest extends BaseVolleyPostRequest<JsonRequest, JSONObject> {
    // 操作超时时间
    final int CUD_SOCKET_TIMEOUT = 10000;
    // 最大重试请求次数
    final int MAX_RETRIES = 0;

    public Handler handler;
    public Context context;

    /**
     * 请求参数json[需要子类重写]
     */
    protected abstract String getParamsJson();

    @SuppressWarnings("unchecked")
    @Override
    public JsonRequest getRequest() {
        JsonObjectRequest request = null;
        try {
            request = new JsonObjectRequest(requestType(), getUrl(), null,
                    responseSuccessListener, responseErrorListener) {
                @Override
                public byte[] getBody() {
                    return addRequestPrefixData(getParamsJson());
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    //转换编码，解决中文乱码问题
                    try {
                        JSONObject jsonObject = new JSONObject(new String(response.data, "UTF-8"));
                        return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (Exception je) {
                        return Response.error(new ParseError(je));
                    }
                }
            };
            // 超时时间、重试次数设定
            request.setRetryPolicy(new DefaultRetryPolicy(CUD_SOCKET_TIMEOUT, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            if (requestTag() != null && !"".equals(requestTag()))
                request.setTag(requestTag());
            LogUtil.i("request json: [" + requestTag() + "]: " + new String(request.getBody()).substring(20));
        } catch (Throwable e) {
            LogUtil.e("request json error [" + requestTag() + "] ", e);
        }
        return request;
    }

    @Override
    protected void responseError(VolleyError error) {
        LogUtil.e("response error json: [" + requestTag() + "]: ", error.getCause());
        if (null == handler)
            return;
        Bundle b = new Bundle();
        Message msg = new Message();
        b.putString("message", VolleyErrorHelper.getMessage(error, context));
        msg.what = MessageSignConstant.SERVER_OR_NETWORK_ERROR;
        msg.setData(b);
        handler.sendMessage(msg);
    }
}
