package com.xywy.askforexpert.widget.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.d_platform_n.NumberPicker;

/**
 * 日期 选择器
 *
 * @author 王鹏
 * @2015-5-7下午8:39:51
 */
@SuppressLint("NewApi")
public class NumberPickerPopupwinow2 extends PopupWindow {

    public String str1 = "";
    public String str2 = "";
    public String str3 = "";
    Time t;
    int year;
    int month;
    int day;
    private Activity context;
    private Activity context1;
    // private int style;
    private View mMenuView;
    private NumberPicker np1, np2, np3;
    private TextView date_text;
    private TextView save;

    public NumberPickerPopupwinow2(Activity context, OnClickListener saveOnclick) {
        super(context);
        this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        mMenuView = inflater.inflate(R.layout.numberpickpopupwindow, null);

        np1 = (NumberPicker) mMenuView.findViewById(R.id.np1);
        np2 = (NumberPicker) mMenuView.findViewById(R.id.np2);
        np3 = (NumberPicker) mMenuView.findViewById(R.id.np3);
        date_text = (TextView) mMenuView.findViewById(R.id.date_text);
        save = (TextView) mMenuView.findViewById(R.id.btn_save);
        date_text.setText(getDate());
        t = new Time();
        t.setToNow();
        year = t.year;
        month = t.month + 1;
        day = t.monthDay;

        np1.setMaxValue(2100);
        np1.setMinValue(year-1);
        np1.setValue(year);
        str1 = year + "";
        str2 = month + "";
        str3 = day + "";
        date_text.setText(getDate());
        np1.setFocusableInTouchMode(true);
        np2.setFocusableInTouchMode(true);
        np3.setFocusableInTouchMode(true);
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                str1 = np1.getValue() + "";
                date_text.setText(getDate());
                if (Integer.parseInt(str1) % 4 == 0
                        && Integer.parseInt(str1) % 100 != 0
                        || Integer.parseInt(str1) % 400 == 0) {
                    if (str2.equals("1") || str2.equals("3")
                            || str2.equals("5") || str2.equals("7")
                            || str2.equals("8") || str2.equals("10")
                            || str2.equals("12")) {
                        np3.setMaxValue(31);
                        np3.setMinValue(1);
                    } else if (str2.equals("4") || str2.equals("6")
                            || str2.equals("9") || str2.equals("11")) {
                        np3.setMaxValue(30);
                        np3.setMinValue(1);
                    } else {
                        np3.setMaxValue(29);
                        np3.setMinValue(1);
                    }

                } else {
                    if (str2.equals("1") || str2.equals("3")
                            || str2.equals("5") || str2.equals("7")
                            || str2.equals("8") || str2.equals("10")
                            || str2.equals("12")) {
                        np3.setMaxValue(31);
                        np3.setMinValue(1);
                    } else if (str2.equals("4") || str2.equals("6")
                            || str2.equals("9") || str2.equals("11")) {
                        np3.setMaxValue(30);
                        np3.setMinValue(1);
                    } else {
                        np3.setMaxValue(28);
                        np3.setMinValue(1);
                    }
                }

            }
        });

        np2.setMaxValue(12);
        np2.setMinValue(1);
        np2.setValue(month);
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                str2 = np2.getValue() + "";
                date_text.setText(getDate());
                if (str2.equals("1") || str2.equals("3") || str2.equals("5")
                        || str2.equals("7") || str2.equals("8")
                        || str2.equals("10") || str2.equals("12")) {
                    np3.setMaxValue(31);
                    np3.setMinValue(1);
                } else if (str2.equals("4") || str2.equals("6")
                        || str2.equals("9") || str2.equals("11")) {
                    np3.setMaxValue(30);
                    np3.setMinValue(1);
                } else {
                    if (Integer.parseInt(str1) % 4 == 0
                            && Integer.parseInt(str1) % 100 != 0
                            || Integer.parseInt(str1) % 400 == 0) {
                        np3.setMaxValue(29);
                        np3.setMinValue(1);
                    } else {
                        np3.setMaxValue(28);
                        np3.setMinValue(1);
                    }
                }
            }
        });

        np3.setMaxValue(31);
        np3.setMinValue(1);
        np3.setValue(day);
        np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                str3 = np3.getValue() + "";
                date_text.setText(getDate());
            }
        });
        save.setOnClickListener(saveOnclick);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x70f000f);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
    }

    public String getDate() {
        return str1 + "年" + str2 + "月" + str3 + "日";
    }

    public String getData2() {
        return str1 + "-" + str2 + "-" + str3;
    }

    // @Override
    // public void show() {
    // // TODO Auto-generated method stub
    // super.show();
    // }
}
