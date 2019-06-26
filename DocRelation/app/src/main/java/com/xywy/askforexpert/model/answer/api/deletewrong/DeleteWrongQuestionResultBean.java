package com.xywy.askforexpert.model.answer.api.deletewrong;

import com.xywy.askforexpert.model.api.BaseResultBean;

/**
 * Created by bailiangjin on 16/8/22.
 */
public class DeleteWrongQuestionResultBean extends BaseResultBean {

    private String userId;
    private boolean result;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
