package com.xywy.im.sdk;

import java.nio.ByteBuffer;

/**
 * Created by xugan on 2018/7/18.
 */

public interface WebSocketStatusCallBack {
    public abstract void onOpen();
//    public abstract void onConnect();
    public abstract void onMessage(ByteBuffer buf);
    public abstract void onClose(int code, final String reason, final boolean remote);
    public abstract void onError(Exception e);
}
