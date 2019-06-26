package com.xywy.askforexpert.module.docotorcirclenew.requestbean.rxevent;

import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;

/**
 * Created by bailiangjin on 2016/12/3.
 */

public class DCDeleteMsgEventBean {

    /**
     * 消息列表
     */
    private String msgId;

    /**
     * 消息类型 实名or 匿名
     */
    private DCMsgType type;


    public DCDeleteMsgEventBean(String msgId, DCMsgType type) {
        this.msgId = msgId;
        this.type = type;
    }


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public DCMsgType getType() {
        return type;
    }

    public void setType(DCMsgType type) {
        this.type = type;
    }
}
