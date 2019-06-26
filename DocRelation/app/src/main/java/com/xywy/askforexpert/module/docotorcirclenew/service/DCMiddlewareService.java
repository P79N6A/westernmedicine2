package com.xywy.askforexpert.module.docotorcirclenew.service;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.uilibrary.dialog.pndialog.XywyPNDialog;
import com.xywy.uilibrary.dialog.pndialog.listener.PNDialogListener;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.model.doctor.Messages;
import com.xywy.askforexpert.module.docotorcirclenew.activity.DoctorMessageNotify;
import com.xywy.askforexpert.module.docotorcirclenew.activity.NewTopicDetailActivity;
import com.xywy.askforexpert.module.docotorcirclenew.activity.detailpage.NoNameDetailActivity;
import com.xywy.askforexpert.module.docotorcirclenew.activity.detailpage.RealNameDetailActivity;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.CommentParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.DoctorInfoParam;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.InterestPersonParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.MoreParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.PraiseParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareClickParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.TopicParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCCommentType;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.ShareSourceType;
import com.xywy.askforexpert.module.docotorcirclenew.utils.DCShareHelpUtils;
import com.xywy.askforexpert.module.doctorcircle.DiscussDetailActivity;
import com.xywy.askforexpert.module.doctorcircle.InterestPersonActivity;
import com.xywy.askforexpert.module.doctorcircle.report.ReportActivity;
import com.xywy.askforexpert.module.doctorcircle.topic.MoreTopicActivity;
import com.xywy.askforexpert.module.main.media.MediaSettingActivity;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.friend.AddCardHoldVerifyActiviy;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.module.message.usermsg.DiscussSettingsActivity;
import com.xywy.askforexpert.widget.module.doctorcircle.DoctorCircleBottomPopupWindow;
import com.xywy.askforexpert.widget.module.doctorcircle.DoctorCircleCommentPopupWindow;
import com.xywy.askforexpert.widget.module.doctorcircle.PopupWindowType;
import com.xywy.askforexpert.widget.module.doctorcircle.listener.PopupWindowListener;
import com.xywy.askforexpert.widget.module.doctorcircle.listener.SendMsgWindowListener;

import rx.Subscriber;

import static com.xywy.askforexpert.appcommon.utils.ToastUtils.shortToast;
import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Anonymous;

/**
 * 医圈中间件 服务
 * Created by bailiangjin on 2016/11/9.
 */

public class DCMiddlewareService {

    /**
     *  医圈列表 item 整体点击事件
     * @param activity
     * @param msgId 医圈状态id
     * @param source 消息来源 用于区分病例研讨班 或者 一般状态
     * @param type 实名 or 匿名
     */
    public static void onItemClick(Activity activity, String msgId, String source, String type) {

//        DCMsgType msgType = PublishType.Realname.equals(type) ? DCMsgType.REAL_NAME : DCMsgType.NO_NAME;

//        if (DCMiddlewareService.isInvalidDCUser(msgType,"查看详情")){
//            return;
//        }

        if (PublishType.Realname.equals(type)) {
            if ("4".equals(source)) {
                // 病例研讨班详情
                DiscussDetailActivity.startActivity(activity, msgId, PublishType.Realname);
            } else {
                RealNameDetailActivity.start(activity, msgId);
            }
        } else {
            NoNameDetailActivity.start(activity, msgId);
        }

    }

    /**
     * 删除评论
     *
     * @param activity
     * @param commentId  当该commentId对应的评论 为当前登录用户发表时才能调用 调用前先做是否为当前用户校验
     * @param subscriber
     */
    public static void deleteComment(Activity activity, final String commentId, final Subscriber subscriber) {
        new XywyPNDialog.Builder()
                .setContent("删除这条评论？")
                .setPositiveStr("删除")
                .setNegativeStr("取消")
                .setNoTitle(true)
                .create(activity, new PNDialogListener() {
                    @Override
                    public void onPositive() {
                        String userId = YMUserService.getCurUserId();
                        DoctorCircleService.deleteComment(userId, commentId, subscriber);
                    }

                    @Override
                    public void onNegative() {

                    }
                }).show();
    }

    /**
     * 内容cell 分享 item 点击事件
     *
     * @param activity
     * @param paramBean
     */
    public static void shareItemClick(Activity activity, ShareClickParamBean paramBean) {

        if (TextUtils.isEmpty(paramBean.getSource())) {
            LogUtils.e("分享类型错误");
            shortToast("分享类型错误");
            return;
        }
        ShareSourceType shareSourceType = DCShareHelpUtils.getShareType(paramBean.getSource());
        String shareTitle = paramBean.getTitle();
        String shareUrl = paramBean.getUrl();
        String shareImageUrl = paramBean.getImageUrl();
        String extraData = paramBean.getExtraData();
        switch (shareSourceType) {
            case INFO:
                LogUtils.d("share link = " + paramBean.getUrl());
                DCShareHelpUtils.toShareInfoPage(activity, shareTitle, shareUrl, shareImageUrl, extraData);
                break;
            case COMMON_WEB:
                DCShareHelpUtils.toCommonWeb(activity, shareTitle, shareUrl, shareImageUrl, extraData);
                break;
            case MEDIA:
                DCShareHelpUtils.toShareMediaPage(activity, shareUrl);
                break;
            case VIDEO:
                // 原生视频分享
                LogUtils.d("video share other = " + extraData + ", "
                        + shareTitle);
                DCShareHelpUtils.toShareVideoPage(activity, extraData);
                break;
            case ANSWER:
                // 医学试题
                DCShareHelpUtils.toShareAnswerPage(activity, extraData);
                break;
            case SHARE_WEB:
                DCShareHelpUtils.toShareWebPage(activity, shareTitle, shareUrl, shareImageUrl);
                break;

        }

    }


    /**
     * 医圈消息
     *
     * @param activity
     */
    public static void circleMsg(Activity activity, Messages msgs, @PublishType String type) {
        if (Anonymous.equals(type)){
            msgs.setNunread("0");
        }
        else {
            msgs.setSunread("0");
        }
        YmRxBus.notifyMessageCountChanged(msgs);
        DoctorMessageNotify.startActivity(activity, type);
    }


    /**
     * 查看了我的人
     *
     * @param activity
     */
    public static void theUsersSeeMe(Activity activity) {
        InterestPersonActivity.startActivity(activity, "2");
    }

    /**
     * 查看更多 推荐的朋友
     *
     * @param activity
     * @param paramBean
     */
    public static void interestingPerson(Activity activity, InterestPersonParamBean paramBean) {
        InterestPersonActivity.startActivity(activity, paramBean.getType());
    }

    /**
     * 话题事件处理
     *
     * @param activity
     * @param topicParamBean
     */
    public static void topicClick(final Activity activity, final TopicParamBean topicParamBean) {
        //TODO:具体位置打点统计事件
        Intent intent = new Intent(activity, NewTopicDetailActivity.class);
        intent.putExtra(NewTopicDetailActivity.TOPICID_INTENT_PARAM_KEY, topicParamBean.getId());
        activity.startActivity(intent);
    }

    /**
     * 更多话题按钮
     *
     * @param activity
     */
    public static void moreTopicClick(final Activity activity) {
        StatisticalTools.eventCount(activity, "yqListTopicMore");
        Intent intent = new Intent(activity, MoreTopicActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 删除一条医圈动态
     *
     * @param activity
     * @param msgId
     * @param subscriber
     */
    public static void deleteMsg(Activity activity, final String msgId, final Subscriber subscriber) {
        if (YMUserService.isLoginUser(activity)) {
            // 弹出删除提示框
            new XywyPNDialog.Builder()
                    .setTitle("删除动态")
                    .setContent("是否删除动态？")
                    .setPositiveStr("删除")
                    .setNegativeStr("取消")
                    .create(activity, new PNDialogListener() {
                        @Override
                        public void onPositive() {
                            //删除消息接口逻辑
                            DoctorCircleService.deleteCircleMsg(msgId, new CommonResponse<BaseResultBean>() {
                                @Override
                                public void onNext(BaseResultBean baseResultBean) {
                                    shortToast("删除成功");
                                    //调用接口成功 通知界面刷新数据
                                    subscriber.onNext(baseResultBean);
                                }
                            });
                        }

                        @Override
                        public void onNegative() {
                            //do nothing
                        }
                    }).show();
        }
    }

    /**
     * 更多按钮 倒三角下拉按钮
     *
     * @param activity
     * @param moreParamBean
     */
    public static void moreClick(final Activity activity, final MoreParamBean moreParamBean, final Subscriber subscriber) {

        boolean isDeleteShow = moreParamBean.getToUserId().equals(YMApplication.getPID());
        PopupWindowType popupWindowType = new PopupWindowType(moreParamBean.getRelation(), isDeleteShow);


        new DoctorCircleBottomPopupWindow(activity, popupWindowType, new PopupWindowListener() {
            @Override
            public void onAddFriend() {

                String userId = moreParamBean.getToUserId();

                toAddFriend(activity, userId);
            }

            @Override
            public void onReport() {

                if (!YMUserService.isGuest()) {

                    ReportActivity.start(activity, moreParamBean.getMsgId(), Constants.REPORT_STYLE_CIRCLE);
                } else {
                    DialogUtil.LoginDialog(new YMOtherUtils(activity).context);
                }
            }

            @Override
            public void onDelete() {
                deleteMsg(activity, moreParamBean.getMsgId(), subscriber);
            }
        }).show();
    }

    public static void toAddFriend(Activity activity, String userId) {
        StatisticalTools.eventCount(activity, "yqRecomAddfriends");
        if (YMUserService.isGuest()) {// 登陆
            DialogUtil.LoginDialog(new YMOtherUtils(activity).context);
            return;
        }

        Intent intent = new Intent(activity, AddCardHoldVerifyActiviy.class);
        intent.putExtra("toAddUsername", "did_" + userId);
        activity.startActivity(intent);
    }

    /**
     * 医圈分享按钮
     *
     * @param activity
     * @param shareParamBean
     */
    public static void share(Activity activity, ShareParamBean shareParamBean) {
        if(!YMUserService.isLoginUser(activity)){
            return;
        }

        ShareUtil shareUtil = new ShareUtil.Builder()
                .setTitle(shareParamBean.getTitle())
                .setText(shareParamBean.getContent())
                .setTargetUrl(shareParamBean.getTargetUrl())
                .setImageUrl(shareParamBean.getImgUrl())
                .setShareId(shareParamBean.getShareId())
                .build(activity);

        switch (shareParamBean.getShareDirectionType()) {
            case OUTER_SHARE:
                shareUtil.outerShare();
                break;
            case INNER_SHARE:
                shareUtil.innerShare();
                break;
            default:
                break;
        }


    }

    /**
     * 医圈 弹出评论框
     *
     * @param activity
     * @param commentParamBean
     */
    public static void showCommentCircleMsgWindow(Activity activity, final CommentParamBean commentParamBean) {
        commentParamBean.setCommentType(DCCommentType.TO_CIRCLE_MSG);
        showCommentDialog(activity, commentParamBean);
    }


    /**
     * 医圈 弹出评论框
     *
     * @param activity
     * @param commentParamBean
     */
    public static void showCommentCommentMsgWindow(Activity activity, final CommentParamBean commentParamBean) {
        commentParamBean.setCommentType(DCCommentType.TO_COMMENT_MSG);
        showCommentDialog(activity, commentParamBean);
    }

    private static void showCommentDialog(final Activity activity, final CommentParamBean commentParamBean) {
//        if(isInvalidDCUser(commentParamBean.getMsgType(),"评论实名状态")){
//            return;
//        }
//        new DoctorCircleCommentPopupWindow(activity, commentParamBean.getCommentType() == DCCommentType.TO_CIRCLE_MSG ? "" : commentParamBean.getToUserName(), true, new SendMsgWindowListener() {
//            @Override
//            public void onSend(String content) {
//                commentParamBean.setContent(content);
//                DCMiddlewareService.comment(commentParamBean);
//            }
//        }).show();
        DialogUtil.showUserCenterCertifyDialog(activity, new MyCallBack() {
                    @Override
                    public void onClick(Object object) {
                        new DoctorCircleCommentPopupWindow(activity, commentParamBean.getCommentType() == DCCommentType.TO_CIRCLE_MSG ? "" : commentParamBean.getToUserName(), true, new SendMsgWindowListener() {
                            @Override
                            public void onSend(String content) {
                                commentParamBean.setContent(content);
                                DCMiddlewareService.comment(commentParamBean);
                            }
                        }).show();
                    }
                }, null, null);
    }


    /**
     * 医圈 点击头像或用户名
     *
     * @param activity
     * @param doctorInfoParam
     */
    public static void headOrNameClick(Activity activity, DoctorInfoParam doctorInfoParam) {
        if (doctorInfoParam.getType() != null) {
            if (("seminar").equals(doctorInfoParam.getType()) || ("zixun").equals(doctorInfoParam.getType())) {
                Intent senIntent = new Intent(activity, DiscussSettingsActivity.class);
                senIntent.putExtra("uuid", doctorInfoParam.getUserId());
                senIntent.putExtra("isDoctor", doctorInfoParam.getRelation());
                activity.startActivity(senIntent);
            } else if (("media").equals(doctorInfoParam.getType())) {
                Intent medIntent = new Intent(activity, MediaSettingActivity.class);
                activity.startActivity(medIntent);
            } else {
                Intent itemIntent = new Intent(activity, PersonDetailActivity.class);
                itemIntent.putExtra("uuid", doctorInfoParam.getUserId());
                itemIntent.putExtra("isDoctor", doctorInfoParam.getRelation());
                activity.startActivity(itemIntent);
            }
        }
    }


    /**
     * 点赞
     *
     * @param praiseParamBean
     */
    public static void praise(PraiseParamBean praiseParamBean) {
//        if (isInvalidDCUser(praiseParamBean.getMsgType(),"为实名动态点赞")) return;

        switch (praiseParamBean.getMsgType()) {
            case REAL_NAME:

                DoctorCircleService.praiseRealName(praiseParamBean.getMsgId(), praiseParamBean.getCommentId(), praiseParamBean.getToUserId(), praiseParamBean.getSubscriber());
                break;
            case NO_NAME:
                DoctorCircleService.praiseRealName(praiseParamBean.getMsgId(), praiseParamBean.getCommentId(), praiseParamBean.getToUserId(), praiseParamBean.getSubscriber());
                break;
            default:
                break;
        }

    }

//    /**
//     * 检测医圈动态 操作用户身份 匿名消息只检测是否为登录用户 实名消息 检测登录状态&&信息是否完善
//     * @param msgType 实名消息or匿名消息
//     * @param noticeStr 提示语
//     * @return
//     */
//    public static boolean isInvalidDCUser(DCMsgType msgType, String noticeStr) {
//        Activity activity = YMApplication.getInstance().getTopActivity();
//        if (null!=activity) {
//            if(DCMsgType.REAL_NAME == msgType){
////                    return !YMUserService.isUserInfoComplete(activity, noticeStr);
//                    return !YMUserService.isUserApproved(activity);
//            }else {
//                    return !YMUserService.isLoginUser(activity);
//            }
//        }else {
//            return true;
//        }
//
//    }

    /**
     * 评论
     *
     * @param commentParamBean
     */
    private static void comment(CommentParamBean commentParamBean) {
//        if (isInvalidDCUser(commentParamBean.getMsgType(),"评论实名动态")) return;

        switch (commentParamBean.getMsgType()) {
            case REAL_NAME:
                DoctorCircleService.commentRealName(commentParamBean.getMsgId(), commentParamBean.getCommentId(), commentParamBean.getToUserId(), commentParamBean.getContent(), commentParamBean.getSubscriber());
                break;
            case NO_NAME:
                DoctorCircleService.commentNoName(commentParamBean.getFakeUserId(), commentParamBean.getMsgId(), commentParamBean.getCommentId(), commentParamBean.getToUserId(), commentParamBean.getContent(), commentParamBean.getSubscriber());

                break;
            default:
                break;
        }
    }
}
