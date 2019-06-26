package com.xywy.askforexpert.model.im.group;

import com.google.gson.annotations.SerializedName;

import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bobby on 16/6/17.
 */
public class ContactBean {

    @PrimaryKey
    @SerializedName("userid")
    private String userId;

    @SerializedName("photo")
    private String headUrl;

    @SerializedName("realname")
    private String userName;

    /**
     * 用户 环信id
     */
    private String hxId;

    /**
     * 是否为群主
     */
    private boolean isMaster;

    /**
     * 是否是我的好友 显示用
     */
    private boolean isMyFriend;


    /**
     * 是否勾选 显示用 不存储
     */
    @Ignore
    private boolean isChecked;


    /**
     * 是否为组成员 显示用 不存储
     */
    @Ignore
    private boolean isGroupMember;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getHxId() {
        return hxId;
    }

    public void setHxId(String hxId) {
        this.hxId = hxId;
    }

    public boolean isMyFriend() {
        return isMyFriend;
    }

    public void setMyFriend(boolean myFriend) {
        isMyFriend = myFriend;
    }

    public boolean isGroupMember() {
        return isGroupMember;
    }

    public void setGroupMember(boolean groupMember) {
        isGroupMember = groupMember;
    }

}
