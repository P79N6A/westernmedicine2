package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 钱包实体类
 *
 * @author SHR
 * @2015-5-5上午9:55:13
 */
public class PurseInfo {
    /**
     * 绩效
     */
    private String performance;
    /**
     * 账户余额
     */
    private String balance;
    /**
     * 账单列表
     */
    private List<BillItem> billLists;
    /**
     * 时间
     */
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<BillItem> getBillLists() {
        return billLists;
    }

    public void setBillLists(List<BillItem> billLists) {
        this.billLists = billLists;
    }

}
