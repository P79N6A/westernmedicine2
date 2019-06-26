package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 招聘高级搜索
 *
 * @author 王鹏
 * @2015-6-18下午1:48:50
 */
public class RecruitSerchMainInfo {
    private String code;
    private String msg;
    private RecruitSerchMainInfo list;
    private String message;
    private String count;
    private List<RecruitSerchMainInfo> data;
    private String url;
    private String id;
    private String entename;
    private String title;
    private String updatetime;

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
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

    public RecruitSerchMainInfo getList() {
        return list;
    }

    public void setList(RecruitSerchMainInfo list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<RecruitSerchMainInfo> getData() {
        return data;
    }

    public void setData(List<RecruitSerchMainInfo> data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntename() {
        return entename;
    }

    public void setEntename(String entename) {
        this.entename = entename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
