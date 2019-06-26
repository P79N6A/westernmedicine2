package com.xywy.askforexpert.model.websocket.rxevent;

/**
 * 新版本msg_id替代id qid类型有变化 stone
 */
public class MsgReadEventBody {

    private String id;
    private String qid;

    public MsgReadEventBody(String id, String qid) {
        this.id = id;
        this.qid = qid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

// TODO: 2018/5/29 websocket新版本 stone
//   private int msg_id;
//   private int qid;
//
//
//    public MsgReadEventBody(int msgId, int qid) {
//        this.msg_id = msgId;
//        this.qid = qid;
//    }
//
//
//    public int getMsg_id() {
//        return msg_id;
//    }
//
//    public void setMsg_id(int msg_id) {
//        this.msg_id = msg_id;
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
