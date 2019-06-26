package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 快捷回复列表 医患
 *
 * @author 王鹏
 * @2015-6-4上午11:15:21
 */
public class AskPatientReplyInfo {
    private String code;
    private String msg;
    private List<AskPatientReplyInfo> data;
    private String id;
    private String word;

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

    public List<AskPatientReplyInfo> getData() {
        return data;
    }

    public void setData(List<AskPatientReplyInfo> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


}
