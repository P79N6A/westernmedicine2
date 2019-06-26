package com.xywy.askforexpert.widget.view;

import android.content.Context;
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
 * 文献时间选择
 *
 * @author apple
 */
public class PopWxNumberP extends PopupWindow {

    private NumberPicker wxnp1;
    private NumberPicker wxnp2;

    public PopWxNumberP(Context context, OnClickListener onc) {

        View inflate = LayoutInflater.from(context).inflate(R.layout.pop_wx_select_time, null);
        wxnp1 = (NumberPicker) inflate.findViewById(R.id.wxnp1);
        wxnp2 = (NumberPicker) inflate.findViewById(R.id.wxnp2);
        Time time = new Time();
        time.setToNow();

        wxnp1.setMaxValue(time.year);
        wxnp2.setMaxValue(time.year);
        wxnp1.setMinValue(1998);

        wxnp2.setMinValue(1998);

        wxnp1.setValue(1998);
        wxnp2.setValue(time.year);
        wxnp1.getChildAt(0).setFocusable(false);
//		((EditText) wxnp1.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.doctor_cirlor_name));
//		((EditText) wxnp2.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.doctor_cirlor_name));
        wxnp2.getChildAt(0).setFocusable(false);
        setFocusable(true);
        setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);
        setHeight(LayoutParams.MATCH_PARENT);
        setWidth(LayoutParams.MATCH_PARENT);
        setContentView(inflate);

        View view = inflate.findViewById(R.id.view);
        TextView tv_ok = (TextView) inflate.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        view.setOnClickListener(onc);
        tv_ok.setOnClickListener(onc);
        tv_cancel.setOnClickListener(onc);


    }

    public int getWxnp1() {

        if (wxnp1 != null) {

            return wxnp1.getValue();
        }
        return -1;
    }

    public int getWxnp2() {

        if (wxnp2 != null) {

            return wxnp2.getValue();
        }
        return -1;
    }

}
