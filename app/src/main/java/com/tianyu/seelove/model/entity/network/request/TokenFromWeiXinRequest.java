package com.tianyu.seelove.model.entity.network.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.common.MessageSignConstant;
import com.tianyu.seelove.controller.UserController;
import com.tianyu.seelove.model.entity.network.response.TokenFromWeiXinInfo;
import com.tianyu.seelove.network.request.base.PostJsonRequest;
import com.tianyu.seelove.utils.GsonUtil;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;
import org.json.JSONObject;

/**
 * @author shisheng.zhao
 * @Description: 从微信获取token请求request
 * @date 2017-05-05 19:52
 */
public class TokenFromWeiXinRequest extends PostJsonRequest {
    private String code;

    public TokenFromWeiXinRequest(Handler h, Context c, String code) {
        this.handler = h;
        this.context = c;
        this.code = code;
    }

    @Override
    protected String getParamsJson() {
        return "";
    }

    @Override
    protected String getUrl() {
        return Constant.WEIXIN_TOKEN_URL + "?appid=" + Constant.WEIXIN_APP_ID + "&secret=" + Constant.WEIXIN_APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
    }

    @Override
    protected String requestTag() {
        return "tokenFromWeiXin";
    }

    @Override
    protected void responseSuccess(JSONObject response) {
        Bundle b = new Bundle();
        Message msg = new Message();
        try {
            LogUtil.i("response success json: [" + requestTag() + "]: " + response.toString());
            TokenFromWeiXinInfo info = GsonUtil.fromJson(response.toString(), TokenFromWeiXinInfo.class);
            //响应正常
            if (StringUtils.isNotBlank(info.getAccessToken())) {
                //通过token从微信获取用户信息
                UserController controller = new UserController(context, handler);
                controller.getUserInfoByTokenFromWeiXin(info.getAccessToken(), info.getOpenId());
                LogUtil.i("token from weixin success:token:" + info.getAccessToken());
            }
            //响应失败
            else {
                b.putInt("code", info.getErrCode());
                b.putString("message", info.getErrMsg());
                msg.what = MessageSignConstant.TOKEN_OR_USERINFO_FROM_WEIXIN_FAILURE;
                msg.setData(b);
                handler.sendMessage(msg);
                LogUtil.e("token from weixin failure: code: " + info.getStatusCode() + ",message: " + info.getStatusMsg(), "");
            }
        } catch (Throwable e) {
            handler.sendEmptyMessage(MessageSignConstant.UNKNOWN_ERROR);
            LogUtil.e("token from weixin error", e);
        }
    }
}
