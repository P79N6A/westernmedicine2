package com.xywy.askforexpert.model.doctor;

import java.io.Serializable;

/**
 * 医圈新动态提醒
 */
public class Messages implements Serializable {
    /**
     * 实名未读消息
     */
    public String sunread;
    /**
     * 实名新消息数
     */
    public String snew;

    public int id;
    /**
     * 匿名未读消息
     */
    public String nunread;
    /**
     * 匿名新消息数
     */
    public String nnew;
    /**
     * 1 动态列表 1：正常  0：资料不全
     */
    public String is_doctor;// 1 动态列表 1：正常  0：资料不全

    public String getSunread() {
        return sunread;
    }

    public void setSunread(String sunread) {
        this.sunread = sunread;
    }

    public String getSnew() {
        return snew;
    }

    public void setSnew(String snew) {
        this.snew = snew;
    }

    public String getNunread() {
        return nunread;
    }

    public void setNunread(String nunread) {
        this.nunread = nunread;
    }

    public String getNnew() {
        return nnew;
    }

    public void setNnew(String nnew) {
        this.nnew = nnew;
    }

    public String getIs_doctor() {
        return is_doctor;
    }

    public void setIs_doctor(String is_doctor) {
        this.is_doctor = is_doctor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Messages [sunread=" + sunread + ", snew=" + snew + ", nunread="
                + nunread + ", nnew=" + nnew + ", is_doctor=" + is_doctor + "]";
    }

}
