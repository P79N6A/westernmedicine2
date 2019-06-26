package com.xywy.askforexpert.module.docotorcirclenew.interfaze;

import android.app.Activity;

import com.xywy.askforexpert.module.docotorcirclenew.requestbean.CircleUrlParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.CommentParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.MoreParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.PraiseParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareClickParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareParamBean;

import rx.Subscriber;

/**
 * 医圈动态 主内容 点击事件监听接口
 * Created by bailiangjin on 2016/11/3.
 */
 public interface CircleMsgClickListener {


    /**
     * 医圈动态 Item整体点击事件
     * @param msgId 医圈状态id
     * @param source 消息来源 用于区分病例研讨班 或者 一般状态
     * @param type
     */
    void onItemClick(Activity activity, String msgId, String source, String type);

    /**
     * 点赞按钮点击事件
     */
     void onPraiseClick(PraiseParamBean praiseParamBean);

    /**
     * 评论按钮点击事件
     */
     void onReplyMsgClick(Activity activity, CommentParamBean commentParamBean);

    /**
     * 回复某人按钮点击事件
     */
     void onReplyCommentClick(Activity activity, final CommentParamBean commentParamBean);

    /**
     * 分享按钮点击事件
     */
     void onShareBtnClick(Activity activity, ShareParamBean shareParamBean);

    /**
     * 分享的内容点击事件
     */
    void onShareItemClick(Activity activity, ShareClickParamBean paramBean);



    /**
     * 下拉框点击事件
     */
     void onMoreClick(Activity activity, MoreParamBean moreParamBean,Subscriber subscriber);

    /**
     * 单独的删除医圈动态按钮点击事件
     */
    void onDeleteMsgClick(Activity activity, String msgId,Subscriber subscriber);



    /**
     * 长按删除评论事件
     */
    void onDeleteCommentClick(Activity activity, String commentId, Subscriber subscriber);


    /**
     * 链接点击事件
     * @param activity
     * @param circleUrlParamBean
     */
    void onUrlCLick(Activity activity, CircleUrlParamBean circleUrlParamBean);
}
