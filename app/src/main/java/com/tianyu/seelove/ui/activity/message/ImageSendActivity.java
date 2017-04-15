package com.tianyu.seelove.ui.activity.message;

import java.util.Date;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.common.Actions;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author shisheng.zhao
 * @Description: 拍照之后发送图片
 * @date 2017-04-15 14:29
 */
public class ImageSendActivity extends BaseActivity {
    private TextView titleView;
    private String picPath;
    private PhotoView photoView;
    private long target = 0l;
    private String targetName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_imagesend);
        picPath = getIntent().getExtras().getString("images");
        target = getIntent().getExtras().getLong("target");
        initView();
    }

    private void initView() {
        titleView = (TextView) findViewById(R.id.titleView);
        ImageView backView = (ImageView) findViewById(R.id.leftBtn);
        photoView = (PhotoView) findViewById(R.id.photoView);
        backView.setVisibility(View.VISIBLE);
        titleView.setText(R.string.send_image);
        Button btnSend = (Button) findViewById(R.id.btn_send);
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSend.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        backView.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(ImageLoaderUtil.getAcceptableUri(picPath), photoView, ImageLoaderUtil.getDefaultDisplayOptions());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.leftBtn:
                finish();
                break;
            case R.id.btn_send:
                sendImageMessage();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    /**
     * 发送图片消息
     */
    private void sendImageMessage() {
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
            insertMessageTask.setOnPostExecuteHandler(new BaseTask.OnPostExecuteHandler<Boolean>() {
                        @Override
                        public void handle(Boolean result) {
                            // 发送融云广播
                            Intent send_Intent = new Intent(Actions.ACTION_SNED_SINGLE_MESSAGE);
                            send_Intent.putExtra("messageId", String.valueOf(lastId));
                            send_Intent.putExtra("chatType", "single");
                            sendOrderedBroadcast(send_Intent, null);
                            // 本地会话广播
                            Intent intent = new Intent(Actions.SINGLEMESSAGE_ADD_ACTION);
                            intent.putExtra("messageID", String.valueOf(lastId));
                            sendOrderedBroadcast(intent, null);
                            // 图片消息发送的时候默认发送一个进度,让图片加灰处理
                            Intent process_Intent = new Intent(Actions.ACTION_UPDATE_IMGMESSAGE_PROCESS);
                            process_Intent.putExtra("ProcessCount", "0");
                            process_Intent.putExtra("MessageID", String.valueOf(lastId));
                            sendOrderedBroadcast(process_Intent, null);
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
            session_intent.putExtra("targetId", session.getTargetId());
            sendOrderedBroadcast(session_intent, null);
            ImageSendActivity.this.finish();
        } else {
            Toast.makeText(ImageSendActivity.this, getString(R.string.select_photos_no), Toast.LENGTH_LONG).show();
            ImageSendActivity.this.finish();
        }
    }
}
