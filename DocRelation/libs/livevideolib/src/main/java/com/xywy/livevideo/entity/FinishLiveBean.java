package com.xywy.livevideo.entity;

import java.io.Serializable;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/6 10:19
 */


    public  class FinishLiveBean implements Serializable {
        /**
         * id : 19
         * userid : 18732252
         * name : 123
         * rtmp : rtmp://7958.livepush.myqcloud.com/live/7958_309da4d1?bizid=7958&txSecret=0b5f794b613bc1e26b182c0eb7cbdbec&txTime=58AC33AE
         * state : -1
         * amount : 0
         * cover : 123123
         * createtime : 2017-02-20 20:33:50
         * giftNum : 0
         * user : {"userid":18732252,"name":"asd","sex":1,"synopsis":"12341234123412","state":0,"lever":0}
         */

        private int id;
        private int userid;
        private String name;
        private String rtmp;
        private int state;
        private int amount;
        private String cover;
        private String createtime;
        private String giftNum;
        private UserBean user;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRtmp() {
            return rtmp;
        }

        public void setRtmp(String rtmp) {
            this.rtmp = rtmp;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getGiftNum() {
            return giftNum;
        }

        public void setGiftNum(String giftNum) {
            this.giftNum = giftNum;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean implements Serializable{
            /**
             * userid : 18732252
             * name : asd
             * sex : 1
             * synopsis : 12341234123412
             * state : 0
             * lever : 0
             */

            private int userid;
            private String name;
            private int sex;
            private String synopsis;
            private int state;
            private int lever;

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getSynopsis() {
                return synopsis;
            }

            public void setSynopsis(String synopsis) {
                this.synopsis = synopsis;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getLever() {
                return lever;
            }

            public void setLever(int lever) {
                this.lever = lever;
            }
        }
    }

