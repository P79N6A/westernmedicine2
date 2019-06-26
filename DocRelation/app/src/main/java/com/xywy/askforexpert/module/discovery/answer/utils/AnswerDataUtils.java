package com.xywy.askforexpert.module.discovery.answer.utils;

import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.answer.api.answered.AnsweredParamItem;
import com.xywy.askforexpert.model.answer.api.paper.Question;
import com.xywy.askforexpert.model.answer.show.AnsweredListItem;
import com.xywy.askforexpert.module.discovery.answer.activity.AnswerDetailActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 数据工具类
 * Created by bailiangjin on 16/4/19.
 */
public class AnswerDataUtils {




    /**
     * 将要删除的map 转为 json数组
     *
     * @param deleteParamMap 要删除的 条目集合
     * @return
     */
    public static String toDeleteParamJson(Map<String, AnsweredListItem> deleteParamMap) {

        List<AnsweredParamItem> paramItemList = new ArrayList<>();
        if (null != deleteParamMap) {
            Iterator iterator = deleteParamMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, AnsweredListItem> entry = (Map.Entry<String, AnsweredListItem>) iterator.next();
                AnsweredListItem item = entry.getValue();
                AnsweredParamItem answeredParamItem = new AnsweredParamItem();
                answeredParamItem.setId(item.getId());
                answeredParamItem.setType(item.getType());
                paramItemList.add(answeredParamItem);
            }
        }
        return GsonUtils.toJson(paramItemList);
    }




    public static String getQuestionContent(Question curQuestion, int showType) {
        if (null == curQuestion) {
            LogUtils.e("curQuestion is null");
            return "";
        }
        String content = "";
        switch (curQuestion.getShowType()) {

            case Question.SHOW_TYPE_NOT_SUPPORT:
                //不显示题目共用内容
                content = "旧版本不支持该题型 可能显示错误 请升级最新版本 以便正常使用";
                break;
            case Question.SHOW_TYPE_NORMAL:
                //不显示题目共用内容
                content = null;
                break;
            case Question.SHOW_TYPE_COMMON_CONTENT:
                if (AnswerDetailActivity.SHOW_TYPE_PAPER == showType) {
                    //试题
                    content = curQuestion.getQuestionNumberStr() + "题(共用题干)  " + curQuestion.getBackgroundMaterial() + " (" + curQuestion.getGroupScore() + "分)";
                } else {
                    //错题
                    content = curQuestion.getBackgroundMaterial();
                }

                break;
            case Question.SHOW_TYPE_COMMON_SELECT:
                if (AnswerDetailActivity.SHOW_TYPE_PAPER == showType) {
                    //试题
                    content = curQuestion.getQuestionNumberStr() + "题(共用选项)" + " (" + curQuestion.getGroupScore() + "分)";
                } else {
                    //错题
                    content = curQuestion.getBackgroundMaterial();
                }
                break;
            default:
                break;
        }
        return content;
    }


    /**
     * 判断试题是否答完
     *
     * @param questionList 试题List
     * @return
     */
    public static boolean isAnswered(List<Question> questionList) {
        if (null == questionList || questionList.isEmpty()) {
            return false;
        }
        for (Question question : questionList) {
            if (question.isAnswered()) {
                return true;
            }
        }
        return false;
    }





}
