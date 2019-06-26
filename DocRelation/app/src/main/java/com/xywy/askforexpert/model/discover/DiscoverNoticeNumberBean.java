package com.xywy.askforexpert.model.discover;

import com.xywy.askforexpert.model.api.BaseResultBean;

/**
 * 发现服务 提醒数目
 * Created by bailiangjin on 2016/12/29.
 */

public class DiscoverNoticeNumberBean extends BaseResultBean{

    /**
     * code : 0
     * msg : 成功
     * data : {"ques":0,"famuly":0}
     */

    private NoticeNumberBean data;

    public NoticeNumberBean getData() {
        return data;
    }

    public void setData(NoticeNumberBean data) {
        this.data = data;
    }

    public static class NoticeNumberBean {
        /**
         * 修建英
         ask 提问数
         hnum   帮助数
         family  家庭医生数
         plus_num  加号数
         phone_num  电话医生订单数
         */

        private int ask;
        private int hnum;
        private int family;
        private int plus_num;
        private int phone_num;

        public int getAsk() {
            return ask;
        }

        public void setAsk(int ask) {
            this.ask = ask;
        }

        public int getHnum() {
            return hnum;
        }

        public void setHnum(int hnum) {
            this.hnum = hnum;
        }

        public int getFamily() {
            return family;
        }

        public void setFamily(int family) {
            this.family = family;
        }

        public int getPlus_num() {
            return plus_num;
        }

        public void setPlus_num(int plus_num) {
            this.plus_num = plus_num;
        }

        public int getPhone_num() {
            return phone_num;
        }

        public void setPhone_num(int phone_num) {
            this.phone_num = phone_num;
        }
    }


}

