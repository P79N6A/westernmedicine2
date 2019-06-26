package com.xywy.askforexpert.model;

/**
 * 是否收藏  是否点赞
 *
 * @author 王鹏
 * @2015-6-19下午4:15:19
 */
public class BookBaseInfo {

    private String code;
    private String msg;
    private BookBaseInfo list;
    private String iscollection;

    private String msg_iscollection;

    private String ispraise;

    private String msg_ispraise;

    private String commNum;


    public String getCommNum() {
        return commNum;
    }

    public void setCommNum(String commNum) {
        this.commNum = commNum;
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

    public BookBaseInfo getList() {
        return list;
    }

    public void setList(BookBaseInfo list) {
        this.list = list;
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

    public String getIspraise() {
        return ispraise;
    }

    public void setIspraise(String ispraise) {
        this.ispraise = ispraise;
    }

    public String getMsg_ispraise() {
        return msg_ispraise;
    }

    public void setMsg_ispraise(String msg_ispraise) {
        this.msg_ispraise = msg_ispraise;
    }


}
