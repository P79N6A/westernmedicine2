package com.xywy.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** 供添加提醒使用的 时间 日期处理类
 * Created by xywy on 2016/7/13.
 */
public class CalenderDateUtil {

    /**
     * 算天数
     *
     * @param data 10
     * @param type 天，周，月
     * @return
     */
    public static int getAdminDay(int data, String type) {
        if (type.equals("天")) {
            return data;
        } else if (type.equals("周")) {
            return data * 7;
        } else {
            return data * 30;
        }
    }

    /**
     * 返回之后days 天内的日期集合 （毫秒）
     *
     * @param data 2016-07-08 日期
     * @param days 10 天数
     * @param time 18:00 时间
     * @return
     */
    public static List<Long> getDateTime(String data, int days, String time) {
        List<Long> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        sb.append(data).append(" ").append(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;
        try {
            date = sdf.parse(sb.toString());
            for (int i = 0; i < days; i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, i);
                Long day = calendar.getTime().getTime();
                list.add(day);
                Log.d("SMILE", " Long day" + sdf.format(day));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 起始当天，每次用药long 集合
     * @param data
     * @param time
     * @return
     */
    public static List<Long> getDateTime(String data,String time) {
        List<Long> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        sb.append(data).append(" ").append(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;
        try {
                date = sdf.parse(sb.toString());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                Long day = calendar.getTime().getTime();
                list.add(day);
                Log.d("SMILE", " Long day" + sdf.format(day));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 计算出 过期时间
     * @param startTime
     * @param days
     * @return
     */
    public static Long getOverTime(String startTime,int days){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, days);
            Log.d("SMILE","overtime"+ calendar.getTime().getTime());
           return calendar.getTime().getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
