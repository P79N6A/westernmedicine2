package com.xywy.askforexpert.model.doctor;

import java.io.Serializable;


public class CommentBean implements Serializable {
    public User user;
    public String praisecount;
    public String is_praise;
    public Touser touser;
    public String content;
    public String id;


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public String getPraisecount() {
        return praisecount;
    }


    public void setPraisecount(String praisecount) {
        this.praisecount = praisecount;
    }


    public String getIs_praise() {
        return is_praise;
    }


    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }


    public Touser getTouser() {
        return touser;
    }


    public void setTouser(Touser touser) {
        this.touser = touser;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "CommList [user=" + user.toString() + ", praisecount=" + praisecount
                + ", is_praise=" + is_praise + ", touser=" + touser.toString()
                + ", content=" + content + ", id=" + id + "]";
    }
}
