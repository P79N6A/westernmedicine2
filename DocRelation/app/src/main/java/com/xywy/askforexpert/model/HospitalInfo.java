package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 医院
 *
 * @author 王鹏
 * @2015-5-25下午8:50:40
 */
public class HospitalInfo {
    private String code;
    private String msg;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }


}
