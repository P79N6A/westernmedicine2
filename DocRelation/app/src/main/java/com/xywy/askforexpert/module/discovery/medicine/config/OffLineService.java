package com.xywy.askforexpert.module.discovery.medicine.config;

/**
 * 线下版 业务
 * Created by bailiangjin on 2017/4/19.
 */
public class OffLineService {

    public static boolean isOffLine() {
        return BusinessLine.OFF_LINE == MSConfig.getCurrentBusinessLine();
    }

}
