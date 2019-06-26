package com.xywy.component.uimodules.utils.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by stone on 2018/5/28.
 */

public class StatusBarUtils {

    public static void initSystemBar(Activity activity) {

        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上的手机
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
            ViewGroup childView = (ViewGroup) content.getChildAt(0);
            if (childView != null) {
                childView.setFitsSystemWindows(true);
            }
            window.setStatusBarColor(Color.WHITE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4以上5.0一下的手机
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
            ViewGroup childView = (ViewGroup) content.getChildAt(0);
            if (childView != null) {
                childView.setFitsSystemWindows(true);
            }

            View view = new View(activity);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusHeight(activity)));
            view.setBackgroundColor(Color.WHITE);
            content.addView(view);
        }


        applay(activity, true);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //android6.0以后可以对状态栏文字颜色和图标进行修改
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }


//        initSystemBar(activity, R.drawable.toolbar_bg_no_alpha_new);
    }

    public static void applay(Activity activity, boolean isDarkFont) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IStatusBar statusBar = null;
            if (OSUtils.isMeizuFlymeOS()) {
                statusBar = new MeizuStatusBar();
            } else if (OSUtils.isMIUI()) {
                statusBar = new MiuiStatusbar();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBar = new OSM();
            }

            if (statusBar != null) {
                statusBar.setStatusBarLightMode(activity, isDarkFont);
            }

        }

    }


    /**
     * 获取状态栏高度
     *
     * @return
     */
    private static int getStatusHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

}
