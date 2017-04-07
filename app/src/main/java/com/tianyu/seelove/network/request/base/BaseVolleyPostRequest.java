package com.tianyu.seelove.network.request.base;

import com.android.volley.Request;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * author : L.jinzhu
 * date : 2015/8/21
 * introduce : Volley请求顶级对象
 */
@SuppressWarnings("unchecked")
public abstract class BaseVolleyPostRequest<REQUEST, RESPONSE> extends BaseVolleyRequest<REQUEST, RESPONSE> {

    @Override
    protected int requestType() {
        return Request.Method.POST;
    }

    /**
     * 拼接请求前缀
     *
     * @param jsonStr 请求json数据
     */
    public byte[] addRequestPrefixData(String jsonStr) {
        ByteArrayOutputStream os = null;
        DataOutputStream dos = null;
        byte[] bytes = new byte[]{};
        try {
            os = new ByteArrayOutputStream();
            dos = new DataOutputStream(os);
            //二进制前缀
//            String userId = AppUtils.getInstance().getUserId();
//            long uid = StringUtils.isNotBlank(userId) ? Long.parseLong(userId) : 0;
//            int version = 100;
//            long tmp = 0;
//            dos.writeLong(uid);//写入uid
//            dos.writeInt(version);//协议版本
//            dos.writeLong(tmp);//占位符
            dos.write(jsonStr.getBytes("utf-8"));
            bytes = os.toByteArray();
        } catch (Throwable e) {
            LogUtil.e("request add prefix data error", e);
        } finally {
            try {
                if (null != os)
                    os.close();
                if (null != dos)
                    dos.close();
            } catch (IOException e) {
                LogUtil.e("request add prefix data error: close io error", e);
            }
        }
        return bytes;
    }
}
