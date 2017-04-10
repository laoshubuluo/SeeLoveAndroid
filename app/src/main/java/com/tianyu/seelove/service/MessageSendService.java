package com.tianyu.seelove.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.manager.RongCloudManager;
import com.tianyu.seelove.model.entity.message.SLAudioMessage;
import com.tianyu.seelove.model.entity.message.SLLocationMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.receiver.NotificationMessageReceiver;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.BitmapUtils;
import com.tianyu.seelove.utils.FaceConversionUtils;
import com.tianyu.seelove.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

public class MessageSendService extends Service {
    private MessageSendBroadCastReceiver messageSendBroadCastReceiver =
            new MessageSendBroadCastReceiver();
    private NotificationMessageReceiver receiver = new NotificationMessageReceiver();
    public static final String TAG = "Monitor";
    public static final int MSG_RESET_PRIORITY = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        initJniService();
        AppUtils.getInstance().setJniServiceVersion(false);
        LogUtil.i("----------------------" + "JniService");
        // 初始化表情
        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceConversionUtils.getInstace().getEmojiList(getApplicationContext());
            }
        }).start();
        // 链接融云服务器
        String token = AppUtils.getInstance().getUserToken(); //当前用户token
        RongCloudManager.getInstance().connect(token);
        // 同步加入通讯录群组信息
       /* GroupController groupController = new GroupController(getApplication(), null);
        groupController.getGroupList();*/
        IntentFilter intentFilter = new IntentFilter();
        // 添加消息发送注册广播
        intentFilter.addAction(Actions.ACTION_SNED_SINGLE_MESSAGE);
        intentFilter.addAction(Actions.ACTION_SNED_GROUP_MESSAGE);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        this.registerReceiver(messageSendBroadCastReceiver, intentFilter);
        // 注册通知栏广播监听器---通知欄廣播最好是在登陸成功之後進行註冊,保證數據庫初始化完成
        registerBoradcastReceiver();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i("MessageSendService===onStartCommand");
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public void registerBoradcastReceiver() {
        // 注册通知栏消息显示广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.ACTION_RECEIVER_SINGLE_MESSAGE);
        filter.addAction(Actions.ACTION_RECEIVER_GROUP_MESSAGE);
        registerReceiver(receiver, filter);
    }

    public class MessageSendBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /** 发送消息 */
            if (intent.getAction().equals(Actions.ACTION_SNED_SINGLE_MESSAGE) ||
                    intent.getAction().equals(Actions.ACTION_SNED_GROUP_MESSAGE)) {
                String messageId = intent.getStringExtra("MessageID");
                String chatType = intent.getStringExtra("chatType");
                sendMessage(messageId, chatType);
            }
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                Log.e(TAG, "----------- screen on");
                sendMsg(MSG_RESET_PRIORITY);
            } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                Log.e(TAG, "----------- screen unlock");
                sendMsg(MSG_RESET_PRIORITY);
            }
        }
    }

    private void sendMessage(final String messageId, final String chatType) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                int result = 0;
                UserDao userDao = new UserDaoImpl();
                final SLUser user = userDao.getUserByUserId(AppUtils.getInstance().getUserId());
                MessageDao messageDao = new MessageDaoImpl();
                final SLMessage message = messageDao.getMessageById(messageId);
                try {
                    final UserInfo userInfo = new UserInfo(AppUtils.getInstance().getUserId(),
                            user.getNickName(), Uri.parse(user.getHeadUrl()));
                    // 文字，表情消息
                    if (message.getMessageType() == MessageType.TEXT) {
                        TextMessage textMessage = new TextMessage(message.getMessageContent());
                        textMessage.setUserInfo(userInfo);
                        String pushContent = user.getNickName() + ":" + message.getMessageContent();
                        if (chatType.equals("single")) {
                            RongCloudManager.getInstance().sendMessage(textMessage, Conversation.ConversationType.PRIVATE, message.getUserTo(), pushContent, messageId);
                        }
                    } else if (message.getMessageType() == MessageType.LOCATION) { // 位置消息
                        final SLLocationMessage slLocationMessage = (SLLocationMessage) message;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String base64Image = "";
                                String filePath = message.getMessageContent();
                                base64Image = getImageBase64(filePath);
                                int index = filePath.lastIndexOf("/");
                                String fileName = filePath.substring(index + 1, filePath.length());
                                // 缩略图
                                File imageFileThumb = new File(getCacheDir(), fileName);
                                try {
                                    // 读取图片。
                                    FileInputStream inputStream = new FileInputStream(filePath);
                                    Bitmap bmpSource = BitmapFactory.decodeStream(inputStream);
                                    // 创建缩略图变换矩阵。
                                    Matrix m = new Matrix();
                                    m.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, 400, 400), Matrix.ScaleToFit.CENTER);
                                    // 生成缩略图。
                                    Bitmap bmpThumb = Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), m, true);
                                    imageFileThumb.createNewFile();
                                    FileOutputStream fosThumb = new FileOutputStream(imageFileThumb);
                                    // 保存缩略图。
                                    bmpThumb.compress(Bitmap.CompressFormat.JPEG, 100, fosThumb);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                LocationMessage locationMsg =
                                        LocationMessage.obtain(slLocationMessage.getLat(),
                                                slLocationMessage.getLng(), "", Uri.fromFile(imageFileThumb));
                                locationMsg.setUserInfo(userInfo);
                                String appendExtra = slLocationMessage.getAddress() + "amen" + base64Image;
                                locationMsg.setExtra(appendExtra);
                                String pushContent = user.getNickName() + ":" + "[位置]";
                                if (chatType.equals("single")) {
                                    RongCloudManager.getInstance().sendMessage(locationMsg, Conversation.ConversationType.PRIVATE, message.getUserTo(), pushContent, messageId);
                                }
                            }
                        }).start();
                    } else if (message.getMessageType() == MessageType.AUDIO) { // 语音消息
                        final SLAudioMessage amAudioMessage = (SLAudioMessage) message;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String filePath = message.getMessageContent();
                                    int index = filePath.lastIndexOf("/");
                                    String fileName = filePath.substring(index + 1, filePath.length());
                                    File voiceFile = new File(getCacheDir(), fileName);
                                    try {
                                        // 读取音频文件。
                                        FileInputStream inputStream = new FileInputStream(filePath);
                                        OutputStream os = new FileOutputStream(voiceFile);
                                        byte[] buffer = new byte[1024];
                                        int bytesRead;
                                        // 写入缓存文件。
                                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                                            os.write(buffer, 0, bytesRead);
                                        }
                                        inputStream.close();
                                        os.flush();
                                        os.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    VoiceMessage vocMsg = VoiceMessage.obtain(Uri.fromFile(voiceFile), amAudioMessage.getAudioLength());
                                    vocMsg.setUserInfo(userInfo);
                                    String pushContent = user.getNickName() + ":" + "[语音]";
                                    if (chatType.equals("single")) {
                                        RongCloudManager.getInstance().sendMessage(vocMsg, Conversation.ConversationType.PRIVATE, message.getUserTo(), pushContent, messageId);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else if (message.getMessageType() == MessageType.IMAGE) { // 图片消息
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String filePath = message.getMessageContent();
                                int index = filePath.lastIndexOf("/");
                                String fileNamePath = filePath.substring(index + 1, filePath.length());
                                int indexpath = fileNamePath.lastIndexOf(".");
                                String bFileName = fileNamePath.substring(0, indexpath) + "_b" +
                                        fileNamePath.substring(indexpath, fileNamePath.length());
                                String sFileName = fileNamePath.substring(0, indexpath) + "_s" +
                                        fileNamePath.substring(indexpath, fileNamePath.length());
                                // 原图
                                File imageFileSource = new File(getCacheDir(), bFileName);
                                // 缩略图
                                File imageFileThumb = new File(getCacheDir(), sFileName);
                                try {
                                    //读取图片。
                                    FileInputStream inputStream = new FileInputStream(filePath);
                                    Bitmap bmpSource = BitmapFactory.decodeStream(inputStream);
                                    imageFileSource.createNewFile();
                                    FileOutputStream fosSource = new FileOutputStream(imageFileSource);
                                    // 保存原图。
                                    bmpSource.compress(Bitmap.CompressFormat.PNG, 100, fosSource);
//                                    // 创建缩略图变换矩阵。
//                                    Matrix m = new Matrix();
//                                    m.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, 100, 500), Matrix.ScaleToFit.CENTER);
//                                    // 生成缩略图。
//                                    Bitmap bmpThumb = Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), m, true);
                                    Bitmap bmpThumb = BitmapUtils.getImageFromFileWithHighResolution(filePath, 240, 400);
                                    imageFileThumb.createNewFile();
                                    FileOutputStream fosThumb = new FileOutputStream(imageFileThumb);
                                    // 保存缩略图。
                                    bmpThumb.compress(Bitmap.CompressFormat.PNG, 60, fosThumb);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                new MessageDaoImpl().updateMessageThumUrlByMessageId(messageId, Uri.fromFile(imageFileThumb).toString());
                                ImageMessage imgMsg = ImageMessage.obtain(Uri.fromFile(imageFileThumb), Uri.fromFile(imageFileSource));
                                imgMsg.setUserInfo(userInfo);
                                String pushContent = user.getNickName() + ":" + "[图片]";
                                if (chatType.equals("group")) {
                                    RongCloudManager.getInstance().sendImageMessage(imgMsg, Conversation.ConversationType.GROUP, message.getUserTo(), pushContent, messageId);
                                } else if (chatType.equals("single")) {
                                    RongCloudManager.getInstance().sendImageMessage(imgMsg, Conversation.ConversationType.PRIVATE, message.getUserTo(), pushContent, messageId);
                                }
                            }
                        }).start();
                    }
                } catch (Exception e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (result == 1) {  // 如果result=0代表单边好友发送消息模拟发送成功,刷新消息发送状态
                    Intent send_Intent = new Intent(
                            Actions.ACTION_UPDATE_MESSAGE_STATUE);
                    Bundle bundle = new Bundle();
                    bundle.putInt("MessageStatus", SLMessage.MessagePropertie.MSG_SENDSUS);
                    bundle.putString("MessageID", messageId);
                    send_Intent.putExtras(bundle);
                    sendOrderedBroadcast(send_Intent, null);
                }
            }
        }.execute();
    }

//    private void connRongClient(Context context) {
//        if (StringUtils.isNotBlank(AppUtils.getInstance().getUserToken())) {
//            // 初始化融云SDK
//            RongIMClient.init(context);
//            RongIMClient.ConnectionStatusListener.ConnectionStatus connectionStatus = RongIMClient.getInstance()
//                    .getCurrentConnectionStatus();
//            LogUtil.i("connectionStatus:" + connectionStatus.getMessage());
////            try {
////                RongIMClient.registerMessageType(BibleMessage.class);
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////            RongCloudManager.getInstance().init(context);
////            //链接融云服务器
////            String token = AppUtils.getInstance().getUserToken(); //当前用户token
////            RongCloudManager.getInstance().connect(token);
//        }
//    }

    private String getImageBase64(String imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream inputStream = new FileInputStream(imgPath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(receiver);
            unregisterReceiver(messageSendBroadCastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void initJniService() {
        String userId = getUserSerial();
        Log.e(TAG, "+++++++++++++ Monitor: " + userId);
        startMonitor(userId, "/sdcard/", Constant.childCount, Constant.processDepth);
//        final IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_USER_PRESENT);
//        mBatInfoReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(final Context context, final Intent intent) {
//                String action = intent.getAction();
//                if (Intent.ACTION_SCREEN_ON.equals(action)) {
//                    Log.d(TAG, "----------- screen on");
//                    sendMsg(MSG_RESET_PRIORITY);
//                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
//                    Log.d(TAG, "----------- screen unlock");
//                    sendMsg(MSG_RESET_PRIORITY);
//                }
//            }
//        };
//
//        registerReceiver(mBatInfoReceiver, filter);
    }

    private String getUserSerial() {

        Object userManager = this.getSystemService("user");

        if (userManager == null) {
            Log.e(TAG, "userManager not exsit !!!");
            return "";
        }

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod(
                    "myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(
                    android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod(
                    "getSerialNumberForUser", myUserHandle.getClass());
            long userSerial = (Long) getSerialNumberForUser.invoke(userManager,
                    myUserHandle);
            return String.valueOf(userSerial);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "", e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "", e);
        }

        return "";
    }

    public native String startMonitor(String userId, String workDir, int forkChildCount, int forkGrandSonDepth);

    public native void sendMsg(int msgId);


    /* this is used to load the 'hello-jni' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.example.hellojni/lib/libhello-jni.so at
     * installation time by the package manager.
     */
    static {
        System.loadLibrary("hello-jni");
    }
}
