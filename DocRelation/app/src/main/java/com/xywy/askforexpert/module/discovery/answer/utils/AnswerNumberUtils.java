package com.xywy.askforexpert.module.discovery.answer.utils;

/**
 * Created by bailiangjin on 16/6/13.
 */
public class AnswerNumberUtils {

    /**
     * 中文试题号 最大支持到二十
     */
    public static final String[] CN_QUESTION_NUMBER =
            {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
                    "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十"};

    public static String getCNNnumber(int number) {
        if (number <= 0) {
            return "题号为零或负值";
        }
        if (number > 20) {
            return "" + number;
        }
        return CN_QUESTION_NUMBER[number - 1];
    }
}
