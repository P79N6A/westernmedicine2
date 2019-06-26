package com.xywy.askforexpert.model.api;

/**
 * 服务的返回数据基类 code 为String类型的
 * Created by bailiangjin on 16/5/13.
 */
public class BaseStringResultBean {

    /**
     * 成功
     */
    public static final String CODE_SUCCESS = "0";

    /**
     * 请求参数异常
     */
    public static final String CODE_PARAM_ERROR = "-10005";


    protected String code;
    protected String msg;

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
}
