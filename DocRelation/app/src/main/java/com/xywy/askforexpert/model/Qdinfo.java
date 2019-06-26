package com.xywy.askforexpert.model;

/**
 * 签到model
 *
 * @author 王鹏
 * @2015-8-13下午4:20:15
 */
public class Qdinfo {

    public void setAccess(int access) {
        this.access = access;
    }

    private int access;
    private String code;
    private String msg;
    private QdInfo2 data;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public QdInfo2 getData() {
        return data;
    }

    public int getAccess() {
        return access;
    }
}
