package com.xywy.askforexpert.module.docotorcirclenew.service;

import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.retrofit.rxbus.EventSubscriber;

/**
 * Created by bailiangjin on 2016/11/14.
 */

public class DCEventService {
    /**
     * 点赞成功 事件
     */
    public static final String CIRCLE_PRAISE_SUCCESS_EVENT = "CIRCLE_PRAISE_SUCCESS_EVENT";
    /**
     * 评论成功事件
     */
    public static final String CIRCLE_COMMENT_SUCCESS_EVENT = "CIRCLE_COMMENT_SUCCESS_EVENT";

    /**
     * 注册点赞成功事件监听
     *
     * @param eventSubscriber
     */
    public static void registerPraiseListener(EventSubscriber eventSubscriber,Object tag) {
        YmRxBus.registerEvent(CIRCLE_PRAISE_SUCCESS_EVENT, eventSubscriber, tag);
    }

    /**
     * post 点赞成功事件
     *
     * @param data
     */
    public static void postPraiseEvent(Object data) {
        YmRxBus.postEvent(CIRCLE_PRAISE_SUCCESS_EVENT, data);
    }

    /**
     * 注册评论成功事件监听
     *
     * @param eventSubscriber
     */
    public static void registerCommentListener(EventSubscriber eventSubscriber,Object tag) {
        YmRxBus.registerEvent(CIRCLE_COMMENT_SUCCESS_EVENT, eventSubscriber, tag);
    }

    /**
     * post 评论成功事件
     *
     * @param commentId
     */
    public static void postCommentEvent(String commentId) {
        YmRxBus.postEvent(CIRCLE_COMMENT_SUCCESS_EVENT, commentId);
    }


}
