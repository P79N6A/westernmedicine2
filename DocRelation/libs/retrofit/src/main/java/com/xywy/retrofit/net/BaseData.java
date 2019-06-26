package com.xywy.retrofit.net;

/**
 * 数据返回的基类
 */
public class BaseData<T> {

    private int code = -1;

    private String msg;

    private T data;
    private int total;
    private int no_read_total;

    public int getNo_read_total() {
        return no_read_total;
    }

    public void setNo_read_total(int no_read_total) {
        this.no_read_total = no_read_total;
    }

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
