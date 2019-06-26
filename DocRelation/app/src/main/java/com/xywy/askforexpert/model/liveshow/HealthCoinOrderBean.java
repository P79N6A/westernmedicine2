package com.xywy.askforexpert.model.liveshow;

/**
 * Created by bailiangjin on 2017/2/24.
 */

public class HealthCoinOrderBean {


    /**
     * code : 10000
     * msg : 成功
     * data : {"uid":68248180,"order_id":"28","order_bn":"JKB000000000028","recharge_money":6,"jkb_num":60,"addtime":"2017-02-24 14:44:59"}
     */

    private int code;
    private String msg;
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
        /**
         * uid : 68248180
         * order_id : 28
         * order_bn : JKB000000000028
         * recharge_money : 6
         * jkb_num : 60
         * addtime : 2017-02-24 14:44:59
         */

        private int uid;
        private String order_id;
        private String order_bn;
        private String recharge_money;
        private String jkb_num;
        private String addtime;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_bn() {
            return order_bn;
        }

        public void setOrder_bn(String order_bn) {
            this.order_bn = order_bn;
        }

        public String getRecharge_money() {
            return recharge_money;
        }

        public void setRecharge_money(String recharge_money) {
            this.recharge_money = recharge_money;
        }

        public String getJkb_num() {
            return jkb_num;
        }

        public void setJkb_num(String jkb_num) {
            this.jkb_num = jkb_num;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
