package com.xywy.askforexpert.module.docotorcirclenew.activity.detailpage;

import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;

import rx.Subscriber;

/**
 * 自定义的 动态详情页 生命周期接口 用于在具体activity 中实现区别化配置
 * Created by bailiangjin on 2016/11/25
 * */
public interface DetailPageLifecycle {


    void setTitle();

    void setHeaderViewVisibility();

    void setHeaderViewData();


    void comment(final String curUserId, final String commentId, final String toUserId, String content, final Subscriber subscriber);

    DCMsgType getMSGType();



}