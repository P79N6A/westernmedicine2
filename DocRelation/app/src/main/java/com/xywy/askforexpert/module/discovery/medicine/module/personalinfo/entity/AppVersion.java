package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity;

/**
 * Created by xgxg on 2017/5/19.
 */

public class AppVersion {

    /*
        "url":"http://appdl.xywy.com/android/medicine_supermarket/offline/1.0.0/medicine_supermarket_offline_1.0.0.apk",
        "version":"1.1",
        "is_upgrade":"0",
        "msg":"欢迎使用闻康用药助手APP",
        "app_secret_key":"e94c4906207941609cb6b9abfb0631e1"
    */

    private String url;
    private String version;
    private String is_upgrade;
    private String msg;
    private String app_secret_key;

    public String getIs_upgrade() {
        return is_upgrade;
    }

    public void setIs_upgrade(String is_upgrade) {
        this.is_upgrade = is_upgrade;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public String getApp_secret_key() {
        return app_secret_key;
    }

    public void setApp_secret_key(String app_secret_key) {
        this.app_secret_key = app_secret_key;
    }



}
