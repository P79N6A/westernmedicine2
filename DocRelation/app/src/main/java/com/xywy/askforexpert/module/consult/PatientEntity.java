package com.xywy.askforexpert.module.consult;

import com.xywy.askforexpert.model.PatienTtitle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jason on 2018/11/12.
 */

public class PatientEntity implements Serializable{
    private int code;
    private String msg;
    private ArrayList<PatienTtitle> data;

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

    public ArrayList<PatienTtitle> getData() {
        return data;
    }

    public void setData(ArrayList<PatienTtitle> data) {
        this.data = data;
    }
}
