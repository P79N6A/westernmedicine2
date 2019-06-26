package com.xywy.askforexpert.model.discussDetail;

/**
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/21 18:07
 */
public class RootData {
    private String code;
    private String msg;
    private DiscussDetailData data;
    private DiscussDetailUser user;

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

    public DiscussDetailData getData() {
        return data;
    }

    public void setData(DiscussDetailData data) {
        this.data = data;
    }

    public DiscussDetailUser getUser() {
        return user;
    }

    public void setUser(DiscussDetailUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "RootData{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", user=" + user +
                '}';
    }
}
