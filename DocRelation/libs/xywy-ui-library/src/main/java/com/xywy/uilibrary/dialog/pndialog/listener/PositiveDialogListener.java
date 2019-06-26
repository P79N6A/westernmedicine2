package com.xywy.uilibrary.dialog.pndialog.listener;

/**
 * 只需对 确认事件处理的 使用该 Listener 确认取消 都不需要 回调 Listener 传null
 * Created by bailiangjin on 16/8/26.
 */
public abstract class PositiveDialogListener implements PNDialogListener {
    @Override
    public void onNegative() {

    }
}
