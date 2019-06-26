package com.xywy.util;

import android.net.Uri;

import java.io.File;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/29 16:41
 */

public class UriHelper {
    public static String  getUriFromFile(String filepath){
        return Uri.fromFile(new File(filepath)).toString();
    }
}
