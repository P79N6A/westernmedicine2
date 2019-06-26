package com.xywy.askforexpert.module.discovery.answer.utils;

import android.app.Activity;

import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.answer.local.AnswerShareParam;
import com.xywy.askforexpert.module.discovery.answer.AnswerMainActivity;
import com.xywy.askforexpert.module.discovery.answer.activity.AnswerDetailActivity;
import com.xywy.askforexpert.module.discovery.answer.activity.AnswerMultiLevelListActivity;

/**
 * Created by bailiangjin on 16/7/19.
 */
public class AnswerShareHelpUtils {

    public static void start(Activity activity, AnswerShareParam answerShareParam) {

        if (null == activity || null == answerShareParam) {
            ToastUtils.shortToast( "分享打开试题传入参数为空");
            return;
        } else {
            switch (answerShareParam.getType()) {
                case 1:
                    //试题首页
                    AnswerMainActivity.start(activity);
                    break;
                case 2:
                    //试题列表页
                    AnswerMultiLevelListActivity.start(activity, answerShareParam.toTestSet());
                    break;
                case 3:
                    //试题详情页
                    AnswerDetailActivity.start(activity, answerShareParam.toPaperItem(), AnswerDetailActivity.SHOW_TYPE_PAPER);
                    break;
                default:
                    ToastUtils.shortToast( "分享打开试题传入类型错误");
                    break;
            }
        }
    }
}
