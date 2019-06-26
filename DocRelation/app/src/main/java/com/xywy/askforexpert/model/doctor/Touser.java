package com.xywy.askforexpert.model.doctor;

import java.io.Serializable;

public class Touser implements Serializable {
    public String userid;
    public String nickname;
    public String photo;
    public String subject;
    public String hospital;

    @Override
    public String toString() {
        return "Touser [userid=" + userid + ", nickname=" + nickname
                + ", photo=" + photo + ", subject=" + subject + ", hospital="
                + hospital + "]";
    }
}
