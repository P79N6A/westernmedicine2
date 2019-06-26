package com.xywy.askforexpert.model.consultentity;

import java.io.Serializable;

/**
 * Created by zhangzheng on 2017/4/26. stone
 */

public class ConsultPagerTabItemBean implements Serializable {
    //stone 咨询中 问题库 历史回复 未回复
    public static final int TYPE_MY_CONSULT = 0xf0;
    public static final int TYPE_QUESTIONS = TYPE_MY_CONSULT + 1;
    public static final int TYPE_ANSWERED = TYPE_QUESTIONS + 1;
    public static final int TYPE_UNANSWERED = TYPE_ANSWERED + 1;

    private int type;
    private String text;


    public ConsultPagerTabItemBean() {

    }

    public ConsultPagerTabItemBean(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
