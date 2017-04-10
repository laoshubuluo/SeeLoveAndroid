package com.tianyu.seelove.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.tianyu.seelove.R;
import com.tianyu.seelove.application.SeeLoveApplication;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.IsVisbleStatus;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.ui.activity.message.SingleChatActivity;

public class MessageNotification {
    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private PendingIntent mPendingIntent;
    int count = 0;
    private MessageDaoImpl daoImpl = new MessageDaoImpl();

    public MessageNotification(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void showNotification(SLMessage amMessage, int countNum) throws Exception {
        NotificationManager mNotificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        String username = "您的好友";
        try {
            SLUser user = new UserDaoImpl().getUserByUserId(amMessage.getUserFrom());
            if (user != null) {
                username = user.getNickName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBuilder = new NotificationCompat.Builder(mContext);
        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(bm);
        if (amMessage.getMessageType().equals(MessageType.TEXT)) {
            if (IsVisbleStatus.INVISBLE.getResultCode() == amMessage.getIsVisible()) {
                mBuilder.setSmallIcon(R.drawable.small_icon)
                        .setContentTitle(username)
                        .setContentText((amMessage.getMessageContent()));
                mBuilder.setTicker(username + ":" + amMessage.getMessageContent());
            } else {
                mBuilder.setSmallIcon(R.drawable.small_icon)
                        .setContentTitle(username)
                        .setContentText((amMessage.getMessageContent()));
                mBuilder.setTicker(username + ":" + amMessage.getMessageContent());
            }
        } else if (amMessage.getMessageType().equals(MessageType.AUDIO)) {
            mBuilder.setSmallIcon(R.drawable.small_icon)
                    .setContentTitle(username).setContentText("[语音]");
            mBuilder.setTicker(username + ":" + "发来一段语音");
        } else if (amMessage.getMessageType().equals(MessageType.IMAGE)) {
            mBuilder.setSmallIcon(R.drawable.small_icon)
                    .setContentTitle(username).setContentText("[图片]");
            mBuilder.setTicker(username + ":" + "发来一张图片");
        } else if (amMessage.getMessageType().equals(MessageType.LOCATION)) {
            mBuilder.setSmallIcon(R.drawable.small_icon)
                    .setContentTitle(username).setContentText("[位置]");
            mBuilder.setTicker(username + ":" + "分享了一个位置");
        }
        mBuilder.setAutoCancel(true); // 点击自动消息
        Intent intent = null;
        if (IsVisbleStatus.INVISBLE.getResultCode() == amMessage.getIsVisible()) {
        } else {
            intent = new Intent(mContext, SingleChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userId", amMessage.getUserFrom());
            intent.putExtras(bundle);
        }
        mPendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(mPendingIntent);
        Notification notification = mBuilder.build();
//        notification.setLatestEventInfo(mContext, username, amMessage.getContent(),
//        mPendingIntent);
        // notification.sound
        if (countNum == 0) {
            Constant.tempMill = Constant.currentMill;
            Constant.currentMill = System.currentTimeMillis();
            long tempCount = Constant.currentMill - Constant.tempMill;
            if (tempCount > 2000) {
//                notification.sound = Uri.parse("android.resource://"
//                        + mContext.getPackageName() + "/" + R.raw.notification);
                notification.defaults |= Notification.DEFAULT_SOUND;
            }
        }
//        notification.defaults = Notification.DEFAULT_ALL;
        String deviceMode = SeeLoveApplication.deviceMode;
        if ("X1 7.0".equals(deviceMode)) {
            notification.flags = Notification.FLAG_ONGOING_EVENT;
        } else {
            notification.flags = Notification.FLAG_AUTO_CANCEL;
        }
        if (IsVisbleStatus.INVISBLE.getResultCode() == amMessage.getIsVisible()) {
        } else {
            mNotificationManager.notify(Integer.valueOf(amMessage.getUserFrom()), notification);
        }
    }

    private int getTempId(long tempService, String userId) {
        return (int) (tempService + Long.parseLong(userId));
    }
}
