package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.ui.activity.message.SingleChatActivity;
import com.tianyu.seelove.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragmengt(交流)
 * @author shisheng.zhao
 * @date 2017-03-29 15:15
 */
public class MessageFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private SessionDao sessionDao;
    private UserDao userDao;
    private List<SLSession> sessionList;
    private List<SLSession> tempSessionList;
    private View view = null;
    private ListView messageList;
    private LinearLayout connectLayout;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("MessageFragment____onCreateView");
        // 防止onCreateView被多次调用
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent)
                parent.removeView(view);
        } else {
            view = inflater.inflate(R.layout.fragment_message, container, false);
            initView(view);
            initData();
        }
        return view;
    }

    private void initView(View view) {
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        messageList = (ListView) view.findViewById(R.id.messageList);
        connectLayout = (LinearLayout) view.findViewById(R.id.network_connect_layout);
        titleView.setText(R.string.message);
        messageList.setOnItemClickListener(this);
        connectLayout.setOnClickListener(this);
    }

    private void initData() {
        sessionList = new ArrayList<SLSession>();
        tempSessionList = sessionDao.getLatestSessions(1000);
        for (SLSession slSession : tempSessionList) {
            if (SessionType.CHAT.equals(slSession.getSessionType())) {
                SLUser slUser = userDao.getUserByUid(slSession.getTargetId());
                if (null != slUser) {
                    slSession.setSessionIcon(slUser.getHeadUrl());
                    slSession.setSessionName(slUser.getNickName());
                }
            }
            sessionList.add(slSession);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        SLSession session = (SLSession) adapterView.getAdapter().getItem(position);
        if (session.getSessionType()
                .equals(SessionType.CHAT)) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), SingleChatActivity.class);
            intent.putExtra("user_id", session
                    .getTargetId());
            startActivity(intent);
            getActivity().overridePendingTransition(
                    R.anim.in_from_right, R.anim.out_to_left);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("MessageFragment____onResume");
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("MessageFragment____onDetach");
    }
}