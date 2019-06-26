package com.xywy.askforexpert.model.websocket.msg;

import com.xywy.askforexpert.model.websocket.type.ActType;

/**
 * 客户端发出的请求连接msg
 * 新版本去掉userid,转而添加了uid role vhost字段 stone
 */
public class ConnectMsg extends BaseSocketMsg {
    /**
     * 用户id 医脉中为医生用户id
     */
    protected String userid;

    public ConnectMsg(String userid) {
        this.userid = userid;
        act = ActType.CONNECT;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    // TODO: 2018/5/29 websocket新版本 stone
    //新版本字段添加 stone
//    protected int uid;
//    protected int role;
//    protected String vhost;
//
//    public ConnectMsg(int uid) {
//        this.uid = uid;
//        this.vhost = "com.xywy.ym";
//        act = ActType.CONNECT;
//    }
//
//    public int getUid() {
//        return uid;
//    }
//
//    public void setUid(int uid) {
//        this.uid = uid;
//    }
//
//    public String getVhost() {
//        return vhost;
//    }
//
//    public void setVhost(String vhost) {
//        this.vhost = vhost;
//    }
//
//    public int getRole() {
//        return role;
//    }
//
//    public void setRole(int role) {
//        this.role = role;
//    }
}
