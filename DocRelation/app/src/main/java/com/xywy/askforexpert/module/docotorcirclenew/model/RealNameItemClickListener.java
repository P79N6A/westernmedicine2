package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ClickUtil;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMDebugOnClickListener;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.model.api.BaseStringResultBean;
import com.xywy.askforexpert.model.doctor.CommentBean;
import com.xywy.askforexpert.model.doctor.PraiseBean;
import com.xywy.askforexpert.model.doctor.RealNameItem;
import com.xywy.askforexpert.model.doctor.Share;
import com.xywy.askforexpert.model.doctor.Touser;
import com.xywy.askforexpert.model.doctor.User;
import com.xywy.askforexpert.model.newdoctorcircle.CommentResultBean;
import com.xywy.askforexpert.model.newdoctorcircle.PraiseResultBean;
import com.xywy.askforexpert.model.topics.MoreTopicItem;
import com.xywy.askforexpert.module.discovery.DoctorOneDayActivity;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.activity.NewTopicDetailActivity;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.CommentParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.DoctorInfoParam;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.MoreParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.PraiseParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareClickParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.ShareDirectionType;
import com.xywy.askforexpert.module.docotorcirclenew.service.DCMiddlewareService;
import com.xywy.askforexpert.module.docotorcirclenew.service.DcCommonClickService;
import com.xywy.askforexpert.module.doctorcircle.InterestPersonActivity;
import com.xywy.askforexpert.module.doctorcircle.topic.MoreTopicActivity;

import java.util.Iterator;
import java.util.List;

public class RealNameItemClickListener extends YMDebugOnClickListener {
    DcCommonClickService clickService = new DcCommonClickService();
    Activity context;
    IRecycleViewModel recycleViewModel;

    public RealNameItemClickListener(Activity context, IRecycleViewModel recycleViewModel) {
        this.context = context;
        this.recycleViewModel = recycleViewModel;
    }


    public void shareTo(Object value) {
        StatisticalTools.eventCount(context, "yqListShare");

        if (!NetworkUtil.isNetWorkConnected()) {
            ToastUtils.shortToast("网络异常，请检查网络连接");
            return;
        }
        RealNameItem shredItem = (RealNameItem) value;
        Share share1 = shredItem.share;
        String share_title = share1.share_title;
        String[] splitTitles = null;
        if (share_title != null && share_title.contains("\n")) {
            splitTitles = share_title.split("\\n");
        }
        String shareTitle = shredItem.content.replaceAll(Constants.TOPIC, "");
        String shareContent = shredItem.content.replaceAll(Constants.TOPIC, "");
        if (shareTitle == null || "".equals(shareTitle)) {
            if (splitTitles != null && splitTitles.length > 0) {
                shareTitle = splitTitles[0];
            } else {
                if (share_title == null || "".equals(share_title)) {
                    shareTitle = "医脉";
                } else {
                    shareTitle = share_title;
                }
            }
        }
        if (shareContent == null || "".equals(shareContent)) {
            if (splitTitles != null && splitTitles.length > 1) {
                shareContent = splitTitles[1];
            } else {
                shareContent = "分享自医脉";
            }
        }
        String shareImg = "";
        if (shredItem.minimgs != null && shredItem.minimgs.size() > 0) {
            shareImg = shredItem.minimgs.get(0);
        } else {
            shareImg = Constants.COMMON_SHARE_IMAGE_URL;
        }
        if (shareImg.equals(Constants.COMMON_SHARE_IMAGE_URL) && share1.share_img != null && !share1.share_img.equals("")) {
            shareImg = share1.share_img;
        }
        clickService.onShareBtnClick(context, new ShareParamBean(shareTitle, shareContent, shredItem.url, shareImg, shredItem.id, ShareDirectionType.OUTER_SHARE));
    }


    public void praise(final Object value) {
        StatisticalTools.eventCount(context, "yqListPraise");
        final RealNameItem item = (RealNameItem) value;
        clickService.onPraiseClick(new PraiseParamBean(item.id, "0", item.getUserid(), DCMsgType.REAL_NAME, new CommonResponse<PraiseResultBean>(YMApplication.getInstance().getApplicationContext()) {
            @Override
            public void onNext(PraiseResultBean praiseResultBean) {
                String is_praise = item.is_praise;
                if ("1".equals(is_praise)) {
                    item.is_praise = "0";
                    item.praiseNum = "" + (Integer.parseInt(item.praiseNum) - 1);
                    ToastUtils.shortToast("取消点赞成功");
                    Iterator<PraiseBean> it = item.praiselist.iterator();
                    while (it.hasNext()) {
                        PraiseBean praiseListitenm = it.next();
                        if (praiseListitenm.userid.equals(YMApplication.getUUid())) {
                            it.remove();
                        }
                    }
                } else {
                    item.is_praise = "1";
                    item.praiseNum = "" + (Integer.parseInt(item.praiseNum) + 1);
                    ToastUtils.shortToast("点赞成功");
                    List<PraiseBean> praiselis = item.praiselist;
                    PraiseBean praiseBean2 = new PraiseBean();
                    praiseBean2.userid = YMApplication.getUUid();
                    praiseBean2.nickname = YMApplication.getLoginInfo().getData().getRealname();
                    praiselis.add(0, praiseBean2);
                }
                recycleViewModel.notifyDataChanged();
            }
        }));

    }

    /**
     * 评论医圈消息中的评论
     *
     * @param toMsg
     * @param toComment
     */
    public void replyComment(final Object toMsg, final CommentBean toComment) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        StatisticalTools.eventCount(context, "yqListComment");


        final RealNameItem item = (RealNameItem) toMsg;
        //回复评论
        User user = toComment.user;
        if (YMApplication.getUUid().equals(user.userid)) {
            clickService.onDeleteCommentClick(context, toComment.getId(), new CommonResponse() {
                @Override
                public void onNext(Object o) {
                    ToastUtils.shortToast("删除成功");

                    item.commlist.remove(toComment);
                    // 评论数量 -1
                    item.commentNum = "" + (Integer.parseInt(item.commentNum) - 1);
                    recycleViewModel.notifyDataChanged();
                }
            });
            return;
        }

        clickService.onReplyCommentClick(context, new CommentParamBean(item.id, toComment.getId(), toComment.getUser().getNickname(), toComment.getUser().getUserid(), DCMsgType.REAL_NAME, new CommonResponse<CommentResultBean>() {
            @Override
            public void onNext(CommentResultBean commentResultBean) {

                if (null != commentResultBean) {
                    if (BaseStringResultBean.CODE_SUCCESS.equals(commentResultBean.getCode())) {
                        ToastUtils.shortToast("评论成功");
                        //TODO:刷新界面 替换原来的垃圾逻辑
                        updateData(commentResultBean, item, toComment);

                    } else if ("-3".equals(commentResultBean.getCode())) {
                        CommonUtils.showWarnDialog(context, commentResultBean.getMsg());
                    } else {
                        ToastUtils.shortToast("评论失败");
                    }
                }
            }
        }));
    }

    /**
     * 评论医圈消息
     *
     * @param toMsg
     */
    public void replyMsg(final Object toMsg) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        StatisticalTools.eventCount(context, "yqListComment");
        final RealNameItem item = (RealNameItem) toMsg;
        clickService.onReplyMsgClick(context, new CommentParamBean(item.id, DCMsgType.REAL_NAME, new CommonResponse<CommentResultBean>() {
            @Override
            public void onNext(CommentResultBean commentResultBean) {

                if (null != commentResultBean) {
                    if (BaseStringResultBean.CODE_SUCCESS.equals(commentResultBean.getCode())) {
                        ToastUtils.shortToast("评论成功");
                        //TODO:刷新界面 替换原来的垃圾逻辑
                        updateData(commentResultBean, item, null);

                    } else if ("-3".equals(commentResultBean.getCode())) {
                        CommonUtils.showWarnDialog(context, commentResultBean.getMsg());
                    } else {
                        ToastUtils.shortToast("评论失败");
                    }
                }
            }
        }));
    }


    public void showDetail(Object value) {
        final RealNameItem contentItem = (RealNameItem) value;
        if (!NetworkUtil.isNetWorkConnected()) {
            ToastUtils.shortToast(context.getString(R.string.no_network));//"网络连接异常，请检查网络连接");
            return;
        }
        if (contentItem != null) {
            clickService.onItemClick(context, contentItem.getId(), contentItem.getSource(), PublishType.Realname);

        }
    }


    public void showPersonalInfo(Object item) {
        final RealNameItem contentItem = (RealNameItem) item;
        clickService.onNameClick(context, new DoctorInfoParam(contentItem.user.getUserType(), contentItem.user.getUserid(), "" + contentItem.user.getRelation()));
    }


    public void showItemMenu(final Object item) {
        final RealNameItem contentItem = (RealNameItem) item;
        clickService.onMoreClick(context, new MoreParamBean(0, contentItem.id, contentItem.getUserid(), contentItem.getRelation(), DCMsgType.REAL_NAME), new CommonResponse<BaseResultBean>() {
            @Override
            public void onNext(BaseResultBean baseResultBean) {
                //删除完成 关闭界面
                recycleViewModel.deleteItem(contentItem);
                recycleViewModel.notifyDataChanged();
            }
        });
    }

    private void updateData(CommentResultBean result, RealNameItem item, CommentBean commentBean) {

        List<CommentBean> commlist = item.commlist;
        CommentBean newCommentBean = new CommentBean();
        User newUser = new User();
        Touser newTOuser = new Touser();
        if (null != commentBean) {// 对评论评论
            newUser.userid = YMApplication.getUUid();
            newUser.nickname = YMApplication.getLoginInfo().getData().getRealname();
            newUser.photo = YMApplication.getLoginInfo().getData().getPhone();
            newTOuser.userid = commentBean.user.userid;
            newTOuser.nickname = commentBean.user.nickname;
            newTOuser.photo = commentBean.user.photo;
        } else {// 消息评论
            newUser.userid = YMApplication.getUUid();
            newUser.nickname = YMApplication.getLoginInfo().getData().getRealname();
            newUser.photo = YMApplication.getLoginInfo().getData().getPhone();
        }
        newCommentBean.id = result.getCommentId();
        newCommentBean.content = result.getCommentStr();
        newCommentBean.user = newUser;
        newCommentBean.touser = newTOuser;
        if (null != commentBean) {
            commlist.add(newCommentBean);
        } else {
            commlist.add(newCommentBean);
        }

        // 评论数量加1
        item.commentNum = "" + (Integer.parseInt(item.commentNum) + 1);
        recycleViewModel.notifyDataChanged();
    }

    @Override
    public void onClick(View v) {
        final Object item = v.getTag();
        switch (v.getId()) {
            case R.id.ll_share://点击分享外链
                Share shareData = ((RealNameItem) item).getShare();
                DCMiddlewareService.shareItemClick((Activity) context, new ShareClickParamBean(shareData.getShare_source(), shareData.getShare_title(), shareData.getShare_link(), shareData.getShare_img(), shareData.getShare_other()));
                break;
            case R.id.find_more_tv://推荐更多医生
                StatisticalTools.eventCount(context, "yqRecomMore");
                InterestPersonActivity.startActivity(context, PublishType.Realname);
                break;
            case R.id.ll_doctor_share://分享
//                shareTo(item);
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                //添加验证认证状态 stone
                DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        shareTo(item);
                    }
                }, null, null);
                break;
            case R.id.ll_discuss:// 评论
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                replyMsg(item);
                break;
            case R.id.ll_praise:// 点赞
//                praise(item);
                if(!ClickUtil.isFastDoubleClick()){
                    DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {
                            praise(item);
                        }
                    }, null, null);
                }
                break;

            case R.id.iv_add:// 添加好友、删除、举报
//                showItemMenu(item);
                //添加验证认证状态
                DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        showItemMenu(item);
                    }
                }, null, null);
                break;


            case R.id.expandable_text://  点击动态内容进入动态详情
            case R.id.expand_collapse_view_group:
            case R.id.post_content:
            case R.id.tv_all_discusss:// 查看全部评论
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                showDetail(item);
                break;
            case R.id.iv_usrer_pic://点击用户头像跳转个人中心
                StatisticalTools.eventCount(context, "yqListAuthFace");
                showPersonalInfo(item);
                break;
            case R.id.tv_username://点击用户姓名跳转个人中心
                StatisticalTools.eventCount(context, "yqListAuthName");
                showPersonalInfo(item);
                break;
            case R.id.ll_userdetaile:
                clickService.onItemClick(context, ((RealNameItem) item).getId(), ((RealNameItem) item).getSource(), PublishType.Realname);
                break;
            case R.id.topic_rl_1:
                StatisticalTools.eventCount(context, "yqListTopic1");
                NewTopicDetailActivity.startActivity(context, ((MoreTopicItem.ListEntity) item).getId());
                break;
            case R.id.topic_rl_2:
                StatisticalTools.eventCount(context, "yqListTopic2");
                NewTopicDetailActivity.startActivity(context, ((MoreTopicItem.ListEntity) item).getId());
                break;
            case R.id.topic_rl_3:
                StatisticalTools.eventCount(context, "yqListTopic3");
                NewTopicDetailActivity.startActivity(context, ((MoreTopicItem.ListEntity) item).getId());
                break;
            case R.id.topic_rl_4:
//                StatisticalTools.eventCount(context, "yqListTopicMore");
//                Intent intent = new Intent(context, MoreTopicActivity.class);
//                context.startActivity(intent);
                break;
            case R.id.iv_topic:
                //stone 医圈-更多话题/热门话题
                StatisticalTools.eventCount(context,Constants.HOTTOPIC);
//                StatisticalTools.eventCount(context, "yqListTopicMore");

                Intent intent = new Intent(context, MoreTopicActivity.class);
                context.startActivity(intent);
                break;
            case R.id.iv_one_day:
                //中国医生的一天 stone
                StatisticalTools.eventCount(context, Constants.ONEDAYOFCHINESEDOCTOR);

//                StatisticalTools.eventCount(context, "yqListTopicMore");
                context.startActivity(new Intent(YMApplication.getAppContext(),
                        DoctorOneDayActivity.class));
                break;
            case R.id.ll_view:
                RealNameItem mRealNameItem = (RealNameItem) v.getTag(R.string.first_tag);
                CommentBean commentBean = (CommentBean) v.getTag(R.string.second_tag);
                replyComment(mRealNameItem, commentBean);
                break;

        }
    }
}