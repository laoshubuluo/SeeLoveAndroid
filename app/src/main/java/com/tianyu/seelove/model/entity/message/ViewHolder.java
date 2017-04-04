package com.tianyu.seelove.model.entity.message;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianyu.seelove.view.image.BubbleImageView;

/**
 * @author shisheng.zhao
 * @Description: 聊天adapter viewholer
 * @date 2015-09-11 下午17:57:22
 */
public class ViewHolder {

    // 文章消息
    private LinearLayout contentLayout;
    private TextView title;
    private ImageView imageUrl;
    // 经文消息
    private TextView bibleCatalog; // 经文消息目录
    private TextView address; // 位置消息位置信息
    /**
     * 图片
     */
    private BubbleImageView bubbleImageView;
    private TextView imgProcessCount;
    private RelativeLayout processLayout;
    private TextView contentLength;
    /**
     * 文字内容
     */
    private TextView content;
    /**
     * 消息创建时间
     */
    private TextView createTime;
    /**
     * 用户名称
     */
    private TextView username;
    /**
     * 用户头像
     */
    private ImageView header;
    /**
     * 发送进度
     */
    private ProgressBar progress;
    /**
     * 发送失败状态
     */
    private TextView sendFail;
    /**
     * 发送成功
     */
    private ImageView sendSuccess;

    /**
     * 语音发送者的语音播放动画
     */
    private ImageView frameUser;

    /**
     * 接收到的语音的语音播放动画
     */
    private ImageView frameFriend;
    /**
     * 医生认证
     */
    private ImageView imageAuth;

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public ImageView getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ImageView imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageView getFrameUser() {
        return frameUser;
    }

    public void setFrameUser(ImageView frameUser) {
        this.frameUser = frameUser;
    }

    public ImageView getFrameFriend() {
        return frameFriend;
    }

    public void setFrameFriend(ImageView frameFriend) {
        this.frameFriend = frameFriend;
    }

    public TextView getContentLength() {
        return contentLength;
    }

    public void setContentLength(TextView contentLength) {
        this.contentLength = contentLength;
    }

    public ProgressBar getProgress() {
        return progress;
    }

    public void setProgress(ProgressBar progress) {
        this.progress = progress;
    }

    public TextView getSendFail() {
        return sendFail;
    }

    public void setSendFail(TextView sendFail) {
        this.sendFail = sendFail;
    }

    public ImageView getSendSuccess() {
        return sendSuccess;
    }

    public void setSendSuccess(ImageView sendSuccess) {
        this.sendSuccess = sendSuccess;
    }


    public ImageView getHeader() {
        return header;
    }

    public void setHeader(ImageView header) {
        this.header = header;
    }

    public TextView getUsername() {
        return username;
    }

    public void setUsername(TextView username) {
        this.username = username;
    }

    public TextView getContent() {
        return content;
    }

    public void setContent(TextView content) {
        this.content = content;
    }

    public TextView getCreateTime() {
        return createTime;
    }

    public void setCreateTime(TextView createTime) {
        this.createTime = createTime;
    }

    public ImageView getImageAuth() {
        return imageAuth;
    }

    public void setImageAuth(ImageView imageAuth) {
        this.imageAuth = imageAuth;
    }

    public BubbleImageView getBubbleImageView() {
        return bubbleImageView;
    }

    public void setBubbleImageView(BubbleImageView bubbleImageView) {
        this.bubbleImageView = bubbleImageView;
    }

    public TextView getAddress() {
        return address;
    }

    public void setAddress(TextView address) {
        this.address = address;
    }

    public TextView getImgProcessCount() {
        return imgProcessCount;
    }

    public void setImgProcessCount(TextView imgProcessCount) {
        this.imgProcessCount = imgProcessCount;
    }

    public RelativeLayout getProcessLayout() {
        return processLayout;
    }

    public void setProcessLayout(RelativeLayout processLayout) {
        this.processLayout = processLayout;
    }

    public TextView getBibleCatalog() {
        return bibleCatalog;
    }

    public void setBibleCatalog(TextView bibleCatalog) {
        this.bibleCatalog = bibleCatalog;
    }

    public LinearLayout getContentLayout() {
        return contentLayout;
    }

    public void setContentLayout(LinearLayout contentLayout) {
        this.contentLayout = contentLayout;
    }
}

