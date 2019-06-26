package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 新朋友 信息
 *
 * @author 王鹏
 * @2015-6-12下午3:50:32
 */
public class NewCardInfo {
    private String code;
    private String msg;
    private List<NewCardInfo> data;
    private String photo;
    private String userid;
    private String nickname;
    private String subject;
    private String hospital;
    private String synopsis;
    private String job;
    private String sex;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<NewCardInfo> getData() {
        return data;
    }

    public void setData(List<NewCardInfo> data) {
        this.data = data;
    }


}
