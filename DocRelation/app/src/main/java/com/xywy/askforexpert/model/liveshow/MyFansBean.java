package com.xywy.askforexpert.model.liveshow;

/**
 * Created by bailiangjin on 2017/3/1.
 */

public class MyFansBean {
    /**
     * user : {"userid":12345,"name":"","sex":0,"synopsis":"","state":0,"lever":0}
     * userid : 12345
     * touserid : 123123
     */

    private UserBean user;
    private int userid;
    private int touserid;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getTouserid() {
        return touserid;
    }

    public void setTouserid(int touserid) {
        this.touserid = touserid;
    }

    public static class UserBean {
        /**
         * userid : 12345
         * name :
         * sex : 0
         * synopsis :
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
