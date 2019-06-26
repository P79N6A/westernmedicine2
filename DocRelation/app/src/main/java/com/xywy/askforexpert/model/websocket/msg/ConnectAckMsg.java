package com.xywy.askforexpert.model.websocket.msg;

import com.xywy.askforexpert.model.websocket.type.ActType;

/**
 * 服务端发来的连接简历成功 msg
 * Created by bailiangjin on 2017/4/27.
 */
public class ConnectAckMsg extends BaseSocketMsg{

    protected int sequence;


    public ConnectAckMsg(int sequence) {
        this.sequence = sequence;
        act= ActType.CONNECT_ACK;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
