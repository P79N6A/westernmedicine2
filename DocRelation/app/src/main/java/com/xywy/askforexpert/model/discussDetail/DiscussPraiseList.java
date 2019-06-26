package com.xywy.askforexpert.model.discussDetail;

import javax.annotation.Generated;

/**
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/21 18:19
 */
@Generated("org.jsonschema2pojo")
public class DiscussPraiseList {
    private String userid;
    private String nickname;
    private String photo;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "DiscussPraiseList{" +
                "userid='" + userid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
