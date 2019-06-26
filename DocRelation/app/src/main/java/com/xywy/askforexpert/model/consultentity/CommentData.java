package com.xywy.askforexpert.model.consultentity;

/**
 * Created by jason on 2018/7/9.
 */

public class CommentData {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(double satisfied) {
        this.satisfied = satisfied;
    }

    private double satisfied;
}
