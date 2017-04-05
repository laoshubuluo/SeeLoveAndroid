package com.tianyu.seelove.model.entity.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Base64;
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
import com.tianyu.seelove.dao.SessionDao;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.dao.impl.UserDaoImpl;
import com.tianyu.seelove.manager.IntentManager;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.task.InsertMessageTask;
import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.ui.activity.message.PositionActivity;
import com.tianyu.seelove.ui.activity.user.UserInfoActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.AsyncTaskUtils;
import com.tianyu.seelove.utils.BitmapUtils;
import com.tianyu.seelove.utils.DateUtils;
import com.tianyu.seelove.utils.DimensionUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.view.dialog.SureDialog;
import com.tianyu.seelove.view.image.BubbleImageView;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class LocationMessageEntity extends MessageEntity {
    SLLocationMessage locationMessage;
    SLUser user = null;
    SLUser self = null;
    Bitmap mBitmap = null;
    ViewHolder viewHolder = null;

    public LocationMessageEntity(SLMessage message) {
        super(message);
        this.locationMessage = (SLLocationMessage) message;
        this.user = new UserDaoImpl().getUserByUid(message.getUserFrom());
        this.self = new UserDaoImpl().getUserByUid(AppUtils.getInstance()
                .getUserId());
    }

    @Override
    protected View inflateView(Context context) {
        if (locationMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            return LayoutInflater.from(context).inflate(
                    R.layout.chatting_item_msg_location_right, null);
        } else {
            return LayoutInflater.from(context).inflate(
                    R.layout.chatting_item_msg_location_left, null);
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
        viewHolder.setAddress((TextView) convertView
                .findViewById(R.id.mc_tv_address));
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
        // 设置头像
        if (locationMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            viewHolder.getHeader().setImageResource(R.mipmap.default_head);
            if (self != null && self.getHeadUrl() != null
                    && self.getHeadUrl().length() > 0) {
                viewHolder.getHeader().setImageResource(R.mipmap.default_head);
                ImageLoader.getInstance().displayImage(
                        ImageLoaderUtil.getAcceptableUri(self.getHeadUrl()),
                        viewHolder.getHeader(),
                        ImageLoaderUtil.getDefaultDisplayOptions());
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
                        ImageLoaderUtil.getDefaultDisplayOptions());

            } else {
                viewHolder.getHeader().setImageResource(R.mipmap.default_head);
            }
            if (viewHolder.getUsername() != null) {
                viewHolder.getUsername().setVisibility(View.GONE);
            }
            viewHolder.getImageAuth().setVisibility(View.GONE);
        }
        // 设置发送时间
        if (locationMessage.getIsShowTime()) {
            viewHolder.getCreateTime().setVisibility(View.VISIBLE);
            viewHolder.getCreateTime().setText(DateUtils.getFriendlyDate(locationMessage.getTimestamp()));
        } else {
            viewHolder.getCreateTime().setVisibility(View.GONE);
        }
        try {
            String imagePath = "";
            if (locationMessage.getUserFrom().equals(AppUtils.getInstance().getUserId())) {
                imagePath = ImageLoaderUtil.getSmallPic(locationMessage.getMessageContent());
                ImageLoader.getInstance().loadImage(imagePath,
                        ImageLoaderUtil.getDefaultDisplayOptions(),
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri,
                                                          View view, Bitmap loadedImage) {
                                final int w = DimensionUtils.convertDipToPixels(
                                        context.getResources(), 203);
                                final int h = DimensionUtils.convertDipToPixels(
                                        context.getResources(), 135);
                                mBitmap = BitmapUtils.zoomBitmap(loadedImage, w, h);
                                if (null != mBitmap) {
                                    viewHolder.getBubbleImageView().setImageBitmap(mBitmap);
                                }
                                viewHolder.getAddress().setVisibility(View.VISIBLE);
                                viewHolder.getAddress().setText("  " + locationMessage.getAddress());
                            }

                        });
            } else {
                Bitmap bitmap = stringtoBitmap(locationMessage.getMessageContent());
                if (null != bitmap) {
                    final int w = DimensionUtils.convertDipToPixels(
                            context.getResources(), 203);
                    final int h = DimensionUtils.convertDipToPixels(
                            context.getResources(), 135);
                    mBitmap = BitmapUtils.zoomBitmap(bitmap, w, h);
                    if (null != mBitmap) {
                        viewHolder.getBubbleImageView().setImageBitmap(mBitmap);
                    }
                    viewHolder.getAddress().setVisibility(View.VISIBLE);
                    viewHolder.getAddress().setText("  " + locationMessage.getAddress());
                } else {
                    imagePath = ImageLoaderUtil.getSmallPic2(locationMessage.getMessageContent());
                    ImageLoader.getInstance().loadImage(imagePath,
                            ImageLoaderUtil.getDefaultDisplayOptions(),
                            new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri,
                                                              View view, Bitmap loadedImage) {
                                    final int w = DimensionUtils.convertDipToPixels(
                                            context.getResources(), 203);
                                    final int h = DimensionUtils.convertDipToPixels(
                                            context.getResources(), 135);
                                    mBitmap = BitmapUtils.zoomBitmap(loadedImage, w, h);
                                    if (null != mBitmap) {
                                        viewHolder.getBubbleImageView().setImageBitmap(mBitmap);
                                    }
                                    viewHolder.getAddress().setVisibility(View.VISIBLE);
                                    viewHolder.getAddress().setText("  " + locationMessage.getAddress());
                                }

                            });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (locationMessage.getUserFrom().equals(
                AppUtils.getInstance().getUserId())) {
            viewHolder.getSendFail().setTag(locationMessage.getMessageId());
            if (locationMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_SENDSUS) {
                viewHolder.getSendFail().setVisibility(View.INVISIBLE);
                viewHolder.getProgress().setVisibility(View.INVISIBLE);
            }
            if (locationMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_FAIL) {
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
                                intent.putExtra("messageID", locationMessage.getMessageId());
                                context.sendOrderedBroadcast(intent, null);
                                // Step2: 发送消息
                                sendMessage(locationMessage, context);
                            }
                        });
                        sureDialog.show();
                    }
                });
            }
            if (locationMessage.getSendStatue() == SLMessage.MessagePropertie.MSG_SENDING) {
                viewHolder.getProgress().setVisibility(View.VISIBLE);
                viewHolder.getSendFail().setVisibility(View.INVISIBLE);
            }
        }
        viewHolder.getBubbleImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PositionActivity.class);
                intent.putExtra("lng", locationMessage.getLng());
                intent.putExtra("lat", locationMessage.getLat());
                intent.putExtra("address", locationMessage.getAddress());
                intent.putExtra("isEdit", false); // 是否可以编辑
                context.startActivity(intent);
            }
        });
        viewHolder.getHeader().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationMessage.getUserFrom().equals(
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

    public Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void sendMessage(SLLocationMessage amMessage, final Context context) {
        final long lastId = System.currentTimeMillis();
        final boolean isGroup;
        SLLocationMessage amLocationMessage = new SLLocationMessage();
        amLocationMessage.setMessageId(String.valueOf(lastId));
        amLocationMessage.setUserFrom(AppUtils.getInstance().getUserId());
        amLocationMessage.setUserTo(amMessage.getUserTo());
        amLocationMessage.setMessageContent(amMessage.getMessageContent());
        amLocationMessage.setTimestamp(new Date().getTime());
        amLocationMessage.setIsRead(SLMessage.msgRead);
        amLocationMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
        amLocationMessage.setLat(amMessage.getLat());
        amLocationMessage.setLng(amMessage.getLng());
        amLocationMessage.setAddress(amMessage.getAddress());
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
        AsyncTaskUtils.execute(insertMessageTask, amLocationMessage);
        // 保存一条Session记录
        SLSession session = new SLSession();
        session.setLastMessageId(String.valueOf(lastId));
        session.setPriority(amLocationMessage.getTimestamp());
        session.setTargetId(amMessage.getUserTo());
        session.setMessageType(amLocationMessage.getMessageType());
        session.setSessionContent(amLocationMessage.getMessageContent());
        SLUser user = new UserDaoImpl().getUserByUid(amLocationMessage.getUserTo());
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
