package com.tianyu.seelove.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.RongCloudErrorCode;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.model.entity.message.SLAudioMessage;
import com.tianyu.seelove.model.entity.message.SLImageMessage;
import com.tianyu.seelove.model.entity.message.SLLocationMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.entity.message.SLTextMessage;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;
import java.util.Date;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 融云通讯能力管理类
 * @author shisheng.zhao
 * @date 2017-03-31 19:54
 */
public class RongCloudManager {
    private static RongCloudManager instance;
    public RongIMClient rongIMClient;
    public Context context;

    private RongCloudManager() {
    }

    //单例模式：同步锁，保证线程安全，双重检查，避免同步锁引起的性能问题
    public static RongCloudManager getInstance() {
        if (instance == null) {
            synchronized (RongCloudManager.class) {
                if (instance == null) {
                    instance = new RongCloudManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context c) {
        this.context = c;
    }

    /**
     * 连接融云服务器
     */
    public void connect(String token) {
        rongIMClient = RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                LogUtil.e("RongCloudManager", "rongcloud connect error: token incorrect");
            }

            @Override
            public void onSuccess(String userId) {
                LogUtil.i("rongcloud connect success: userId: " + userId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtil.e("RongCloudManager", "rongcloud connect error: errorCode: " + errorCode.getValue());
            }
        });
        //绑定消息监听器
        rongIMClient.setOnReceiveMessageListener(onReceiveMessageListener);
        rongIMClient.setConnectionStatusListener(onConnectionStatusListener);
    }

    /**
     * 和融云断开连接
     */
    public void logOut() {
        if (null != rongIMClient)
            rongIMClient.logout();
    }

    public void disconnect() {
        if (null != rongIMClient) {
            rongIMClient.disconnect();
        }
    }

    public RongIMClient.ConnectionStatusListener.ConnectionStatus getConnectStatus() {
        if (null != rongIMClient) {
            return rongIMClient.getCurrentConnectionStatus();
        }
        return null;
    }

    /**
     * 消息监听器
     */
    RongIMClient.OnReceiveMessageListener onReceiveMessageListener = new RongIMClient.OnReceiveMessageListener() {
        @Override
        public boolean onReceived(Message message, int left) {
            //文字消息
            if (message.getContent() instanceof TextMessage) {
                TextMessage msg = (TextMessage) message.getContent();
                SLTextMessage amTextMessage = null;
                long lastId = System.currentTimeMillis();
                amTextMessage = new SLTextMessage();
                amTextMessage.setMessageId(String.valueOf(lastId));
                amTextMessage.setUserFrom(message.getSenderUserId());
                amTextMessage.setUserTo(AppUtils.getInstance().getUserId());
                amTextMessage.setContent(msg.getContent());
                amTextMessage.setTimestamp(new Date().getTime());
                amTextMessage.setIsRead(SLMessage.msg_unread);
                amTextMessage.setState(SLMessage.MessagePropertie.MSG_SENDSUS);
                // 保存本地数据库记录
                MessageDaoImpl messageDao = new MessageDaoImpl();
                messageDao.addMessage(amTextMessage);
                // 群组ID message.getTargetId();
                if (message.getConversationType().getName().equals("private")) { // 接收单聊文本消息
                    Intent single_intent = new Intent(Actions.ACTION_RECEIVER_SINGLE_MESSAGE);
                    Bundle bundle = new Bundle();
                    bundle.putString("messageID", amTextMessage.getMessageId());
                    bundle.putInt("messageCount", left);
                    single_intent.putExtras(bundle);
                    context.sendOrderedBroadcast(single_intent, null);
                }
                SLSession session = new SLSession();
                session.setLastMessageId(amTextMessage.getMessageId());
                session.setPriority(amTextMessage.getTimestamp());
                session.setMessageType(MessageType.TEXT);
                session.setSessionContent(amTextMessage.getContent());
                if (message.getConversationType().getName().equals("private")) {
                    session.setSessionType(SessionType.CHAT);
                    session.setTargetId(amTextMessage.getUserFrom());
                }
                SessionDaoImpl sessionDaoImpl = new SessionDaoImpl();
                sessionDaoImpl.addSession(session);
                Intent session_intent = new Intent(Actions.ACTION_SESSION);
                Bundle bundle = new Bundle();
                bundle.putString("targetId", session.getTargetId());
                session_intent.putExtras(bundle);
                context.sendOrderedBroadcast(session_intent, null);
                // 发送广播 通知MainActivity 重新设置tab 数字标签
                context.sendBroadcast(new Intent(Actions.MESSAGE_READ_CHANGE));
                LogUtil.i("rongcloud receive message[TextMessage]: getContent: " + msg.getContent() + ",getExtra: " + msg.getExtra());
            }
            //位置消息
            else if (message.getContent() instanceof LocationMessage) {
                LocationMessage msg = (LocationMessage) message.getContent();
                SLLocationMessage amLocationMessage = null;
                long lastId = System.currentTimeMillis();
                amLocationMessage = new SLLocationMessage();
                amLocationMessage.setMessageId(String.valueOf(lastId));
                amLocationMessage.setUserFrom(message.getSenderUserId());
                amLocationMessage.setUserTo(AppUtils.getInstance().getUserId());
                try {
                    String[] srts = msg.getExtra().split("amen");
                    String imageBase64 = srts[1];
                    String address = srts[0];
                    amLocationMessage.setContent(imageBase64);
                    amLocationMessage.setAddress(address);
                } catch (Exception e) {
                    amLocationMessage.setAddress(msg.getExtra());
                    amLocationMessage.setContent(msg.getImgUri().toString());
                }
                amLocationMessage.setTimestamp(new Date().getTime());
                amLocationMessage.setLat(msg.getLat());
                amLocationMessage.setLng(msg.getLng());
                amLocationMessage.setIsRead(SLMessage.msg_unread);
                amLocationMessage.setState(SLMessage.MessagePropertie.MSG_SENDSUS);
                // 保存本地数据库记录
                MessageDaoImpl messageDao = new MessageDaoImpl();
                messageDao.addMessage(amLocationMessage);
                // 群组ID message.getTargetId();
                if (message.getConversationType().getName().equals("private")) { // 接收单聊文本消息
                    Intent single_intent = new Intent(Actions.ACTION_RECEIVER_SINGLE_MESSAGE);
                    Bundle bundle = new Bundle();
                    bundle.putString("messageID", amLocationMessage.getMessageId());
                    single_intent.putExtras(bundle);
                    context.sendOrderedBroadcast(single_intent, null);
                }
                SLSession session = new SLSession();
                session.setLastMessageId(amLocationMessage.getMessageId());
                session.setPriority(amLocationMessage.getTimestamp());
                session.setMessageType(MessageType.LOCATION);
                session.setSessionContent(amLocationMessage.getContent());
                if (message.getConversationType().getName().equals("private")) {
                    session.setSessionType(SessionType.CHAT);
                    session.setTargetId(amLocationMessage.getUserFrom());
                }
                SessionDaoImpl sessionDaoImpl = new SessionDaoImpl();
                sessionDaoImpl.addSession(session);
                Intent session_intent = new Intent(Actions.ACTION_SESSION);
                Bundle bundle = new Bundle();
                bundle.putString("targetId", session.getTargetId());
                session_intent.putExtras(bundle);
                context.sendOrderedBroadcast(session_intent, null);
                // 发送广播 通知MainActivity 重新设置tab 数字标签
                context.sendBroadcast(new Intent(Actions.MESSAGE_READ_CHANGE));
                LogUtil.i("rongcloud receive message[LocationMessage]: getContent: " + msg.getImgUri().toString() + ",getExtra: " + msg.getExtra());
            }
            //语音消息
            else if (message.getContent() instanceof VoiceMessage) {
                VoiceMessage msg = (VoiceMessage) message.getContent();
                SLAudioMessage audioMessage = null;
                long lastId = System.currentTimeMillis();
                audioMessage = new SLAudioMessage();
                audioMessage.setMessageId(String.valueOf(lastId));
                audioMessage.setContent(msg.getUri().toString());
                audioMessage.setAudioLength(msg.getDuration());
                audioMessage.setUserFrom(message.getSenderUserId());
                audioMessage.setUserTo(AppUtils.getInstance().getUserId());
                audioMessage.setTimestamp(new Date().getTime());
                audioMessage.setIsRead(SLMessage.msg_unread);
                audioMessage.setState(SLMessage.MessagePropertie.MSG_SENDSUS);
                // 保存本地数据库记录
                MessageDaoImpl messageDao = new MessageDaoImpl();
                messageDao.addMessage(audioMessage);
                if (message.getConversationType().getName().equals("private")) { // 接收单聊文本消息
                    Intent single_intent = new Intent(Actions.ACTION_RECEIVER_SINGLE_MESSAGE);
                    single_intent.putExtra("messageID", audioMessage.getMessageId());
                    context.sendOrderedBroadcast(single_intent, null);
                }
                SLSession session = new SLSession();
                session.setLastMessageId(audioMessage.getMessageId());
                session.setPriority(audioMessage.getTimestamp());
                session.setMessageType(MessageType.AUDIO);
                session.setSessionContent(audioMessage.getContent());
                if (message.getConversationType().getName().equals("private")) {
                    session.setSessionType(SessionType.CHAT);
                    session.setTargetId(audioMessage.getUserFrom());
                }
                SessionDaoImpl sessionDaoImpl = new SessionDaoImpl();
                sessionDaoImpl.addSession(session);
                Intent session_intent = new Intent(Actions.ACTION_SESSION);
                Bundle bundle = new Bundle();
                bundle.putString("targetId", session.getTargetId());
                session_intent.putExtras(bundle);
                context.sendOrderedBroadcast(session_intent, null);
                // 发送广播 通知MainActivity 重新设置tab 数字标签
                context.sendBroadcast(new Intent(Actions.MESSAGE_READ_CHANGE));
                LogUtil.i("rongcloud send message[VoiceMessage]: getUri: " + msg.getUri() + ",getExtra: " + msg.getExtra());
            }
            //图片消息
            else if (message.getContent() instanceof ImageMessage) {
                ImageMessage msg = (ImageMessage) message.getContent();
                SLImageMessage amImageMessage = null;
                long lastId = System.currentTimeMillis();
                amImageMessage = new SLImageMessage();
                amImageMessage.setMessageId(String.valueOf(lastId));
                amImageMessage.setContent(msg.getRemoteUri().toString());
                amImageMessage.setThumUrl(msg.getThumUri().toString());
                amImageMessage.setUserFrom(message.getSenderUserId());
                amImageMessage.setUserTo(AppUtils.getInstance().getUserId());
                amImageMessage.setIsRead(SLMessage.msg_unread);
                amImageMessage.setState(SLMessage.MessagePropertie.MSG_SENDSUS);
                amImageMessage.setTimestamp(new Date().getTime());
                // 保存本地数据库记录
                MessageDaoImpl messageDao = new MessageDaoImpl();
                messageDao.addMessage(amImageMessage);
                if (message.getConversationType().getName().equals("private")) { // 接收单聊文本消息
                    Intent single_intent = new Intent(Actions.ACTION_RECEIVER_SINGLE_MESSAGE);
                    single_intent.putExtra("messageID", amImageMessage.getMessageId());
                    context.sendOrderedBroadcast(single_intent, null);
                }
                SLSession session = new SLSession();
                session.setLastMessageId(amImageMessage.getMessageId());
                session.setPriority(amImageMessage.getTimestamp());
                session.setMessageType(MessageType.IMAGE);
                session.setSessionContent(amImageMessage.getContent());
                if (message.getConversationType().getName().equals("private")) {
                    session.setSessionType(SessionType.CHAT);
                    session.setTargetId(amImageMessage.getUserFrom());
                }
                SessionDaoImpl sessionDaoImpl = new SessionDaoImpl();
                sessionDaoImpl.addSession(session);
                Intent session_intent = new Intent(Actions.ACTION_SESSION);
                Bundle bundle = new Bundle();
                bundle.putString("targetId", session.getTargetId());
                session_intent.putExtras(bundle);
                context.sendOrderedBroadcast(session_intent, null);
                // 发送广播 通知MainActivity 重新设置tab 数字标签
                context.sendBroadcast(new Intent(Actions.MESSAGE_READ_CHANGE));
                LogUtil.i("rongcloud send message[ImageMessage]: getRemoteUri: " + msg.getRemoteUri() + ",getThumUri: " + msg.getThumUri());
            } else {
                LogUtil.i("rongcloud receive message[UnkownType]");
            }
            return false;
        }

    };

    /**
     * 连接状态监听器
     */
    RongIMClient.ConnectionStatusListener onConnectionStatusListener = new RongIMClient.ConnectionStatusListener() {
        @Override
        public void onChanged(ConnectionStatus connectionStatus) {
            switch (connectionStatus) {
//                case CONNECTED://连接成功。
//                    Log.i("connection=====", "CONNECTED");
//                    Intent sucess_intent = new Intent(Actions.CONNECTION_SUCCESS);
//                    context.sendOrderedBroadcast(sucess_intent, null);
//                    break;
//                case DISCONNECTED://断开连接。
//                    Log.i("connection=====", "DISCONNECTED");
//                    Intent faile_intent = new Intent(Actions.CONNECTION_FAILED);
//                    context.sendOrderedBroadcast(faile_intent, null);
//                    break;
//                case CONNECTING://连接中。
//                    Log.i("connection=====", "CONNECTING");
//                    Intent link_intent = new Intent(Actions.CONNECTION_LINKING);
//                    context.sendOrderedBroadcast(link_intent, null);
//                    break;
//                case NETWORK_UNAVAILABLE://网络不可用。
//                    Log.i("connection=====", "NETWORK_UNAVAILABLE");
//                    Intent unavailable_intent = new Intent(Actions.CONNECTION_UNAVAILABLE);
//                    context.sendOrderedBroadcast(unavailable_intent, null);
//                    break;
                case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                    Handler handler = new Handler(context.getMainLooper()) {
                        @Override
                        public void handleMessage(android.os.Message msg) {
                            super.handleMessage(msg);
//                            AppManager.getAppManager().appExitAndCleanup(context, true);
//                            Intent i = new Intent(context, LoginOpenPlatformActivity.class);
//                            i.putExtra("promptContent", context.getString(R.string.offline_by_other_client));
//                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(i);
                        }
                    };
                    handler.sendEmptyMessage(0);
                    break;
            }
        }
    };

    /**
     * 发送消息
     */
    public void sendMessage(final MessageContent message, Conversation.ConversationType type, String targetUserId, String pushContent, final String messageId) {
        if (rongIMClient == null) {
            LogUtil.e("RongCloudManager", "rongcloud send message error: rongIMClient is null ");
            return;
        }
        rongIMClient.sendMessage(type, targetUserId, message, pushContent, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                //发送失败
                LogUtil.e("RongCloudManager", "rongcloud send message error: errorCode: " + errorCode.getValue());
                if (RongCloudErrorCode.RC_NET_CHANNEL_INVALID == errorCode.getValue()) {
                    //链接融云服务器
                    String token = AppUtils.getInstance().getUserToken(); //当前用户token
                    RongCloudManager.getInstance().connect(token);
                }
                Intent send_Intent = new Intent(
                        Actions.ACTION_UPDATE_MESSAGE_STATUE);
                Bundle bundle = new Bundle();
                bundle.putInt("MessageStatus", SLMessage.MessagePropertie.MSG_FAIL);
                bundle.putString("MessageID", messageId);
                send_Intent.putExtras(bundle);
                context.sendOrderedBroadcast(send_Intent, null);
            }

            @Override
            public void onSuccess(Integer integer) {
                if (message instanceof TextMessage) {
                    TextMessage msg = (TextMessage) message;
                    LogUtil.i("rongcloud send message[TextMessage]: getContent: " + msg.getContent() + ",getExtra: " + msg.getExtra());
                } else if (message instanceof ImageMessage) {
                    ImageMessage msg = (ImageMessage) message;
                    LogUtil.i("rongcloud send message[ImageMessage]: getRemoteUri: " + msg.getRemoteUri() +
                            ",getThumUri: " + msg.getThumUri() + ",getLocalUri: " + msg.getLocalUri());
                } else if (message instanceof VoiceMessage) {
                    VoiceMessage msg = (VoiceMessage) message;
                    LogUtil.i("rongcloud send message[VoiceMessage]: getUri: " + msg.getUri() + ",getExtra: " + msg.getExtra());
                } else if (message instanceof LocationMessage) {
                    LocationMessage msg = (LocationMessage) message;
                    LogUtil.i("rongcloud send message[LocationMessage]: getLat: " + msg.getLat());
                    //消息发送成功,通知刷新消息状态
                    Intent send_Intent = new Intent(
                            Actions.ACTION_UPDATE_MESSAGE_STATUE);
                    Bundle bundle = new Bundle();
                    bundle.putInt("MessageStatus", SLMessage.MessagePropertie.MSG_SENDSUS);
                    bundle.putString("MessageID", messageId);
                    send_Intent.putExtras(bundle);
                    context.sendOrderedBroadcast(send_Intent, null);
                }
            }
        });
    }

    /**
     * 发送图片
     * @param message
     * @param type
     * @param targetUserId
     */

    public void sendImageMessage(final MessageContent message, Conversation.ConversationType type, String targetUserId, String pushContent, final String messageId) {
        if (rongIMClient == null) {
            LogUtil.e("RongCloudManager", "rongcloud send message error: rongIMClient is null ");
            return;
        }
        rongIMClient.sendImageMessage(type, targetUserId, message, pushContent, null, new RongIMClient.SendImageMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //保存数据库成功
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //发送失败
                LogUtil.e("RongCloudManager", "rongcloud send message error: errorCode: " + errorCode.getValue());
                if (RongCloudErrorCode.RC_NET_CHANNEL_INVALID == errorCode.getValue()) {
                    //链接融云服务器
                    String token = AppUtils.getInstance().getUserToken(); //当前用户token
                    RongCloudManager.getInstance().connect(token);
                }
                Intent send_Intent = new Intent(
                        Actions.ACTION_UPDATE_MESSAGE_STATUE);
                Bundle bundle = new Bundle();
                bundle.putInt("MessageStatus", SLMessage.MessagePropertie.MSG_FAIL);
                bundle.putString("MessageID", messageId);
                send_Intent.putExtras(bundle);
                context.sendOrderedBroadcast(send_Intent, null);
            }

            @Override
            public void onSuccess(Message message) {
                //消息发送成功,通知刷新消息状态
                Intent send_Intent = new Intent(
                        Actions.ACTION_UPDATE_MESSAGE_STATUE);
                Bundle bundle = new Bundle();
                bundle.putInt("MessageStatus", SLMessage.MessagePropertie.MSG_SENDSUS);
                bundle.putString("MessageID", messageId);
                send_Intent.putExtras(bundle);
                context.sendOrderedBroadcast(send_Intent, null);
            }

            @Override
            public void onProgress(Message message, int i) {
                //发送进度
                //消息发送成功,通知刷新消息状态
                Intent send_Intent = new Intent(
                        Actions.ACTION_UPDATE_IMGMESSAGE_PROCESS);
                Bundle bundle = new Bundle();
                bundle.putString("ProcessCount", i + "");
                bundle.putString("MessageID", messageId);
                send_Intent.putExtras(bundle);
                context.sendOrderedBroadcast(send_Intent, null);
            }
        });
    }
}