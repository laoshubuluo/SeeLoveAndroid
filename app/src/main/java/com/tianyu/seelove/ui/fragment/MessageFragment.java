package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.SessionAdapter;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.ui.activity.message.SingleChatActivity;
import com.tianyu.seelove.ui.activity.system.NetworkConnectActivity;
import com.tianyu.seelove.ui.activity.user.UserLoginActivity;
import com.tianyu.seelove.ui.fragment.base.BaseFragment;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.view.dialog.SureDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmengt(交流)
 *
 * @author shisheng.zhao
 * @date 2017-03-29 15:15
 */
public class MessageFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    private TextView titleView;
    private SessionDao sessionDao;
    private SessionReciver reciver;
    private SessionAdapter sessionAdapter;
    private UserDao userDao;
    private MessageDao messageDao;
    private List<SLSession> sessionList;
    private List<SLSession> tempSessionList;
    private View view = null;
    private ListView messageList;
    private LinearLayout connectLayout;
    private boolean isNewAdd = true;
    private View emptyView;
    private TextView errorContent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("MessageFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("MessageFragment____onCreate");
        sessionDao = new SessionDaoImpl();
        userDao = new UserDaoImpl();
        messageDao = new MessageDaoImpl();
        reciver = new SessionReciver();
        sessionAdapter = new SessionAdapter(getActivity(), sessionList);
        initIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("MessageFragment____onCreateView");
        // 防止onCreateView被多次调用
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_message, container, false);
            initView(view);
            if (0l != AppUtils.getInstance().getUserId()) {
                initData();
            }
        }
        return view;
    }

    private void initView(View view) {
        titleView = (TextView) view.findViewById(R.id.titleView);
        messageList = (ListView) view.findViewById(R.id.messageList);
        connectLayout = (LinearLayout) view.findViewById(R.id.network_connect_layout);
        emptyView = view.findViewById(R.id.emptyLayout);
        errorContent = (TextView) emptyView.findViewById(R.id.errorContent);
        emptyView.setVisibility(View.GONE);
        titleView.setText(R.string.message);
        messageList.setOnItemClickListener(this);
        messageList.setOnItemLongClickListener(this);
        messageList.setVisibility(View.VISIBLE);
        connectLayout.setOnClickListener(this);
    }

    private void initData() {
        sessionList = new ArrayList<>();
        tempSessionList = sessionDao.getLatestSessions(1000);
        if (tempSessionList.size() > 0) {
            for (SLSession slSession : tempSessionList) {
                if (SessionType.CHAT.equals(slSession.getSessionType())) {
                    SLUser slUser = userDao.getUserByUserId(slSession.getTargetId());
                    if (null != slUser) {
                        slSession.setSessionIcon(slUser.getHeadUrl());
                        slSession.setSessionName(slUser.getNickName());
                    }
                }
                sessionList.add(slSession);
            }
            sessionAdapter = new SessionAdapter(getActivity(), sessionList);
            messageList.setAdapter(sessionAdapter);
            messageList.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            messageList.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            errorContent.setText(R.string.message_no_data);
        }
    }

    private void initIntent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(1100);
        intentFilter.addAction(Actions.ACTION_EXIT_APP);
        intentFilter.addAction(Actions.ACTION_LOGIN_SUCCESS);
        intentFilter.addAction(Actions.ACTION_SESSION);
        intentFilter.addAction(Actions.ACTION_CLEAN_USER_SESSION);
        intentFilter.addAction(Actions.CONNECTION_FAILED);
        intentFilter.addAction(Actions.CONNECTION_SUCCESS);
        intentFilter.addAction(Actions.CONNECTION_LINKING);
        intentFilter.addAction(Actions.CONNECTION_UNAVAILABLE);
        getActivity().registerReceiver(reciver, intentFilter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.network_connect_layout:
                Intent intent = IntentManager.createIntent(getActivity(), NetworkConnectActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        SLSession session = (SLSession) adapterView.getAdapter().getItem(position);
        if (session.getSessionType().equals(SessionType.CHAT)) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), SingleChatActivity.class);
            intent.putExtra("userId", session.getTargetId());
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final SLSession slSession = (SLSession) adapterView.getAdapter().getItem(position);
        final SureDialog sureDialog = new SureDialog(getActivity());
        sureDialog.initData("", getString(R.string.sure_delete_message));
        sureDialog.getSureTV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionType.CHAT.equals(slSession.getSessionType())) {
                    messageDao.cleanSingalChatMessage(AppUtils.getInstance().getUserId(), slSession.getTargetId());
                    sessionDao.deleteSession(slSession.getTargetId());
                    sessionAdapter.deleteData(slSession);
                    if (sessionAdapter.getSessionsSize() <= 0) {
                        messageList.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        errorContent.setText(R.string.message_no_data);
                    } else {
                        messageList.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                    // 发送广播通知MainActivity重新设置tab数字标签
                    getActivity().sendBroadcast(new Intent(Actions.MESSAGE_READ_CHANGE));
                    sureDialog.dismiss();
                }
            }
        });
        sureDialog.show();
        return true;
    }

    private class SessionReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Actions.ACTION_SESSION)) {
                long targetId = intent.getLongExtra("targetId", 0l);
                updateLastSession(targetId);
            } else if (intent.getAction().equals(Actions.ACTION_CLEAN_USER_SESSION)) {
                long targetId = intent.getLongExtra("userId", 0l);
                SLSession slSession = sessionDao.getSessionByTargetId(targetId);
                // 删除session会话
                sessionDao.deleteSession(targetId);
                if (null != slSession) {
                    sessionAdapter.deleteData(slSession);
                    sessionAdapter.notifyDataSetChanged();
                }
            } else if (intent.getAction().equals(Actions.ACTION_EXIT_APP)) {
                if (null == sessionList) {
                    sessionList = new ArrayList<>();
                }
                sessionList.clear();
                sessionAdapter = new SessionAdapter(getActivity(), sessionList);
                messageList.setAdapter(sessionAdapter);
            } else if (intent.getAction().equals(Actions.ACTION_LOGIN_SUCCESS)) {
                initData();
            }
            if (null != titleView) {
                if (intent.getAction().equals(Actions.CONNECTION_SUCCESS)) {
                    titleView.setTextColor(getResources().getColor(R.color.font_black));
                    titleView.setText(R.string.message);
                    connectLayout.setVisibility(View.GONE);
                } else if (intent.getAction().equals(Actions.CONNECTION_FAILED)) {
                    titleView.setTextColor(getResources().getColor(R.color.font_black));
                    titleView.setText(R.string.message_no_connect);
                    connectLayout.setVisibility(View.VISIBLE);
                } else if (intent.getAction().equals(Actions.CONNECTION_LINKING)) {
                    titleView.setTextColor(getResources().getColor(R.color.font_black));
                    titleView.setText(R.string.message_connecting);
                    connectLayout.setVisibility(View.GONE);
                } else if (intent.getAction().equals(Actions.CONNECTION_UNAVAILABLE)) {
                    titleView.setTextColor(Color.RED);
                    titleView.setText(R.string.message_failed_connect);
                    connectLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void updateLastSession(long targetId) {
        SLSession slSession = sessionDao.getSessionByTargetId(targetId);
        if (null == slSession) {
            return;
        }
        if (SessionType.CHAT.equals(slSession.getSessionType())) {
            SLUser slUser = userDao.getUserByUserId(slSession.getTargetId());
            if (null != slUser) {
                slSession.setSessionIcon(slUser.getHeadUrl());
                slSession.setSessionName(StringUtils.isNotBlank(slUser.getNickName()) ? slUser.getNickName() : String.valueOf(slUser.getUserId()));
                sessionDao.updateSessionName(slUser.getNickName(), String.valueOf(slUser.getUserId()));
            }
        }
        sessionAdapter.addDataSession(slSession, isNewAdd);
        if (sessionAdapter.getSessionsSize() <= 0) {
            messageList.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            errorContent.setText(R.string.message_no_data);
        } else {
            messageList.setAdapter(sessionAdapter);
            messageList.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("MessageFragment____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("MessageFragment____onStart");
        if (0l == AppUtils.getInstance().getUserId() && !Constant.loginActivityIng) {
            Intent intent = IntentManager.createIntent(getActivity(), UserLoginActivity.class);
            startActivityForResult(intent, 0);
            getActivity().overridePendingTransition(R.anim.up_in, R.anim.up_out);
            Toast.makeText(getActivity(), R.string.login_tips, Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("MessageFragment____onResume");
        if (null != sessionAdapter) {
            sessionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("MessageFragment____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("MessageFragment____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("MessageFragment____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("MessageFragment____onDestroy");
        if (null != reciver) {
            getActivity().unregisterReceiver(reciver);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("MessageFragment____onDetach");
    }
}