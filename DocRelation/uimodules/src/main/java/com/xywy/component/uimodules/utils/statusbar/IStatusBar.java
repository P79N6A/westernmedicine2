package com.xywy.component.uimodules.utils.statusbar;

import android.app.Activity;

/**
 * Created by stone on 2018/4/27.
 */
public interface IStatusBar {

    public static final int MIUI = 1;
    public static final int FLYME = 2;
    public static final int OS_M = 3;

    public boolean setStatusBarLightMode(Activity activity, boolean isFontColorDark);

    public int getTypeName();
}
