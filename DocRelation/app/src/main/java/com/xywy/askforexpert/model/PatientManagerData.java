package com.xywy.askforexpert.model;

import java.io.Serializable;

/**
 * Created by jason on 2018/11/2.
 */

public class PatientManagerData implements Serializable {
    private String code;
    private String msg;
    private PatientData data;

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

    public PatientData getData() {
        return data;
    }

    public void setData(PatientData data) {
        this.data = data;
    }
}
