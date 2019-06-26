package com.xywy.askforexpert.module.discovery.answer;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.answer.api.ScoreBean;
import com.xywy.askforexpert.model.answer.api.answered.AnsweredListPageBean;
import com.xywy.askforexpert.model.answer.api.deletewrong.DeleteWrongQuestionResultBean;
import com.xywy.askforexpert.model.answer.api.deletewrong.DeleteWrongSetResultBean;
import com.xywy.askforexpert.model.answer.api.paperlist.PaperListPageBean;
import com.xywy.askforexpert.model.answer.api.set.SetPageBean;
import com.xywy.askforexpert.model.answer.api.wrongquestion.WrongPaperPageBean;
import com.xywy.askforexpert.model.answer.local.ExamPaper;

import rx.Subscriber;

/**
 * Created by bailiangjin on 16/8/17.
 */
public class AnswerService {
    /**
     * 获取试卷分类列表 1383
     *
     * @param subscriber
     */
    public static void getSetList(final Subscriber<SetPageBean> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().getSetList(YMUserService.getCurUserId(), new CommonResponse<SetPageBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(SetPageBean setPageBeanBaseData) {
                LogUtils.d("setPageBeanBaseData:" + GsonUtils.toJson(setPageBeanBaseData));
                if (null == setPageBeanBaseData) {
                    return;
                }

                subscriber.onNext(setPageBeanBaseData);

            }
        });
    }

    /**
     * 获取 试卷列表 1384
     *
     * @param subscriber
     */
    public static void getPaperList(String classId, final Subscriber<PaperListPageBean> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().getPaperList(YMUserService.getCurUserId(), classId, new CommonResponse<PaperListPageBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(PaperListPageBean setPageBeanBaseData) {
                LogUtils.d("setPageBeanBaseData:" + GsonUtils.toJson(setPageBeanBaseData));
                if (null == setPageBeanBaseData) {
                    return;
                }
                subscriber.onNext(setPageBeanBaseData);

            }
        });
    }

    /**
     * 获取 试卷内容信息 1385
     *
     * @param subscriber
     */
    public static void getPaperContent(String paperId, final Subscriber<ExamPaper> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().getPaperContent(YMUserService.getCurUserId(), paperId, new CommonResponse<ExamPaper>(YMApplication.getAppContext()) {
            @Override
            public void onNext(ExamPaper examPaper) {
                LogUtils.d("setPageBeanBaseData:" + GsonUtils.toJson(examPaper));
                if (null == examPaper) {
                    return;
                }
                subscriber.onNext(examPaper);
            }
        });
    }

    /**
     * 提交答题信息  1386
     *
     * @param subscriber
     */
    public static void submitForScore(String paperId, int score, String wrongQuestionArrayStr, final Subscriber<ScoreBean> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().submitForScore(YMUserService.getCurUserId(), paperId, score, wrongQuestionArrayStr, new CommonResponse<ScoreBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(ScoreBean scoreBean) {
                LogUtils.d("setPageBeanBaseData:" + GsonUtils.toJson(scoreBean));
                if (null == scoreBean) {
                    return;
                }
                subscriber.onNext(scoreBean);
            }
        });
    }

    /**
     * 获取已答试题记录  1387
     *
     * @param subscriber
     */
    public static void getAnsweredPaperList(final Subscriber<AnsweredListPageBean> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().getAnsweredPaperList(YMUserService.getCurUserId(), new CommonResponse<AnsweredListPageBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(AnsweredListPageBean answeredListPageBean) {
                LogUtils.d("setPageBeanBaseData:" + GsonUtils.toJson(answeredListPageBean));
                if (null == answeredListPageBean) {
                    return;
                }
                subscriber.onNext(answeredListPageBean);
            }
        });
    }

    /**
     * 获取错题集详情  1388
     *
     * @param subscriber
     */
    public static void getPaperWrongQuestionList(String paperId, final Subscriber<WrongPaperPageBean> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().getPaperWrongQuestionList(YMUserService.getCurUserId(), paperId, new CommonResponse<WrongPaperPageBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(WrongPaperPageBean wrongPaperPageBean) {
                LogUtils.d("setPageBeanBaseData:" + GsonUtils.toJson(wrongPaperPageBean));
                if (null == wrongPaperPageBean) {
                    return;
                }
                subscriber.onNext(wrongPaperPageBean);
            }
        });
    }

    /**
     * 删除错题集  1389
     *
     * @param subscriber
     */
    public static void deleteWrongQuestionSet(String paperId, String action, final Subscriber<DeleteWrongSetResultBean> subscriber) {
        //网络获取最新数据
        RetrofitServiceProvider.getInstance().deleteWrongQuestionSet(YMUserService.getCurUserId(), paperId, action, new CommonResponse<DeleteWrongSetResultBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(DeleteWrongSetResultBean deleteWrongQuestionResultBean) {
                LogUtils.d("setPageBeanBaseData:" + GsonUtils.toJson(deleteWrongQuestionResultBean));
                if (null == deleteWrongQuestionResultBean) {
                    return;
                }
                subscriber.onNext(deleteWrongQuestionResultBean);
            }
        });
    }


    /**
     * 删除单个错题  1390
     *
     * @param subscriber
     */
    public static void deleteWrongQuestion(String paperId, String questionId, final Subscriber<DeleteWrongQuestionResultBean> subscriber) {

        //网络获取最新数据
        RetrofitServiceProvider.getInstance().deleteWrongQuestion(YMUserService.getCurUserId(), paperId, questionId, new CommonResponse<DeleteWrongQuestionResultBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(DeleteWrongQuestionResultBean deleteWrongQuestionResultBean) {
                LogUtils.d("setPageBeanBaseData:" + GsonUtils.toJson(deleteWrongQuestionResultBean));
                if (null == deleteWrongQuestionResultBean) {
                    return;
                }
                subscriber.onNext(deleteWrongQuestionResultBean);
            }
        });
    }


}
