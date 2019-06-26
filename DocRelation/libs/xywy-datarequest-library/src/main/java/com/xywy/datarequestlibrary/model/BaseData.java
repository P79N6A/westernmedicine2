package com.xywy.datarequestlibrary.model;

/**
 * 数据返回的基类
 */
public class BaseData<T> {

    protected int code = -1;

    protected String msg;

    protected T data;


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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
