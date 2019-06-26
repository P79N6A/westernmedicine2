package com.xywy.askforexpert.module.docotorcirclenew.requestbean;

import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;

/**
 *
 * 评论请求参数
 * Created by bailiangjin on 2016/11/9.
 */
public class MoreParamBean {

    /**
     * 条目位置
     */
    private int position;

    /**
     * 医圈消息id
     */
    private String msgId;

    /**
     * 回复人Id
     */
    private String toUserId;

    /**
     * relation 类型
     */
    private String relation;

    /**
     * 消息类型 实名or 匿名
     */
    private DCMsgType msgType;

    public MoreParamBean(int position, String msgId, String toUserId, String relation, DCMsgType msgType) {
        this.position = position;
        this.msgId = msgId;
        this.toUserId = toUserId;
        this.relation = relation;
        this.msgType = msgType;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
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
}
