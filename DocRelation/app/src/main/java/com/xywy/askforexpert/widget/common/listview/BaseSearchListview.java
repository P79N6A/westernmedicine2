package com.xywy.askforexpert.widget.common.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.widget.Sidebar2;

/**
 * Created by bobby on 16/6/21.
 */
public class BaseSearchListview extends RelativeLayout {

    private ListView listView;
    private Sidebar2 sidebar;

    public BaseSearchListview(Context context) {
        super(context);
    }

    public BaseSearchListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSearchListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
