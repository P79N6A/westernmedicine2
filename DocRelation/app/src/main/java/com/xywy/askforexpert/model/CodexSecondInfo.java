package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 药典二级列表
 *
 * @author 王鹏
 * @2015-5-13下午4:39:50
 */
public class CodexSecondInfo {

    private String msg;
    private String code;
    private String total;

    private List<CodexSecondInfo> list;

    private String title;
    private String id;
    private String url;
    private String status;

    private String author;
    private String createtime;
    private String filesize;

    private String source;
    private String downloadurl;
    private String iscollection;

    private String msg_iscollection;


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public String getIscollection() {
        return iscollection;
    }

    public void setIscollection(String iscollection) {
        this.iscollection = iscollection;
    }

    public String getMsg_iscollection() {
        return msg_iscollection;
    }

    public void setMsg_iscollection(String msg_iscollection) {
        this.msg_iscollection = msg_iscollection;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<CodexSecondInfo> getList() {
        return list;
    }

    public void setList(List<CodexSecondInfo> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
