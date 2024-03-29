package com.letv.skin.v4;

import android.content.Context;
import android.util.AttributeSet;

import com.letv.skin.base.BaseChgScreenBtn;

public class V4ChgScreenBtn extends BaseChgScreenBtn {

    public V4ChgScreenBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4ChgScreenBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4ChgScreenBtn(Context context) {
        super(context);
    }

    @Override
    protected String getLayoutId() {
        return "letv_skin_v4_btn_chgscreen_layout";
    }

    @Override
    protected String getZoomInStyle() {
        return "to_full_screen";
    }

    @Override
    protected String getZoomOutStyle() {
        return "to_vertical_screen";
    }

}
