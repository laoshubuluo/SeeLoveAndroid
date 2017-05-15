//package com.tianyu.seelove.manager;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.widget.Toast;
//import com.tianyu.seelove.R;
//import com.tianyu.seelove.model.enums.ShareTo;
//import com.tianyu.seelove.utils.LogUtil;
//import java.util.HashMap;
//
///**
// * 分享管理类
// * @author shisheng.zhao
// * @date 2017-05-07 16:49
// */
//public class ShareManager {
//    private Context context;
//    private String paltformName;
//
//    public ShareManager(Context context) {
//        this.context = context;
//    }
//
//    /**
//     * 分享
//     * @param shareTo
//     */
//    public void share(ShareTo shareTo) {
//        shareBegin(shareTo);
//    }
//
//    /**
//     * 分享开始
//     */
//    private void shareBegin(ShareTo shareTo) {
//        final int type = shareTo.getType();
//        if (ShareTo.WECHAT.getType() == type)
//            share2WechatAndCircle(Wechat.NAME);
//        else if (ShareTo.WECHAT_CIRCLE.getType() == type)
//            share2WechatAndCircle(WechatMoments.NAME);
//        else if (ShareTo.QQ.getType() == type)
//            share2Qq();
//        else if (ShareTo.QQ_CIRCLE.getType() == type)
//            share2QqCircle();
//    }
//
//    private void share2WechatAndCircle(String paltform) {
//        paltformName = paltform;
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);
//        sp.setTitle("视爱－真实的短视屏婚恋平台!");
//        sp.setText("真实的，免费的短视屏婚恋平台");
//        sp.setImageUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg");
//        sp.setUrl("https://www.baidu.com");
//        Platform platform = ShareSDK.getPlatform(paltformName);
//        platform.setPlatformActionListener(platformActionListener);
//        platform.removeAccount();
//        platform.share(sp);
//    }
//
//    private void share2Qq() {
//        paltformName = QQ.NAME;
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setTitle("视爱－真实的短视屏婚恋平台!");
//        sp.setTitleUrl("https://www.baidu.com");
//        sp.setText("真实的，免费的短视屏婚恋平台");
//        sp.setImageUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg");
//        Platform platform = ShareSDK.getPlatform(paltformName);
//        platform.setPlatformActionListener(platformActionListener);
//        platform.removeAccount();
//        platform.share(sp);
//    }
//
//    private void share2QqCircle() {
//        paltformName = QZone.NAME;
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setTitle("视爱－真实的短视屏婚恋平台!");
//        sp.setTitleUrl("https://www.baidu.com");
//        sp.setText("真实的，免费的短视屏婚恋平台");
//        sp.setImageUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg");
//        sp.setSite(context.getString(R.string.app_name));
//        sp.setSiteUrl("https://www.baidu.com");
//        Platform platform = ShareSDK.getPlatform(paltformName);
//        platform.setPlatformActionListener(platformActionListener);
//        platform.removeAccount();
//        platform.share(sp);
//    }
//
//    PlatformActionListener platformActionListener = new PlatformActionListener() {
//        @Override
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            LogUtil.i("shareMessageShare onComplete:" + platform.getName());
//        }
//
//        @Override
//        public void onError(Platform platform, int i, Throwable throwable) {
//            LogUtil.i("shareMessageShare onError:" + platform.getName() + ": " + throwable);
//            String expName = throwable.getClass().getSimpleName();
//            String msgStr;
//            // 判断有没有安装客户端
//            if ("WechatClientNotExistException".equals(expName)
//                    || "WechatTimelineNotSupportedException".equals(expName)
//                    || "WechatFavoriteNotSupportedException".equals(expName)) {
//                msgStr = getShareToStrByPlatform(platform.getName()) + context.getString(R.string.weixin_not_install);
//            } else {
//                msgStr = getShareToStrByPlatform(platform.getName()) + context.getString(R.string.share_failure);
//            }
//            Message msg = new Message();
//            Bundle b = new Bundle();
//            b.putString("msg", msgStr);
//            msg.setData(b);
//            handler4Msg.sendMessage(msg);
//        }
//
//        @Override
//        public void onCancel(Platform platform, int i) {
//            LogUtil.i("shareMessageShare onCancel:" + platform.getName());
//            Message msg = new Message();
//            Bundle b = new Bundle();
//            b.putString("msg", getShareToStrByPlatform(platform.getName()) + context.getString(R.string.share_cancel));
//            msg.setData(b);
//            handler4Msg.sendMessage(msg);
//        }
//    };
//
//    /**
//     * 通过Handler在UI层显示Toast
//     */
//    Handler handler4Msg = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Toast.makeText(context, msg.getData().getString("msg"), Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    private String getShareToStrByPlatform(String paltformName) {
//        if (Wechat.NAME.equals(paltformName))
//            return context.getString(R.string.share_wechat);
//        else if (WechatMoments.NAME.equals(paltformName))
//            return context.getString(R.string.share_wechat_space);
//        else if (QQ.NAME.equals(paltformName))
//            return context.getString(R.string.share_qq);
//        else if (QZone.NAME.equals(paltformName))
//            return context.getString(R.string.share_qq_space);
//        else
//            return "";
//    }
//}