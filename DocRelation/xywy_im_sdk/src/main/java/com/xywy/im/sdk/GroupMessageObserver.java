package com.xywy.im.sdk;

public interface GroupMessageObserver {
    public void onGroupMessage(Message msg);
    public void onGroupMessageACK(String msgLocalID, long uid);
    public void onGroupMessageFailure(String msgLocalID, long uid);
    public void onGroupNotification(String notification);
}