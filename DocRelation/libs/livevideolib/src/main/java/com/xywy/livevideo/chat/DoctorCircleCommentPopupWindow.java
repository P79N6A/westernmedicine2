package com.xywy.livevideo.chat;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.xywy.livevideo.chat.interfaces.SendMsgWindowListener;
import com.xywy.livevideolib.R;
import com.xywy.util.DensityUtil;
import com.xywy.util.KeyBoardUtils;
import com.xywy.util.L;
import com.xywy.util.T;

/**
 * 医圈 评论 回复弹框
 * Created by bailiangjin on 2016/11/8.
 */

public class DoctorCircleCommentPopupWindow extends BasePopupWindow {

    private Activity activity;
    private EditText et_content;


    public DoctorCircleCommentPopupWindow(final Activity activity, String hint, boolean cancelable, final SendMsgWindowListener sendMsgWindowListener) {
        super(activity);
        this.activity = activity;

        View view = View.inflate(activity, R.layout.popu_edit, null);
        setContentView(view);
        setSoftInputMode(INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOutsideTouchable(cancelable);
        setFocusable(true);
        setWidth(WindowManager.LayoutParams.FILL_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                KeyBoardUtils.closeKeyboard(et_content);
            }
        });


        et_content = (EditText) view.findViewById(R.id.et_sendmmot);
        et_content.setHint(TextUtils.isEmpty(hint) ? "评论" : "回复 " + hint);
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
            }
        });

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = et_content.getText().toString().trim();
                if (content.length() > mCount) {
                    T.showShort(getContext(),"最多输入"+mCount+"个字");
                    content = content.substring(0, mCount);
                    et_content.setText(content);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // 发送评论
        Button btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    T.showShort(getContext(),"内容不能为空");
                    return;
                }
                et_content.setText("");
                dismiss();
                sendMsgWindowListener.onSend(content);
            }
        });
    }

    public void setHint(String hint) {
        et_content.setHint(TextUtils.isEmpty(hint) ? "评论" : hint);
    }
    private int mCount=200;
    public void setMaxWord(int count) {
       this.mCount=count;
    }

    @Override
    public void dismiss() {
        KeyBoardUtils.closeKeyboard(et_content);
        super.dismiss();
    }

    public void show() {
        L.d("MODEL" + Build.MODEL + ", " + Build.BOARD + ", " + Build.MANUFACTURER);
        if ("R7Plus".equals(Build.MODEL)) {
            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, DensityUtil.dip2px(36));
        } else {
            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //弹出键盘
        KeyBoardUtils.openKeybord(et_content);
        et_content.requestFocus();
    }

}
