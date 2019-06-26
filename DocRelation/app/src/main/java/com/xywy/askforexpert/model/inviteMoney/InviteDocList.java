package com.xywy.askforexpert.model.inviteMoney;

import org.json.JSONObject;

/**
 * 邀请发钱啦 接口返回JSON的好友列表
 * <p>
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/11/27 11:18
 */
public class InviteDocList {

    /**
     * 头像url
     */
    private String photo;

    /**
     * id
     */
    private String id;

    /**
     * 姓名
     */
    private String realname;

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }

    public String getRealname() {
        return realname;
    }

    @Override
    public String toString() {
        return "InviteDocList{" +
                "photo='" + photo + '\'' +
                ", id='" + id + '\'' +
                ", realname='" + realname + '\'' +
                '}';
    }

    public void parseJson(JSONObject jsonObject) {
        if (jsonObject != null) {
            photo = jsonObject.optString("photo");
            id = jsonObject.optString("id");
            realname = jsonObject.optString("realname");
        }
    }
}
