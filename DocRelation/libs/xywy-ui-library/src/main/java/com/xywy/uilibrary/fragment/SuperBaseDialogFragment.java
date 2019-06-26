package com.xywy.uilibrary.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bailiangjin on 2017/1/17.
 */

public abstract class SuperBaseDialogFragment extends DialogFragment implements FragmentLifecycle {

    protected View rootView;

    public SuperBaseDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutResId(), container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        beforeViewBind();
        bindView(rootView);
        initView();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("SuperBaseDialogFragment","DialogFragment:::-->>onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        unBindView();
        super.onDestroyView();
    }



}
