package com.tianyu.seelove.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.model.entity.message.SLAudioMessage;
import com.tianyu.seelove.model.entity.message.SLImageMessage;
import com.tianyu.seelove.model.entity.message.SLLocationMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.entity.message.SLTextMessage;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.task.InsertMessageTask;
import com.tianyu.seelove.task.base.BaseTask;

import java.util.Date;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 发送消息工具类--封装一些发送消息的公共方法,后期抽象
 * @date 2015-12-05 下午16:27:24
 */
public class PublicMessageSendUtils {
    private Context context;
    private SLMessage amMessage;

    public PublicMessageSendUtils(Context context) {
        this.context = context;
    }


    public PublicMessageSendUtils(Context context, SLMessage amMessage) {
        this.context = context;
        this.amMessage = amMessage;
    }

    /**
     * 用于处理消息发送过程出现的异常情况--将所有发送中状态重置为发送失败
     * @param slMessage
     */
    public static void updateMessageSendStatus(SLMessage slMessage) {
        if (SLMessage.MessagePropertie.MSG_SENDING == slMessage.getSendStatue()) {
            slMessage.setSendStatue(SLMessage.MessagePropertie.MSG_FAIL);
        }
    }

    public static List<SLMessage> updateMessageIsShowTime(List<SLMessage> list, int i) {
        if (i == 0) {
            list.get(i).setIsShowTime(true);
        } else {
            long tempTime = list.get(i - 1).getTimestamp();
            long currentTime = list.get(i).getTimestamp();
            long currentTemp = ((currentTime - tempTime) / 1000) / 60;
            if (currentTemp > 5) {
                list.get(i).setIsShowTime(true);
            } else {
                list.get(i).setIsShowTime(false);
            }
        }
        return list;
    }

    public void sendMessage(String targetId, final boolean isGroup) {
        final long lastId = System.currentTimeMillis();
        if (amMessage instanceof SLTextMessage) { // 文本消息
            SLTextMessage amTextMessage = (SLTextMessage) amMessage;
            amTextMessage.setMessageId(String.valueOf(lastId));
            amTextMessage.setUserFrom(AppUtils.getInstance().getUserId());
            amTextMessage.setUserTo(targetId);
            amTextMessage.setTimestamp(new Date().getTime());
            amTextMessage.setIsRead(SLMessage.msgRead);
            amTextMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
            InsertMessageTask insertMessageTask = new InsertMessageTask();
            insertMessageTask.setOnPostExecuteHandler(new BaseTask.OnPostExecuteHandler<Boolean>() {
                @Override
                public void handle(Boolean result) {
                    sendMessageBroadcast(context, String.valueOf(lastId), isGroup);
                }
            });
            AsyncTaskUtils.execute(insertMessageTask, amTextMessage);
            // 保存一条Session记录
            SLSession session = new SLSession();
            session.setLastMessageId(String.valueOf(lastId));
            session.setPriority(amTextMessage.getTimestamp());
            session.setTargetId(targetId);
            session.setMessageType(amTextMessage.getMessageType());
            session.setSessionContent(amTextMessage.getMessageContent());
            session.setSessionType(SessionType.CHAT);
            session.setSessionName(targetId);
            SessionDao sessionDao = new SessionDaoImpl();
            sessionDao.addSession(session);
            Intent session_intent = new Intent(Actions.ACTION_SESSION);
            Bundle bundle = new Bundle();
            bundle.putString("targetId", session.getTargetId());
            session_intent.putExtras(bundle);
            context.sendOrderedBroadcast(session_intent, null);
        } else if (amMessage instanceof SLImageMessage) { // 图片消息
            SLImageMessage amImageMessage = (SLImageMessage) amMessage;
            amImageMessage.setMessageId(String.valueOf(lastId));
            amImageMessage.setUserFrom(AppUtils.getInstance().getUserId());
            amImageMessage.setUserTo(targetId);
            amImageMessage.setTimestamp(new Date().getTime());
            amImageMessage.setIsRead(SLMessage.msgRead);
            amImageMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
            InsertMessageTask insertMessageTask = new InsertMessageTask();
            insertMessageTask.setOnPostExecuteHandler(new BaseTask.OnPostExecuteHandler<Boolean>() {
                @Override
                public void handle(Boolean result) {
                    sendMessageBroadcast(context, String.valueOf(lastId), isGroup);
                }
            });
            AsyncTaskUtils.execute(insertMessageTask, amImageMessage);
            // 保存一条Session记录
            SLSession session = new SLSession();
            session.setLastMessageId(String.valueOf(lastId));
            session.setPriority(amImageMessage.getTimestamp());
            session.setTargetId(targetId);
            session.setMessageType(amImageMessage.getMessageType());
            session.setSessionContent(amImageMessage.getMessageContent());
            session.setSessionType(SessionType.CHAT);
            session.setSessionName(targetId);
            SessionDao sessionDao = new SessionDaoImpl();
            sessionDao.addSession(session);
            Intent session_intent = new Intent(Actions.ACTION_SESSION);
            Bundle bundle = new Bundle();
            bundle.putString("targetId", session.getTargetId());
            session_intent.putExtras(bundle);
            context.sendOrderedBroadcast(session_intent, null);
        } else if (amMessage instanceof SLAudioMessage) {// 语音消息
            SLAudioMessage amAudioMessage = (SLAudioMessage) amMessage;
            amAudioMessage.setMessageId(String.valueOf(lastId));
        } else if (amMessage instanceof SLLocationMessage) { // 位置消息
            SLLocationMessage amLocationMessage = (SLLocationMessage) amMessage;
            amLocationMessage.setMessageId(String.valueOf(lastId));
        }
    }


    /**
     * 发送消息广播(包括本地消息和融云消息)
     *
     * @param isGroup
     */
    public static void sendMessageBroadcast(Context context, String messageId, boolean isGroup) {
        // 发送融云广播
        Intent send_Intent = new Intent(Actions.ACTION_SNED_SINGLE_MESSAGE);
        send_Intent.putExtra("MessageID", messageId);
        send_Intent.putExtra("chatType", "single");
        context.sendOrderedBroadcast(send_Intent, null);
        // 本地会话广播
        Intent intent = new Intent(Actions.SINGLEMESSAGE_ADD_ACTION);
        intent.putExtra("messageID", messageId);
        context.sendOrderedBroadcast(intent, null);
    }

    /**
     * 根据时间差,判断是否显示聊天界面消息时间
     *
     * @param lastTime
     * @param nextTime
     * @return
     */
    public static boolean isShowMessageTime(long lastTime, long nextTime) {
        if (lastTime == 0) {
            return true;
        } else {
            if ((nextTime - lastTime) > 1 * 60 * 60 * 1000) {
                return true;
            } else {
                return false;
            }
        }
    }
}
