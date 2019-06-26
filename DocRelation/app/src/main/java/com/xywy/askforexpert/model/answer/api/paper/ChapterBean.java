package com.xywy.askforexpert.model.answer.api.paper;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bailiangjin on 16/5/9.
 */
public class ChapterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 试题类型 或试题组类型
     */
    private int type;

    @SerializedName("paper_id")
    private String pid;

    @SerializedName("paper_node_id")
    private String id;

    @SerializedName("paper_node_name")
    private String name;

    @SerializedName("paper_node_desc")
    private String desc;

    @SerializedName("list_order")
    private int order;

    @SerializedName("total")
    private int total;

    @SerializedName("isdel")
    private String isdel;
    /**
     * question_id : 3636
     * content : 癫痫患者可进行的日常活动项目是（　　）。
     * select_answer : 游泳|太极拳|开汽车|单独外出|登高
     * question_score : 1
     * question_analysis : 癫痫患者经常会在任何时间、地点、环境下且不能自我控制地突然发作，容易出现摔伤、烫伤、溺水、交通事故等，故癫痫患者不可进行游泳、开车、单独外出、登高等比较危险的活动。
     * list_order : 1
     * correct_answer : B
     * question_type_id : 1
     * create_time : 1461294083
     * update_time : 0
     * isdel : 51
     * listOrder : 1
     * node_question_score : 1
     * paper_name : 2015年护理学（师）专业知识真题及详解
     */

    @SerializedName("question")
    private List<Question> questionList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    /**
     * 获取 自然 Question List 包含层级 试题下可能有子试题
     *
     * @return
     */
    public int getUserScore() {

        if (null == questionList || questionList.isEmpty()) {
            return 0;

        } else {
            int userScore = 0;
            for (Question question : questionList) {
                if (null == question) {
                    continue;
                }

                List<Question> innerQuestionList = question.getQuestionList();
                int groupScore;
                int questionScore;
                groupScore = question.getUserScore();
                userScore += groupScore;
            }
            return userScore;
        }
    }


    /**
     * 获取 自然 Question List 包含层级 试题下可能有子试题
     *
     * @return
     */
    public List<Question> getQuestionList() {

        if (null == questionList || questionList.isEmpty()) {
            return null;

        } else {
            for (Question question : questionList) {
                if (null == question) {
                    continue;
                }

                List<Question> innerQuestionList = question.getQuestionList();
                if (null != innerQuestionList) {
                    for (Question subQuestion : innerQuestionList) {
                        if (null != subQuestion) {
                            // 处理 嵌套数据
                            subQuestion.subQuestionFillData(this, question);
                        }
                    }
                } else {
                    question.fillData(this);
                }
            }

            return questionList;
        }
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public int getListSize() {
        return null == questionList ? 0 : questionList.size();
    }

    /**
     * 将试题内容转换为扁平的Question列表数据 去除 试题组一级 答题卡 模块用
     *
     * @return
     */
    public List<Question> getFlatQuestionList() {
        List<Question> flatQuestionList = new ArrayList<>();
        int count = 0;
        List<Question> chapterQuestionList = getQuestionList();
        if (null != chapterQuestionList && !chapterQuestionList.isEmpty()) {
            for (Question question : chapterQuestionList) {
                //试题组 添加子条目 到list
                List<Question> subQuestionList = question.getQuestionList();
                if (null != subQuestionList && !subQuestionList.isEmpty()) {
                    for (Question subQuestion : subQuestionList) {
                        subQuestion.subQuestionFillData(this, question);
                        subQuestion.setOrderInNode(++count);
                        flatQuestionList.add(subQuestion);
                    }
                } else {
                    //单个试题 添加
                    question.fillData(this);
                    question.setOrderInNode(++count);
                    flatQuestionList.add(question);
                }
            }
        }
        return flatQuestionList;
    }

}
