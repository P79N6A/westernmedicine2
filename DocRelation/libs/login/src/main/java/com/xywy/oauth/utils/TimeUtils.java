package com.xywy.oauth.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dongbo on 2015/11/16.
 */
public class TimeUtils {
    public static String getTime() {
        Long tsLong = System.currentTimeMillis();
        String s = tsLong + "";
        return s;
    }

    public static String getSecondTime() {
        Long tsLong = System.currentTimeMillis();
        String s = tsLong / 1000 + "";
        return s;
    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime + "-16:00";


    }

    // 将时间戳转为字符串
    public static String getStrDate(String cc_time) {
        String re_StrTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;


    }


}
