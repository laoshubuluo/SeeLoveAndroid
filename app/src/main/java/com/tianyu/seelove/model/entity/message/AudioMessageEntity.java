package com.tianyu.seelove.model.entity.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.manager.MediaPlayerManager;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.task.InsertMessageTask;
import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.ui.activity.user.UserInfoActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.AsyncTaskUtils;
import com.tianyu.seelove.utils.DateUtils;
import com.tianyu.seelove.utils.DimensionUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.view.dialog.SureDialog;

import java.util.Date;

public class AudioMessageEntity extends MessageEntity {
    private SLAudioMessage audioMessage;
    private SLUser user;
    private SLUser self;
    private AnimationDrawable aduser;
    private AnimationDrawable adfriend;
    ViewHolder viewHolder = null;
    private AnimationDrawable animationDrawable;

    public AudioMessageEntity(SLMessage message) {
        super(message);
        audioMessage = (SLAudioMessage) message;
        user = new UserDaoImpl().getUserByUserId(message.getUserFrom());
        self = new UserDaoImpl().getUserByUserId(AppUtils.getInstance()
                .getUserId());
    }

    @Override
    protected View inflateView(Context context) {
        if (audioMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            return LayoutInflater.from(context).inflate(
                    R.layout.chatting_item_msg_text_right, null);
        } else {
            return LayoutInflater.from(context).inflate(
                    R.layout.chatting_item_msg_text_left, null);
        }
    }

    @Override
    public View getConvertView(final Context context, View convertView,
                               ViewGroup viewGroup) {
        convertView = inflateView(context);
        viewHolder = new ViewHolder();
        viewHolder.setUsername((TextView) convertView
                .findViewById(R.id.mc_tv_username));
        viewHolder.setContent((TextView) convertView
                .findViewById(R.id.mc_tv_chatcontent));
        viewHolder.setCreateTime((TextView) convertView
                .findViewById(R.id.mc_tv_sendtime));
        viewHolder.setContentLength((TextView) convertView
                .findViewById(R.id.mc_tv_time));
        viewHolder.setHeader((ImageView) convertView
                .findViewById(R.id.mc_iv_userhead));
        viewHolder.setProgress((ProgressBar) convertView
                .findViewById(R.id.mc_progressbar));
        viewHolder.setSendFail((TextView) convertView
                .findViewById(R.id.mc_sendfail));
        viewHolder.setImageAuth((ImageView) convertView
                .findViewById(R.id.user_recognise));
        convertView.setTag(viewHolder);
        if (audioMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            viewHolder.setFrameUser((ImageView) convertView
                    .findViewById(R.id.ivuser));
        } else {
            viewHolder.setFrameFriend((ImageView) convertView
                    .findViewById(R.id.ivfriend));
        }
        viewHolder.getContent().setMaxWidth(Constant.screenWidth - DimensionUtils.convertDipToPixels(context.getResources(), 100));
        // 设置头像
        if (audioMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            viewHolder.getHeader().setImageResource(R.mipmap.default_head);
            // viewHolder.
            // .setTag(doctor.getHeader());
            if (self != null && self.getHeadUrl() != null
                    && self.getHeadUrl().length() > 0) {
                viewHolder.getHeader().setImageResource(R.mipmap.default_head);
                ImageLoader.getInstance().displayImage(
                        ImageLoaderUtil.getAcceptableUri(self.getHeadUrl()),
                        viewHolder.getHeader(),
                        ImageLoaderUtil.getSmallImageOptions());
            } else {
                viewHolder.getHeader().setImageResource(R.mipmap.default_head);
                viewHolder.getUsername().setVisibility(View.GONE);
            }
            viewHolder.getImageAuth().setVisibility(View.GONE);
        } else {
            viewHolder.getHeader().setImageResource(R.mipmap.default_head);
            if (user.getHeadUrl() != null && user.getHeadUrl().length() > 0) {
                ImageLoader.getInstance().displayImage(
                        ImageLoaderUtil.getAcceptableUri(user.getHeadUrl()),
                        viewHolder.getHeader(),
                        ImageLoaderUtil.getSmallImageOptions());
            } else {
                viewHolder.getHeader().setImageResource(R.mipmap.default_head);
            }
            if (viewHolder.getUsername() != null) {
                viewHolder.getUsername().setVisibility(View.GONE);
            }
            viewHolder.getImageAuth().setVisibility(View.GONE);
        }
        viewHolder.getContent().setText("");
        viewHolder.getContent().setSingleLine();
        int timeL = audioMessage.getAudioLength();
        /**
         * 根据说话的时间改变语音对话框的长度
         */
        if (!audioMessage.getUserTo().equals(
                AppUtils.getInstance().getUserId())) {
            if (null != viewHolder.getFrameUser()) {
                viewHolder.getFrameUser().setVisibility(View.VISIBLE);
            }
            StringBuffer spaceBuffer = new StringBuffer();
            for (int i = 1, len = timeL; i < (len <= 20 ? len : 20); i++) {
                spaceBuffer.append("   ");
            }
            viewHolder.getContent().setText(spaceBuffer.toString());
        } else {
            if (null != viewHolder.getFrameFriend()) {
                viewHolder.getFrameFriend().setVisibility(View.VISIBLE);
            }
            StringBuffer spaceBuffer = new StringBuffer();
            for (int i = 1, len = timeL; i < (len <= 20 ? len : 20); i++) {
                spaceBuffer.append("   ");
            }
            viewHolder.getContent().setText(spaceBuffer.toString());
        }
        viewHolder.getContent().setSingleLine();
        viewHolder.getContentLength().setText(
                audioMessage.getAudioLength() + "'");
        // 设置发送时间
        if (audioMessage.getIsShowTime()) {
            viewHolder.getCreateTime().setVisibility(View.VISIBLE);
            viewHolder.getCreateTime().setText(DateUtils.getFriendlyDate(audioMessage.getTimestamp()));
        } else {
            viewHolder.getCreateTime().setVisibility(View.GONE);
        }
        viewHolder.getContent().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioMessage.getUserFrom().equals(AppUtils.getInstance().getUserId())) {
                    ImageView ivuser = viewHolder.getFrameUser();
                    ivuser.setBackgroundResource(R.drawable.audio_list_r);
                    ivuser.setVisibility(View.VISIBLE);
                    AnimationDrawable aduser = (AnimationDrawable) ivuser.getBackground();
//                    aduser.start();
                    MediaPlayerManager.getInstance().playVoice(audioMessage.getMessageContent(), aduser);
                } else {
                    ImageView ivfriend = viewHolder.getFrameFriend();
                    ivfriend.setBackgroundResource(R.drawable.audio_list_l);
                    ivfriend.setVisibility(View.VISIBLE);
                    AnimationDrawable adfriend = (AnimationDrawable) ivfriend.getBackground();
                    adfriend.start();
                    MediaPlayerManager.getInstance().playVoice(audioMessage.getMessageContent(), adfriend);
                }
            }
        });
        if (audioMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            viewHolder.getSendFail().setTag(audioMessage.getMessageId());
            if (audioMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_SENDSUS) {
                viewHolder.getSendFail().setVisibility(View.INVISIBLE);
                viewHolder.getProgress().setVisibility(View.INVISIBLE);
            }
            if (audioMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_FAIL) {
                viewHolder.getSendFail().setVisibility(View.VISIBLE);
                viewHolder.getProgress().setVisibility(View.INVISIBLE);
                viewHolder.getSendFail().setBackgroundResource(R.mipmap.message_status_fail);
                viewHolder.getSendFail().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final SureDialog sureDialog = new SureDialog(view.getContext());
                        sureDialog.initData("", "重发该消息？");
                        sureDialog.getSureTV().setText("重发");
                        sureDialog.getSureTV().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sureDialog.dismiss();
                                // Step1: 先清除本条消息记录
                                Intent intent = new Intent(Actions.ACTION_DELETE_MESSAGE_BYMESSAGEID);
                                intent.putExtra("messageID", audioMessage.getMessageId());
                                context.sendOrderedBroadcast(intent, null);
                                // Step2: 发送消息
                                sendMessage(audioMessage, context);
                            }
                        });
                        sureDialog.show();
                    }
                });
            }
            if (audioMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_SENDING) {
                viewHolder.getProgress().setVisibility(View.VISIBLE);
                viewHolder.getSendFail().setVisibility(View.INVISIBLE);
            }
        }
        viewHolder.getHeader().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioMessage.getUserFrom().equals(
                        AppUtils.getInstance().getUserId())) {
                    Intent intent = IntentManager.createIntent(v.getContext(), UserInfoActivity.class);
                    intent.putExtra("user", self);
                    v.getContext().startActivity(intent);
                } else {
                    Intent intent = IntentManager.createIntent(v.getContext(), UserInfoActivity.class);
                    intent.putExtra("user", user);
                    ((Activity) context).startActivityForResult(intent, 1);
                }
            }
        });
        return convertView;
    }

    private void sendMessage(SLAudioMessage amMessage, final Context context) {
        final long lastId = System.currentTimeMillis();
        final boolean isGroup;
        SLAudioMessage amAudioMessage = new SLAudioMessage();
        amAudioMessage.setMessageId(String.valueOf(lastId));
        amAudioMessage.setUserFrom(AppUtils.getInstance().getUserId());
        amAudioMessage.setUserTo(amMessage.getUserTo());
        amAudioMessage.setMessageContent(amMessage.getMessageContent());
        amAudioMessage.setAudioLength(amMessage.getAudioLength());
        amAudioMessage.setTimestamp(new Date().getTime());
        amAudioMessage.setIsRead(SLMessage.msgRead);
        amAudioMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
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
            }
        });
        AsyncTaskUtils.execute(insertMessageTask, amAudioMessage);
        // 保存一条Session记录
        SLSession session = new SLSession();
        session.setLastMessageId(String.valueOf(lastId));
        session.setPriority(amAudioMessage.getTimestamp());
        session.setTargetId(amMessage.getUserTo());
        session.setMessageType(amAudioMessage.getMessageType());
        session.setSessionContent(amAudioMessage.getMessageContent());
        SLUser user = new UserDaoImpl().getUserByUserId(amAudioMessage.getUserTo());
        session.setSessionIcon(user.getHeadUrl());
        session.setSessionType(SessionType.CHAT);
        session.setSessionName(user.getNickName());
        SessionDao sessionDao = new SessionDaoImpl();
        sessionDao.addSession(session);
        Intent session_intent = new Intent(Actions.ACTION_SESSION);
        Bundle bundle = new Bundle();
        bundle.putString("targetId", session.getTargetId());
        session_intent.putExtras(bundle);
        context.sendOrderedBroadcast(session_intent, null);
    }
}
