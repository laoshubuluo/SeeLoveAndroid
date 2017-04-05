package com.tianyu.seelove.network.request.base;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianyu.seelove.R;

import java.util.HashMap;
import java.util.Map;

/**
 * author : L.jinzhu
 * date : 2015/8/21
 * introduce : Volley请求错误码
 */
public class VolleyErrorHelper {
    /**
     * 获取异常信息
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.network_timeout_error);
        } else if (isServerProblem(error)) {
            return context.getResources().getString(R.string.server_error);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.network_error);
        }
        return context.getResources().getString(R.string.server_error);
    }

    /**
     * 网络错误
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * 服务器错误
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * 服务器错误
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    try {
                        // server might return error like this { "error": "Some error occured" }
                        // Use "Gson" to parse the result
                        HashMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<Map<String, String>>() {
                                }.getType());

                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return error.getMessage();
                default:
                    return context.getResources().getString(R.string.server_error);
            }
        }
        return context.getResources().getString(R.string.server_error);
    }
}
