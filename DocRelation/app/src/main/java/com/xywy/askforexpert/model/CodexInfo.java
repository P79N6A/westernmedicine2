package com.xywy.askforexpert.model;

import java.util.List;


/**
 * 药典
 *
 * @author 王鹏
 * @2015-5-13下午2:04:35
 */
public class CodexInfo {

    private String code;
    private String msg;
    private String total;
    private List<CodexInfo> list;
    private String name;
    private String id;
    private List<CodexInfo> list_second;

    public List<CodexInfo> getList() {
        return list;
    }

    public void setList(List<CodexInfo> list) {
        this.list = list;
    }

    public List<CodexInfo> getList_second() {
        return list_second;
    }

    public void setList_second(List<CodexInfo> list_second) {
        this.list_second = list_second;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
