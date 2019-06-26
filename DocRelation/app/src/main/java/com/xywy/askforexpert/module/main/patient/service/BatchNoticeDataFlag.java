package com.xywy.askforexpert.module.main.patient.service;

/**
 * Created by jason on 2018/11/14.
 */

public class BatchNoticeDataFlag {
    static private BatchNoticeDataFlag instance;
    public static boolean BACK_FLAG = false;
    private BatchNoticeDataFlag() {
    }
    static public BatchNoticeDataFlag getInstance() {
        if(instance ==null) {
            instance = new BatchNoticeDataFlag();
        }
        return instance;
    }


}
