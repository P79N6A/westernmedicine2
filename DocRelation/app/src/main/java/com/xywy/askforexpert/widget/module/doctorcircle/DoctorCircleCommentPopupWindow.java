package com.xywy.askforexpert.widget.module.doctorcircle;

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

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.KeyBoardUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.view.BasePopupWindow;
import com.xywy.askforexpert.widget.PasteEditText;
import com.xywy.askforexpert.widget.module.doctorcircle.listener.SendMsgWindowListener;

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

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                KeyBoardUtils.closeKeyboard(et_content);
            }
        });


        et_content = (PasteEditText) view.findViewById(R.id.et_sendmmot);
        et_content.setHint(TextUtils.isEmpty(hint) ? "评论" : "回复 " + hint);
        setOnDismissListener(new OnDismissListener() {
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
                if (content.length() > 200) {
                    ToastUtils.shortToast("最多输入200个字");
                    content = content.substring(0, 200);
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
                    ToastUtils.shortToast("内容不能为空");
                    return;
                }
                dismiss();
                sendMsgWindowListener.onSend(content);
            }
        });
    }

    @Override
    public void dismiss() {
        KeyBoardUtils.closeKeyboard(et_content);
        super.dismiss();
    }

    public void show() {
        LogUtils.d("MODEL" + android.os.Build.MODEL + ", " + Build.BOARD + ", " + Build.MANUFACTURER);
        if ("R7Plus".equals(Build.MODEL)) {
            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, DensityUtils.dp2px(36));
        } else {
            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //弹出键盘
        KeyBoardUtils.openKeybord(et_content);
        et_content.requestFocus();
    }

}
