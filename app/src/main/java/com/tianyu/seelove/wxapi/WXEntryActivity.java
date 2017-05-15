//package com.tianyu.seelove.wxapi;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.widget.Toast;
//
//import com.tencent.mm.sdk.constants.ConstantsAPI;
//import com.tencent.mm.sdk.modelbase.BaseReq;
//import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.tencent.mm.sdk.modelmsg.SendAuth;
//import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tianyu.seelove.R;
//import com.tianyu.seelove.common.Constant;
//import com.tianyu.seelove.common.MessageSignConstant;
//import com.tianyu.seelove.controller.UserController;
//import com.tianyu.seelove.model.entity.network.response.UserInfoFromWeiXinInfo;
//import com.tianyu.seelove.model.enums.AccountType;
//import com.tianyu.seelove.utils.LogUtil;
//import com.tianyu.seelove.view.dialog.CustomProgressDialog;
//import com.tianyu.seelove.view.dialog.PromptDialog;
//
//import cn.sharesdk.wechat.utils.WechatHandlerActivity;
//
///**
// * @author shisheng.zhao
// * @Description: wechat登录
// * @date 2017-05-05 19:46
// */
//public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler, Handler.Callback {
//    public final static int WX_LOGIN = 1;// 登录
//    // IWXAPI 是第三方app和微信通信的openapi接口
//    private IWXAPI api;
//    public Handler handler;
//    public PromptDialog promptDialog;
//    public CustomProgressDialog customProgressDialog;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        promptDialog = new PromptDialog(WXEntryActivity.this);
//        handler = new Handler(this);
//        //注册到微信
//        regist2WX();
//        wxLogin();
//    }
//
//    /**
//     * 注册到微信
//     */
//    private void regist2WX() {
//        // 通过WXAPIFactory工厂，获取IWXAPI的实例
//        if (api == null)
//            api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APP_ID, false);
//        // 未安装微信
//        if (!api.isWXAppInstalled()) {
//            Toast.makeText(getApplicationContext(), getString(R.string.weixin_not_install), Toast.LENGTH_LONG).show();
//            loginCancle();
//            return;
//        }
//        api.handleIntent(getIntent(), this);
//        // 将该app注册到微信
//        api.registerApp(Constant.WEIXIN_APP_ID);
//    }
//
//    /**
//     * 微信登录
//     */
//    private void wxLogin() {
//        //微信登录
//        SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        LogUtil.i("call weixin open platform to login, sendReq: " + api.sendReq(req));
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        api.handleIntent(intent, this);
//    }
//
//    // 微信发送请求到第三方应用时，会回调到该方法
//    @Override
//    public void onReq(BaseReq req) {
//        switch (req.getType()) {
//            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                break;
//            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                break;
//            default:
//                break;
//        }
//    }
//
//    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
//    @Override
//    public void onResp(BaseResp resp) {
//        if (promptDialog == null || promptDialog.isShowing())
//            promptDialog = new PromptDialog(WXEntryActivity.this);
//        Bundle bundle = new Bundle();
//        switch (resp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                LogUtil.i("call weixin open platform: success");
//                resp.toBundle(bundle);
//                SendAuth.Resp sp = new SendAuth.Resp(bundle);
//                // 从微信平台获取微信code
//                UserController controller = new UserController(getApplication(), handler);
//                controller.getTokenByCodeFromWeiXin(sp.code);
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                LogUtil.i("call weixin open platform: cancel");
//                loginCancle();
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                LogUtil.e(this.getClass().getName(), "call weixin open platform: deny");
//                break;
//            default:
//                LogUtil.e(this.getClass().getName(), "call weixin open platform: error");
//                break;
//        }
//    }
//
//    @Override
//    public boolean handleMessage(Message msg) {
//        if (customProgressDialog != null)
//            customProgressDialog.dismiss();
//        if (promptDialog == null || promptDialog.isShowing())
//            promptDialog = new PromptDialog(WXEntryActivity.this);
//        int code;
//        String message;
//        switch (msg.what) {
//            case MessageSignConstant.TOKEN_OR_USERINFO_FROM_WEIXIN_SUCCESS:
//                UserInfoFromWeiXinInfo info = (UserInfoFromWeiXinInfo) msg.getData().getSerializable("info");
//                //正式登录
//                UserController controller = new UserController(getApplication(), Constant.loginHandler);
//                controller.login4Platform(AccountType.WECHAT.getCode(), info.getOpenid(), info.toString());
//                finish();
//                break;
//            case MessageSignConstant.TOKEN_OR_USERINFO_FROM_WEIXIN_FAILURE:
//                break;
//            case MessageSignConstant.SERVER_OR_NETWORK_ERROR:
//                promptDialog.initData(getString(R.string.token_from_weixin_error), msg.getData().getString("message"));
//                promptDialog.show();
//                break;
//            case MessageSignConstant.UNKNOWN_ERROR:
//                promptDialog.initData(getString(R.string.token_from_weixin_error), getString(R.string.unknown_error));
//                promptDialog.show();
//                break;
//        }
//        return false;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (null != promptDialog)
//            promptDialog.dismiss();
//        finish();
//    }
//
//    /**
//     * 通知上层界面（登录界面），因错误或主动操作，第三方登录流程退出
//     */
//    private void loginCancle() {
//        Constant.loginOpenPlatformIng = false;
//        finish();
//    }
//
//    /**
//     * 处理微信发出的向第三方应用请求app message
//     * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
//     * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
//     * 做点其他的事情，包括根本不打开任何页面
//     */
//    public void onGetMessageFromWXReq(WXMediaMessage msg) {
//        Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
//        startActivity(iLaunchMyself);
//    }
//
//    /**
//     * 处理微信向第三方应用发起的消息
//     * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
//     * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
//     * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
//     * 回调。
//     * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
//     */
//    public void onShowMessageFromWXReq(WXMediaMessage msg) {
//        if (msg != null && msg.mediaObject != null
//                && (msg.mediaObject instanceof WXAppExtendObject)) {
//            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
//            Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
//        }
//    }
//}