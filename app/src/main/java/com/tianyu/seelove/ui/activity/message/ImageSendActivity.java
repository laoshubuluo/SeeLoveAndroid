package com.tianyu.seelove.ui.activity.message;

import java.util.Date;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.injection.ControlInjection;
import com.tianyu.seelove.model.entity.message.SLImageMessage;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.task.InsertMessageTask;
import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.ui.activity.base.BaseActivity;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.view.photoview.PhotoView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ImageSendActivity extends BaseActivity {
    @ControlInjection(R.id.titleView)
    private TextView titleView;
    private String picPath;
    private RelativeLayout layout;
    private String target = null;
    private String targetName = "";
    private String targetGroup = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_imagesend);
        titleView.setText("");
        layout = (RelativeLayout) findViewById(R.id.contain_layout);
        picPath = getIntent().getExtras().getString("images");
        target = getIntent().getExtras().getString("target");
        targetGroup = getIntent().getExtras().getString("targetGroup");
        PhotoView photoView = new PhotoView(ImageSendActivity.this);
        ImageLoader.getInstance().displayImage(
                ImageLoaderUtil.getAcceptableUri(picPath), photoView,
                ImageLoaderUtil.getDefaultDisplayOptions());
        layout.addView(photoView,
                android.view.WindowManager.LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        bindEvent();
    }

    private void bindEvent() {
        Button btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btn_send = (Button) this.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(picPath)) {
                    final long lastId = System.currentTimeMillis();
                    SLImageMessage imageMessage = new SLImageMessage();
                    imageMessage.setMessageId(String.valueOf(lastId));
                    imageMessage.setMessageContent(picPath);
                    imageMessage.setUserFrom(AppUtils.getInstance().getUserId());
                    imageMessage.setUserTo(target);
                    imageMessage.setIsRead(SLMessage.msgRead);
                    imageMessage.setTimestamp(new Date().getTime());
                    imageMessage.setSendStatue(SLMessage.MessagePropertie.MSG_SENDING);
                    InsertMessageTask insertMessageTask = new InsertMessageTask();
                    insertMessageTask
                            .setOnPostExecuteHandler(new BaseTask.OnPostExecuteHandler<Boolean>() {
                                @Override
                                public void handle(Boolean result) {
                                    // 发送融云广播
                                    Intent send_Intent = new Intent(Actions.ACTION_SNED_SINGLE_MESSAGE);
                                    send_Intent.putExtra("MessageID",
                                            String.valueOf(lastId));
                                    send_Intent.putExtra("chatType", "single");
                                    getApplicationContext().sendOrderedBroadcast(send_Intent, null);
                                    // 本地会话广播
                                    Intent intent = new Intent(Actions.SINGLEMESSAGE_ADD_ACTION);
                                    intent.putExtra("messageID",
                                            String.valueOf(lastId));
                                    getApplicationContext().sendOrderedBroadcast(intent, null);
                                    // 图片消息发送的时候默认发送一个进度,让图片加灰处理
                                    Intent process_Intent = new Intent(
                                            Actions.ACTION_UPDATE_IMGMESSAGE_PROCESS);
                                    process_Intent.putExtra("ProcessCount", "0");
                                    process_Intent.putExtra("MessageID", String.valueOf(lastId));
                                    getApplicationContext().sendOrderedBroadcast(process_Intent, null);
                                }
                            });
                    insertMessageTask.execute(imageMessage);
                    SLSession session = new SLSession();
                    session.setLastMessageId(String.valueOf(lastId));
                    session.setPriority(imageMessage.getTimestamp());
                    session.setTargetId(target);
                    session.setSessionContent(imageMessage.getMessageContent());
                    session.setMessageType(imageMessage.getMessageType());
                    session.setSessionType(SessionType.CHAT);
                    session.setSessionName(targetName);
                    SessionDaoImpl sessionDaoImpl = new SessionDaoImpl();
                    sessionDaoImpl.addSession(session);
                    Intent session_intent = new Intent(Actions.ACTION_SESSION);
                    Bundle bundle = new Bundle();
                    bundle.putString("targetId", session.getTargetId());
                    session_intent.putExtras(bundle);
                    sendOrderedBroadcast(session_intent, null);
                    ImageSendActivity.this.finish();
                } else {
                    Toast.makeText(ImageSendActivity.this, getString(R.string.select_photos_no), Toast.LENGTH_LONG).show();
                    ImageSendActivity.this.finish();
                }
            }
        });
    }
}
