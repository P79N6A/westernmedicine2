package com.xywy.askforexpert.model.main.promotion;

import com.google.gson.annotations.SerializedName;

/**
 *  晋级数据体 对应晋级页接口 data 字段
 * Created by bailiangjin on 2016/10/21.
 */

public  class PromotionBean {

    /**
     * 当前等级
     */
    @SerializedName("medal_name")
    private String curLevel;

    /**
     * 是否为顶级
     */
    @SerializedName("top")
    private boolean isTopLevel;
    /**
     * 下一级
     */
    @SerializedName("next")
    private String nextLevel;

    /**
     * 晋级规则H5链接
     */
    @SerializedName("url")
    private String ruleUrl;



    @SerializedName("day")
    private int answerDays;

    /**
     * 采纳率
     */
    @SerializedName("rate")
    private String adoptRate;

    /**
     * 采纳率
     */
    @SerializedName("t_rate")
    private String passRate;

    /**
     * 活跃度
     */
    @SerializedName("work")
    private int dynamicRate;

    /**
     * 处罚次数
     */
    @SerializedName("chufa")
    private String punishTime;




    public int getAnswerDays() {
        return answerDays;
    }

    public void setAnswerDays(int answerDays) {
        this.answerDays = answerDays;
    }

    public String getAdoptRate() {
        return adoptRate;
    }

    public void setAdoptRate(String adoptRate) {
        this.adoptRate = adoptRate;
    }

    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }

    public int getDynamicRate() {
        return dynamicRate;
    }

    public void setDynamicRate(int dynamicRate) {
        this.dynamicRate = dynamicRate;
    }

    public String getPunishTime() {
        return punishTime;
    }

    public void setPunishTime(String punishTime) {
        this.punishTime = punishTime;
    }

    public String getCurLevel() {
        return curLevel;
    }

    public void setCurLevel(String curLevel) {
        this.curLevel = curLevel;
    }

    public String getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(String nextLevel) {
        this.nextLevel = nextLevel;
    }

    public String getRuleUrl() {
        return ruleUrl;
    }

    public void setRuleUrl(String ruleUrl) {
        this.ruleUrl = ruleUrl;
    }

    public boolean isTopLevel() {
        return isTopLevel;
    }

    public void setTopLevel(boolean topLevel) {
        isTopLevel = topLevel;
    }
}