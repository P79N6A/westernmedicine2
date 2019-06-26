package com.xywy.askforexpert.model;

import java.util.ArrayList;

/**
 * Created by jason on 2018/11/8.
 */

public class GrouManageData {
    private int code;
    private String msg;
    private ArrayList<GrouManageList> data;

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

    public ArrayList<GrouManageList> getData() {
        return data;
    }

    public void setData(ArrayList<GrouManageList> data) {
        this.data = data;
    }
}
