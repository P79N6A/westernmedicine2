package com.xywy.askforexpert.model.answer.api.answered;

/**
 * Created by bailiangjin on 16/5/10.
 */
public class WrongBean {
    private String paper_id;
    private String answer_man_id;
    private String paper_name;

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

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }
}
