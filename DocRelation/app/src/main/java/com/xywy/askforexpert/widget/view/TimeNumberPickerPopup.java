package com.xywy.askforexpert.widget.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.d_platform_n.NumberPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间选择
 *
 * @author 王鹏
 * @2015-5-26下午8:13:57
 */

@SuppressLint("NewApi")
public class TimeNumberPickerPopup extends PopupWindow {
    private Activity context;
    private View mMenuView;
    private NumberPicker time_np1, time_np2;
    private TextView date_text, save;
    private String str;
    private RelativeLayout re_number;
    private String str2;
    private int id;
    private int num;
    private EditText edit_num;

    private boolean isNumShwo = false;

    /**
     * @param context
     * @param saveOnclick
     * @param min         最小值
     * @param max         最大值
     * @param num         第几行
     */
    public TimeNumberPickerPopup(Activity context, OnClickListener saveOnclick,
                                 int min, int max, int num) {
        super(context);
        this.context = context;
        this.num = num;
        str = min + "";
        str2 = min + 1 + "";
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.fam_time_num_pic_popup, null);
        re_number = (RelativeLayout) mMenuView.findViewById(R.id.re_number);
        edit_num = (EditText) mMenuView.findViewById(R.id.edit_free_num);
        time_np1 = (NumberPicker) mMenuView.findViewById(R.id.time_np1);
        time_np2 = (NumberPicker) mMenuView.findViewById(R.id.time_np2);

        date_text = (TextView) mMenuView.findViewById(R.id.date_text);
        save = (TextView) mMenuView.findViewById(R.id.btn_save);

        date_text.setText(getData());
        time_np1.setMinValue(min);
        time_np1.setMaxValue(max - 1);

        time_np2.setMinValue(min + 1);
        time_np2.setMaxValue(max);

        time_np1.setFocusableInTouchMode(true);
        time_np2.setFocusableInTouchMode(true);

        time_np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {
                str = time_np1.getValue() + "";
                date_text.setText(getData());

            }

        });
        time_np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {
                str2 = time_np2.getValue() + "";
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

    public void getIsNumShow(boolean isNumShwo) {
        this.isNumShwo = isNumShwo;

        re_number.setVisibility(View.VISIBLE);

    }

    public String getData() {
        int min = Integer.parseInt(str);
        int max = Integer.parseInt(str2);
        if (max > min) {
            return str + "时 - " + str2 + "时";
        } else {
            ToastUtils.shortToast( "开始时间必须小于结束时间");
            return "";
        }

    }

    public List<String> getList() {
        List<String> list = new ArrayList<String>();
        int min = Integer.parseInt(str);
        int max = Integer.parseInt(str2);
        if (max > min) {
            for (int i = 0; i <= (max - min); i++) {
                list.add(num + "_" + (min + i));
            }
        }

        // list.add(num+"_"+str2);
        return list;
    }

    /**
     * 获取名额
     */
    public String getEidtStr() {
        return edit_num.getText().toString().trim();
    }

    public String getStrPhone() {
        return num + "_" + str + "_" + str2 + "_" + edit_num.getText();
    }
}
