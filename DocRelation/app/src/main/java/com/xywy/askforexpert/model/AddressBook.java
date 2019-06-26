package com.xywy.askforexpert.model;

import android.os.Build;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 通讯录
 * <p>
 * created by shihao
 */
public class AddressBook implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;
    private List<AddressBook> data;
    private String id;
    private String photo;
    private String realname;
    private String hxusername;
    private String mobile;
    private String hassend;
    private String header;
    private int type;//type 0 我的助理，1 医生，2 病历研讨班，3 媒体号

    private String xm;
    private String xb;
    private String txdz;
    private String nl;
    private String hxid;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
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

    public List<AddressBook> getData() {
        return data;
    }

    public void setData(List<AddressBook> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getHassend() {
        return hassend;
    }

    public void setHassend(String hassend) {
        this.hassend = hassend;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getHxid() {
        return hxid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof AddressBook) {
            AddressBook another = (AddressBook) o;
            // id相同即为同一对象
            return another.getId().equals(this.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hashCode(id);
        }else {
            return id.hashCode();
        }
    }
}
