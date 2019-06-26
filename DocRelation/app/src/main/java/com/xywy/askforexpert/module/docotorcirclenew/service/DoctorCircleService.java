package com.xywy.askforexpert.module.docotorcirclenew.service;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.model.doctor.DynamicDtaile;
import com.xywy.askforexpert.model.doctor.MoreMessage;
import com.xywy.askforexpert.model.newdoctorcircle.CommentResultBean;
import com.xywy.askforexpert.model.newdoctorcircle.PraiseResultBean;

import rx.Subscriber;

/**
 * 医圈 请求服务端 Service
 * Created by bailiangjin on 2016/11/4.
 */
public class DoctorCircleService {

    /**
     * 医圈动态类型 实名
     */
    public static final String MSG_TYPE_REAL_NAME = "1";

    /**
     * 医圈动态类型 匿名
     */
    public static final String MSG_TYPE_NO_NAME = "2";


    /**
     * 实名点赞
     *
     * @param msgId
     * @param commentId
     * @param toUserId
     * @param subscriber
     */
    public static void praiseRealName(String msgId, String commentId, String toUserId, final Subscriber<PraiseResultBean> subscriber) {
        praise(msgId, commentId, toUserId, MSG_TYPE_REAL_NAME, subscriber);
    }

    /**
     * 匿名点赞
     *
     * @param msgId
     * @param commentId
     * @param toUserId
     * @param subscriber
     */
    public static void praiseNoName(String msgId, String commentId, String toUserId, final Subscriber<PraiseResultBean> subscriber) {
        praise(msgId, commentId, toUserId, MSG_TYPE_NO_NAME, subscriber);
    }


    /**
     * 实名评论
     *
     * @param msgId
     * @param commentId
     * @param toUserId
     * @param content
     * @param subscriber
     */
    public static void commentRealName(String msgId, final String commentId, String toUserId, String content, final Subscriber<CommentResultBean> subscriber) {

        comment(YMUserService.getCurUserId(),msgId, commentId, toUserId, content, MSG_TYPE_REAL_NAME, subscriber);
    }

    /**
     * 匿名评论
     *
     * @param fakeUserId 匿名用户id 非当前用户id
     * @param msgId
     * @param commentId
     * @param toUserId
     * @param content
     * @param subscriber
     */
    public static void commentNoName(String fakeUserId,String msgId, final String commentId, String toUserId, String content, final Subscriber<CommentResultBean> subscriber) {

        comment(fakeUserId,msgId, commentId, toUserId, content, MSG_TYPE_NO_NAME, subscriber);
    }

    /**
     * 点赞 290
     *
     * @param subscriber
     */
    private static void praise(String msgId, String commentId, String toUserId, String type, final Subscriber<PraiseResultBean> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().praise(YMUserService.getCurUserId(), msgId, commentId, toUserId, type, new CommonResponse<PraiseResultBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(PraiseResultBean praiseResultBean) {
                LogUtils.d("praiseResultBean:" + GsonUtils.toJson(praiseResultBean));
                if (null == praiseResultBean) {
                    return;
                }
                subscriber.onNext(praiseResultBean);

            }
        });
    }


    /**
     * 评论 286
     *
     * @param subscriber
     */
    private static void comment(String curUserId,String msgId, final String commentId, String toUserId, final String content, String type, final Subscriber<CommentResultBean> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().comment(curUserId, msgId, commentId, toUserId, content, type, new Subscriber<CommentResultBean>() {
            @Override
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override
            public void onNext(CommentResultBean commentResultBean) {
                LogUtils.d("commentResultBean:" + GsonUtils.toJson(commentResultBean));
                if (null == commentResultBean) {
                    return;
                }
                commentResultBean.setCommentStr(content);
                subscriber.onNext(commentResultBean);

            }
        });
    }

    /**
     * 删除评论 287
     *
     * @param subscriber
     */
    public static void deleteComment(String userId, final String commentId, final Subscriber<BaseResultBean> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().deleteComment(userId, commentId, new CommonResponse<BaseResultBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseResultBean baseResultBean) {
                LogUtils.d("删除评论baseResultBean:" + GsonUtils.toJson(baseResultBean));
                if (null == baseResultBean) {
                    return;
                }
                subscriber.onNext(baseResultBean);

            }
        });
    }


    /**
     * 删除医圈动态 283
     *
     * @param msgId
     * @param subscriber
     */
    public static void deleteCircleMsg(final String msgId, final Subscriber<BaseResultBean> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().deleteCircleMsg(YMUserService.getCurUserId(), msgId, new CommonResponse<BaseResultBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(BaseResultBean baseResultBean) {
                LogUtils.d("删除医圈动态baseResultBean:" + GsonUtils.toJson(baseResultBean));
                if (null == baseResultBean) {
                    return;
                }
                subscriber.onNext(baseResultBean);

            }
        });
    }


    /**
     * 获取医圈动态 详情页信息 实名
     *
     * @param msgId
     * @param subscriber
     */
    public static void getDynamicDetailPageInfo(final String msgId, final Subscriber<DynamicDtaile> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().getDynamicDetailPageInfo(YMUserService.getCurUserId(), msgId, new CommonResponse<DynamicDtaile>(YMApplication.getAppContext()) {
            @Override
            public void onNext(DynamicDtaile dynamicDtaile) {
                LogUtils.d("获取医圈动态dynamicDtaile:" + GsonUtils.toJson(dynamicDtaile));
                if (null == dynamicDtaile) {
                    return;
                }
                subscriber.onNext(dynamicDtaile);

            }
        });
    }

    /**
     * 获取医圈动态 详情页信息 实名
     *
     * @param msgId
     * @param subscriber
     */
    public static void getDynamicDetailPageInfoWithLastPage(final String msgId, int page,final Subscriber<DynamicDtaile> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().getDynamicDetailPageInfoWithLastPage(YMUserService.getCurUserId(), msgId,page, new CommonResponse<DynamicDtaile>(YMApplication.getAppContext()) {
            @Override
            public void onNext(DynamicDtaile dynamicDtaile) {
                LogUtils.d("获取医圈动态dynamicDtaile:" + GsonUtils.toJson(dynamicDtaile));
                if (null == dynamicDtaile) {
                    return;
                }
                subscriber.onNext(dynamicDtaile);

            }
        });
    }


    /**
     * 获取医圈动态 详情页信息 实名
     *
     * @param msgId
     * @param subscriber
     */
    public static void getDynamicDetailMoreData(final String msgId, int page, final Subscriber<MoreMessage> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().getDynamicDetailMoreComment(msgId, page, new CommonResponse<MoreMessage>(YMApplication.getAppContext()) {
            @Override
            public void onNext(MoreMessage moreMessage) {
                LogUtils.d("获取医圈动态dynamicDtaile:" + GsonUtils.toJson(moreMessage));
                if (null == moreMessage) {
                    return;
                }
                subscriber.onNext(moreMessage);

            }
        });
    }
}
