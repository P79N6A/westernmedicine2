package com.xywy.askforexpert.widget.module.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.uilibrary.titlebar.ItemClickListener;


/**
 * Service Item 自定义View
 */
public class HomeRoomServiceItemView extends FrameLayout {
    private RelativeLayout rl_root;
    private ImageView iv_icon;
    private TextView tv_name;
    private TextView tv_open;
    private TextView tv_notice_number;


    public HomeRoomServiceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_home_room_service_cell, this);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_open = (TextView) findViewById(R.id.tv_open);
        tv_notice_number = (TextView) findViewById(R.id.tv_notice_number);
        parseAttrs(context, attrs);

    }


    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiscoverServiceItemView);
        setImageRes(iv_icon, R.styleable.DiscoverServiceItemView_icon_src, typedArray);

        setTvContent(tv_name, R.styleable.DiscoverServiceItemView_name_text, typedArray);
        setTvContent(tv_notice_number, R.styleable.DiscoverServiceItemView_number_text, typedArray);

        setViewVisibility(tv_notice_number, R.styleable.DiscoverServiceItemView_number_text_visibility, typedArray, false);
        setViewVisibility(tv_open, R.styleable.DiscoverServiceItemView_open_text_visibility, typedArray, true);

        typedArray.recycle();
    }

    private void setViewVisibility(View v, int styleableId, TypedArray typedArray) {
        boolean visibility = typedArray.getBoolean(styleableId, false);
        v.setVisibility(visibility ? VISIBLE : GONE);
    }

    private void setViewVisibility(View v, int styleableId, TypedArray typedArray, boolean defaultVisibility) {
        boolean visibility = typedArray.getBoolean(styleableId, defaultVisibility);
        v.setVisibility(visibility ? VISIBLE : GONE);
    }

    private void setImageRes(ImageView iv, int styleableId, TypedArray typedArray) {
        int resId = typedArray.getResourceId(styleableId, -1);
        if (resId != -1) {
            iv.setImageResource(resId);
        }

    }

    private void setViewBackground(View v, int styleableId, TypedArray typedArray) {
        int resId = typedArray.getResourceId(styleableId, -1);
        if (resId != -1) {
            v.setBackgroundResource(resId);
        }
    }

    private void setTvContent(TextView tv, int styleableId, TypedArray typedArray) {
        String str = typedArray.getString(styleableId);
        if (!TextUtils.isEmpty(str)) {
            tv.setText(str);
        }
    }

    public void setIconRes(int resId) {
        iv_icon.setImageResource(resId);
    }


    public void setNumberText(int number) {
       if(number<=0){
           tv_notice_number.setVisibility(GONE);
       }else {
           tv_notice_number.setVisibility(VISIBLE);
           if (number>=100){
               tv_notice_number.setText(99+"+");
           }else{
               tv_notice_number.setText(""+number);
           }

       }
    }

    public void setName(String description){
        if(!TextUtils.isEmpty(description)){
            tv_name.setText(description);
        }else {
            tv_name.setVisibility(INVISIBLE);
        }
    }

    public void setName(String description,int dimen){
        tv_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,dimen);
        if(!TextUtils.isEmpty(description)){
            tv_name.setText(description);
        }else {
            tv_name.setVisibility(INVISIBLE);
        }
    }

    public String getName(){
       return tv_name.getText().toString();
    }

    public void setState(String state){
        if(!TextUtils.isEmpty(state)){
            tv_open.setText(state);
        }else {
            tv_open.setVisibility(INVISIBLE);
        }
    }

    public void setState(String state,int dimen){
        tv_open.setTextSize(TypedValue.COMPLEX_UNIT_PX,dimen);
        if(!TextUtils.isEmpty(state)){
            tv_open.setText(state);
        }else {
            tv_open.setVisibility(INVISIBLE);
        }
    }

    public void setOpenTvTextSize(int textSize) {
        tv_open.setTextSize(textSize);
    }

    public void setOpenTvTextColor(int textColor) {
        tv_open.setTextColor(textColor);
    }

    public void setOpenTvVisibility(boolean isVisible) {
        tv_open.setVisibility(isVisible ? VISIBLE : INVISIBLE);
    }

    public void setNumberTvVisibility(boolean isVisible) {
        tv_notice_number.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setClickListener(final ItemClickListener listener) {
        rl_root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick();
            }
        });
    }
}