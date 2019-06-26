package com.xywy.askforexpert.model.liveshow;

/**
 * Created by bailiangjin on 2017/2/24.
 */

public class AlipayResultBean {


    /**
     * code : 10000
     * msg : 成功
     * data : {"order_id":"Z16032142438","order_name":"%D7%A8%BC%D2%CD%F8+-+%B5%E7%BB%B0%D2%BD%C9%FA%C6%B5%B5%C0","order_total_amount":"300.00","order_time":"1458526150","order_buyer_account":"65246946","payment_trade_no":"145852863358076760"}
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
         * order_id : Z16032142438
         * order_name : %D7%A8%BC%D2%CD%F8+-+%B5%E7%BB%B0%D2%BD%C9%FA%C6%B5%B5%C0
         * order_total_amount : 300.00
         * order_time : 1458526150
         * order_buyer_account : 65246946
         * payment_trade_no : 145852863358076760
         */

        private String order_id;
        private String order_name;
        private String order_total_amount;
        private String order_time;
        private String order_buyer_account;
        private String payment_trade_no;
        private String str;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_name() {
            return order_name;
        }

        public void setOrder_name(String order_name) {
            this.order_name = order_name;
        }

        public String getOrder_total_amount() {
            return order_total_amount;
        }

        public void setOrder_total_amount(String order_total_amount) {
            this.order_total_amount = order_total_amount;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public String getOrder_buyer_account() {
            return order_buyer_account;
        }

        public void setOrder_buyer_account(String order_buyer_account) {
            this.order_buyer_account = order_buyer_account;
        }

        public String getPayment_trade_no() {
            return payment_trade_no;
        }

        public void setPayment_trade_no(String payment_trade_no) {
            this.payment_trade_no = payment_trade_no;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }
}
