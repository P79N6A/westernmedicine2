package com.xywy.askforexpert.module.main.service.que.adapter;

/**
 * Created by bailiangjin on 2017/4/28.
 */

public class QuestionDetailBtnItem {

    private Type type;
    private String name;
    private int imageResId;
    private boolean enabled;

    public QuestionDetailBtnItem(Type type, String name, int imageResId) {
        this.type = type;
        this.name = name;
        this.imageResId = imageResId;
    }

    public QuestionDetailBtnItem(Type type, String name, int imageResId, boolean enabled) {
        this.type = type;
        this.name = name;
        this.imageResId = imageResId;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public enum Type{
        RECOMMEND_MEDICINE, //推荐用药
        CORRECT_OFFICE,//纠正科室
        RAPID_REPLY,//快捷回复
        REPORT,//举报
        SKIP//跳过
    }
}
