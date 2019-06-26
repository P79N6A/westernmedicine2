package com.xywy.askforexpert.appcommon.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * 进行BigDecimal对象的加减乘除，四舍五入等运算的工具类
 * Created by stone on 2016/12/6.
 */
public class ArithUtils {
    /**
     * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
     * 确的浮点数运算，包括加减乘除和四舍五入。
     */
    //默认运算精度
    private static final int DEF_SCALE = 2;

    //这个类不能实例化
    private ArithUtils() {
    }


    /**
     * 提供精确的加法运算。将结果进行四舍五入,保留2位小数
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal addForBigDecimal(BigDecimal v1, BigDecimal v2) {
        return v1.add(v2).setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal addForBigDecimal(String v1, String v2) {
        return new BigDecimal(v1).add(new BigDecimal(v2)).setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 提供精确的减法运算,将结果进行四舍五入,保留2位小数
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
//    public static float subForFloat(String v1, String v2) {
//        return new BigDecimal(v1).subtract(new BigDecimal(v2)).setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP).floatValue();
//    }
    public static BigDecimal subForBigDecimal(String v1, String v2) {
        return new BigDecimal(v1).subtract(new BigDecimal(v2)).setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal subForBigDecimal(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v2).setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的乘法运算。 四舍五入 保留小数点后两位
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mulForBigDecimal(String v1, String v2) {
        return new BigDecimal(v1).multiply(new BigDecimal(v2)).setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal mulForBigDecimal(BigDecimal v1, BigDecimal v2) {
        return v1.multiply(v2).setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，表示表示需要精确到小数点以后2位。
     * 定精度，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
//    public static double div(String v1, String v2) {
//        return new BigDecimal(v1).multiply(new BigDecimal(v2)).setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }


    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param numStr 需要四舍五入的数字 保留小数点后2位
     * @return 四舍五入后的结果
     */
    public static BigDecimal roundForBigDeciml(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            numStr = "0";
        }
        return new BigDecimal(numStr).setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal roundForBigDeciml(BigDecimal num) {
        return num.setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 精确对比两个数字
     *
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
     */
    public static int compareTo(String v1, String v2) {
        return new BigDecimal(v1).compareTo(new BigDecimal(v2));
    }

    /**
     * 精确对比两个数字
     *
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
     */
    public static int compareTo(BigDecimal v1, BigDecimal v2) {
        return v1.compareTo(v2);
    }
}
