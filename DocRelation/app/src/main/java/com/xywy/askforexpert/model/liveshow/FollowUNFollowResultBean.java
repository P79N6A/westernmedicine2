package com.xywy.askforexpert.model.liveshow;

/**
 * Created by bailiangjin on 2017/3/1.
 */

public class FollowUNFollowResultBean {


    /**
     * code : 10000
     * msg : 成功
     * data : {}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
