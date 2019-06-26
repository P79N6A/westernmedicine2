package com.xywy.askforexpert.model.answer.local;

/**
 * Created by bailiangjin on 16/4/26.
 */
public class ItemSelectedMsg {
    /**
     * 试题ID
     */
    private String questionId;

    /**
     * 题号
     */
    private int questionNumber;

    /**
     * 答案
     */
    private String answer;


    public ItemSelectedMsg(String questionId, int questionNumber, String answer) {
        this.questionId = questionId;
        this.questionNumber = questionNumber;
        this.answer = answer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
