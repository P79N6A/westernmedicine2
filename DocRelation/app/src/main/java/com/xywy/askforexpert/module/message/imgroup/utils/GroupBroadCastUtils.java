package com.xywy.askforexpert.module.message.imgroup.utils;

import android.content.Context;
import android.content.Intent;

import com.xywy.askforexpert.module.message.imgroup.constants.GroupBroadCastAction;

/**
 * Created by bailiangjin on 16/7/18.
 */
public class GroupBroadCastUtils {

    public static void sendExistGroupBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(GroupBroadCastAction.EXIST_GROUP_ACTION);
        context.sendBroadcast(intent);
    }

    public static void sendModifyGroupHeadBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(GroupBroadCastAction.MODIFY_GROUP_HEAD_ACTION);
        context.sendBroadcast(intent);
    }
}
