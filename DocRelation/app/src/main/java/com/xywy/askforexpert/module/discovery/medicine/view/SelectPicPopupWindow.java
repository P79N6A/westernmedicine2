package com.xywy.askforexpert.module.discovery.medicine.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.util.CamaraUtil;

import java.io.File;


public class SelectPicPopupWindow extends PopupWindow {

    public File getImageUri() {
        return imageUri;
    }

    /**
     * 图片地址
     */
    private  File imageUri;
    /**
     * 拍照 本地 取消 系统图片
     */
    private TextView linear_take_photo, linear_pick_photo, linear_cancel;
    private View mMenuView;
    // 头像修改 窗口实现监听类
    View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            dismiss();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:
                    imageUri =CamaraUtil.startCamera(mContext);
                    // startActivity(intent);
                    break;
                case R.id.item_popupwindows_Photo:
                    CamaraUtil.startPhoto(mContext);
                    break;
                case R.id.item_popupwindows_cancel:
                    dismiss();
                    break;
                default:
                    break;
            }

        }

    };
    Context mContext;
    public SelectPicPopupWindow(Activity context) {
        super(context);
        this.mContext=context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.item_popupwindows, null);
        linear_take_photo = (TextView) mMenuView.findViewById(R.id.item_popupwindows_camera);
        linear_pick_photo = (TextView) mMenuView.findViewById(R.id.item_popupwindows_Photo);
        linear_cancel = (TextView) mMenuView.findViewById(R.id.item_popupwindows_cancel);
        // 取消按钮
        linear_pick_photo.setOnClickListener(itemsOnClick);
        linear_take_photo.setOnClickListener(itemsOnClick);
        linear_cancel.setOnClickListener(itemsOnClick);
        // 设置按钮监听
//		linear_system_img.setOnClickListener(itemsOnClick);

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(50000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
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
