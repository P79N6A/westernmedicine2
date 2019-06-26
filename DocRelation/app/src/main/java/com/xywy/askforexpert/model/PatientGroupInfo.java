package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 患者 分组管理模型
 *
 * @author 王鹏
 * @2015-5-21下午6:12:04
 */
public class PatientGroupInfo {

    private String code;
    private String msg;
    private List<PatientGroupInfo> data;
    private String id;
    private String name;

    public List<PatientGroupInfo> getData() {
        return data;
    }

    public void setData(List<PatientGroupInfo> data) {
        this.data = data;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
