package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 邀请好友
 *
 * @author 王鹏
 * @2015-6-11下午6:34:50
 */
public class InviteNewFriendInfo {
    private String code;
    private String msg;
    private List<InviteNewFriendInfo> data;
    private String xywyid;
    private String isfriend;
    private String name;
    private String phone;
    private String hassend;

    public String getHassend() {
        return hassend;
    }

    public void setHassend(String hassend) {
        this.hassend = hassend;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<InviteNewFriendInfo> getData() {
        return data;
    }

    public void setData(List<InviteNewFriendInfo> data) {
        this.data = data;
    }

    public String getXywyid() {
        return xywyid;
    }

    public void setXywyid(String xywyid) {
        this.xywyid = xywyid;
    }

    public String getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(String isfriend) {
        this.isfriend = isfriend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "InviteNewFriendInfo [code=" + code + ", msg=" + msg + ", data="
                + data + ", xywyid=" + xywyid + ", isfriend=" + isfriend
                + ", name=" + name + ", phone=" + phone + ", hassend="
                + hassend + "]";
    }


}
