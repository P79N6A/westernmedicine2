package com.xywy.askforexpert.module.main.service.que.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * 功能：时间格式
 * </p>
 *
 * @author liuxuejiao
 * @2015-05-12 下午17:30:36
 */
public class TimeFormatUtils {

    /**
     * <p>
     * 功能：(医圈发布动态时间、资讯评论、临床指南评论、家庭医生评论显示页)时间格式
     * </p>
     *
     * @param serverTime 服务器时间(注意格式yyyy-MM-dd HH:mm:ss)
     * @return 时间字符串
     * @author liuxuejiao
     * @version
     * @2015-05-12 下午17:48:36
     */
    public static String formatShowTime(String serverTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date serverDate = sdf.parse(serverTime);
            long cha = System.currentTimeMillis() - serverDate.getTime();
            double hours = cha * 1.0 / (60 * 60 * 1000);
            Log.e("formatShowTime", hours + " " + serverDate.toString());
            if (hours > 0 && hours <= 1) {
                return "1小时前";
            } else if (hours > 1 && hours < 24) {

                return ((int) hours) + "小时前";
            } else if (hours >= 24 && hours < 48) {
                return "1天前";
            } else if (hours >= 48) {
                return formatTime(serverDate.getTime(), "yyyy-MM-dd HH:mm");
            } else {
                return "时间解析错误";

            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "时间解析错误";
        }

    }

    /**
     * <p>
     * 功能：(医圈发布动态时间、资讯评论、临床指南评论、家庭医生评论显示页)时间格式
     * </p>
     *
     * @param serverTime 服务器时间
     * @return 时间字符串
     * @author liuxuejiao
     * @version
     * @2015-05-15 下午17:48:36
     */
    public static String formatShowTime(long serverTime) {
        Log.e("formatShowTime-long", "serverTime:" + serverTime);
        long cha = System.currentTimeMillis() - serverTime * 1000;
        double hours = cha * 1.0 / (60 * 60 * 1000);
        if (hours > 0 && hours <= 1) {
            return "1小时前";
        } else if (hours > 1 && hours < 24) {

            return ((int) hours) + "小时前";
        } else if (hours >= 24) {
            return formatTime(serverTime * 1000, "yyyy-MM-dd");
        } else {
            return "时间解析错误";

        }

    }

    /**
     * <p>
     * 功能：(资讯发布时间、招聘的职位发布时间、各个收藏时间)时间格式
     * </p>
     *
     * @param serverTime 服务器时间
     * @param format     格式（默认yyyy-MM-dd）
     * @return 指定格式的时间字符串
     * @author liuxuejiao
     * @version
     * @2015-05-12 下午18:10:36
     */
    public static String formatTime(long serverTime, String format) {

        try {
            SimpleDateFormat sdf = null;
            if (TextUtils.isEmpty(format)) {
                sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            } else {
                sdf = new SimpleDateFormat(format, Locale.CHINA);
            }

            return sdf.format(serverTime);
        } catch (Exception e) {
            e.printStackTrace();
            return "时间解析错误";
        }

    }

    /**
     * <p>
     * 功能：(消息、问题广场)时间格式
     * </p>
     *
     * @param serverTime 服务器时间(注意格式yyyy-MM-dd HH:mm:ss)
     * @return 时间字符串
     * @author liuxuejiao
     * @version
     * @2015-05-12 下午18:23:36
     */
    public static String formatMsgTime(String serverTime) {

        if(TextUtils.isEmpty(serverTime)){
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date serverDate = sdf.parse(serverTime);
            Date currentDate = new Date();
            long cha = currentDate.getTime() - serverDate.getTime();
            double minute = cha * 1.0 / (60 * 1000);
            double hours = cha * 1.0 / (60 * 60 * 1000);
            if ((currentDate.getDate() == serverDate.getDate())
                    && (currentDate.getHours() == serverDate.getHours())
                    && (minute > 0 && minute <= 59)) {
                int currentMinute = (int) minute;
                if(0==currentMinute){
                    return "刚刚";
                }
                return currentMinute + "分钟前";

            }
            // else if (minute > 10 && minute <= 59) {
            // if ((currentDate.getDate() == serverDate.getDate())
            // && (currentDate.getHours() - serverDate.getHours() > 0)
            // && (serverDate.getHours() <= 23)) {
            // return formatTime(serverDate.getTime(), "HH:mm");
            // } else {
            // return formatTime(serverDate.getTime(), "MM-dd HH:mm");
            // }
            // } else {
            // return formatTime(serverDate.getTime(), "MM-dd HH:mm");
            // }
            else if (hours > 0 && hours <= 1) {
                return "1小时前";
            } else if (hours > 1 && hours < 24) {

                return ((int) hours) + "小时前";
            } else if (hours >= 24) {
                return formatTime(serverDate.getTime(), "MM-dd HH:mm");
            }
//			else if (hours >= 24 && hours < 48) {
//				return "1天前";
//			} else if (hours >= 48) {
//				return formatTime(serverDate.getTime(), "yyyy-MM-dd HH:mm");
//			} 
            else {
                return "时间解析错误";

            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "时间解析错误";
        }
    }

//	public static String getCurrentTimes() {
//		return getCurrentTimes();
//	}


}
