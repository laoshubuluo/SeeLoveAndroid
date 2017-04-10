package com.tianyu.seelove.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.IsVisbleStatus;

public class NotificationMessageReceiver extends BroadcastReceiver {
    private MessageNotification messageNotification;
    private MessageDaoImpl daoImpl = new MessageDaoImpl();
    private UserDao userDao = new UserDaoImpl();

    @Override
    public void onReceive(Context context, Intent intent) {
        // 单聊Notification
        if (intent.getAction().equals(Actions.ACTION_RECEIVER_SINGLE_MESSAGE)) {
            messageNotification = new MessageNotification(context);
            Bundle bundle = intent.getExtras();
            String messageID = bundle.getString("messageID");
            int countNum = bundle.getInt("messageCount");
            SLMessage amMessage = null;
            if (daoImpl != null) {
                amMessage = daoImpl.getMessageById(messageID);
            }
            try {
                SLUser user = null;
                if (IsVisbleStatus.INVISBLE.getResultCode() == amMessage.getIsVisible()) {
                } else {
                    user = userDao.getUserByUserId(amMessage.getUserFrom());
                    messageNotification.showNotification(amMessage, countNum);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
