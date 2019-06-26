package com.xywy.livevideo.entity;

/**
 * Created by zhangzheng on 2017/3/6.
 */
public class EnterLiveRoomRespEntity {


    /**
     * code : 10000
     * msg : 成功
     * data : {"id":123,"userid":123123,"name":"123","rtmp":"rtmp://7958.livepush.myqcloud.com/live/7958_47e10149?bizid=7958&txSecret=5713bff5a0642641223921c5e1a10ef3&txTime=58BA76D0","vod":"http://200056529.vod.myqcloud.com/200056529_d0178306beeb4d7594a75da4fe1fe705.f0.flv","state":1,"amount":0,"cover":"http://img2.cache.netease.com/photo/0010/2017-02-27/600x450_CE9TA26L50CB0010.png","chatroomsid":"9573834489857","createtime":"2017-03-03 16:12:00","giftNum":0,"user":{"userid":123123,"name":"汪振","sex":1,"synopsis":"1231231231","state":0,"lever":0,"portrait":"http://xs3.op.xywy.com/club.xywy.com/doc/20160326/0f2c685f2489cd.jpg","follow_state":0}}
     */

    private int code;
    private String msg;
    /**
     * id : 123
     * userid : 123123
     * name : 123
     * rtmp : rtmp://7958.livepush.myqcloud.com/live/7958_47e10149?bizid=7958&txSecret=5713bff5a0642641223921c5e1a10ef3&txTime=58BA76D0
     * vod : http://200056529.vod.myqcloud.com/200056529_d0178306beeb4d7594a75da4fe1fe705.f0.flv
     * state : 1
     * amount : 0
     * cover : http://img2.cache.netease.com/photo/0010/2017-02-27/600x450_CE9TA26L50CB0010.png
     * chatroomsid : 9573834489857
     * createtime : 2017-03-03 16:12:00
     * giftNum : 0
     * user : {"userid":123123,"name":"汪振","sex":1,"synopsis":"1231231231","state":0,"lever":0,"portrait":"http://xs3.op.xywy.com/club.xywy.com/doc/20160326/0f2c685f2489cd.jpg","follow_state":0}
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
        private int id;
        private int userid;
        private String name;
        private String rtmp;
        private String vod;
        private int state;
        private int amount;
        private String cover;
        private String chatroomsid;
        private String createtime;
        private int giftNum;
        /**
         * userid : 123123
         * name : 汪振
         * sex : 1
         * synopsis : 1231231231
         * state : 0
         * lever : 0
         * portrait : http://xs3.op.xywy.com/club.xywy.com/doc/20160326/0f2c685f2489cd.jpg
         * follow_state : 0
         */

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

        public String getVod() {
            return vod;
        }

        public void setVod(String vod) {
            this.vod = vod;
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

        public String getChatroomsid() {
            return chatroomsid;
        }

        public void setChatroomsid(String chatroomsid) {
            this.chatroomsid = chatroomsid;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getGiftNum() {
            return giftNum;
        }

        public void setGiftNum(int giftNum) {
            this.giftNum = giftNum;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            private int userid;
            private String name;
            private int sex;
            private String synopsis;
            private int state;
            private int lever;
            private String portrait;
            private int follow_state;

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

            public String getPortrait() {
                return portrait;
            }

            public void setPortrait(String portrait) {
                this.portrait = portrait;
            }

            public int getFollow_state() {
                return follow_state;
            }

            public void setFollow_state(int follow_state) {
                this.follow_state = follow_state;
            }
        }
    }
}
