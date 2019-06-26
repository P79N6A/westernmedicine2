package com.xywy.askforexpert.model.wenxian;

import java.io.Serializable;
import java.util.List;

/**
 * 文献搜索
 *
 * @author apple
 */
public class WXSeachBean implements Serializable {

    private String code;
    private String msg;
    private int Total;
    private List<ItemRecords> Records;

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

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public List<ItemRecords> getRecords() {
        return Records;
    }

    public void setRecords(List<ItemRecords> records) {
        Records = records;
    }

    @Override
    public String toString() {
        return "WXSeachBean [code=" + code + ", msg=" + msg + ", Total="
                + Total + ", Records=" + Records + "]";
    }

}
