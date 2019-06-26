package com.xywy.askforexpert.module.message.healthrecord.adapter;


import com.xywy.askforexpert.model.healthrecord.BloodSugarModel;

/**
 * Created by yuwentao on 16/6/5.
 */
public class BloodSugarAdapterModel {

    String time;

    String type = "0";

    BloodSugarModel.BloodSugarRecordItem mBloodSugarRecordItem;

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

    public BloodSugarModel.BloodSugarRecordItem getBloodSugarRecordItem() {
        return mBloodSugarRecordItem;
    }

    public void setBloodSugarRecordItem(final BloodSugarModel.BloodSugarRecordItem bloodSugarRecordItem) {
        mBloodSugarRecordItem = bloodSugarRecordItem;
    }
}
