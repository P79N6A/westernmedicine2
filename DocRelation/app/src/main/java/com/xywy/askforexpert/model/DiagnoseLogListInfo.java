package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 诊断纪录 列表
 *
 * @author 王鹏
 * @2015-5-22上午10:13:02
 */
public class DiagnoseLogListInfo {
    // WebChromeClient

    private String code;
    private String msg;
    private List<DiagnoseLogListInfo> data;
    private String id;
    private String did;
    private String updatetime;
    private String cotent;
    private String iscreate;
    private String realname;
    private String sickinfo;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSickinfo() {
        return sickinfo;
    }

    public void setSickinfo(String sickinfo) {
        this.sickinfo = sickinfo;
    }

    public String getIscreate() {
        return iscreate;
    }

    public void setIscreate(String iscreate) {
        this.iscreate = iscreate;
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

    public List<DiagnoseLogListInfo> getData() {
        return data;
    }

    public void setData(List<DiagnoseLogListInfo> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCotent() {
        return cotent;
    }

    public void setCotent(String cotent) {
        this.cotent = cotent;
    }

}
