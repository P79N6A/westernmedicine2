package com.xywy.askforexpert.module.message;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/8/1 10:03
 */
public abstract class BaseCustomViewDialogFragment extends DialogFragment {
    public static final String CUSTOM_CONTENT_VIEW_ID = "contentViewLayoutId";

    protected int mContentViewLayoutId;

    protected String userId;

    public BaseCustomViewDialogFragment() {

    }

    public static void setArgs(BaseCustomViewDialogFragment fragment, int contentViewLayoutId) {
        Bundle args = new Bundle();
        args.putInt(CUSTOM_CONTENT_VIEW_ID, contentViewLayoutId);
        fragment.setArguments(args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = YMUserService.isGuest() ? "0" : YMApplication.getPID();

        Bundle arguments = getArguments();
        if (arguments != null) {
            mContentViewLayoutId = arguments.getInt(CUSTOM_CONTENT_VIEW_ID, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(mContentViewLayoutId, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initViews(contentView);

        return contentView;
    }

    protected abstract void initViews(View contentView);
}
