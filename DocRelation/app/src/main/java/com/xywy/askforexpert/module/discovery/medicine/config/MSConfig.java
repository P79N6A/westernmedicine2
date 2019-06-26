package com.xywy.askforexpert.module.discovery.medicine.config;


import com.xywy.askforexpert.BuildConfig;

/**
 * Created by bailiangjin on 2017/4/19.
 */

public class MSConfig {
    /**
     * 当前业务线
     */
    private final static BusinessLine currentBusinessLine = BuildConfig.isOffLine ? BusinessLine.OFF_LINE : BusinessLine.ON_LINE;

    public static BusinessLine getCurrentBusinessLine() {
        return currentBusinessLine;
    }


}
