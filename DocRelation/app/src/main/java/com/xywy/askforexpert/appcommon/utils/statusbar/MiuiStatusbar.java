package com.xywy.askforexpert.appcommon.utils.statusbar;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by stone on 2018/4/27.
 */

public class MiuiStatusbar implements IStatusBar {

    /**
     * 设置状态栏字体图标为深色，需要MIUI6以上
     *
     * @param isFontColorDark 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @Override
    public boolean setStatusBarLightMode(Activity activity, boolean isFontColorDark) {
        Window window = activity.getWindow();
        boolean result = false;
        try {
            if (window != null) {
                Class clazz = window.getClass();
                try {
                    int darkModeFlag = 0;
                    Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                    Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                    darkModeFlag = field.getInt(layoutParams);
                    Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                    if (isFontColorDark) {
                        extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                    } else {
                        extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                    }

                    if (isFontColorDark) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        int flag = window.getDecorView().getSystemUiVisibility()
                                & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                        window.getDecorView().setSystemUiVisibility(flag);
                    }
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public int getTypeName() {
        return IStatusBar.MIUI;
    }
}
