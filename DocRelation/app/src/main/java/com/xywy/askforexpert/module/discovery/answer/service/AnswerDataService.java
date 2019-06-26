package com.xywy.askforexpert.module.discovery.answer.service;

import android.text.TextUtils;

import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.answer.local.ExamPaper;

import java.util.HashMap;
import java.util.Map;

/**
 * 试题数据 内存缓存类
 * 枚举形式 实现单例
 * Created by bailiangjin on 16/7/16.
 */
public enum AnswerDataService {
    INSTANCE;


    Map<String, ExamPaper> examPaperHashMap;


    /**
     * 构造方法 参数初始化 单例形式 只会初始化一次 避免不必要的资源开支
     */
    private AnswerDataService() {

        examPaperHashMap = new HashMap<>();

    }

    public boolean addExamPaper(ExamPaper examPaper) {
        if (null == examPaper || TextUtils.isEmpty(examPaper.getId())) {
            LogUtils.e("examPaper is null or examPaperId is null");
            return false;
        }
        examPaperHashMap.put(examPaper.getId(), examPaper);
        return true;
    }

    public ExamPaper getExamPaper(String examPaperId) {

        if (TextUtils.isEmpty(examPaperId)) {
            LogUtils.e("examPaperId is null");
            return null;
        }
        return examPaperHashMap.get(examPaperId);
    }

    public boolean removeExamPaper(String examPaperId) {

        if (TextUtils.isEmpty(examPaperId)) {
            LogUtils.e("examPaperId is null");
            return false;
        }

        if (examPaperHashMap.containsKey(examPaperId)) {
            examPaperHashMap.remove(examPaperId);
            return true;
        }

        return false;
    }

}







