package com.xywy.askforexpert.model.liveshow;

/**
 * Created by bailiangjin on 2017/3/8.
 */

public class LiveShowStateInfoEntity {
    /**
     * is_Anchor : 1
     * cover : http://img2.cache.netease.com/photo/0010/2017-02-27/600x450_CE9TA26L50CB0010.png
     * balance : 10000
     */

    private int is_Anchor;
    private String cover;
    private int balance;

    public int getIs_Anchor() {
        return is_Anchor;
    }

    public void setIs_Anchor(int is_Anchor) {
        this.is_Anchor = is_Anchor;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
