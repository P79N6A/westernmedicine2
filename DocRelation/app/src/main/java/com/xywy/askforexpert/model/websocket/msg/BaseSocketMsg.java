package com.xywy.askforexpert.model.websocket.msg;

import com.xywy.askforexpert.model.websocket.type.ActType;

/**
 * Created by bailiangjin on 2017/4/27.
 */

public class BaseSocketMsg {

    protected ActType act;

    public ActType getAct() {
        return act;
    }

    public void setAct(ActType act) {
        this.act = act;
    }
}
