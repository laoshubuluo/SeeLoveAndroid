package com.tianyu.seelove.ui.activity.message;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.ChatMsgViewAdapter;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.injection.ControlInjection;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.message.MessageEntity;
import com.tianyu.seelove.model.entity.message.MessageEntityFactory;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.IsVisbleStatus;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.ui.activity.system.MainActivity;
import com.tianyu.seelove.utils.AppUtils;
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
 * @date 2015-09-01 下午17:34:21
 */
public class SingleChatActivity extends BaseActivity implements AbsListView.OnScrollListener {
    @ControlInjection(R.id.titleView)
    private TextView titleView;
    @ControlInjection(R.id.message_sender)
    private MessageSender messageSender;
    @ControlInjection(R.id.et_sendmessage1)
    private EditText mEditTextContent;
    @ControlInjection(R.id.message_listview)
    private ListView listView;
    @ControlInjection(R.id.message_plugin_box)
    private RelativeLayout pluginbox;
    private List<SLMessage> list = new ArrayList<SLMessage>();
    private Map<String, MessageEntity> listdata = new LinkedHashMap<String, MessageEntity>();
    public MessageDao messageDao;
    private ChatMsgViewAdapter mAdapter;
    public MessageBroadcastReceiver broadcast;
    private UserDao userDao;
    private String user_id;
    private String user_self;
    private SLUser user;
    private List<SLMessage> tempList = new ArrayList<SLMessage>();
    // ListView 底部View
    private View moreView;
    private TextView bt;
    private ProgressBar pg;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        messageDao = new MessageDaoImpl();
        userDao = new UserDaoImpl();
        broadcast = new MessageBroadcastReceiver();
        user_id = getIntent().getStringExtra("user_id");
        user = userDao.getUserByUid(user_id);
        user_self = AppUtils.getInstance().getUserId();
        try {
            notificationManager.cancel(Integer.valueOf(user_id));
        } catch (Exception e) {
        }
        initView();
        initData();
        messageSender.reset();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Actions.ACTION_GROUP_MESSAGE_RCODE);
        intentFilter.addAction(Actions.SINGLEMESSAGE_ADD_ACTION);
        intentFilter.addAction(Actions.ACTION_RECEIVER_SINGLE_MESSAGE);
        intentFilter.addAction(Actions.ACTION_UPDATE_MESSAGE_STATUE);
        intentFilter.addAction(Actions.ACTION_DELETE_MESSAGE_BYMESSAGEID);
        intentFilter.addAction(Actions.ACTION_UPDATE_IMGMESSAGE_PROCESS);
        intentFilter.setPriority(1000);
        registerReceiver(broadcast, intentFilter);
    }

    public void initView() {
        titleView.setText(user.getNickName());
        messageSender.setTarget(String.valueOf(user.getUserId()));
        messageSender.setTargetName(user.getNickName());
        // 实例化底部布局
        moreView = getLayoutInflater().inflate(R.layout.moredata, null);
        bt = (TextView) moreView.findViewById(R.id.bt_load);
        pg = (ProgressBar) moreView.findViewById(R.id.pg);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm;
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditTextContent.getWindowToken(),
                        0);
                ViewUtils.shutView(pluginbox);
                messageSender.reset();
                return false;
            }
        });
        messageSender.getMessageET().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    listView.setSelection(listView.getCount() - 1);
                }
                // 这玩意是为了解决一个神奇的bug,点击表情,然后点击输入框表情被顶上去没有隐藏,
                // 哈哈哈,赵捷太牛逼了,这么牛逼的Bug都能测出来
                ViewUtils.shutView(pluginbox);
                messageSender.reset();
                return false;
            }
        });
        messageSender.getToggle_emoji_btn().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    listView.setSelection(listView.getCount() - 1);
                }
                return false;
            }
        });
        messageSender.getBtn_media().setOnTouchListener(new View.OnTouchListener() {
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
        list = messageDao.getMessageByPage(AppUtils.getInstance().getUserId(),
                String.valueOf(user.getUserId()), 0, 15);
        if (list.size() < 15) {
            bt.setVisibility(View.GONE);
        }
        // 根据Collections.sort重载方法来实现
//        Collections.sort(list, new Comparator<AMMessage>() {
//            @Override
//            public int compare(AMMessage b1, AMMessage b2) {
//                return (int) b1.getTimestamp() - (int) b2.getTimestamp();
//            }
//        });
        messageDao.setAllRead(AppUtils.getInstance().getUserId(), String.valueOf(user.getUserId()));
        // 发送广播 通知MainActivity 重新设置tab 数字标签
        sendBroadcast(new Intent(Actions.MESSAGE_READ_CHANGE));
        for (int i = 0; i < list.size(); i++) {
            list = PublicMessageSendUtils.updateMessageIsShowTime(list, i);
            // 消息发送状态异常处理
            PublicMessageSendUtils.updateMessageSendStatus(list.get(i));
            listdata.put(list.get(i).getMessageId(),
                    MessageEntityFactory.getMessageEntity(list.get(i)));
        }
        mAdapter = new ChatMsgViewAdapter(this, listdata);
        // 加上底部View,注意要放在setAdapter方法前
        listView.addHeaderView(moreView);
        listView.setAdapter(mAdapter);
        // 绑定监听器
        listView.setOnScrollListener(this);
        listView.setSelection(listView.getCount() - 1);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pg.setVisibility(View.VISIBLE);// 将进度条可见
                bt.setVisibility(View.GONE);// 按钮不可见
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreDate();// 加载更多数据
                        bt.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();// 通知listView刷新数据
                        listView.setSelection(tempList.size());
                    }
                }, 2000);
            }
        });
    }

    private void loadMoreDate() {
        int count = mAdapter.getCount();
        // 每次加载
        tempList = messageDao.getMessageByPage(AppUtils.getInstance().getUserId(),
                String.valueOf(user.getUserId()), count, 15);
        if (tempList.size() < 15) {
            listView.removeHeaderView(moreView);
        }
        list.addAll(tempList);
        // 根据Collections.sort重载方法来实现
        Collections.sort(list, new Comparator<SLMessage>() {
            @Override
            public int compare(SLMessage b1, SLMessage b2) {
                return (int) b1.getTimestamp() - (int) b2.getTimestamp();
            }
        });
        listdata.clear();
        for (int i = 0; i < list.size(); i++) {
            list = PublicMessageSendUtils.updateMessageIsShowTime(list, i);
            // 消息发送状态异常处理
            PublicMessageSendUtils.updateMessageSendStatus(list.get(i));
            listdata.put(list.get(i).getMessageId(),
                    MessageEntityFactory.getMessageEntity(list.get(i)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        messageSender.notifyResultChanged(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String resultType = data.getStringExtra("resultCode");
        switch (requestCode) {
            case 1:
                if (resultType.equals("close")) {
                    finish();
                }
                break;
            default:
                break;
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
                    pg.setVisibility(View.VISIBLE);// 将进度条可见
                    bt.setVisibility(View.GONE);// 按钮不可见
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadMoreDate();// 加载更多数据
                            bt.setVisibility(View.VISIBLE);
                            pg.setVisibility(View.GONE);
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
                    if (message.getUserFrom().equals(user_self) && message.getUserTo().equals(user_id)) {
                        messageDao.updateMessageIsReadByMessageId(message.getMessageId());
                        MessageEntity messageEntity = MessageEntityFactory.getMessageEntity(message);
                        mAdapter.addData(message.getMessageId(), messageEntity);
                        mAdapter.notifyDataSetChanged();
                        listView.setSelection(listView.getCount() - 1);
                        isShow = true;
                    } else if (message.getUserTo().equals(user_self) && message.getUserFrom().equals(user_id)) {
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
                SLMessage amMessage = null;
                MessageDao messageDao = new MessageDaoImpl();
                String messageID = intent.getStringExtra("MessageID");
                int messageStatus = intent.getIntExtra("MessageStatus", SLMessage.MessagePropertie.MSG_SENDING);
                amMessage = new MessageDaoImpl().getMessageById(messageID);
                amMessage.setState(messageStatus);
                list.add(amMessage);
                if (null != amMessage) {
                    if (list.size() == 1) {
                        amMessage.setIsShowTime(true);
                    } else {
                        long tempTime = list.get(list.size() - 2).getTimestamp();
                        long currentTime = amMessage.getTimestamp();
                        long currentTemp = ((currentTime - tempTime) / 1000) / 60;
                        if (currentTemp > 5) {
                            amMessage.setIsShowTime(true);
                        } else {
                            amMessage.setIsShowTime(false);
                        }
                    }
                }
                if (messageStatus == SLMessage.MessagePropertie.MSG_SENDSUS) {
                    messageDao.markAsSusMsg(messageID);
                    amMessage.setState(SLMessage.MessagePropertie.MSG_SENDSUS);
                    if (!amMessage.getMessageType().equals(MessageType.IMAGE)) {
                        mAdapter.cleanData(amMessage.getMessageId(),
                                MessageEntityFactory
                                        .getMessageEntity(amMessage));
                        mAdapter.notifyDataSetChanged();
                    }
                    listView.setSelection(listView.getCount() - 1);
                } else if (messageStatus == SLMessage.MessagePropertie.MSG_FAIL) {
                    messageDao.markAsFailedMsg(messageID);
                    amMessage.setState(SLMessage.MessagePropertie.MSG_FAIL);
                    mAdapter.cleanData(amMessage.getMessageId(),
                            MessageEntityFactory
                                    .getMessageEntity(amMessage));
                    mAdapter.notifyDataSetChanged();
                    listView.setSelection(listView.getCount() - 1);
                } else if (messageStatus == SLMessage.MessagePropertie.MSG_SENDING) {

//                    Toast.makeText(getApplicationContext(), "消息正在发送...",
//                            Toast.LENGTH_LONG).show();
                }
            }
            /** 更新图片消息的上传进度*/
            if (intent.getAction().equals(Actions.ACTION_UPDATE_IMGMESSAGE_PROCESS)) {
                SLMessage amMessage = null;
                String messageID = intent.getStringExtra("MessageID");
                String ProcessCount = intent.getStringExtra("ProcessCount");
                amMessage = new MessageDaoImpl().getMessageById(messageID);
//                amMessage.setProcessCount(ProcessCount);
                mAdapter.cleanData(amMessage.getMessageId(),
                        MessageEntityFactory
                                .getMessageEntity(amMessage));
                mAdapter.notifyDataSetChanged();
            }
            /** 删除消息 */
            if (intent.getAction().equals(Actions.ACTION_DELETE_MESSAGE_BYMESSAGEID)) {
                MessageDao messageDao = new MessageDaoImpl();
                String messageID = intent.getStringExtra("messageID");
                SLMessage amMessageDelete = messageDao.getMessageById(messageID);
                // 同步clean list下的object
                Iterator iterator = list.iterator();
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
                messageDao.deleteMessageById(messageID);
                // todo 删除消息之后需要发送广播通知会话列表更新最新内容 shisheng.zhao
                SessionDao sessionDao = new SessionDaoImpl();
                List<SLMessage> list = messageDao.getMessageByPage(AppUtils.getInstance().getUserId(),
                        String.valueOf(user.getUserId()), 0, 1);
                SLMessage amMessage = list.get(0);
                sessionDao.updateSessionContent(amMessage.getContent(), user_id);
                Intent session_intent = new Intent(Actions.ACTION_SESSION);
                Bundle bundle = new Bundle();
                bundle.putString("targetId", user_id);
                session_intent.putExtras(bundle);
                sendOrderedBroadcast(session_intent, null);
            }
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = IntentManager.createIntent(SingleChatActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
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
        unregisterReceiver(broadcast);
    }
}
