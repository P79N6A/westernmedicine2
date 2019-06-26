package com.xywy.askforexpert.model.discussDetail;

import com.xywy.askforexpert.model.doctor.Touser;
import com.xywy.askforexpert.model.doctor.User;

/**
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/21 18:19
 */
public class DiscussCommentList {
    private User user;
    private String praisecount;
    private String is_praise;
    private Touser touser;
    private String content;
    private String id;

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
        return "DiscussCommentList{" +
                "user=" + user +
                ", praisecount='" + praisecount + '\'' +
                ", is_praise='" + is_praise + '\'' +
                ", touser=" + touser +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
