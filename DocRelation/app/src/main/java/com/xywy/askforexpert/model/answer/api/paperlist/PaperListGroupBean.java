package com.xywy.askforexpert.model.answer.api.paperlist;

import java.util.List;

/**
 * Created by bailiangjin on 16/5/5.
 */
public class PaperListGroupBean {
    private String class_id;
    private String class_name;
    private String pid;
    private String class_level;
    private String isdel;
    /**
     * paper_id : 65
     * paper_name : 2015年护士执业资格考试（专业实务）真题及详解
     * class_id : 22
     * pass_score : 0
     * total_score : 120
     * create_time : 1461294198
     * update_time : 0
     * paper_status : 1
     * isdel : 51
     * audit_man_id : 0
     */

    private List<PaperListItemBean> data;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getClass_level() {
        return class_level;
    }

    public void setClass_level(String class_level) {
        this.class_level = class_level;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public List<PaperListItemBean> getData() {
        return data;
    }

    public void setData(List<PaperListItemBean> data) {
        this.data = data;
    }


}
