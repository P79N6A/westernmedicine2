package com.xywy.askforexpert.model.videoNews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/7/28 13:52
 */
@Generated("org.jsonschema2pojo")
public class VersionUpdateAndroidData {

    @SerializedName("apkname")
    @Expose
    private String apkname;
    @SerializedName("versioncode")
    @Expose
    private String versioncode;
    @SerializedName("apkurl")
    @Expose
    private String apkurl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("isupdate")
    @Expose
    private String isupdate;
    @SerializedName("isStopUpdate")
    @Expose
    private String isStopUpdate;

    @SerializedName("md5")
    @Expose
    private String fileMD5;

    /**
     * @return The apkname
     */
    public String getApkname() {
        return apkname;
    }

    /**
     * @param apkname The apkname
     */
    public void setApkname(String apkname) {
        this.apkname = apkname;
    }

    /**
     * @return The versioncode
     */
    public String getVersioncode() {
        return versioncode;
    }

    /**
     * @param versioncode The versioncode
     */
    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    /**
     * @return The apkurl
     */
    public String getApkurl() {
        return apkurl;
    }

    /**
     * @param apkurl The apkurl
     */
    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The isupdate
     */
    public String getIsupdate() {
        return isupdate;
    }

    /**
     * @param isupdate The isupdate
     */
    public void setIsupdate(String isupdate) {
        this.isupdate = isupdate;
    }

    public String getIsStopUpdate() {
        return isStopUpdate;
    }

    public void setIsStopUpdate(String isStopUpdate) {
        this.isStopUpdate = isStopUpdate;
    }


    public String getFileMD5() {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }
}
