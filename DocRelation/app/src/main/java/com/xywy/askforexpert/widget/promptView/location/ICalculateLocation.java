package com.xywy.askforexpert.widget.promptView.location;

import android.view.View;

/**
 * Created by chenpengfei on 2016/11/2.
 */
public interface ICalculateLocation {

    int[] calculate(int[] srcViewLocation, View srcView, View promptView);

}
