package com.tianyu.seelove.model.entity.network.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.model.entity.network.response.UserInfoFromWeiXinInfo;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;
import org.json.JSONObject;

/**
 * author : L.jinzhu
 * date : 2015/8/24
 * introduce : 从微信获取userInfo请求request
 */
public class UserInfoFromWeiXinRequest extends PostJsonRequest {
    private String accessToken;
    private String openId;

    public UserInfoFromWeiXinRequest(Handler handler, Context context, String accessToken, String openId) {
        this.handler = handler;
        this.context = context;
        this.accessToken = accessToken;
        this.openId = openId;
    }

    @Override
    protected String getParamsJson() {
        return "";
    }

    @Override
    protected String getUrl() {
        return Constant.WEIXIN_USER_INFO_URL + "?access_token=" + accessToken + "&openid=" + openId;
    }

    @Override
    protected String requestTag() {
        return "userInfoFromWeiXin";
    }

    @Override
    protected void responseSuccess(JSONObject response) {
        Bundle b = new Bundle();
        Message msg = new Message();
        try {
            LogUtil.i("response success json: [" + requestTag() + "]: " + response.toString());
            UserInfoFromWeiXinInfo info = GsonUtil.fromJson(response.toString(), UserInfoFromWeiXinInfo.class);
            //响应正常
            if (StringUtils.isNotBlank(info.getOpenid())) {
                b.putSerializable("info", info);
                msg.what = MessageSignConstant.TOKEN_OR_USERINFO_FROM_WEIXIN_SUCCESS;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.i("userInfo from weixin success");
            }
            //响应失败
            else {
                b.putInt("code", info.getErrCode());
                b.putString("message", info.getErrMsg());
                msg.what = MessageSignConstant.TOKEN_OR_USERINFO_FROM_WEIXIN_FAILURE;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.e("userInfo from weixin failure: code: " + info.getStatusCode() + ",message: " + info.getStatusMsg(), "");
            }
        } catch (Throwable e) {
            handler.sendEmptyMessage(MessageSignConstant.UNKNOWN_ERROR);
            LogUtil.e("userInfo from weixin error", e);
        }
    }
}