package com.letv.skin.v4;

import android.content.Context;
import android.util.AttributeSet;

import com.letv.skin.base.BasePlayBtn;

public class V4PlayBtn extends BasePlayBtn {

    public V4PlayBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4PlayBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4PlayBtn(Context context) {
        super(context);
    }

    @Override
    protected String getPauseStyle() {
        return "video_pause";
    }

    @Override
    protected String getPlayStyle() {
        return "video_play";
    }

    @Override
    protected String getLayout() {
        return "letv_skin_v4_btn_play_layout";
    }

}
