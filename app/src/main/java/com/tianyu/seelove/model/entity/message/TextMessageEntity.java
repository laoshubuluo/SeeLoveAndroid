package com.tianyu.seelove.model.entity.message;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.task.InsertMessageTask;
import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.ui.activity.user.UserInfoActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.AsyncTaskUtils;
import com.tianyu.seelove.utils.DateUtils;
import com.tianyu.seelove.utils.DimensionUtils;
import com.tianyu.seelove.utils.FaceConversionUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.utils.TextUtils;
import com.tianyu.seelove.view.dialog.SureDialog;
import com.tianyu.seelove.view.pop.ResourcePopupWindow;

import java.util.Date;

/**
 * @author shisheng.zhao
 * @Description: 文字消息
 * @date 2015-09-11 下午17:57:22
 */
public class TextMessageEntity extends MessageEntity {
    private SLTextMessage textMessage;
    private SLUser user;
    private SLUser self;

    public TextMessageEntity(SLMessage message) {
        super(message);
        this.textMessage = (SLTextMessage) message;
        user = new UserDaoImpl().getUserByUserId(message.getUserFrom());
        self = new UserDaoImpl().getUserByUserId(AppUtils.getInstance().getUserId());
    }

    // FIXME:这里等重构完成之后可以改成用ViewCache
    @Override
    public View getConvertView(final Context context, View convertView,
                               ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        convertView = inflateView(context);
        viewHolder = new ViewHolder();
        viewHolder.setUsername((TextView) convertView
                .findViewById(R.id.mc_tv_username));
        viewHolder.setContent((TextView) convertView
                .findViewById(R.id.mc_tv_chatcontent));
        viewHolder.setCreateTime((TextView) convertView
                .findViewById(R.id.mc_tv_sendtime));
        viewHolder.setHeader((ImageView) convertView
                .findViewById(R.id.mc_iv_userhead));
        viewHolder.setProgress((ProgressBar) convertView
                .findViewById(R.id.mc_progressbar));
        viewHolder.setSendFail((TextView) convertView
                .findViewById(R.id.mc_sendfail));
        viewHolder.setImageAuth((ImageView) convertView
                .findViewById(R.id.user_recognise));
        convertView.setTag(viewHolder);
//        viewHolder.getContent().setMaxWidth(Constant.screenWidth - DimensionUtils.convertDipToPixels(context.getResources(), 100));
        // 设置头像 姓名
        if (textMessage.getUserFrom() == AppUtils.getInstance().getUserId()) {
            if (self != null && self.getHeadUrl() != null && self.getHeadUrl().length() > 0) {
                ImageLoader.getInstance().displayImage(ImageLoaderUtil.getAcceptableUri(self.getHeadUrl()), viewHolder.getHeader(), ImageLoaderUtil.getSmallImageOptions());
            }
            viewHolder.getUsername().setVisibility(View.GONE);
            viewHolder.getImageAuth().setVisibility(View.GONE);
        } else {
            if (user != null && user.getHeadUrl() != null && user.getHeadUrl().length() > 0) {
                ImageLoader.getInstance().displayImage(ImageLoaderUtil.getAcceptableUri(user.getHeadUrl()), viewHolder.getHeader(), ImageLoaderUtil.getSmallImageOptions());
            }
            if (viewHolder.getUsername() != null) {
                viewHolder.getUsername().setVisibility(View.GONE);
            }
            viewHolder.getImageAuth().setVisibility(View.GONE);
        }
        // 设置发送时间
        if (textMessage.getIsShowTime()) {
            viewHolder.getCreateTime().setVisibility(View.VISIBLE);
            viewHolder.getCreateTime().setText(DateUtils.getFriendlyDate(textMessage.getTimestamp()));
        } else {
            viewHolder.getCreateTime().setVisibility(View.GONE);
        }
        viewHolder.getContent().setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        viewHolder.getContent().clearComposingText();
        viewHolder.getContent().setText("");
        viewHolder.getContent().setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString spannableString = FaceConversionUtils.getInstace().getExpressionString(context, TextUtils.StringFilter(textMessage.getMessageContent()), false);
        viewHolder.getContent().setText(spannableString);
        if (textMessage.getUserFrom() == AppUtils.getInstance().getUserId()) {
            viewHolder.getSendFail().setTag(textMessage.getMessageId());
            if (textMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_SENDSUS) {
                viewHolder.getSendFail().setVisibility(View.INVISIBLE);
                viewHolder.getProgress().setVisibility(View.INVISIBLE);
            }
            if (textMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_FAIL) {
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
                                intent.putExtra("messageID", textMessage.getMessageId());
                                context.sendOrderedBroadcast(intent, null);
                                // Step2: 发送消息
                                sendMessage(textMessage, context);
                            }
                        });
                        sureDialog.show();
                    }
                });
            }
            if (textMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_SENDING) {
                viewHolder.getProgress().setVisibility(View.VISIBLE);
                viewHolder.getSendFail().setVisibility(View.INVISIBLE);
            }
        }
        viewHolder.getHeader().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textMessage.getUserFrom() ==
                        AppUtils.getInstance().getUserId()) {
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
        // 文本消息长按复制,转发
        viewHolder.getContent().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final ResourcePopupWindow rPopupWindow;
                if (textMessage.getUserFrom() ==
                        AppUtils.getInstance().getUserId()) {
                    rPopupWindow = new ResourcePopupWindow(
                            context, R.layout.right_message_pop);
                } else {
                    rPopupWindow = new ResourcePopupWindow(
                            context, R.layout.left_message_pop);
                }
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                rPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - rPopupWindow.getHeight());
                View mView = rPopupWindow.getView();
                Button btnCopy = (Button) mView
                        .findViewById(R.id.btn_pop_copy);
                Button btnRepeat = (Button) mView
                        .findViewById(R.id.btn_pop_repeat);
                Button btnDelete = (Button) mView
                        .findViewById(R.id.btn_pop_delete);
//                Button btnMore = (Button) mView
//                        .findViewById(R.id.btn_pop_more);
                // 拷贝
                btnCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(textMessage.getMessageContent());
                        rPopupWindow.dismiss();
                        Toast.makeText(context, context.getResources().getString(R.string.message_copy_sucess), Toast.LENGTH_SHORT).show();
                    }
                });
                // 转发
                btnRepeat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        rPopupWindow.dismiss();
//                        Intent intent = IntentManager.createIntent(context, MessageRepeatActivity.class);
//                        intent.putExtra("AMMessage", (SLMessage) textMessage);
//                        context.startActivity(intent);
//                        ((Activity) context).finish();
                    }
                });
                // 删除
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rPopupWindow.dismiss();
                        Intent intent = new Intent(Actions.ACTION_DELETE_MESSAGE_BYMESSAGEID);
                        intent.putExtra("messageID", textMessage.getMessageId());
                        context.sendOrderedBroadcast(intent, null);
                    }
                });
                // 更多
//                btnMore.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
                return true;
            }
        });
        return convertView;
    }

    @Override
    protected View inflateView(Context context) {
        if (textMessage.getUserFrom() ==
                AppUtils.getInstance().getUserId()) {
            return LayoutInflater.from(context).inflate(
                    R.layout.chatting_item_msg_text_right, null);
        } else {
            return LayoutInflater.from(context).inflate(
                    R.layout.chatting_item_msg_text_left, null);
        }
    }

    private void sendMessage(SLTextMessage amMessage, final Context context) {
        final long lastId = System.currentTimeMillis();
        SLTextMessage amTextMessage = new SLTextMessage();
        amTextMessage.setMessageId(String.valueOf(lastId));
        amTextMessage.setUserFrom(AppUtils.getInstance().getUserId());
        amTextMessage.setUserTo(amMessage.getUserTo());
        amTextMessage.setMessageContent(amMessage.getMessageContent());
        amTextMessage.setTimestamp(new Date().getTime());
        amTextMessage.setIsRead(SLMessage.msgRead);
        amTextMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
        InsertMessageTask insertMessageTask = new InsertMessageTask();
        insertMessageTask.setOnPostExecuteHandler(new BaseTask.OnPostExecuteHandler<Boolean>() {
            @Override
            public void handle(Boolean result) {
                // 发送融云广播
                Intent send_Intent = new Intent(Actions.ACTION_SNED_SINGLE_MESSAGE);
                send_Intent.putExtra("messageId", String.valueOf(lastId));
                send_Intent.putExtra("chatType", "single");
                context.sendOrderedBroadcast(send_Intent, null);
                // 本地会话广播
                Intent intent = new Intent(Actions.SINGLEMESSAGE_ADD_ACTION);
                intent.putExtra("messageID", String.valueOf(lastId));
                context.sendOrderedBroadcast(intent, null);
            }
        });
        AsyncTaskUtils.execute(insertMessageTask, amTextMessage);
        // 保存一条Session记录
        SLSession session = new SLSession();
        session.setLastMessageId(String.valueOf(lastId));
        session.setPriority(amTextMessage.getTimestamp());
        session.setTargetId(amMessage.getUserTo());
        session.setMessageType(amTextMessage.getMessageType());
        session.setSessionContent(amTextMessage.getMessageContent());
        SLUser user = new UserDaoImpl().getUserByUserId(amTextMessage.getUserTo());
        session.setSessionIcon(user.getHeadUrl());
        session.setSessionType(SessionType.CHAT);
        session.setSessionName(user.getNickName());
        SessionDao sessionDao = new SessionDaoImpl();
        sessionDao.addSession(session);
        Intent session_intent = new Intent(Actions.ACTION_SESSION);
        Bundle bundle = new Bundle();
        bundle.putLong("targetId", session.getTargetId());
        session_intent.putExtras(bundle);
        context.sendOrderedBroadcast(session_intent, null);
    }
}
