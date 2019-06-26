package com.xywy.askforexpert.module.drug;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;

/**
 * Created by jason on 2018/7/16.
 */

public class OnlineRoomCloseActivity extends YMBaseActivity {
    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("问诊用药");
        findViewById(R.id.phone_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008591200"));
                startActivity(dialIntent);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.online_room_close_layout;
    }
}
