package com.xywy.askforexpert.model.discussDetail;

/**
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/21 18:10
 */
public class DataUser {
    private String id;
    private String nickname;
    private String realname;
    private String subject;
    private String job;
    private String hospital;

    public String getUserid() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRealname() {
        return realname;
    }

    public String getSubject() {
        return subject;
    }

    public String getJob() {
        return job;
    }

    public String getHospital() {
        return hospital;
    }

    @Override
    public String toString() {
        return "DataUser{" +
                "userid='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", realname='" + realname + '\'' +
                ", subject='" + subject + '\'' +
                ", job='" + job + '\'' +
                ", hospital='" + hospital + '\'' +
                '}';
    }
}
