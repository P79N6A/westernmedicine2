package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 我的小站
 *
 * @author 王鹏
 * @2015-5-23下午3:15:55
 */
public class MySmallActionInfo {
    private String code;
    private String msg;
    private MySmallAction data;
    private class MySmallAction{
        public String synopsis;
        public Clinic clinic;
        public String stat;
        public List<Grade> grade;
    }
    private List<MySmallActionInfo> grade;

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

    public MySmallAction getData() {
        return data;
    }

    public void setData(MySmallAction data) {
        this.data = data;
    }

    private class Clinic{
        public String dati;
        public String yuyue;
        public String phone;
        public String iszj;
        public String family;
        public String isjob;
        public String isdoctor;
    }

    private class Grade{
        public String g_uid;
        public String g_cons;
        public String g_stat;
        public String g_date;
        public String g_uname;

    }
}
