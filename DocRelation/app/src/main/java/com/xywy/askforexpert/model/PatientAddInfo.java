package com.xywy.askforexpert.model;

/**
 * 添加患者 微信二维码
 *
 * @author 王鹏
 * @2015-6-10下午8:28:39
 */
public class PatientAddInfo {

    private String code;
    private String msg;
    private PatientAddInfo data;
    private String realname;
    private String job;
    private String hospital;
    private String wximg;
    private String depart;

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

    public PatientAddInfo getData() {
        return data;
    }

    public void setData(PatientAddInfo data) {
        this.data = data;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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

    public String getWximg() {
        return wximg;
    }

    public void setWximg(String wximg) {
        this.wximg = wximg;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

}
