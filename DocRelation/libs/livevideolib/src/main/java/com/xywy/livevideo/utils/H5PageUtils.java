package com.xywy.livevideo.utils;

import android.app.Activity;
import android.content.Intent;

import com.xywy.livevideo.common_interface.Constant;
import com.xywy.util.L;

/**
 * Created by bailiangjin on 2017/3/16.
 */

public class H5PageUtils {

   public static final String CONSUME_DETAIL_PAGE_URL=Constant.H5_URL+Constant.CONSUME_DETAIL;
   public static final String LIVE_SHOW_INCOME_PAGE_URL =Constant.H5_URL+"account/index";

    public static void toH5Page(Activity activity, String title,String url) {
        Intent intent = new Intent();
        intent.putExtra("title", title);
        intent.putExtra("activity_url", url);
        intent.setAction("com.xywy.action.WebView");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {  //存在
            activity.startActivity(intent);
        } else {    //不存在
            L.e(intent.getAction()+" activity not exists");
        }
    }
}
