package com.xywy.askforexpert.widget.promptView.location;

import android.view.View;

/**
 * Created by chenpengfei on 2016/11/2.
 */
public class TopLeftLocation implements ICalculateLocation {

    @Override
    public int[] calculate(int[] srcViewLocation, View srcView, View promptView) {
        int[] location = new int[2];
        location[0] = srcViewLocation[0];
        location[1] = srcViewLocation[1] - promptView.getHeight();
        return location;
    }
}
