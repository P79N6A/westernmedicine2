package com.xywy.askforexpert.model.answer.api.set;

/**
 * Created by bailiangjin on 16/5/5.
 */
public class SetBean {
    private String class_id;
    private String class_name;
    private String pid;
    private String class_level;
    private String isdel;
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}