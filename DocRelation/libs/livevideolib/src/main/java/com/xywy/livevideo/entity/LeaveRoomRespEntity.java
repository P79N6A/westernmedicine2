package com.xywy.livevideo.entity;

/**
 * Created by zhangzheng on 2017/3/6.
 */
public class LeaveRoomRespEntity {

    /**
     * code : 10000
     * msg : 成功
     * data : {"id":4,"userid":18732252,"rid":19,"createtime":1487656653,"chatroomsid":"8593739939841","state":-1}
     */

    private int code;
    private String msg;
    /**
     * id : 4
     * userid : 18732252
     * rid : 19
     * createtime : 1487656653
     * chatroomsid : 8593739939841
     * state : -1
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
        private int rid;
        private int createtime;
        private String chatroomsid;
        private int state;

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

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }

        public String getChatroomsid() {
            return chatroomsid;
        }

        public void setChatroomsid(String chatroomsid) {
            this.chatroomsid = chatroomsid;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
