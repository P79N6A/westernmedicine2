package com.xywy.askforexpert.model.answer.api.wrongquestion;

import com.google.gson.annotations.SerializedName;
import com.xywy.askforexpert.model.answer.api.paper.Question;
import com.xywy.askforexpert.model.api.BaseResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 错题集 类
 * Created by bailiangjin on 16/5/10.
 */
public class WrongPaperPageBean extends BaseResultBean {


    private String userId;
    private String paper_id;
    private String paper_name;

    @SerializedName("data")
    private List<Question> questionList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(String paper_id) {
        this.paper_id = paper_id;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }


    /**
     * 获取 flat Question List 包含层级 试题下可能有子试题
     *
     * @return
     */
    public List<Question> getFlatQuestionList() {
        if (null == questionList || questionList.isEmpty()) {
            return null;
        } else {
            List<Question> flatQuestionList = new ArrayList<>();
            for (Question question : questionList) {
                if (null == question) {
                    continue;
                }
                question = question.fillData(null);
                if(question.isMulti()){
                    question.setComplete(true);
                }else {
                    question.setAnswered(true);
                }
                question.setAnswered(true);
                question.setUserAnswerMap(question.getUserAnswerMap());
                question.setWrongState(true);
                List<Question> innerQuestionList = question.getQuestionList();
                if (null != innerQuestionList) {
                    for (Question subQuestion : innerQuestionList) {
                        if (null != subQuestion) {
                            subQuestion.subQuestionFillData(null, question);
                            if (subQuestion.isMulti()){
                                subQuestion.setComplete(true);
                            }else {
                                subQuestion.setAnswered(true);
                            }
                            subQuestion.setUserAnswerMap(subQuestion.getUserAnswerMap());
                            subQuestion.setWrongState(true);
                            flatQuestionList.add(subQuestion);
                        }
                    }
                } else {
                    flatQuestionList.add(question);
                }
            }
            return flatQuestionList;
        }
    }


}
