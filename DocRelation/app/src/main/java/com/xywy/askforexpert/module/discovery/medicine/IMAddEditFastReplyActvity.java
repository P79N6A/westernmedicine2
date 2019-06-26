package com.xywy.askforexpert.module.discovery.medicine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.KeyBoardUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.uilibrary.titlebar.ItemClickListener;

/**
 * IM添加编辑快捷回复 stone
 */
public class IMAddEditFastReplyActvity extends YMBaseActivity {

    private EditText edit_text;
    private int type;//0 添加 1 编辑
    private int position;
    private String value;


    @Override
    protected void initView() {

        titleBarBuilder.addItem("保存", new ItemClickListener() {
            @Override
            public void onClick() {
                String str_mobile = edit_text.getText().toString().trim();
                if (TextUtils.isEmpty(str_mobile)) {
                    ToastUtils.shortToast("请编辑快捷回复内容");
                    return;
                }
                //stone 字数限制
                if (str_mobile.length() < 2 || str_mobile.length() > 100) {
                    ToastUtils.shortToast("快捷回复字数要求2-100字");
                    return;
                }
                if (type == 0) {
                    Intent data = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.KEY_VALUE, str_mobile);
                    data.putExtras(bundle);
                    setResult(RESULT_OK, data);
                    finish();
                } else if (type == 1) {
                    Intent data = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.KEY_VALUE, str_mobile);
                    bundle.putInt(Constants.KEY_POS, position);
                    data.putExtras(bundle);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        }).build();

        edit_text = (EditText) findViewById(R.id.edit_info);

        type = getIntent().getIntExtra(Constants.KEY_TYPE, 0);
        position = getIntent().getIntExtra(Constants.KEY_POS, 0);
        value = getIntent().getStringExtra(Constants.KEY_VALUE);

        if (type == 0) {
            titleBarBuilder.setTitleText("添加快捷回复");
            edit_text.setHint("请添加快捷回复内容");
        } else if (type == 1) {
            titleBarBuilder.setTitleText("编辑快捷回复");
            edit_text.setHint("请编辑快捷回复内容");
            edit_text.setText(value);
            edit_text.setSelection(value.length());
        }


        edit_text.setFocusable(true);
        edit_text.setFocusableInTouchMode(true);
        edit_text.requestFocus();
        edit_text.setSelection(edit_text.getText().toString().length());

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    //弹出键盘
                    KeyBoardUtils.openKeybord(edit_text);
                }
            }
        }, 200);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.im_fast_reply_add_edit_activity;
    }
}
