package com.xywy.askforexpert.model.im.hxuser;

import android.text.TextUtils;

import com.xywy.askforexpert.model.im.group.ContactModel;

/**
 * Created by bailiangjin on 16/7/6.
 */
public class HxUserEntity {
    private int id;
    private String photo;
    private String realname;
    private String hxusername;
    private String mobile;
    private int hassend;
    private int type;//type 0 我的助理，1 医生，2 病历研讨班，3 媒体号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getHxusername() {
        return hxusername;
    }

    public void setHxusername(String hxusername) {
        this.hxusername = hxusername;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getHassend() {
        return hassend;
    }

    public void setHassend(int hassend) {
        this.hassend = hassend;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public ContactModel toContactModel() {
        ContactModel contactModel = new ContactModel();
        contactModel.setUserId("" + getId());
        contactModel.setHxId(getHxusername());
        contactModel.setUserName(TextUtils.isEmpty(getRealname()) ? contactModel.getUserId() : getRealname());
        contactModel.setHeadUrl(getPhoto());
        contactModel.setMyFriend(true);
        return contactModel;
    }


}