package com.xywy.oauth.model;

/**
 * Created by DongJr on 2016/3/18.
 */
public class WXModel {
    //    access_token	接口调用凭证
//    expires_in	access_token接口调用凭证超时时间，单位（秒）
//    refresh_token	用户刷新access_token
//    openid	授权用户唯一标识
//    scope	用户授权的作用域，使用逗号（,）分隔
//    unionid	 当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
    private String access_token;
    private long expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
