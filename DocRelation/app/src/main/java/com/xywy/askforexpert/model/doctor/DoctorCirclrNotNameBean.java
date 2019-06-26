package com.xywy.askforexpert.model.doctor;

import java.io.Serializable;
import java.util.List;


/**
 * 匿名动态
 */
public class DoctorCirclrNotNameBean implements Serializable {
    public int code;
    public String msg;
    public String is_doctor;// 1 动态列表 1：正常  0：资料不全

    public List<NotRealNameItem> list;
    public Messages message;
}
