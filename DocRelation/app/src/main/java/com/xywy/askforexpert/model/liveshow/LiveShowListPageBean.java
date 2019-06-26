package com.xywy.askforexpert.model.liveshow;

import java.util.List;

/**
 * Created by bailiangjin on 2017/3/2.
 */

public class LiveShowListPageBean {


    /**
     * code : 10000
     * msg : 成功
     * data : [{"id":29,"userid":18732252,"name":"123123","rtmp":"rtmp://7958.livepush.myqcloud.com/live/7958_c5d1524b?bizid=7958&txSecret=1be16a38eef9dfb94819677b51bfeb66&txTime=58AD496A","state":1,"amount":0,"cover":"123123","createtime":"2017-02-21 16:18:50","user":{"userid":18732252,"name":"asd","sex":1,"synopsis":"12341234123412","state":0,"lever":0}}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 29
         * userid : 18732252
         * name : 123123
         * rtmp : rtmp://7958.livepush.myqcloud.com/live/7958_c5d1524b?bizid=7958&txSecret=1be16a38eef9dfb94819677b51bfeb66&txTime=58AD496A
         * state : 1
         * amount : 0
         * cover : 123123
         * createtime : 2017-02-21 16:18:50
         * user : {"userid":18732252,"name":"asd","sex":1,"synopsis":"12341234123412","state":0,"lever":0}
         */

        private String id;
        private int userid;
        private String name;
        private String rtmp;
        private String vod;
        private List<String> vod_list;
        private int state;
        private int amount;
        private String cover;
        private String chatroomsid;
        private String createtime;
        private UserBean user;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getVod() {
            return vod;
        }

        public void setVod(String vod) {
            this.vod = vod;
        }

        public List<String> getVod_list() {
            return vod_list;
        }

        public void setVod_list(List<String> vod_list) {
            this.vod_list = vod_list;
        }

        public String getChatroomsid() {
            return chatroomsid;
        }

        public void setChatroomsid(String chatroomsid) {
            this.chatroomsid = chatroomsid;
        }

        public static class UserBean {
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
            private String portrait;
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

            public String getPortrait() {
                return portrait;
            }

            public void setPortrait(String portrait) {
                this.portrait = portrait;
            }
        }
    }
}
