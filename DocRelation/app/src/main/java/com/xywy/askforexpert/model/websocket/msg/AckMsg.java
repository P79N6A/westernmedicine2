package com.xywy.askforexpert.model.websocket.msg;

import com.xywy.askforexpert.model.websocket.type.ActType;

/**
 * 新版本使用msg_id替代id 先使用旧版本 stone
 */

public class AckMsg extends BaseSocketMsg {

    protected String id;

    public AckMsg(String id) {
        this.id = id;
        act = ActType.PUB_ACK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    // TODO: 2018/5/29 websocket新版本 stone
//    protected int msg_id;
//
//    public AckMsg(int msg_id) {
//        this.msg_id = msg_id;
//        act = ActType.PUB_ACK;
//    }
//
//    public int getMsg_id() {
//        return msg_id;
//    }
//
//    public void setMsg_id(int msg_id) {
//        this.msg_id = msg_id;
//    }
}
