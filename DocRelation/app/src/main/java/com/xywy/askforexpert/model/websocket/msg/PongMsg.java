package com.xywy.askforexpert.model.websocket.msg;

import com.xywy.askforexpert.model.websocket.type.ActType;

/**
 * Created by bailiangjin on 2017/4/27.
 */

public class PongMsg extends BaseSocketMsg{

    public PongMsg() {
        act= ActType.PONG;
    }
}
