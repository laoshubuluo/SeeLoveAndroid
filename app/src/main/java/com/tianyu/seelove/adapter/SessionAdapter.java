package com.tianyu.seelove.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.utils.DateUtils;
import com.tianyu.seelove.utils.FaceConversionUtils;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.utils.ListSortUtil;
import com.tianyu.seelove.utils.TextUtils;
import com.tianyu.seelove.view.RedDotView;
import java.util.Iterator;
import java.util.List;

/**
 * 发现显示自定义adapter
 * @author shisheng.zhao
 * @date 2017-03-31 17:52
 */
public class SessionAdapter extends BaseAdapter {
    private List<SLSession> sessions;
    private LayoutInflater inflater;
    private Context mContext;

    public SessionAdapter(Context mContext, List<SLSession> sessions) {
        super();
        this.mContext = mContext;
        this.sessions = sessions;
        this.inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addData(List<SLSession> sessions) {
        this.sessions.clear();
        this.sessions.addAll(sessions);
        notifyDataSetChanged();
    }

    public void addDataSession(SLSession session,boolean isAdd) {
        if (null == session) {
            return;
        }
        for (SLSession sess : sessions) {
            if (sess.getTargetId().equals(session.getTargetId())) {
                sess.setLastMessageId(session.getLastMessageId());
                sess.setMessageType(session.getMessageType());
                sess.setPriority(session.getPriority());
                sess.setSessionContent(session.getSessionContent());
                sess.setSessionIcon(session.getSessionIcon());
                sess.setSessionIsRead(session.getSessionIsRead());
                sess.setSessionName(session.getSessionName());
                sess.setSessionType(session.getSessionType());
                sess.setTargetId(session.getTargetId());
                isAdd = false;
                break;
            } else {
                isAdd = true;
            }
        }
        if (isAdd) {
            sessions.add(session);
        }
        ListSortUtil<SLSession> listSortUtil = new ListSortUtil<SLSession>();
        listSortUtil.sort(sessions, "priority", "desc");
        notifyDataSetChanged();
    }

    public void deleteData(SLSession session) {
        // 同步clean list下的object
        Iterator iterator = sessions.iterator();
        while (iterator.hasNext()) {
            SLSession tempobj = (SLSession) iterator.next();
            if (tempobj.getTargetId().equals(session.getTargetId())) {
                //移除当前的对象
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sessions.size();
    }

    @Override
    public Object getItem(int position) {
        return sessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder = null;
        if (convertView == null) {
            viewHoder = new ViewHoder();
            convertView = inflater.inflate(R.layout.session_item_view, null);
            viewHoder.message_img_photo = (ImageView) convertView.findViewById(R.id.message_img_photo);
            viewHoder.message_tv_content = (TextView) convertView.findViewById(R.id.message_tv_content);
            viewHoder.message_tv_who = (TextView) convertView.findViewById(R.id.message_tv_who);
            viewHoder.message_tv_time = (TextView) convertView.findViewById(R.id.message_tv_time);
            viewHoder.redDotView = (RedDotView) convertView.findViewById(R.id.redDot);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        viewHoder.iv_sign.setVisibility(View.GONE);
        viewHoder.message_tv_who.setText(FaceConversionUtils.getInstace()
                .getExpressionString(convertView.getContext(), TextUtils.StringFilter(sessions.get(position)
                        .getSessionName())));
        if (sessions.get(position).getSessionType() == SessionType.CHAT) {
            ImageLoader.getInstance().displayImage(sessions.get(position).getSessionIcon(),
                    viewHoder.message_img_photo,
                    ImageLoaderUtil.getSmallImageOptions());
        }
        int unreadCount = sessions.get(position).getUnreadCount();
        viewHoder.message_tv_time.setText(DateUtils.getFriendlyDate(sessions
                .get(position).getPriority()));
        if (sessions.get(position).getMessageType()
                .equals(MessageType.AUDIO)) {
            viewHoder.message_tv_content.setText("[语音]");
        } else if (sessions.get(position).getMessageType()
                .equals(MessageType.IMAGE)) {
            viewHoder.message_tv_content.setText("[图片]");
        } else if (sessions.get(position).getMessageType()
                .equals(MessageType.LOCATION)) {
            viewHoder.message_tv_content.setText("[位置]");
        } else if (sessions.get(position).getMessageType()
                .equals(MessageType.TEXT)) {
            SpannableString spannableString = FaceConversionUtils
                    .getInstace().getExpressionString(mContext,
                            sessions.get(position).getSessionContent());
            viewHoder.message_tv_content.setText(spannableString);
        }
        return convertView;
    }

    private class ViewHoder {
        ImageView iv_sign;
        ImageView message_img_photo;
        TextView message_tv_who;
        TextView message_tv_time;
        TextView message_tv_content;
        RedDotView redDotView;
    }
}