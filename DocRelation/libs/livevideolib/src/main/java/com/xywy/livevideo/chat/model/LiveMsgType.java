package com.xywy.livevideo.chat.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.xywy.livevideo.chat.model.LiveMsgType.Gift;
import static com.xywy.livevideo.chat.model.LiveMsgType.JoinRoom;
import static com.xywy.livevideo.chat.model.LiveMsgType.LeaveRoom;
import static com.xywy.livevideo.chat.model.LiveMsgType.Txt;


/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/2/22 14:43
 */
@StringDef({Txt,Gift,LeaveRoom,JoinRoom})
@Retention(RetentionPolicy.SOURCE)
public @interface LiveMsgType {
    public static final String Txt = "0";
    public static final String Gift = "1";
    public static final String LeaveRoom = "2";
    public static final String JoinRoom = "3";
}
