package com.xywy.askforexpert.model;

import java.io.Serializable;

/**
 * 患者个人信息
 *
 * @author 王鹏
 * @2015-5-21下午5:33:38
 */
public class PatientPerInfo implements Serializable{
    private String code;
    private String msg;
    private PatientPerInfo data;
    private String realname;
    private String sex;
    private String age;
    private String gid;
    private String gname;
    private String photo;

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

    public PatientPerInfo getData() {
        return data;
    }

    public void setData(PatientPerInfo data) {
        this.data = data;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
