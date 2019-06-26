package com.xywy.askforexpert.appcommon.interfaces;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/27 9:51
 */

/**
 * TextWatcher 实现类，可以选择需要的方法重写，减少代码中不必要的空方法
 *
 * @author Jack Fang
 */
public abstract class TextWatcherImpl implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
