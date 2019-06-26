package com.xywy.askforexpert.module.docotorcirclenew.requestbean;

import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;

import rx.Subscriber;

/**
 * 点赞请求参数
 * Created by bailiangjin on 2016/11/9.
 */
public class PraiseParamBean {

    /**
     * 医圈消息id
     */
    private String msgId;
    /**
     * 评论id
     */
    private String commentId;
    /**
     * 回复人Id
     */
    private String toUserId;

    /**
     * 消息类型 实名or 匿名
     */
    private DCMsgType msgType;

    /**
     * 请求回调
     */
    private Subscriber subscriber;


    public PraiseParamBean(String msgId, String commentId, String toUserId, DCMsgType msgType, Subscriber subscriber) {
        this.msgId = msgId;
        this.commentId = commentId;
        this.toUserId = toUserId;
        this.msgType = msgType;
        this.subscriber = subscriber;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public DCMsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(DCMsgType msgType) {
        this.msgType = msgType;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }
}
