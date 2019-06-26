package com.xywy.askforexpert.module.discovery.answer.constants;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.YMApplication;

/**
 * Created by bailiangjin on 16/7/13.
 */
public class AnswerShareConstants {

    public static final String shareId = "";
    public static final String shareTitle = YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)?"闻康医生助手试题":"医脉试题";
    public static final String shareSource = "6";
    public static final String shareSourceType_Home = "answer_home";//试题首页
    public static final String shareSourceType_List = "answer_list";//试题列表页
    public static final String shareSourceType_Detail = "answer_detail";//试题答题页
}
