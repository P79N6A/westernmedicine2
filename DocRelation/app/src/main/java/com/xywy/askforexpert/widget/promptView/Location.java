package com.xywy.askforexpert.widget.promptView;


import com.xywy.askforexpert.widget.promptView.location.BottomCenterLocation;
import com.xywy.askforexpert.widget.promptView.location.ICalculateLocation;
import com.xywy.askforexpert.widget.promptView.location.TopCenterLocation;
import com.xywy.askforexpert.widget.promptView.location.TopLeftLocation;
import com.xywy.askforexpert.widget.promptView.location.TopRightLocation;

/**
 * Created by chenpengfei on 2016/11/2.
 */
public enum  Location {

    TOP_LEFT(1),
    TOP_CENTER(2),
    TOP_RIGHT(3),
    BOTTOM_LEFT(4),
    BOTTOM_CENTER(5),
    BOTTOM_RIGHT(6);

    ICalculateLocation calculateLocation;

    private Location(int type) {
        switch (type) {
            case 1:
                calculateLocation = new TopLeftLocation();
                break;
            case 2:
                calculateLocation = new TopCenterLocation();
                break;
            case 3:
                calculateLocation = new TopRightLocation();
                break;
            case 4:
                calculateLocation = new TopLeftLocation();
                break;
            case 5:
                calculateLocation = new BottomCenterLocation();
                break;
            case 6:
                calculateLocation = new TopLeftLocation();
                break;
        }
    }

}
