package com.xywy.uilibrary.dialog.pndialog.listener;

/**
 * 确认 取消都需要回调处理的使用该 Listener  确认取消 都不需要 回调 Listener 传null
 *
 * @author bailiangjin
 */
public interface PNDialogListener {

    public void onPositive();


    public void onNegative();

}
