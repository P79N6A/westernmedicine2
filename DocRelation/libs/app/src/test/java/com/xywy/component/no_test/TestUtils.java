package com.xywy.component.no_test;

import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.ShadowToast;

import java.lang.reflect.Field;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/7/19 9:12
 */
public class TestUtils {
    public static void print(String msg) {
        System.out.println(msg);
    }

    public static String getToast() {
        ShadowLooper.idleMainLooper();
        return ShadowToast.getTextOfLatestToast();
    }


    public static Object getDeclaredField(Object obj, String filedName) {
        try {
            Field testField = obj.getClass().getDeclaredField(filedName);
            testField.setAccessible(true);

            return testField.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
