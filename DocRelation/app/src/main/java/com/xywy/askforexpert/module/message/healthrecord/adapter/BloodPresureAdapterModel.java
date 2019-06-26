package com.xywy.askforexpert.module.message.healthrecord.adapter;


import com.xywy.askforexpert.model.healthrecord.BloodPresureModel;

/**
 * Created by yuwentao on 16/6/2.
 */
public class BloodPresureAdapterModel {

    String time;

    String type = "1";

    BloodPresureModel.Everyeasurement mEveryeasurement;

    public String getTime() {
        return time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public BloodPresureModel.Everyeasurement getEveryeasurement() {
        return mEveryeasurement;
    }

    public void setEveryeasurement(final BloodPresureModel.Everyeasurement everyeasurement) {
        mEveryeasurement = everyeasurement;
    }
}
