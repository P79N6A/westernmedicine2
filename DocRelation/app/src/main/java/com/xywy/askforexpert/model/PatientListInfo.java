package com.xywy.askforexpert.model;

import java.util.List;

public class PatientListInfo {
    private String code;
    private String msg;
    private PatientListInfo data;
    private String newpatient;
    private String listcount;
    private String servered;
    private List<PatientListInfo> list;
    private String id;
    //	private String time;
//	private String msgcount;
//	private String last;
    private String photo;
    private String realname;
    private String hxusername;
    private String mobile;

    private String hassend;
    private String header;


    public String getHassend() {
        return hassend;
    }

    public void setHassend(String hassend) {
        this.hassend = hassend;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

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

    public PatientListInfo getData() {
        return data;
    }

    public void setData(PatientListInfo data) {
        this.data = data;
    }

    public String getNewpatient() {
        return newpatient;
    }

    public void setNewpatient(String newpatient) {
        this.newpatient = newpatient;
    }

    public String getListcount() {
        return listcount;
    }

    public void setListcount(String listcount) {
        this.listcount = listcount;
    }

    public List<PatientListInfo> getList() {
        return list;
    }

    public void setList(List<PatientListInfo> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getHxusername() {
        return hxusername;
    }

    public void setHxusername(String hxusername) {
        this.hxusername = hxusername;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getServered() {
        return servered;
    }

    public void setServered(String servered) {
        this.servered = servered;
    }


}
