package com.xywy.askforexpert.model;

/**
 * 折扣设置
 *
 * @author 王鹏
 * @2015-7-3下午3:29:20
 */
public class CateInfo {

    private String code;
    private String msg;

    private CateInfo data;
    private String timebegin;
    private String timeend;
    private String price;
    private String maxnum;
    private String open;

    public String getMaxnum() {
        return maxnum;
    }

    public void setMaxnum(String maxnum) {
        this.maxnum = maxnum;
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

    public CateInfo getData() {
        return data;
    }

    public void setData(CateInfo data) {
        this.data = data;
    }

    public String getTimebegin() {
        return timebegin;
    }

    public void setTimebegin(String timebegin) {
        this.timebegin = timebegin;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }


}
