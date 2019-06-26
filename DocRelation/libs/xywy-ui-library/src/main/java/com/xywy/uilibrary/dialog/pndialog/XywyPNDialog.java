package com.xywy.uilibrary.dialog.pndialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.uilibrary.R;
import com.xywy.uilibrary.dialog.pndialog.listener.PNDialogListener;


/**
 * 确认取消dialog
 * Author:  liangjin.bai
 * Email: bailiangjin@gmail.com
 * Create Time: 2015/11/12 17:32
 */
public class XywyPNDialog {

    AlertDialog alertDialog;
    Context mContext;
    String title;
    String content;
    String positiveStr;
    String negativeStr;
    int positiveStrColor;
    int negativeStrColor;
    boolean cancelable;
    boolean noTitle;
    boolean noNegativeBtn;
    /**
     * 提示内容 tv Gravity
     */
    int contentGravity;

    protected XywyPNDialog(final Builder builder) {
        this.mContext = builder.mContext;
        this.title = builder.title;
        this.content = builder.content;
        this.positiveStr = builder.positiveStr;
        this.negativeStr = builder.negativeStr;
        this.positiveStrColor = builder.positiveStrColor;
        this.negativeStrColor = builder.negativeStrColor;
        this.cancelable = builder.cancelable;
        this.noTitle = builder.noTitle;
        this.noNegativeBtn = builder.noNegativeBtn;
        this.contentGravity = builder.contentGravity;

        //stone 添加主题 解决崩溃(You need to use a Theme.AppCompat theme (or descendant) with this activity)
        //R.style.Theme_AppCompat_Light_Dialog_Alert 备用 没试 Theme_AppCompat_Dialog
        alertDialog = new AlertDialog.Builder(mContext,R.style.Theme_AppCompat_Dialog)
                .setCancelable(cancelable).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_common);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) window.findViewById(R.id.tv_content);
        TextView tv_cancel = (TextView) window.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) window.findViewById(R.id.tv_confirm);
        ImageView iv_vertical_line = (ImageView) window.findViewById(R.id.iv_vertical_line);

        tv_title.setText(title);
        tv_title.setVisibility(noTitle ? View.GONE : View.VISIBLE);

        tv_content.setText(content);
        tv_content.setGravity(builder.contentGravity);
        tv_confirm.setText(positiveStr);
        if (positiveStrColor > 0) {
            tv_confirm.setTextColor(mContext.getResources().getColor(negativeStrColor));
        }
        if (noNegativeBtn) {
            tv_cancel.setVisibility(View.GONE);
            iv_vertical_line.setVisibility(View.GONE);
        } else {
            tv_cancel.setVisibility(View.VISIBLE);
            iv_vertical_line.setVisibility(View.VISIBLE);
            tv_cancel.setText(negativeStr);
            if (negativeStrColor > 0) {
                tv_cancel.setTextColor(mContext.getResources().getColor(negativeStrColor));
            }
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (null != builder.PNDialogListener) {
                        builder.PNDialogListener.onNegative();
                    }

                }
            });
        }

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (null != builder.PNDialogListener) {
                    builder.PNDialogListener.onPositive();
                }

            }
        });
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPositiveStr() {
        return positiveStr;
    }

    public String getNegativeStr() {
        return negativeStr;
    }

    public boolean isCancelable() {
        return cancelable;
    }


    public void show() {
        alertDialog.show();
    }

    public void dismiss() {
        alertDialog.dismiss();
    }

    public static class Builder {
        Context mContext;
        String title;
        String content;
        String positiveStr;
        String negativeStr;
        int positiveStrColor;
        int negativeStrColor;
        boolean cancelable;
        boolean noTitle;
        boolean noNegativeBtn;
        /**
         * 提示内容 tv Gravity
         */
        int contentGravity;

        PNDialogListener PNDialogListener;

        public Builder() {
            this.title = "提示";
            this.content = "";
            this.positiveStr = "确定";
            this.negativeStr = "取消";
            this.positiveStrColor = 0;
            this.negativeStrColor = 0;
            this.cancelable = true;
            this.noTitle = false;
            this.contentGravity = Gravity.CENTER;
        }

        /**
         * 最终创建方法
         *
         * @param context
         * @param PNDialogListener 不需要监听按键回调 传null; 确认、取消都需要监听回调 传YMPNDialogListener; 只需要监听确认回调 传YMPositiveDialogListener
         * @return
         */
        public XywyPNDialog create(Context context, PNDialogListener PNDialogListener) {
            this.mContext = context;
            this.PNDialogListener = PNDialogListener;
            return new XywyPNDialog(this);
        }

        /**
         * 设置提示标题内容
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置提示 内容
         *
         * @param content
         * @return
         */
        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        /**
         * 设置确认按钮文字  默认为 确认
         *
         * @param positiveStr
         * @return
         */
        public Builder setPositiveStr(String positiveStr) {
            this.positiveStr = positiveStr;
            return this;
        }

        /**
         * 设置取消按钮的文字 默认为 取消
         *
         * @param negativeStr
         * @return
         */
        public Builder setNegativeStr(String negativeStr) {
            this.negativeStr = negativeStr;
            return this;
        }

        /**
         * Positive 按钮文字颜色
         * @param positiveStrColor
         * @return
         */
        public Builder setPositiveStrColor(int positiveStrColor) {
            this.positiveStrColor = positiveStrColor;
            return this;
        }

        /**
         * Negative 按钮文字颜色
         * @param negativeStrColor
         * @return
         */
        public Builder setNegativeStrColor(int negativeStrColor) {
            this.negativeStrColor = negativeStrColor;
            return this;
        }

        /**
         * 设置是否可取消
         *
         * @param cancelable
         * @return
         */
        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * 设置无提示标题
         *
         * @param noTitle
         * @return
         */
        public Builder setNoTitle(boolean noTitle) {
            this.noTitle = noTitle;
            return this;
        }

        /**
         * 设置无取消按钮
         *
         * @param noNegativeBtn
         * @return
         */
        public Builder setNoNegativeBtn(boolean noNegativeBtn) {
            this.noNegativeBtn = noNegativeBtn;
            return this;
        }

        public Builder setContentGravity(int contentGravity) {
            this.contentGravity = contentGravity;
            return this;
        }
    }

}
