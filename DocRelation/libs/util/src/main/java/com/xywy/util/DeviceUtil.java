package com.xywy.util;

import android.app.Activity;
import android.view.Display;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/24 17:23
 */

public class DeviceUtil {
    public static int[] getScreenWidthAndHeight(Activity context) {
        int[] window = new int[2];
        window[0] = 0;
        window[1] = 1;
        if (context == null) {
            return window;
        }
        Display display = context.getWindowManager().getDefaultDisplay();

        window[0] = display.getWidth();
        window[1] = display.getHeight();
        return window;
    }
    public static int getScreenWidth(Activity context) {
        int[] array = getScreenWidthAndHeight(context);
        if (array.length > 1) {
            return array[0];
        }
        return 0;
    }

    public static int getScreenHeight(Activity context) {
        int[] array = getScreenWidthAndHeight(context);
        if (array.length > 1) {
            return array[1];
        }
        return 0;
    }
    public static String getSerialNumber() {
        return android.os.Build.SERIAL;
    }
}
