package com.xywy.askforexpert.model.answer.api.answered;

import com.xywy.askforexpert.model.api.BaseResultBean;

/**
 * Created by bailiangjin on 16/5/10.
 */
public class AnsweredPaperBean extends BaseResultBean {
    private String record_id;
    private String paper_id;
    private String answer_man_id;
    private String create_time;
    private String update_time;
    private String ispass;
    private int score;
    private String paper_name;
    private String isdel;
    private String paper_status;
    /**
     * paper_id : 3
     * answer_man_id : 10
     * paper_name : 2015年护理学（师）专业知识真题及详解 — 错题集
     */

    private WrongBean wrong;

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(String paper_id) {
        this.paper_id = paper_id;
    }

    public String getAnswer_man_id() {
        return answer_man_id;
    }

    public void setAnswer_man_id(String answer_man_id) {
        this.answer_man_id = answer_man_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIspass() {
        return ispass;
    }

    public void setIspass(String ispass) {
        this.ispass = ispass;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getPaper_status() {
        return paper_status;
    }

    public void setPaper_status(String paper_status) {
        this.paper_status = paper_status;
    }

    public WrongBean getWrong() {
        return wrong;
    }

    public void setWrong(WrongBean wrong) {
        this.wrong = wrong;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
