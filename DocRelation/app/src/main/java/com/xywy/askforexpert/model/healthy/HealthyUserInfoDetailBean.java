package com.xywy.askforexpert.model.healthy;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/8 16:06
 */

import javax.annotation.Generated;

/**
 * code	|code	integer	返回码
 * msg	|msg	string	返回信息
 * data	|data	array	数据体
 * id	|data|id	string	患者id
 * xm	|data|xm	string	患者姓名
 * xb	|data|xb	string	患者性别
 * txdz	|data|txdz	string	患者头像
 * nl	|data|nl	string	患者年龄
 * sfjd	|data|sfjd	integer	建档标识 1：有， 0：无
 * xy	|data|xy	integer	血压标识 1：有，0：无
 * xt	|data|xt	integer	血糖标识 1：有，0：无
 */
@Generated("org.jsonschema2pojo")
public class HealthyUserInfoDetailBean {
    /**
     * code : 10000
     * msg : 成功
     * data : {"id":"12","xm":"","xb":"男","txdz":"","nl":"未知","sfjd":0,"xy":1,"xt":1}
     */

//    private int code;
//    private String msg;
    /**
     * id : 12
     * xm :
     * xb : 男
     * txdz :
     * nl : 未知
     * sfjd : 0
     * xy : 1
     * xt : 1
     * "hxid": qyylxtid_11
     */

//    private DataBean data;

//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }

//    public static class DataBean {
    private String id;
    private String xm;
    private String xb;
    private String txdz;
    private String nl;
    private int sfjd;
    private int xy;
    private int xt;
    private String hxid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getTxdz() {
        return txdz;
    }

    public void setTxdz(String txdz) {
        this.txdz = txdz;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public int getSfjd() {
        return sfjd;
    }

    public void setSfjd(int sfjd) {
        this.sfjd = sfjd;
    }

    public int getXy() {
        return xy;
    }

    public void setXy(int xy) {
        this.xy = xy;
    }

    public int getXt() {
        return xt;
    }

    public void setXt(int xt) {
        this.xt = xt;
    }

    public String getHxid() {
        return hxid;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }
//    }
}
