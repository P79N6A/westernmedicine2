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

public class DeletePopupWindow extends PopupWindow {

    private View mMenuView;

    /**
     * @param context
     * @param itemsOnClick
     * @param str
     * @param type         是否显示 医生身份显示 1 医学生不显示 2 游客 0
     */
    public DeletePopupWindow(Activity context, OnClickListener itemsOnClick, String str, int type) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.delete_popupwindows, null);
        TextView linear_pick_photo = (TextView) mMenuView.findViewById(R.id.item_popupwindows_Photo);
        TextView share_to_friend_btn = (TextView) mMenuView.findViewById(R.id.share_to_friend_btn);
        if (type == 1) {
            share_to_friend_btn.setVisibility(View.VISIBLE);
            share_to_friend_btn.setOnClickListener(itemsOnClick);
        } else if (type == 2) {
            share_to_friend_btn.setVisibility(View.VISIBLE);
            share_to_friend_btn.setOnClickListener(itemsOnClick);
        } else {
            share_to_friend_btn.setVisibility(View.GONE);
        }
        linear_pick_photo.setText(str);
        TextView linear_cancel = (TextView) mMenuView.findViewById(R.id.item_popupwindows_cancel);
        // 取消按钮
        linear_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框eletePo
                dismiss();
            }
        });
        // 设置按钮监听
//		linear_system_img.setOnClickListener(itemsOnClick);
        linear_pick_photo.setOnClickListener(itemsOnClick);
//		linear_take_photo.setOnClickListener(itemsOnClick);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.WRAP_CONTENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(50000000);
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
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
