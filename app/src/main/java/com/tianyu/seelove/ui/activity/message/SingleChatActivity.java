package com.tianyu.seelove.ui.activity.message;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.ChatMsgViewAdapter;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.model.entity.message.MessageEntity;
import com.tianyu.seelove.model.entity.message.MessageEntityFactory;
import com.tianyu.seelove.model.entity.message.SLImageMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.IsVisbleStatus;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.PublicMessageSendUtils;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.utils.ViewUtils;
import com.tianyu.seelove.view.MessageSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shisheng.zhao
 * @Description: 单聊Activity
 * @date 2017-04-05 15:15
 */
public class SingleChatActivity extends BaseActivity implements AbsListView.OnScrollListener {
    private TextView titleView;
    private MessageSender messageSender;
    private EditText inputMessage;
    private ListView listView;
    private RelativeLayout pluginBox;
    private List<SLMessage> slMessageList = new ArrayList<SLMessage>();
    private Map<String, MessageEntity> linkedHashMap = new LinkedHashMap<String, MessageEntity>();
    public MessageDao messageDao;
    private ChatMsgViewAdapter mAdapter;
    public MessageBroadcastReceiver messageBroadcastReceiver;
    private UserDao userDao;
    private long userId = 0l;
    private SLUser user;
    private List<SLMessage> tempList = new ArrayList<SLMessage>();
    // ListView 底部View
    private View moreView;
    private TextView btLoad;
    private ProgressBar progressBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        messageDao = new MessageDaoImpl();
        userDao = new UserDaoImpl();
        messageBroadcastReceiver = new MessageBroadcastReceiver();
        userId = getIntent().getLongExtra("userId", 0l);
        user = userDao.getUserByUserId(userId);
        try {
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel((int) userId);
        } catch (Exception ex) {
            LogUtil.e(this.getClass().getName(), ex);
        }
        initView();
        initData();
        messageSender.reset();
    }

    public void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        ImageView leftBtn = (ImageView) findViewById(R.id.leftBtn);
        messageSender = (MessageSender) findViewById(R.id.message_sender);
        inputMessage = (EditText) findViewById(R.id.inputMessage);
        listView = (ListView) findViewById(R.id.message_listview);
        pluginBox = (RelativeLayout) findViewById(R.id.message_plugin_box);
        titleView.setText(StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : String.valueOf(userId));
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(this);
//        messageSender.setTarget(user.getUserId());
//        messageSender.setTargetName(user.getNickName());
        // todo shishengzhao
        messageSender.setTarget(userId);
        messageSender.setTargetName("天宇");
        // 实例化底部布局
        moreView = getLayoutInflater().inflate(R.layout.moredata, null);
        btLoad = (TextView) moreView.findViewById(R.id.bt_load);
        progressBar = (ProgressBar) moreView.findViewById(R.id.pg);
        btLoad.setOnClickListener(this);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm;
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputMessage.getWindowToken(), 0);
                ViewUtils.shutView(pluginBox);
                messageSender.reset();
                return false;
            }
        });
        messageSender.getInputMessage().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    listView.setSelection(listView.getCount() - 1);
                }
                // 这玩意是为了解决一个神奇的bug,点击表情,然后点击输入框表情被顶上去没有隐藏,
                // 哈哈哈,赵捷太牛逼了,这么牛逼的Bug都能测出来
                ViewUtils.shutView(pluginBox);
                messageSender.reset();
                return false;
            }
        });
        messageSender.getBtn_emoji().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    listView.setSelection(listView.getCount() - 1);
                }
                return false;
            }
        });
        messageSender.getBtn_box().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    listView.setSelection(listView.getCount() - 1);
                }
                return false;
            }
        });
    }

    public void initData() {
        slMessageList = messageDao.getMessageByPage(AppUtils.getInstance().getUserId(), userId, 0, 15);
        if (slMessageList.size() < 15) {
            btLoad.setVisibility(View.GONE);
        }
        messageDao.setAllRead(AppUtils.getInstance().getUserId(), user.getUserId());
        // 发送广播 通知MainActivity 重新设置tab 数字标签
        sendBroadcast(new Intent(Actions.MESSAGE_READ_CHANGE));
        for (int i = 0; i < slMessageList.size(); i++) {
            slMessageList = PublicMessageSendUtils.updateMessageIsShowTime(slMessageList, i);
            // 消息发送状态异常处理
            PublicMessageSendUtils.updateMessageSendStatus(slMessageList.get(i));
            linkedHashMap.put(slMessageList.get(i).getMessageId(), MessageEntityFactory.getMessageEntity(slMessageList.get(i)));
        }
        mAdapter = new ChatMsgViewAdapter(this, linkedHashMap);
        // 加上底部View,注意要放在setAdapter方法前
        listView.addHeaderView(moreView);
        listView.setAdapter(mAdapter);
        // 绑定监听器
        listView.setOnScrollListener(this);
        listView.setSelection(listView.getCount() - 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_load: {
                progressBar.setVisibility(View.VISIBLE);// 将进度条可见
                btLoad.setVisibility(View.GONE);// 按钮不可见
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreDate();// 加载更多数据
                        btLoad.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();// 通知listView刷新数据
                        listView.setSelection(tempList.size());
                    }
                }, 2000);
                break;
            }
            case R.id.leftBtn:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Actions.SINGLEMESSAGE_ADD_ACTION);
        intentFilter.addAction(Actions.ACTION_RECEIVER_SINGLE_MESSAGE);
        intentFilter.addAction(Actions.ACTION_UPDATE_MESSAGE_STATUE);
        intentFilter.addAction(Actions.ACTION_DELETE_MESSAGE_BYMESSAGEID);
        intentFilter.addAction(Actions.ACTION_UPDATE_IMGMESSAGE_PROCESS);
        intentFilter.setPriority(1000);
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    private void loadMoreDate() {
        int count = mAdapter.getCount();
        // 每次加载
        tempList = messageDao.getMessageByPage(AppUtils.getInstance().getUserId(), user.getUserId(), count, 15);
        if (tempList.size() < 15) {
            listView.removeHeaderView(moreView);
        }
        slMessageList.addAll(tempList);
        // 根据Collections.sort重载方法来实现
        Collections.sort(slMessageList, new Comparator<SLMessage>() {
            @Override
            public int compare(SLMessage b1, SLMessage b2) {
                return (int) b1.getTimestamp() - (int) b2.getTimestamp();
            }
        });
        linkedHashMap.clear();
        for (int i = 0; i < slMessageList.size(); i++) {
            slMessageList = PublicMessageSendUtils.updateMessageIsShowTime(slMessageList, i);
            // 消息发送状态异常处理
            PublicMessageSendUtils.updateMessageSendStatus(slMessageList.get(i));
            linkedHashMap.put(slMessageList.get(i).getMessageId(),
                    MessageEntityFactory.getMessageEntity(slMessageList.get(i)));
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        switch (scrollState) { // 当不滚动时
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 判断滚动到底部
                if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {

                } // 判断滚动到顶部
                if (listView.getFirstVisiblePosition() == 0) {
                    progressBar.setVisibility(View.VISIBLE);// 将进度条可见
                    btLoad.setVisibility(View.GONE);// 按钮不可见
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadMoreDate();// 加载更多数据
                            btLoad.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();// 通知listView刷新数据
                            listView.setSelection(tempList.size());
                        }
                    }, 2000);
                }
                break;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {

    }

    public class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /** 接收到消息和发送消息刷新UI */
            if (intent.getAction().equals(Actions.ACTION_RECEIVER_SINGLE_MESSAGE) ||
                    intent.getAction().equals(Actions.SINGLEMESSAGE_ADD_ACTION)) {
                boolean isShow = false;
                String MeeageID = intent.getStringExtra("messageID");
                SLMessage message = new MessageDaoImpl().getMessageById(MeeageID);
                if (IsVisbleStatus.INVISBLE.getResultCode() != message.getIsVisible()) {
                    if (message.getUserFrom() == AppUtils.getInstance().getUserId() && message.getUserTo() == userId) {
                        messageDao.updateMessageIsReadByMessageId(message.getMessageId());
                        MessageEntity messageEntity = MessageEntityFactory.getMessageEntity(message);
                        mAdapter.addData(message.getMessageId(), messageEntity);
                        mAdapter.notifyDataSetChanged();
                        listView.setSelection(listView.getCount() - 1);
                        isShow = true;
                    } else if (message.getUserTo() == AppUtils.getInstance().getUserId() && message.getUserFrom() == userId) {
                        messageDao.updateMessageIsReadByMessageId(message.getMessageId());
                        MessageEntity messageEntity = MessageEntityFactory.getMessageEntity(message);
                        mAdapter.addData(message.getMessageId(), messageEntity);
                        mAdapter.notifyDataSetChanged();
                        listView.setSelection(listView.getCount() - 1);
                        isShow = true;
                    }
                    if (intent.getAction().equals(Actions.ACTION_RECEIVER_SINGLE_MESSAGE) && isShow) {
                        abortBroadcast();
                    }
                }
            }
            /** 刷新消息状态 */
            if (intent.getAction().equals(Actions.ACTION_UPDATE_MESSAGE_STATUE)) {
                SLMessage slMessage = null;
                MessageDao messageDao = new MessageDaoImpl();
                String messageID = intent.getStringExtra("MessageID");
                int messageStatus = intent.getIntExtra("MessageStatus", SLMessage.MessagePropertie.MSG_SENDING);
                slMessage = new MessageDaoImpl().getMessageById(messageID);
                slMessage.setSendStatue(messageStatus);
                slMessageList.add(slMessage);
                if (null != slMessage) {
                    if (slMessageList.size() == 1) {
                        slMessage.setIsShowTime(true);
                    } else {
                        long tempTime = slMessageList.get(slMessageList.size() - 2).getTimestamp();
                        long currentTime = slMessage.getTimestamp();
                        long currentTemp = ((currentTime - tempTime) / 1000) / 60;
                        if (currentTemp > 5) {
                            slMessage.setIsShowTime(true);
                        } else {
                            slMessage.setIsShowTime(false);
                        }
                    }
                }
                if (messageStatus == SLMessage.MessagePropertie.MSG_SENDSUS) {
                    messageDao.updateSendStatueSuccessByMessageId(messageID);
                    slMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDSUS);
                    if (!slMessage.getMessageType().equals(MessageType.IMAGE)) {
                        mAdapter.cleanData(slMessage.getMessageId(),
                                MessageEntityFactory
                                        .getMessageEntity(slMessage));
                        mAdapter.notifyDataSetChanged();
                    }
                    listView.setSelection(listView.getCount() - 1);
                } else if (messageStatus == SLMessage.MessagePropertie.MSG_FAIL) {
                    messageDao.updateSendStatueFailByMessageId(messageID);
                    slMessage.setSendStatue(SLMessage.MessagePropertie.MSG_FAIL);
                    mAdapter.cleanData(slMessage.getMessageId(),
                            MessageEntityFactory
                                    .getMessageEntity(slMessage));
                    mAdapter.notifyDataSetChanged();
                    listView.setSelection(listView.getCount() - 1);
                } else if (messageStatus == SLMessage.MessagePropertie.MSG_SENDING) {
                }
            }
            /** 更新图片消息的上传进度*/
            if (intent.getAction().equals(Actions.ACTION_UPDATE_IMGMESSAGE_PROCESS)) {
                SLMessage slMessage = null;
                String messageID = intent.getStringExtra("MessageID");
                String ProcessCount = intent.getStringExtra("ProcessCount");
                SLImageMessage slImageMessage = (SLImageMessage) new MessageDaoImpl().getMessageById(messageID);
                slImageMessage.setSendProcess(ProcessCount);
                mAdapter.cleanData(slImageMessage.getMessageId(),
                        MessageEntityFactory
                                .getMessageEntity(slImageMessage));
                mAdapter.notifyDataSetChanged();
            }
            /** 删除消息 */
            if (intent.getAction().equals(Actions.ACTION_DELETE_MESSAGE_BYMESSAGEID)) {
                MessageDao messageDao = new MessageDaoImpl();
                String messageID = intent.getStringExtra("messageID");
                SLMessage amMessageDelete = messageDao.getMessageById(messageID);
                // 同步clean list下的object
                Iterator iterator = slMessageList.iterator();
                while (iterator.hasNext()) {
                    SLMessage tempobj = (SLMessage) iterator.next();
                    if (tempobj.getMessageId().equals(amMessageDelete.getMessageId())) {
                        //移除当前的对象
                        iterator.remove();
                    }
                }
                mAdapter.deleteData(messageID);
                mAdapter.notifyDataSetChanged();
                listView.setSelection(listView.getCount() - 1);
                messageDao.deleteMessageByMessageId(messageID);
                // todo 删除消息之后需要发送广播通知会话列表更新最新内容 shisheng.zhao
                SessionDao sessionDao = new SessionDaoImpl();
                List<SLMessage> list = messageDao.getMessageByPage(AppUtils.getInstance().getUserId(), user.getUserId(), 0, 1);
                SLMessage slMessage = list.get(0);
                sessionDao.updateSessionContent(slMessage.getMessageContent(), userId);
                Intent session_intent = new Intent(Actions.ACTION_SESSION);
                Bundle bundle = new Bundle();
                bundle.putLong("targetId", userId);
                session_intent.putExtras(bundle);
                sendOrderedBroadcast(session_intent, null);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopVoice();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != messageBroadcastReceiver) {
            unregisterReceiver(messageBroadcastReceiver);
        }
    }
}
