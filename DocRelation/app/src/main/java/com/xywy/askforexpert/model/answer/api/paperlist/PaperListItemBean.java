package com.xywy.askforexpert.model.answer.api.paperlist;

/**
 * Created by bailiangjin on 16/5/5.
 */
public class PaperListItemBean {
    private String paper_id;
    private String paper_name;
    private String class_id;
    private int pass_score;
    private int total_score;
    private String create_time;
    private String update_time;
    private String paper_status;
    private String isdel;
    private String audit_man_id;

    public String getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(String paper_id) {
        this.paper_id = paper_id;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public int getPass_score() {
        return pass_score;
    }

    public void setPass_score(int pass_score) {
        this.pass_score = pass_score;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getPaper_status() {
        return paper_status;
    }

    public void setPaper_status(String paper_status) {
        this.paper_status = paper_status;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getAudit_man_id() {
        return audit_man_id;
    }

    public void setAudit_man_id(String audit_man_id) {
        this.audit_man_id = audit_man_id;
    }
}
