package com.xywy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dongbo on 2015/11/16.
 */
public class TimeUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String dateFormat_day = "HH:mm";
    public static String dateFormat_month = "MM";
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
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;


    }


    /**
     * 将当前java时间戳转为字符串
     * @param cc_time 时间
     * @param dateFormat 时间显示格式
     * @return
     */
    public static String getStrDate(String cc_time,String dateFormat) {
        String re_StrTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time));
        return re_StrTime;
    }

    /**
     * 将当前java时间戳转为字符串
     * @param cc_time 时间
     * @param dateFormat 时间显示格式
     * @return
     */
    public static String getPhpStrDate(String cc_time,String dateFormat) {
        String re_StrTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }


       /**
         * 日期格式字符串转换成时间戳
         * @param date_str 字符串日期
         * @param format 如：yyyy-MM-dd HH:mm:ss
         * @return
         */
       public static String date2TimeStamp(String date_str,String format){
           try {
               SimpleDateFormat sdf = new SimpleDateFormat(format);
               return String.valueOf(sdf.parse(date_str).getTime()/1000);
           } catch (Exception e) {
               e.printStackTrace();
           }
           return "";
         }

    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long dateToTimeStamp(String date_str,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date parse = sdf.parse(date_str);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String getFullTimeBase(long t){
        Date date = new Date(t);
        return sdf.format(date);
    }

    public static String formatTimeBase(long ts) {
        String s = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long)(ts) * 1000);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String weeks[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        if (isToday(ts)) {
            s = String.format("%02d:%02d", hour, minute);
        } else if (isYesterday(ts)) {
            s = String.format("昨天 %02d:%02d", hour, minute);
        } else if (isInWeek(ts)) {
            s = String.format("%s %02d:%02d", weeks[dayOfWeek - 1], hour, minute);
        } else if (isInYear(ts)) {
            s = String.format("%02d-%02d %02d:%02d", month+1, dayOfMonth, hour, minute);
        } else {
            s = String.format("%d-%02d-%02d %02d:%02d", year, month+1, dayOfMonth, hour, minute);
        }
        return s;
    }

    private static boolean isToday(long ts) {
        int now = now();
        return isSameDay(now, ts);
    }

    public static int now() {
        Date date = new Date();
        long t = date.getTime();
        return (int)(t/1000);
    }

    private static boolean isYesterday(long ts) {
        int now = now();
        int yesterday = now - 24*60*60;
        return isSameDay(ts, yesterday);
    }

    private static boolean isInWeek(long ts) {
        int now = now();
        //6天前
        long day6 = now - 6*24*60*60;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(day6 * 1000);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        int zero = (int)(cal.getTimeInMillis()/1000);
        return (ts >= zero);
    }

    private static boolean isInYear(long ts) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts*1000);
        int year = cal.get(Calendar.YEAR);

        cal.setTime(new Date());
        int y = cal.get(Calendar.YEAR);

        return (year == y);
    }

    private static boolean isSameDay(long ts1, long ts2) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts1 * 1000);
        int year1 = cal.get(Calendar.YEAR);
        int month1 = cal.get(Calendar.MONTH);
        int day1 = cal.get(Calendar.DAY_OF_MONTH);


        cal.setTimeInMillis(ts2 * 1000);
        int year2 = cal.get(Calendar.YEAR);
        int month2 = cal.get(Calendar.MONTH);
        int day2 = cal.get(Calendar.DAY_OF_MONTH);

        return ((year1==year2) && (month1==month2) && (day1==day2));
    }
}
