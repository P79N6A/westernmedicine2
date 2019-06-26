package com.xywy.askforexpert.module.docotorcirclenew.requestbean;

import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCCommentType;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;

import rx.Subscriber;

/**
 *
 * 评论请求参数
 * Created by bailiangjin on 2016/11/9.
 */
public class CommentParamBean {

    /**
     * 匿名用户Id
     */
    private String fakeUserId;
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
    private String toUserName;

    /**
     * 回复人Id
     */
    private String toUserId;

    /**
     * 消息类型 实名or 匿名
     */
    private DCMsgType msgType;


    private DCCommentType commentType;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 请求回调
     */
    private Subscriber subscriber;

    public CommentParamBean(String msgId,DCMsgType msgType,  Subscriber subscriber) {
        this.msgId = msgId;
        this.msgType = msgType;
        this.subscriber = subscriber;
    }


    public CommentParamBean(String msgId, String commentId, String toUserName, String toUserId, DCMsgType msgType,  Subscriber subscriber) {
        this.msgId = msgId;
        this.commentId = commentId;
        this.toUserName = toUserName;
        this.toUserId = toUserId;
        this.msgType = msgType;
        this.subscriber = subscriber;
    }


    public CommentParamBean(String msgId, String commentId, String toUserName, String toUserId, DCMsgType msgType, DCCommentType commentType, String content, Subscriber subscriber) {
        this.msgId = msgId;
        this.commentId = commentId;
        this.toUserName = toUserName;
        this.toUserId = toUserId;
        this.msgType = msgType;
        this.commentType = commentType;
        this.content = content;
        this.subscriber = subscriber;
    }

    public String getFakeUserId() {
        return fakeUserId;
    }

    public void setFakeUserId(String fakeUserId) {
        this.fakeUserId = fakeUserId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public DCCommentType getCommentType() {
        return commentType;
    }

    public void setCommentType(DCCommentType commentType) {
        this.commentType = commentType;
    }
}
