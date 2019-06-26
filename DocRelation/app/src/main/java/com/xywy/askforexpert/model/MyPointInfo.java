package com.xywy.askforexpert.model;

/**
 * 我的积分 余额
 *
 * @author 王鹏
 * @2015-11-3下午1:47:43
 */
public class MyPointInfo {

    private String code;
    private String msg;
    private MyPointInfo data;
    private String balance;
    private String gone_score;
    private String total_score;

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

    public MyPointInfo getData() {
        return data;
    }

    public void setData(MyPointInfo data) {
        this.data = data;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getGone_score() {
        return gone_score;
    }

    public void setGone_score(String gone_score) {
        this.gone_score = gone_score;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }


}
