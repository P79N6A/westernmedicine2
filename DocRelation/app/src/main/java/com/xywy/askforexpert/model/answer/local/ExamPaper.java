package com.xywy.askforexpert.model.answer.local;

import com.google.gson.annotations.SerializedName;
import com.xywy.askforexpert.model.answer.api.ShareBaseBean;
import com.xywy.askforexpert.model.answer.api.paper.ChapterBean;
import com.xywy.askforexpert.model.answer.api.paper.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 试卷详情页 对应API bean
 * Created by bailiangjin on 16/5/9.
 */
public class ExamPaper extends ShareBaseBean implements Serializable {

    private static final long serialVersionUID = 10L;

    @SerializedName("paper_id")
    private String id;

    @SerializedName("paper_name")
    private String name;

    @SerializedName("userId")
    private String userId;


    /**
     * 版本号
     */
    @SerializedName("update_time")
    private String version;

    /**
     * 试卷满分分值
     */
    private int fullScore;


    /**
     * 试卷状态 0:未答 1:答题中 2:完成
     */
    private int status;

    /**
     * 之前用户所处位置
     */
    private int prePosition;


    /**
     * 章节list
     */
    @SerializedName("node")
    private List<ChapterBean> chapterBeanList;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<ChapterBean> getChapterBeanList() {
        return chapterBeanList;
    }

    public void setChapterBeanList(List<ChapterBean> chapterBeanList) {
        this.chapterBeanList = chapterBeanList;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getFullScore() {
        return fullScore;
    }

    public void setFullScore(int fullScore) {
        this.fullScore = fullScore;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrePosition() {
        return prePosition;
    }

    public void setPrePosition(int prePosition) {
        this.prePosition = prePosition;
    }

    /**
     * 获取用户得分
     *
     * @return
     */
    public int getUserScore() {

        List<ChapterBean> list = getChapterBeanList();
        if (null == list || list.isEmpty()) {
            return 0;
        }
        int score = 0;
        for (ChapterBean chapterBean : list) {
            if (null != chapterBean) {
                score += chapterBean.getUserScore();
            }
        }

        return score;
    }


    /**
     * 将试题内容转换为扁平的Question列表数据 去除 Chapter层级
     *
     * @return
     */
    public List<Question> getFlatQuestionList() {

        List<ChapterBean> list = getChapterBeanList();
        if (null == list || list.isEmpty()) {
            return null;
        }
        List<Question> questionList = new ArrayList<>();
        int count = 0;
        for (ChapterBean chapterBean : list) {
            if (null != chapterBean) {

                List<Question> chapterQuestionList = chapterBean.getQuestionList();
                if (null != chapterQuestionList && !chapterQuestionList.isEmpty()) {
                    int nodeCount = 0;
                    for (Question question : chapterQuestionList) {
                        //试题组 添加子条目 到list
                        List<Question> subQuestionList = question.getQuestionList();
                        if (null != subQuestionList && !subQuestionList.isEmpty()) {
                            for (Question subQuestion : subQuestionList) {
                                subQuestion.setGroupScore(question.getScore());
                                subQuestion.setOrderInNode(++nodeCount);
                                subQuestion.setOrderInAll(++count);
                                questionList.add(subQuestion);
                            }
                        } else {
                            //单个试题 添加
                            question.setOrderInNode(++nodeCount);
                            question.setOrderInAll(++count);
                            questionList.add(question);
                        }
                    }
                }
            }
        }

        return questionList;
    }
}
