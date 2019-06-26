package com.xywy.component.datarequest.neworkWrapper;

/**
 * 数据返回的基类
 */
public class BaseData<T> {

    private int code = -1;

    private String msg;

    private T data;
    private String tag;
    private int total;
    private boolean intermediate = false;//是否是中间数据

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isIntermediate() {
        return intermediate;
    }

    public void setIntermediate(boolean intermediate) {
        this.intermediate = intermediate;
    }
}
