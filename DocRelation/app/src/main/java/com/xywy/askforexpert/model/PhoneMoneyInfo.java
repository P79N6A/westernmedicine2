package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 电话医生 通话费用
 *
 * @author 王鹏
 * @2015-5-28下午6:35:18
 */
public class PhoneMoneyInfo {
    private String code;
    private String msg;
    private List<PhoneMoneyInfo> data;
    private String key;
    private String time;
    private String money;

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

    public List<PhoneMoneyInfo> getData() {
        return data;
    }

    public void setData(List<PhoneMoneyInfo> data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }


}
