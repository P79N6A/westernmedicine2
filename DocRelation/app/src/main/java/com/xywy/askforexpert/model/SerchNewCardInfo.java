package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 搜索好友 根据
 *
 * @author 王鹏
 * @2015-6-2上午9:08:07
 */
public class SerchNewCardInfo {

    private String code;
    private String msg;
    private String hasdata;
    private List<SerchNewCardInfo> data;
    private String job;
    private String hospital;
    private String subject;
    private String subject2;
    private String pid;
    private String photo;
    private String realname;
    private String hx_username;

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

    public String getHasdata() {
        return hasdata;
    }

    public void setHasdata(String hasdata) {
        this.hasdata = hasdata;
    }

    public List<SerchNewCardInfo> getData() {
        return data;
    }

    public void setData(List<SerchNewCardInfo> data) {
        this.data = data;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject2() {
        return subject2;
    }

    public void setSubject2(String subject2) {
        this.subject2 = subject2;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }


}
