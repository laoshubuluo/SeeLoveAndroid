package com.tianyu.seelove.manager;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tianyu.seelove.utils.LogUtil;

/**
 * Volley 管理类,对volley进行统一管理
 * @author shisheng.zhao
 * @date 2017-03-28 16:26
 */
public class VolleyManager {
    private static VolleyManager instance;
    private Context context;
    private RequestQueue requestQueue;

    private VolleyManager(Context context) {
        this.context = context;
    }

    //单例模式：同步锁，保证线程安全，双重检查，避免同步锁引起的性能问题
    public static VolleyManager getInstance(Context context) {
        if (instance == null) {
            synchronized (VolleyManager.class) {
                if (instance == null) {
                    instance = new VolleyManager(context);
                }
            }
        }
        return instance;
    }

    public <T> void add2RequestQueue(Request<T> request) {
        if (request == null) {
            LogUtil.e("request is null", new Exception());
            return;
        }
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(String tag) {
        getRequestQueue().cancelAll(tag);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}
