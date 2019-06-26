package com.xywy.askforexpert.model.consultentity;

import java.io.Serializable;


/**
 * stone 评价内容
 */
public class CommentBean implements Serializable{

    //即时问答的历史回复评价
    private String satisfied;//星级 "5" 整数
    private String content;//评价内容

    //问题广场的历史回复评价
    private String star;//星级 "1.5" 小数
    private String con;//评价内容

    public String getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(String satisfied) {
        this.satisfied = satisfied;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }
}