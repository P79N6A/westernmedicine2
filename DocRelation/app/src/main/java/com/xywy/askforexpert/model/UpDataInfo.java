package com.xywy.askforexpert.model;

import java.io.Serializable;

/***
 * 版本更新
 *
 * @author LG
 */
public class UpDataInfo implements Serializable {

    public String appname;
    public String apkname;
    public String apkurl;
    public int versioncode;
    public String versionname;
    public String description;
    public int isupdate;//1 强制更新
    public String type;
    public int size;//文件的大小

    @Override
    public String toString() {
        return "UpDataInfo{" +
                "appname='" + appname + '\'' +
                ", apkname='" + apkname + '\'' +
                ", apkurl='" + apkurl + '\'' +
                ", versioncode=" + versioncode +
                ", versionname='" + versionname + '\'' +
                ", description='" + description + '\'' +
                ", isupdate=" + isupdate +
                ", type='" + type + '\'' +
                ", size=" + size +
                '}';
    }
}
