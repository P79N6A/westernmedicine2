package com.xywy.askforexpert.model.answer.api;

import com.google.gson.annotations.SerializedName;
import com.xywy.askforexpert.model.api.BaseResultBean;

/**
 * 得分结果
 * Created by bailiangjin on 16/5/9.
 */
public class ScoreBean extends BaseResultBean {

    /**
     * 状态码 10000:成功 -10005:提交参数异常
     */

    private int score;
    @SerializedName("ispass")
    private boolean isPass;
    private String paperId;
    private String userId;
    private String answerTime;
    private int userRank;


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean ispass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        this.isPass = pass;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    public int getUserRank() {
        return userRank;
    }

    public void setUserRank(int userRank) {
        this.userRank = userRank;
    }
}
