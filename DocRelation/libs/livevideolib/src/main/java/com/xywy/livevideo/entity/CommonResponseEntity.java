package com.xywy.livevideo.entity;

/**
 * Created by zhangzheng on 2017/3/6.
 */
public class CommonResponseEntity {

    /**
     * code : 10000
     * msg : 成功
     * data : {}
     */

    private int code;
    private String msg;

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
}
