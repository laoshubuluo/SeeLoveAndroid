package com.tianyu.seelove.model.entity.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.task.InsertMessageTask;
import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.ui.activity.user.UserInfoActivity;
import com.tianyu.seelove.ui.fragment.ViewPageFragment;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.AsyncTaskUtils;
import com.tianyu.seelove.utils.DateUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.utils.StringUtils;
import com.tianyu.seelove.view.dialog.SureDialog;
import com.tianyu.seelove.view.image.BubbleImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageMessageEntity extends MessageEntity {
    SLImageMessage imageMessage;
    String imagePath = "";
    SLUser user = null;
    SLUser self = null;
    Bitmap mBitmap = null;
    ViewHolder viewHolder = null;
    private MessageDao messageDao = new MessageDaoImpl();

    public ImageMessageEntity(SLMessage message) {
        super(message);
        this.imageMessage = (SLImageMessage) message;
        this.user = new UserDaoImpl().getUserByUserId(message.getUserFrom());
        this.self = new UserDaoImpl().getUserByUserId(AppUtils.getInstance()
                .getUserId());
    }

    @Override
    protected View inflateView(Context context) {
        if (imageMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            return LayoutInflater.from(context).inflate(
                    R.layout.chatting_item_msg_img_right, null);
        } else {
            return LayoutInflater.from(context).inflate(
                    R.layout.chatting_item_msg_img_left, null);
        }
    }

    @Override
    public View getConvertView(final Context context, View convertView,
                               ViewGroup viewGroup) {
        convertView = inflateView(context);
        viewHolder = new ViewHolder();
        viewHolder.setUsername((TextView) convertView
                .findViewById(R.id.mc_tv_username));
        viewHolder.setBubbleImageView((BubbleImageView) convertView
                .findViewById(R.id.mc_tv_chatcontent));
        convertView.setTag(viewHolder);
        viewHolder.setHeader((ImageView) convertView
                .findViewById(R.id.mc_iv_userhead));
        viewHolder.setCreateTime((TextView) convertView
                .findViewById(R.id.mc_tv_sendtime));
        viewHolder.setProgress((ProgressBar) convertView
                .findViewById(R.id.mc_progressbar));
        viewHolder.setSendFail((TextView) convertView
                .findViewById(R.id.mc_sendfail));
        viewHolder.setImageAuth((ImageView) convertView
                .findViewById(R.id.user_recognise));
        if (imageMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            viewHolder.setImgProcessCount((TextView) convertView.findViewById(R.id.imgProcessCount));
        }
        // 设置头像
        if (imageMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            viewHolder.getHeader().setImageResource(R.mipmap.default_head);
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
            if (user != null && user.getHeadUrl() != null
                    && user.getHeadUrl().length() > 0) {
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

        // 设置发送时间
        if (imageMessage.getIsShowTime()) {
            viewHolder.getCreateTime().setVisibility(View.VISIBLE);
            viewHolder.getCreateTime().setText(DateUtils.getFriendlyDate(imageMessage.getTimestamp()));
        } else {
            viewHolder.getCreateTime().setVisibility(View.GONE);
        }
        if (imageMessage.getUserFrom().equals(AppUtils.getInstance().getUserId())) {
            imagePath = ImageLoaderUtil.getSmallPic(imageMessage.getMessageContent());
        } else {
            imagePath = imageMessage.getMessageContent();
        }
        ImageLoader.getInstance().displayImage(imagePath, viewHolder.getBubbleImageView(), ImageLoaderUtil.getDefaultDisplayOptions(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri,
                                          View view, Bitmap loadedImage) {
                if (imageMessage.getUserFrom().equals(
                        AppUtils.getInstance().getUserId())) {
                    if (StringUtils.isNotBlank(imageMessage.getSendProcess())) {
                        if (Integer.parseInt(imageMessage.getSendProcess()) >= 98) {
                            viewHolder.getImgProcessCount().setVisibility(View.GONE);
                        } else {
                            viewHolder.getImgProcessCount().setVisibility(View.VISIBLE);
                            int width = loadedImage.getWidth();
                            int height = loadedImage.getHeight();
                            viewHolder.getImgProcessCount().setWidth(width - 22);
                            viewHolder.getImgProcessCount().setHeight(height);
                            viewHolder.getImgProcessCount().setBackgroundResource(R.drawable.shape_trans_bg);
                            viewHolder.getImgProcessCount().setText(imageMessage.getSendProcess() + "%");
                        }
                    }
                }
            }
        });
        if (imageMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            viewHolder.getSendFail().setTag(imageMessage.getMessageId());
            if (imageMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_SENDSUS) {
                viewHolder.getSendFail().setVisibility(View.INVISIBLE);
                viewHolder.getProgress().setVisibility(View.INVISIBLE);
            }
            if (imageMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_FAIL) {
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
                                intent.putExtra("messageID", imageMessage.getMessageId());
                                context.sendOrderedBroadcast(intent, null);
                                // Step2: 发送消息
                                sendMessage(imageMessage, context);
                            }
                        });
                        sureDialog.show();
                    }
                });
            }
            if (imageMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_SENDING) {
                viewHolder.getProgress().setVisibility(View.INVISIBLE);
                viewHolder.getSendFail().setVisibility(View.INVISIBLE);
            }
        }
        viewHolder.getBubbleImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ViewPageImage> list = new ArrayList<ViewPageImage>();
                if (imageMessage.getUserFrom().equals(AppUtils.getInstance().getUserId())) {
                    list = getViewPageImageList(messageDao.getImageMessage(AppUtils.getInstance().getUserId(), imageMessage.getUserTo()));
                } else {
                    list = getViewPageImageList(messageDao.getImageMessage(String.valueOf(self.getUserId()), String.valueOf(user.getUserId())));
                }
                Intent intent = IntentManager.createIntent(v.getContext(), ViewPageFragment.class);
                intent.putExtra("viewPageImages", (Serializable) list);
                intent.putExtra("selectImage", getSelectImage(imageMessage));
                v.getContext().startActivity(intent);
//                ((Activity) v.getContext()).overridePendingTransition(R.anim.magnify_fade_in, R.anim.magnify_fade_out);
            }
        });
        viewHolder.getHeader().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageMessage.getUserFrom().equals(
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
        // 文本消息长按复制,转发
//        viewHolder.getBubbleImageView().setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                final ResourcePopupWindow rPopupWindow;
//                if (imageMessage.getUserFrom().equals(
//                        AppUtils.getInstance().getUserId())) {
//                    rPopupWindow = new ResourcePopupWindow(
//                            context, R.layout.right_message_pop);
//                } else {
//                    rPopupWindow = new ResourcePopupWindow(
//                            context, R.layout.left_message_pop);
//                }
//                int[] location = new int[2];
//                view.getLocationOnScreen(location);
//                rPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - rPopupWindow.getHeight());
//                View mView = rPopupWindow.getView();
//                Button btnCopy = (Button) mView
//                        .findViewById(R.id.btn_pop_copy);
//                Button btnRepeat = (Button) mView
//                        .findViewById(R.id.btn_pop_repeat);
//                Button btnDelete = (Button) mView
//                        .findViewById(R.id.btn_pop_delete);
//                // 拷贝
//                btnCopy.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                        clipboardManager.setText(imageMessage.getContent());
//                        rPopupWindow.dismiss();
//                        Toast.makeText(context, context.getResources().getString(R.string.message_copy_sucess), Toast.LENGTH_SHORT).show();
//                    }
//                });
//                // 转发
//                btnRepeat.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        rPopupWindow.dismiss();
//                        Intent intent = IntentManager.createIntent(context, MessageRepeatActivity.class);
//                        intent.putExtra("AMMessage", (AMMessage) imageMessage);
//                        context.startActivity(intent);
//                    }
//                });
//                // 删除
//                btnDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        rPopupWindow.dismiss();
//                        Intent intent = new Intent(Actions.ACTION_DELETE_MESSAGE_BYMESSAGEID);
//                        intent.putExtra("messageID", imageMessage.getMessageId());
//                        context.sendOrderedBroadcast(intent, null);
//                    }
//                });
//                return false;
//            }
//        });
        return convertView;
    }

    private List<ViewPageImage> getViewPageImageList(List<SLMessage> list) {
        List<ViewPageImage> viewPageImages = new ArrayList<ViewPageImage>();
        for (SLMessage amMessage : list) {
            SLImageMessage message = (SLImageMessage) amMessage;
            if (message.getUserFrom().equals(AppUtils.getInstance().getUserId())) {
                imagePath = ImageLoaderUtil.getSmallPic(message.getMessageContent());
            } else {
                imagePath = message.getMessageContent();
            }
            ViewPageImage viewPageImage = new ViewPageImage();
            viewPageImage.setImageUrl(imagePath);
            viewPageImage.setThumImageUrl(message.getThumUrl());
            viewPageImages.add(viewPageImage);
        }
        return viewPageImages;
    }

    private String getSelectImage(SLMessage amMessage) {
        SLImageMessage message = (SLImageMessage) amMessage;
        if (message.getUserFrom().equals(AppUtils.getInstance().getUserId())) {
            return ImageLoaderUtil.getSmallPic(message.getMessageContent());
        } else {
            return message.getMessageContent();
        }
    }

    private void sendMessage(SLImageMessage amMessage, final Context context) {
        final long lastId = System.currentTimeMillis();
        final boolean isGroup;
        SLImageMessage amImageMessage = new SLImageMessage();
        amImageMessage.setMessageId(String.valueOf(lastId));
        amImageMessage.setUserFrom(AppUtils.getInstance().getUserId());
        amImageMessage.setUserTo(amMessage.getUserTo());
        amImageMessage.setMessageContent(amMessage.getMessageContent());
        amImageMessage.setTimestamp(new Date().getTime());
        amImageMessage.setIsRead(SLMessage.msgUnread);
        amImageMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
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
                // 图片消息发送的时候默认发送一个进度,让图片加灰处理
                Intent process_Intent = new Intent(
                        Actions.ACTION_UPDATE_IMGMESSAGE_PROCESS);
                process_Intent.putExtra("ProcessCount", "0");
                process_Intent.putExtra("MessageID", String.valueOf(lastId));
                context.sendOrderedBroadcast(process_Intent, null);
            }
        });
        AsyncTaskUtils.execute(insertMessageTask, amImageMessage);
        // 保存一条Session记录
        SLSession session = new SLSession();
        session.setLastMessageId(String.valueOf(lastId));
        session.setPriority(amImageMessage.getTimestamp());
        session.setTargetId(amImageMessage.getUserTo());
        session.setMessageType(amImageMessage.getMessageType());
        session.setSessionContent(amImageMessage.getMessageContent());
        SLUser user = new UserDaoImpl().getUserByUserId(amImageMessage.getUserTo());
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
