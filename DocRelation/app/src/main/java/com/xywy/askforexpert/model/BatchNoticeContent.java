package com.xywy.askforexpert.model;

import java.util.ArrayList;

/**
 * Created by jason on 2018/11/14.
 */

public class BatchNoticeContent {
    private int code;
    private String msg;
    private ArrayList<BatchNoticeData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<BatchNoticeData> getData() {
        return data;
    }

    public void setData(ArrayList<BatchNoticeData> data) {
        this.data = data;
    }
}
