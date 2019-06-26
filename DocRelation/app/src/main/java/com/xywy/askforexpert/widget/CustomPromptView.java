package com.xywy.askforexpert.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;


public class CustomPromptView extends FrameLayout {

    public TextView tv_copy;

    public CustomPromptView(Context context) {
        super(context, null);
        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_prompt_view, this, true);
        tv_copy=(TextView) rootView.findViewById(R.id.tv_copy);
    }
}
