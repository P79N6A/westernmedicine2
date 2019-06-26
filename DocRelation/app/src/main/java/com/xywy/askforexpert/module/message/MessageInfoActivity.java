package com.xywy.askforexpert.module.message;


import android.content.Intent;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;

/**
 * Created by xugan on 2018/4/11. 消息
 */

public class MessageInfoActivity extends YMBaseActivity {
    public boolean isConflict,isCurrentAccountRemoved;

    @Override
    protected void initView() {
        Intent intent = getIntent();
        isConflict = intent.getBooleanExtra(Constants.INTENT_KEY_ISCONFLICT, false);
        isCurrentAccountRemoved = intent.getBooleanExtra(Constants.INTENT_KEY_ISCURRENTACCOUNTREMOVED, false);
        titleBarBuilder.setTitleText("消息");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_message_info;
    }
}
