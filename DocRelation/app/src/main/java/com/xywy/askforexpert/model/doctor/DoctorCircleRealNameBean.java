package com.xywy.askforexpert.model.doctor;

import com.xywy.askforexpert.model.api.BaseResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * 实名动态bean
 *
 * @author LG
 */
public class DoctorCircleRealNameBean extends BaseResultBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
//    public int code;
    public int _id;
//    public String msg;

    public String is_doctor;// 1 动态列表 1：正常  0：资料不全
    public List<RealNameItem> list;
    public Messages message;
    public List<User> user;

    public String lookmecount;

    public List<InterestePersonItemBean> lookme;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }

    public String getIs_doctor() {
        return is_doctor;
    }

    public void setIs_doctor(String is_doctor) {
        this.is_doctor = is_doctor;
    }

    public List<RealNameItem> getList() {
        return list;
    }

    public void setList(List<RealNameItem> list) {
        this.list = list;
    }

    public Messages getMessage() {
        return message;
    }

    public void setMessage(Messages message) {
        this.message = message;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

//    @Override
//    public String toString() {
//        return "DoctorCirclrRealNameBean [code=" + code + ", _id=" + _id
//                + ", msg=" + msg + ", is_doctor=" + is_doctor + ", list="
//                + list + ", message=" + message + "]";
//    }


}
