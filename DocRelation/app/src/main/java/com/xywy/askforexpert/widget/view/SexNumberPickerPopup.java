package com.xywy.askforexpert.widget.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.d_platform_n.NumberPicker;

/**
 * 性别选择器
 *
 * @author 王鹏
 * @2015-5-8上午9:39:46
 */
@SuppressLint("NewApi")
public class SexNumberPickerPopup extends PopupWindow {
    private Activity context;
    private View mMenuView;
    private NumberPicker sex_np1;
    private TextView date_text, save;
    private String[] str_sex = {"男", "女"};
    private String str = "男";
    private int id;

    public SexNumberPickerPopup(Activity context, OnClickListener saveOnclick) {
        super(context);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.sex_num_pic_popup, null);

        sex_np1 = (NumberPicker) mMenuView.findViewById(R.id.sex_np1);
        date_text = (TextView) mMenuView.findViewById(R.id.date_text);
        save = (TextView) mMenuView.findViewById(R.id.btn_save);
        date_text.setText(getData());
        sex_np1.setDisplayedValues(str_sex);
        sex_np1.setMinValue(0);
        sex_np1.setMaxValue(str_sex.length - 1);

        sex_np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {
                id = sex_np1.getValue();
                str = str_sex[id] + "";
                date_text.setText(getData());

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

    public String getData() {
        return str;
    }

    public int getID() {
        return id;
    }
}
