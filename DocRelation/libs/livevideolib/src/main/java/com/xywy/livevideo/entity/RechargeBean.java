package com.xywy.livevideo.entity;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/3 10:23
 */

public class RechargeBean {
    public int getLiveMoney() {
        return liveMoney;
    }

    public int getRealMoney() {
        return realMoney;
    }

    private final int liveMoney;
    private final int realMoney;

    public RechargeBean(int liveMoney, int realMoney) {
        this.liveMoney = liveMoney;
        this.realMoney = realMoney;
    }
}
