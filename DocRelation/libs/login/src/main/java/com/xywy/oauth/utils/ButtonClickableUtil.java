package com.xywy.oauth.utils;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;


public class ButtonClickableUtil {

    private static final int a = 1;
    private static final float b = 0.2f;

    /**
     * @param etPhone   手机号EditText 不可为null
     * @param et1       可以是密码或者验证码 不可为null
     * @param et2       可以是手机号或验证码  可以为null
     * @param mLoginBtn button
     */
    public static void setButtonClickable(final EditText etPhone, final EditText et1, final EditText et2, final Button mLoginBtn) {

        mLoginBtn.setClickable(false);
        mLoginBtn.setTextColor(Color.parseColor("#cccccc"));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String phone = etPhone.getText().toString().trim();
                String s1 = et1.getText().toString().trim();

                if (et2 != null) {
                    String s2 = et2.getText().toString().trim();

                    if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(s1) &&
                            !TextUtils.isEmpty(s2)) {
                        mLoginBtn.setTextColor(Color.parseColor("#ffffff"));
                        mLoginBtn.setClickable(true);
                    } else {
                        mLoginBtn.setTextColor(Color.parseColor("#999999"));
                        mLoginBtn.setClickable(false);
                    }
                } else {
                    if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(s1)) {
                        mLoginBtn.setTextColor(Color.parseColor("#ffffff"));
                        mLoginBtn.setClickable(true);
                    } else {
                        mLoginBtn.setTextColor(Color.parseColor("#999999"));
                        mLoginBtn.setClickable(false);
                    }
                }
            }
        };
        etPhone.addTextChangedListener(textWatcher);
        et1.addTextChangedListener(textWatcher);
        if (et2 != null) et2.addTextChangedListener(textWatcher);
    }
}