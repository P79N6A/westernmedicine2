package com.xywy.askforexpert.module.docotorcirclenew.service;

import android.app.Activity;

import com.xywy.askforexpert.model.doctor.Messages;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.CircleMsgClickListener;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.DoctorFriendClickListener;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.TopNotificationClickListener;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.TopicClickListener;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.CircleUrlParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.CommentParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.DoctorInfoParam;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.InterestPersonParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.MoreParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.PraiseParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareClickParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.TopicParamBean;

import rx.Subscriber;

/**
 * 医圈通用的 点击事件处理服务
 * Created by bailiangjin on 2016/11/9.
 */
public class DcCommonClickService implements CircleMsgClickListener, TopicClickListener, DoctorFriendClickListener, TopNotificationClickListener {

    //医圈内容点击事件

    /**
     *
     * @param activity
     * @param msgId 医圈状态id
     * @param source 消息来源 用于区分病例研讨班 或者 一般状态
     * @param type
     */
    @Override
    public void onItemClick(Activity activity, String msgId, String source, String type) {

       DCMiddlewareService.onItemClick(activity,msgId,source,type);

    }

    /**
     * 点赞事件
     *
     * @param praiseParamBean
     */
    @Override
    public void onPraiseClick(PraiseParamBean praiseParamBean) {
        DCMiddlewareService.praise(praiseParamBean);
    }

    /**
     * 评论医圈消息事件
     *
     * @param activity
     * @param commentParamBean
     */
    @Override
    public void onReplyMsgClick(Activity activity, final CommentParamBean commentParamBean) {
        DCMiddlewareService.showCommentCircleMsgWindow(activity, commentParamBean);
    }


    /**
     * 回复医圈消息评论列表中某条评论事件
     *
     * @param activity
     * @param commentParamBean
     */
    @Override
    public void onReplyCommentClick(Activity activity, final CommentParamBean commentParamBean) {
        DCMiddlewareService.showCommentCommentMsgWindow(activity, commentParamBean);
    }

    /**
     * 分享按钮事件
     *
     * @param activity
     * @param shareParamBean
     */
    @Override
    public void onShareBtnClick(Activity activity, ShareParamBean shareParamBean) {
        DCMiddlewareService.share(activity, shareParamBean);
    }

    /**
     * 医圈消息内容中的分享的item 点击事件 跳转 分享的资讯 试题 等
     *
     * @param activity
     * @param paramBean
     */
    @Override
    public void onShareItemClick(Activity activity, ShareClickParamBean paramBean) {
        DCMiddlewareService.shareItemClick(activity, paramBean);
    }


    /**
     * item 右上角 倒三角 点击事件
     *
     * @param activity
     * @param moreParamBean
     */
    @Override
    public void onMoreClick(Activity activity, MoreParamBean moreParamBean, Subscriber subscriber) {
        DCMiddlewareService.moreClick(activity, moreParamBean, subscriber);
    }

    /**
     * 删除一条医圈消息 单独功能 匿名详情页调用
     *
     * @param activity
     * @param msgId
     * @param subscriber
     */
    @Override
    public void onDeleteMsgClick(Activity activity, String msgId, Subscriber subscriber) {
        DCMiddlewareService.deleteMsg(activity, msgId, subscriber);
    }

    /**
     * 当该commentId对应的评论 为当前登录用户发表时才能调用 调用前先做是否为当前用户校验
     *
     * @param activity
     * @param commentId
     * @param subscriber
     */
    @Override
    public void onDeleteCommentClick(Activity activity, String commentId, Subscriber subscriber) {
        DCMiddlewareService.deleteComment(activity, commentId, subscriber);

    }


    /**
     * 医圈内容中url 点击事件
     *
     * @param activity
     * @param circleUrlParamBean
     */
    @Override
    public void onUrlCLick(Activity activity, CircleUrlParamBean circleUrlParamBean) {
        //TODO: 跳转到统一的打开URL的WebView Activity
    }


    //话题点击事件

    /**
     * 话题标签点击事件
     *
     * @param activity
     * @param topicParamBean
     */
    @Override
    public void onTopicCLick(Activity activity, TopicParamBean topicParamBean) {
        DCMiddlewareService.topicClick(activity, topicParamBean);
    }

    /**
     * 更多话题点击事件
     *
     * @param activity
     */
    @Override
    public void onMoreTopicClick(Activity activity) {
        DCMiddlewareService.moreTopicClick(activity);

    }

    //好友点击事件

    /**
     * 用户名点击事件
     *
     * @param activity
     * @param doctorInfoParam
     */
    @Override
    public void onNameClick(Activity activity, DoctorInfoParam doctorInfoParam) {
        DCMiddlewareService.headOrNameClick(activity, doctorInfoParam);
    }

    /**
     * 用户头像点击事件
     *
     * @param activity
     * @param doctorInfoParam
     */
    @Override
    public void onHeadImgClick(Activity activity, DoctorInfoParam doctorInfoParam) {
        onNameClick(activity, doctorInfoParam);
    }


    /**
     * 添加好友按钮 点击事件
     *
     * @param activity
     * @param userId
     */
    @Override
    public void onAddFriendClick(Activity activity, String userId) {
        DCMiddlewareService.toAddFriend(activity, userId);
    }

    /**
     * 更多推荐好友按钮点击事件
     *
     * @param activity
     * @param paramBean
     */
    @Override
    public void onMoreFriendClick(Activity activity, InterestPersonParamBean paramBean) {
        DCMiddlewareService.interestingPerson(activity, paramBean);
    }

    /**
     * 更多查看了我的人 按钮点击事件
     *
     * @param activity
     */
    @Override
    public void onPeopleSeeMeClick(Activity activity) {
        DCMiddlewareService.theUsersSeeMe(activity);
    }


    //医圈消息提醒点击事件

    /**
     * 医圈 未读消息提示 点击事件
     *  @param activity
     * @param msg
     * @param type
     */
    @Override
    public void onUnreadClick(Activity activity, Messages msg, @PublishType String type) {
        DCMiddlewareService.circleMsg(activity,  msg,type);
    }
}
