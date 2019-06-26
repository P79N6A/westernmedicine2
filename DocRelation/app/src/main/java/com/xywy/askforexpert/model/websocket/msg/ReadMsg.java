package com.xywy.askforexpert.model.websocket.msg;

import com.xywy.askforexpert.model.websocket.type.ActType;

/**
 * 新版本去掉id,转而添加msg_id qid类型有变化 stone
 */
public class ReadMsg extends AckMsg {

    protected String qid;

    public ReadMsg(String id, String qid) {
        super(id);
        this.qid = qid;
        act = ActType.READ;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    // TODO: 2018/5/29 websocket新版本 stone
//    protected int qid;
//
//    public ReadMsg(int msg_id, int qid) {
//        super(msg_id);
//        this.qid = qid;
//        act = ActType.READ;
//    }
//
//    public int getQid() {
//        return qid;
//    }
//
//    public void setQid(int qid) {
//        this.qid = qid;
//    }

}
