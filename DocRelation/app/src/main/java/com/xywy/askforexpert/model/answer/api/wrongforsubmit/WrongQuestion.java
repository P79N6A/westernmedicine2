package com.xywy.askforexpert.model.answer.api.wrongforsubmit;

import java.io.Serializable;

/**
 * Created by bailiangjin on 16/5/9.
 */
public class WrongQuestion implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * questionId : 2369
     * answer : C
     */

    private String questionId;
    private String answer;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String toJson() {
        return "{\"questionId\":\"" + questionId + "\",\"answer\":\"" + answer + "\"}";
    }
}
