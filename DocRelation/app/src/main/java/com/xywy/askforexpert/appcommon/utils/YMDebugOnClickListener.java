package com.xywy.askforexpert.appcommon.utils;

import android.view.View;

/**
 * 用于打印点击VIew的ID
 */
public class YMDebugOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        String viewStringId=DebugUtils.getViewStringId(v.getContext(),v);
        LogUtils.d(viewStringId+" clicked");
    }
}
