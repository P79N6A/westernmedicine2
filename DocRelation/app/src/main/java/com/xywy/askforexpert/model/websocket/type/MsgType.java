package com.xywy.askforexpert.model.websocket.type;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bailiangjin on 2017/4/27.
 */

public enum MsgType {
    @SerializedName("101001")
    ASK,//提问
    @SerializedName("101002")
    FURTHER_ASK,//追问
    @SerializedName("101003")
    TIMEOUT_NO_RESPONSE //超时未回复
}
