package com.xywy.util;

/**
 * Created by bobby on 16/11/28.
 */
public class Converter {

    public static int StringToInt(String str, int defaultVal) {
        int value;

        try {
            value = Integer.parseInt(str);
        }catch (Exception e) {
            value = defaultVal;
        }
        return value;
    }
}
