package com.xywy.askforexpert.model.doctor;

import java.io.Serializable;

public class PraiseBean implements Serializable {
    public String userid;
    public String nickname;
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
}
