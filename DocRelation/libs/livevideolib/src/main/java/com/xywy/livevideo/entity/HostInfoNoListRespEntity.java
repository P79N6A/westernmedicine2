package com.xywy.livevideo.entity;

/**
 * Created by zhangzheng on 2017/3/6.
 */
public class HostInfoNoListRespEntity {

    /**
     * code : 10000
     * msg : 成功
     * data : {"userid":18732252,"name":"asd","sex":1,"synopsis":"12341234123412","state":0,"lever":0,"followNum":"1","fansNum":"0","portrait":"http://xs3.op.xywy.com/club.xywy.com/doc/20160326/0f2c685f2489cd.jpg","follow_state":0}
     */

    private int code;
    private String msg;
    /**
     * userid : 18732252
     * name : asd
     * sex : 1
     * synopsis : 12341234123412
     * state : 0
     * lever : 0
     * followNum : 1
     * fansNum : 0
     * portrait : http://xs3.op.xywy.com/club.xywy.com/doc/20160326/0f2c685f2489cd.jpg
     * follow_state : 0
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
        private int userid;
        private String name;
        private int sex;
        private String synopsis;
        private int state;
        private int lever;
        private String followNum;
        private String fansNum;
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

        public String getFollowNum() {
            return followNum;
        }

        public void setFollowNum(String followNum) {
            this.followNum = followNum;
        }

        public String getFansNum() {
            return fansNum;
        }

        public void setFansNum(String fansNum) {
            this.fansNum = fansNum;
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
