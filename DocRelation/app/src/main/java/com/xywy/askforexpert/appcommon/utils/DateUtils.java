package com.xywy.askforexpert.appcommon.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bailiangjin on 16/5/12.
 */
public class DateUtils {

    private final static long ONEMINUTE = 60 * 1000;
    private static final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    private static final String CN_TIME_FORMAT_M_d_HH_mm = "M月d日HH时mm分";
    private static final String TIME_FORMAT_yyyy_M_d_HH_mm = "yyyy-M-d HH:mm:ss";

    /**
     * 格式化日期 中国大陆
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString_CN(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINESE);
        return sdf.format(date);
    }

    public static String getAnswerTime(String timeMillis) {
        if (TextUtils.isEmpty(timeMillis)) {
            return null;
        }
        return dateToString_CN(new Date(Long.parseLong(timeMillis)), CN_TIME_FORMAT_M_d_HH_mm);
    }

    public static String getRecordTime(String timeMillis) {
        if (TextUtils.isEmpty(timeMillis)) {
            return null;
        }
        return dateToString_CN(new Date(Long.parseLong(timeMillis)), TIME_FORMAT_yyyy_M_d_HH_mm);
    }

    public static String getTimeBeaforAMoth() {
        long curTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat(TIME_FORMAT_yyyy_M_d_HH_mm);
        Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(new Date(curTime));
        //cal.add(java.util.Calendar.DATE, -30); // 向前一周；如果需要向后一周，用正数即可
        cal.add(java.util.Calendar.MONTH, -1); // 向前一月；如果需要向后一月，用正数即可
        return sdf.format(cal.getTime());
    }


    /**
     * 格式化时间  stone
     */
    public static String formatTimeForMessageSetting(long time) {
        int hour = (int) (time / 3600000);
        int min = (int) (time % 3600000 / 60000);
        int second = (int) (time % 60000 / 1000);
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }

    /**
     * 判断当前系统时间是否不在指定时间的范围内
     *
     * @param beginHour 开始小时，例如22
     * @param beginMin  开始小时的分钟数，例如30
     * @param endHour   结束小时，例如 8
     * @param endMin    结束小时的分钟数，例如0
     * @return true表示在范围内，否则false
     */
//    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
//        boolean result = false;
//        final long aDayInMillis = 1000 * 60 * 60 * 24;
//        final long currentTimeMillis = System.currentTimeMillis();
//
//        Time now = new Time();
//        now.set(currentTimeMillis);
//
//        Time startTime = new Time();
//        startTime.set(currentTimeMillis);
//        startTime.hour = beginHour;
//        startTime.minute = beginMin;
//
//        Time endTime = new Time();
//        endTime.set(currentTimeMillis);
//        endTime.hour = endHour;
//        endTime.minute = endMin;
//
//        if (!startTime.before(endTime)) {
//            // 跨天的特殊情况（比如22:00-8:00）
//            startTime.set(startTime.toMillis(true) - aDayInMillis);
//            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
//
//            //没跨天
//            Time startTimeInThisDay = new Time();
//            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
//            if (!now.before(startTimeInThisDay)) {
//                result = true;
//            }
//        } else {
//            // 普通情况(比如 8:00 - 14:00)
//            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
//        }
//
//        return result;
//    }


    /**
     * 在指定范围时间内 如:5:30-22:30
     *
     * @param beginHour
     * @param beginMin
     * @param endHour
     * @param endMin
     * @return
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时 24H
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        final int start = beginHour * 60 + beginMin;// 起始时间 00:20的分钟数
        final int end = endHour * 60 + endMin;// 结束时间 8:00的分钟数
        if (minuteOfDay > start && minuteOfDay < end) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 转换日期 将日期转为今天, 昨天, 前天, XXXX-XX-XX, ...
     *
     * @param time 时间
     * @return 当前日期转换为更容易理解的方式
     */
    public static String translateDate(Long time) {
        long oneDay = 24 * 60 * 60 * 1000;
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        long todayStartTime = today.getTimeInMillis();

        if (time >= todayStartTime && time < todayStartTime + oneDay) { // today
//            return "今天";
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date(time);
            return dateFormat.format(date);
        } else if (time >= todayStartTime - oneDay && time < todayStartTime) { // yesterday
            return "昨天";
        } else if (time >= todayStartTime - oneDay * 2 && time < todayStartTime - oneDay) { // the day before yesterday
            return "前天";
        } else if (time > todayStartTime + oneDay) { // future
            return "将来某一天";
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(time);
            return dateFormat.format(date);
        }
    }

}
