package com.xywy.askforexpert.module.docotorcirclenew.interfaze;

import android.app.Activity;

import com.xywy.askforexpert.model.doctor.Messages;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;

/**
 * 医圈动态 顶部通知Cell点击监听接口
 * Created by bailiangjin on 2016/11/3.
 */
public interface TopNotificationClickListener {

    /**
     * 医圈消息详情提示点击 事件
     *
     */
    public void onUnreadClick(Activity activity, Messages msg, @PublishType String type);

}
