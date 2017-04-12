package com.tianyu.seelove.task;

import com.tianyu.seelove.dao.MessageDao;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.model.entity.message.SLMessage;
import com.tianyu.seelove.task.base.BaseTask;
import com.tianyu.seelove.utils.LogUtil;

/**
 * 插入聊天记录到本地数据库库异步任务
 * @author shisheng.zhao
 * @date 2017-04-12 17:38
 */
public class InsertMessageTask extends BaseTask<SLMessage, Void, Boolean> {
    private MessageDao messageDao = new MessageDaoImpl();

    @Override
    protected Boolean _doInBackground(SLMessage... params) {
        try {
            messageDao.addMessage(params[0]);
            LogUtil.d("new message saved " + params[0]);
            return true;
        } catch (Exception ex) {
            LogUtil.w("聊天信息保存到数据库失败");
            return false;
        }
    }
}
