package com.tianyu.seelove.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.model.entity.message.SLAudioMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.entity.message.SLTextMessage;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.task.InsertMessageTask;
import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.AsyncTaskUtils;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.SoundMeter;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.utils.ViewUtils;
import com.tianyu.seelove.view.dialog.VolumnDialog;
import com.tianyu.seelove.view.messageplugin.ImojiMessagePlugin;
import com.tianyu.seelove.view.messageplugin.MessagePlugin;
import com.tianyu.seelove.view.messageplugin.PluginManager;
import java.util.Date;

/**
 * @author shisheng.zhao
 * @Description: 自定义Message Sender View
 * @date 2017-04-05 16:42
 */
public class MessageSender extends RelativeLayout implements OnClickListener {
    private static final int POLL_INTERVAL = 300;// 音量获取时间间隔
    private String target; // 聊天对象
    private String targetName; // 聊天对象名称

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
        pluginManager.setTarget(target);
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    private SoundPool sp;// 声明一个SoundPool
    private int music;// 定义一个整型用load（）；来设置suondID
    private View view;
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 输入框
     */
    public EditText inputMessage;
    public ImageView btn_emoji, btn_box;
    /**
     * 是否正在录音状态
     */
    private boolean recordingMode = false;
    private SoundMeter mSensor = new SoundMeter();
    VolumnDialog dialog = null;
    PluginManager pluginManager = null;
    RelativeLayout pluginbox;

    private float touchDownY = 0;
    private float touchUpY = 0;

    public void reset() {
        ViewUtils.shutView(pluginbox);
    }

    public MessageSender(Context context) {
        super(context);
        this.context = context;
    }

    public MessageSender(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MessageSender(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onCreate();
    }

    private void onCreate() {
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inputMessage:
                // 表情按钮点击的时候，展开或者关闭下部的扩展框，然后模拟点击表情按钮，使表情显示出来
                ViewUtils.shutView(pluginbox);
                break;
            case R.id.btn_send:// 如果输入框中有内容，则直接发送
                send();
                break;
            case R.id.btn_switch_to_voice:// 语音切换按钮
                switchToVoice();
                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                if (isOpen) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                }
                break;
            case R.id.btn_voice_changebtn:
                switchToText();
                break;
        }
    }

    /**
     * 切换到文字
     */
    private void switchToText() {
        RelativeLayout voiceSender = (RelativeLayout) findViewById(R.id.voiceSender);
        voiceSender.setVisibility(View.GONE);
        RelativeLayout textSender = (RelativeLayout) findViewById(R.id.textSender);
        textSender.setVisibility(View.VISIBLE);
        recordingMode = false;
    }

    /**
     * 切换到录音
     */
    private void switchToVoice() {
        recordingMode = true;
        RelativeLayout voiceSender = (RelativeLayout) findViewById(R.id.voiceSender);
        voiceSender.setVisibility(View.VISIBLE);
        RelativeLayout textSender = (RelativeLayout) findViewById(R.id.textSender);
        textSender.setVisibility(View.GONE);
        if (pluginbox.getVisibility() == View.VISIBLE) {
            ViewUtils.toggleView(pluginbox);
        }
    }

    /**
     * 发送消息
     */
    private void send() {
        String content = inputMessage.getText().toString();
        if (content.length() > 0) {
            final long lastId = System.currentTimeMillis();
            SLTextMessage message = new SLTextMessage();
            message.setMessageId(String.valueOf(lastId));
            message.setUserFrom(AppUtils.getInstance().getUserId());
            message.setUserTo(target);
            message.setMessageContent(content);
            message.setTimestamp(new Date().getTime());
            message.setIsRead(SLMessage.msgRead);
            message.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
            InsertMessageTask insertMessageTask = new InsertMessageTask();
            insertMessageTask.setOnPostExecuteHandler(new BaseTask.OnPostExecuteHandler<Boolean>() {
                @Override
                public void handle(Boolean result) {
                    // 发送融云广播
                    Intent send_Intent = new Intent(Actions.ACTION_SNED_SINGLE_MESSAGE);
                    send_Intent.putExtra("MessageID", String.valueOf(lastId));
                    send_Intent.putExtra("chatType", "single");
                    context.sendOrderedBroadcast(send_Intent, null);
                    // 本地会话广播
                    Intent intent = new Intent(Actions.SINGLEMESSAGE_ADD_ACTION);
                    intent.putExtra("messageID", String.valueOf(lastId));
                    context.sendOrderedBroadcast(intent, null);
                    inputMessage.getText().clear();
                }
            });
            AsyncTaskUtils.execute(insertMessageTask, message);
            inputMessage.getText().clear();
            // 保存一条Session记录
            SLSession session = new SLSession();
            session.setLastMessageId(String.valueOf(lastId));
            session.setPriority(message.getTimestamp());
            session.setTargetId(target);
            session.setMessageType(message.getMessageType());
            session.setSessionContent(message.getMessageContent());
            SLUser user = new UserDaoImpl().getUserByUid(target);
            session.setSessionIcon(user.getHeadUrl());
            session.setSessionType(SessionType.CHAT);
            session.setSessionName(getTargetName());
            new SessionDaoImpl().addSession(session);
            Intent session_intent = new Intent(Actions.ACTION_SESSION);
            Bundle bundle = new Bundle();
            bundle.putString("targetId", session.getTargetId());
            session_intent.putExtras(bundle);
            context.sendOrderedBroadcast(session_intent, null);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        view = View.inflate(context, R.layout.message_sender_view,
                this);
        pluginbox = (RelativeLayout) view.findViewById(R.id.message_plugin_box);
        inputMessage = (EditText) view.findViewById(R.id.inputMessage);
        inputMessage.setSingleLine(false);
        inputMessage.setMaxLines(5);
        inputMessage.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ViewUtils.shutView(pluginbox);
            }
        });
        inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 语音切换按钮
                Button switchToVoiceButton = ((Button) view
                        .findViewById(R.id.btn_switch_to_voice));
                Button sendButton = (Button) findViewById(R.id.btn_send);
                if (s != null && StringUtils.isNotBlank(s.toString())) {
                    sendButton.setVisibility(VISIBLE);
                    switchToVoiceButton.setVisibility(GONE);
                } else {
                    sendButton.setVisibility(GONE);
                    switchToVoiceButton.setVisibility(VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pluginManager = new PluginManager(context, pluginbox, inputMessage, target);
        inputMessage.setOnClickListener(this);
        // 语音切换按钮
        Button switchToVoiceButton = ((Button) view
                .findViewById(R.id.btn_switch_to_voice));
        switchToVoiceButton.setOnClickListener(this);

        Button switchToTextButton = ((Button) view
                .findViewById(R.id.btn_voice_changebtn));
        switchToTextButton.setOnClickListener(this);
        // 语音切换按钮
        Button sendBtn = ((Button) view.findViewById(R.id.btn_send));
        sendBtn.setOnClickListener(this);
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(context, R.raw.play_completed, 1); // 把声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        /** 录音按键 */
        final TextView recordBtn = (TextView) this.findViewById(R.id.btn_voice);
        recordBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchEvent(event);
                // sp.play(music, 1, 1, 0, 0, 1);
                return true;
            }
        });
        // 表情按钮
        btn_emoji = (ImageView) this
                .findViewById(R.id.btn_emoji);
        btn_emoji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当点击表情按钮时,应该将输入框显示出来
                switchToText();
                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                if (isOpen) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                }
                // 表情按钮点击的时候，展开或者关闭下部的扩展框，然后模拟点击表情按钮，使表情显示出来
                if (pluginbox.getVisibility() == View.VISIBLE) {

                } else {
                    ViewUtils.toggleView(pluginbox);
                }
                MessagePlugin emojiPlugin = pluginManager
                        .getPlugin(ImojiMessagePlugin.class.getName());
                emojiPlugin.onEntranceClick();
            }
        });
        btn_box = (ImageView) view.findViewById(R.id.btn_box);
        btn_box.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm;
                imm = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputMessage.getWindowToken(), 0);
                if (pluginbox.getVisibility() == View.VISIBLE) {

                } else {
                    ViewUtils.toggleView(pluginbox);
                }
                pluginManager.resetAllPlugins();
                if (recordingMode) {
                    switchToText();
                }
            }
        });
    }

    long startVoiceT = 0, endVoiceT = 0;

    public boolean touchEvent(MotionEvent event) {
        final Handler mHandler = new Handler();
        Runnable mPollTask = new Runnable() {
            public void run() {
                double amp = mSensor.getAmplitude();
                dialog.setVolumn(amp);
                mHandler.postDelayed(this, POLL_INTERVAL);
            }
        };
        if (recordingMode) {
            if (event.getAction() == MotionEvent.ACTION_DOWN
                    && !mSensor.isRecording()) {
                touchDownY = event.getY();
                try {
                    startVoiceT = System.currentTimeMillis();
                    String voiceName = startVoiceT + ".amr";
                    mSensor.start(voiceName);
                    dialog = new VolumnDialog(context);
                    // 点击其他地方,关闭Dialog
                    dialog.setCancelable(false);
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {

                        }
                    });
                    mHandler.postDelayed(mPollTask, POLL_INTERVAL);
                    mSensor.setRecording(true);
                    sp.play(music, 1, 1, 0, 0, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "请打开录音授权权限", Toast.LENGTH_SHORT).show();
                }
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE && mSensor.isRecording()) {
                touchUpY = event.getY();
                if (touchDownY > touchUpY) { // 代表向上移动
                    if ((touchDownY - touchUpY) >= 250) { // 如果向上滑动距离超过300,视作取消发送
                        dialog.getChanleRecord().setText(getResources().getText(R.string.message_movechanle_audio));
                        dialog.getChanleRecord().setTextColor(Color.RED);
                    } else {
                        dialog.getChanleRecord().setText(getResources().getText(R.string.message_upchanle_audio));
                        dialog.getChanleRecord().setTextColor(Color.WHITE);
                    }
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP
                    && mSensor.isRecording()) {
                mHandler.removeCallbacks(mPollTask);
                final String voicefilepath = mSensor.stop();
                dialog.dismiss();
                endVoiceT = System.currentTimeMillis();
                mSensor.setRecording(false);
                int time = (int) ((endVoiceT - startVoiceT) / 1000);
                if (time < 1) {
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                        }
                    }, 500);
                    return false;
                }
                touchUpY = event.getY();
                LogUtil.i("MessageSend:" + touchDownY + "up:" + touchUpY);
                if (touchDownY <= touchUpY) { // 代表向下移动
                } else if (touchDownY > touchUpY) { // 代表向上移动
                    if ((touchDownY - touchUpY) >= 250) { // 如果向上滑动距离超过300,视作取消发送
                        return false;
                    }
                }
                //语音消息处理
                final long lastId = System.currentTimeMillis();
                SLAudioMessage audioMessage = new SLAudioMessage();
                audioMessage.setMessageId(String.valueOf(lastId));
                audioMessage.setMessageContent(voicefilepath);
                audioMessage.setAudioLength(time);
                audioMessage.setUserFrom(AppUtils.getInstance().getUserId());
                audioMessage.setUserTo(target);
                audioMessage.setTimestamp(new Date().getTime());
                audioMessage.setIsRead(SLMessage.msgRead);
                audioMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
                InsertMessageTask insertMessageTask = new InsertMessageTask();
                insertMessageTask.setOnPostExecuteHandler(new BaseTask.OnPostExecuteHandler<Boolean>() {
                    @Override
                    public void handle(Boolean result) {
                        // 发送融云广播
                        Intent send_Intent = new Intent(Actions.ACTION_SNED_SINGLE_MESSAGE);
                        send_Intent.putExtra("MessageID",
                                String.valueOf(lastId));
                        send_Intent.putExtra("chatType", "single");
                        context.sendOrderedBroadcast(send_Intent, null);
                        // 本地会话广播
                        Intent intent = new Intent(Actions.SINGLEMESSAGE_ADD_ACTION);
                        intent.putExtra("messageID",
                                String.valueOf(lastId));
                        context.sendOrderedBroadcast(intent, null);
                        inputMessage.getText().clear();
                    }
                });
                AsyncTaskUtils.execute(insertMessageTask, audioMessage);
                // 保存本地Session
                SLSession session = new SLSession();
                session.setLastMessageId(String.valueOf(lastId));
                session.setPriority(audioMessage.getTimestamp());
                session.setTargetId(target);
                session.setMessageType(audioMessage.getMessageType());
                session.setSessionContent(audioMessage.getMessageContent());
                session.setSessionType(SessionType.CHAT);
                session.setSessionName(targetName);
                SessionDao sessionDao = new SessionDaoImpl();
                sessionDao.addSession(session);
                Intent session_intent = new Intent(Actions.ACTION_SESSION);
                Bundle bundle = new Bundle();
                bundle.putString("targetId", session.getTargetId());
                session_intent.putExtras(bundle);
                context.sendOrderedBroadcast(session_intent, null);
                // 初始化Sp
                sp.play(music, 1, 1, 0, 0, 1);
            }
        }
        return super.onTouchEvent(event);
    }

    public void notifyResultChanged(int requestCode, int resultCode, Intent data) {
        pluginManager.notifyResultChanged(requestCode, resultCode, data);
        ViewUtils.shutView(pluginbox);
    }

    public void setInputMessage(EditText inputMessage) {
        this.inputMessage = inputMessage;
    }

    public EditText getInputMessage() {
        return inputMessage;
    }

    public void setBtn_emoji(ImageView btn_emoji) {
        this.btn_emoji = btn_emoji;
    }

    public ImageView getBtn_emoji() {
        return btn_emoji;
    }

    public void setBtn_box(ImageView btn_box) {
        this.btn_box = btn_box;
    }

    public ImageView getBtn_box() {
        return btn_box;
    }
}
