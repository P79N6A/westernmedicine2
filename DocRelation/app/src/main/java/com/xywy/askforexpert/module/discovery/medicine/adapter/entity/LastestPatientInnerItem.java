package com.xywy.askforexpert.module.discovery.medicine.adapter.entity;

import com.xywy.retrofit.net.BaseData;

/**
 * Created by xgxg on 2017/10/23.
 */

public class LastestPatientInnerItem extends BaseData{
    public String realname;
    public String photo;
    public String uid;

    public LastestPatientInnerItem(String uid,String realname, String photo) {
        this.uid = uid;
        this.realname = realname;
        this.photo = photo;
    }
}
