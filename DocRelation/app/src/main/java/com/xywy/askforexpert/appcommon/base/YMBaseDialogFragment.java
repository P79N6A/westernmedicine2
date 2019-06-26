package com.xywy.askforexpert.appcommon.base;

import android.view.View;

import com.xywy.uilibrary.fragment.SuperBaseDialogFragment;

import butterknife.ButterKnife;

/**
 * Created by bailiangjin on 2017/1/18.
 */

public  abstract class YMBaseDialogFragment extends SuperBaseDialogFragment {

    @Override
    public void beforeViewBind() {

    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void unBindView() {
        ButterKnife.unbind(this);
    }

}
