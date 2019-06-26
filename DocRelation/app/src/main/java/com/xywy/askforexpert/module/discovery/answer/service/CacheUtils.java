package com.xywy.askforexpert.module.discovery.answer.service;

import android.text.TextUtils;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.SerializeTools;
import com.xywy.askforexpert.model.answer.local.ExamPaper;

/**
 * 封装的缓存类 可在此处添加其他数据缓存方法
 * Created by bailiangjin on 16/3/25.
 */
public class CacheUtils {


    /**
     * 试卷缓存类
     */
    private static final String EXAM_PAPER_CACHE_FILE_v1 = "EXAM_PAPER_CACHE_FILE_v2";


    /**
     * 缓存数据
     *
     * @param examPaper
     * @return
     */
    public static boolean cacheExamPaper(ExamPaper examPaper) {
        if (examPaper == null || null == examPaper.getFlatQuestionList()) {
            return false;
        }
        String cacheKey = EXAM_PAPER_CACHE_FILE_v1 + YMUserService.getCurUserId() + examPaper.getId();
        return SerializeTools.cacheObj(YMApplication.getAppContext(), cacheKey, examPaper);
    }

    /**
     * 从缓存中获取 缓存数据
     *
     * @return
     */
    public static ExamPaper getExamPaper(String paperId) {
        if (TextUtils.isEmpty(paperId)) {
            LogUtils.e("paperId is empty");
            return null;
        }
        String cacheKey = EXAM_PAPER_CACHE_FILE_v1 + YMUserService.getCurUserId() + paperId;
        return SerializeTools.getObj(YMApplication.getAppContext(), cacheKey, ExamPaper.class);
    }

    /**
     * 删除 缓存数据
     *
     * @return
     */
    public static boolean deleteExamPaper(String paperId) {
        String cacheKey = EXAM_PAPER_CACHE_FILE_v1 + YMUserService.getCurUserId() + paperId;
        return SerializeTools.deleteObj(YMApplication.getAppContext(), cacheKey);
    }


}
