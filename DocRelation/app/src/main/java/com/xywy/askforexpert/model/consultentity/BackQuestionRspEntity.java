package com.xywy.askforexpert.model.consultentity;

import java.util.List;

/**
 * Created by zhangzheng on 2017/5/5.
 */

public class BackQuestionRspEntity {

    /**
     * code : 50000
     * msg : 退回超时，此问题已被分配到其他医生
     * data : []
     */

    private int code;
    private String msg;
    private List<?> data;

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

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
