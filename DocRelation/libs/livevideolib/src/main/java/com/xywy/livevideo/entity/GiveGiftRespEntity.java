package com.xywy.livevideo.entity;

/**
 * Created by zhangzheng on 2017/3/6.
 */
public class GiveGiftRespEntity {


    /**
     * code : 10000
     * msg : 成功
     * data : {"balance":10000}
     */

    private int code;
    private String msg;
    /**
     * balance : 10000
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int balance;

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }
    }
}
