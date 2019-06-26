package com.xywy.askforexpert.widget.module.doctorcircle;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.widget.module.doctorcircle.listener.PopupWindowListener;


/**
 * 医圈 底部弹框
 * Created by bailiangjin on 2016/11/7.
 */

public class DoctorCircleBottomPopupWindow extends BottomPushPopupWindow {

    protected PopupWindowListener popupWindowListener;

    public DoctorCircleBottomPopupWindow(final Activity activity, PopupWindowType relationType, final PopupWindowListener popupWindowListener) {
        super(activity,relationType);
        this.popupWindowListener = popupWindowListener;
    }

    @NonNull
    @Override
    protected View generateCustomView(Activity activity, PopupWindowType relationType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.report_add_window_layout, null);
        TextView report = (TextView) view.findViewById(R.id.report);
        View report_window_divider = view.findViewById(R.id.report_window_divider);
        TextView addFriend = (TextView) view.findViewById(R.id.add_friend);
        final TextView delete = (TextView) view.findViewById(R.id.delete_post);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);

        LogUtils.d("relationType = " + relationType.getShowType());
        //设置元素可见性
        if (("100").equals(relationType.getShowType())) {
            //全显示
        } else if (("2").equals(relationType.getShowType()) || ("3").equals(relationType.getShowType())) {
            addFriend.setVisibility(View.GONE);
            report_window_divider.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        } else if (("4").equals(relationType.getShowType())) {
            addFriend.setVisibility(View.GONE);
            report_window_divider.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
        } else {
            addFriend.setVisibility(View.VISIBLE);
            report_window_divider.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
        }

        if (!relationType.isDeleteShow()) {
            delete.setVisibility(View.GONE);
        }
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                popupWindowListener.onReport();

            }
        });
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                popupWindowListener.onAddFriend();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                popupWindowListener.onDelete();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setAnimationStyle(R.style.PopupAnimation);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        setTouchable(true);

//        setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                //CommonUtils.backgroundAlpha(activity, 1f);
//            }
//        });
        return view;
    }





    public void show() {
        LogUtils.d("MODEL" + android.os.Build.MODEL + ", " + Build.BOARD + ", " + Build.MANUFACTURER);
        if ("R7Plus".equals(Build.MODEL)) {
            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, DensityUtils.dp2px(36));
        } else {
            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //CommonUtils.backgroundAlpha(activity, 0.5f);
    }

}
