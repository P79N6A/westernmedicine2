package com.xywy.askforexpert.model;

import java.util.ArrayList;

/**
 * Created by jason on 2018/11/8.
 */

public class PatientGroupingData {
    private String code;
    private String msg;
    private ArrayList<GroupingData> data;

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

    public ArrayList<GroupingData> getData() {
        return data;
    }

    public void setData(ArrayList<GroupingData> data) {
        this.data = data;
    }
}
