package com.xywy.askforexpert.widget.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xywy.askforexpert.R;

public class MyPopupWindow extends PopupWindow {

    private TextView btnChanel;
    private TextView btn1;
    private TextView btn2;
    private TextView btn3;
    private TextView btn4;
    private TextView btn5;
    private View mMenuView;

    @SuppressWarnings("deprecation")
    public MyPopupWindow(Activity context,
                         OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_window_item, null);
        btn1 = (TextView) mMenuView.findViewById(R.id.btn_content1);
        btn2 = (TextView) mMenuView.findViewById(R.id.btn_content2);
        btn3 = (TextView) mMenuView.findViewById(R.id.btn_content3);
        btn4 = (TextView) mMenuView.findViewById(R.id.btn_content4);
        btn5 = (TextView) mMenuView.findViewById(R.id.btn_content5);
        btnChanel = (TextView) mMenuView.findViewById(R.id.btn_chanel);
        btnChanel.setOnClickListener(itemsOnClick);
        btn1.setOnClickListener(itemsOnClick);
        btn2.setOnClickListener(itemsOnClick);
        btn3.setOnClickListener(itemsOnClick);
        btn4.setOnClickListener(itemsOnClick);
        btn5.setOnClickListener(itemsOnClick);
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.WRAP_CONTENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(50000000);
        this.setBackgroundDrawable(dw);
        mMenuView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
