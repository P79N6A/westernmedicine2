package com.xywy.askforexpert.appcommon.base;

import android.os.Bundle;
import android.os.Message;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.base.view.ProgressDialog;
import com.xywy.uilibrary.activity.XywySuperBaseActivity;

import butterknife.ButterKnife;

/**
 * Created by bailiangjin on 16/09/08.
 */
public abstract class YMBaseActivity extends XywySuperBaseActivity {

    private ProgressDialog progressDialog;

    /**
     * show toast by string
     *
     * @param string
     */
    public void shortToast(String string) {
        ToastUtils.shortToast(string);
    }

    /**
     * show toast by res id
     *
     * @param resId
     */
    public void shortToast(int resId) {
        ToastUtils.shortToast(resId);
    }

    /**
     * long toast
     *
     * @param string
     */
    public void longToast(String string) {
        ToastUtils.longToast(string);
    }

    /**
     * long toast
     *
     * @param resId
     */
    public void longToast(int resId) {
        ToastUtils.shortToast(resId);
    }

    /**
     * 显示正在加载数据进度dialog
     */
    public void showLoadDataDialog() {
        showProgressDialog("正在加载数据...");
    }

    /**
     * 显示进度dialog
     *
     * @param content 提示文字内容
     */
    public void showProgressDialog(String content) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(YMBaseActivity.this, content);
            progressDialog.setCanceledOnTouchOutside(false);

        } else {
            progressDialog.setTitle(content);
        }
        progressDialog.showProgersssDialog();
    }

    /**
     * 隐藏进度dialog
     */
    public void hideProgressDialog() {
        if (null == progressDialog) {
            return;
        }
        if (progressDialog.isShowing()) {
            progressDialog.closeProgersssDialog();
        }
    }

    /**
     * 是否 隐藏绘制的状态来样式
     *
     * @return boolean 返回  false：使用统一绘制的状态来样式 返回 true：使用原有标题栏样式
     */
    @Override
    protected boolean isHideBar() {
        return false;
    }

    /**
     * 统一 handler msg分发 处理回调
     *
     * @param msg
     */
    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
        //stone 断开自动重连 2018年01月11日13:19:59
        YMOtherUtils.autoConnectWebSocketIfNeed();
    }

    @Override
    protected void onPause() {
        hideProgressDialog();
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleBarBuilder
                .setBackGround(R.drawable.toolbar_bg_no_alpha_new)
                .setBackIcon("返回", R.drawable.fanhui)
                .build();
        initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initData();
    }


    @Override
    protected void beforeViewBind(Bundle savedInstanceState) {
        beforeViewBind();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected void beforeViewBind() {
    }


    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void unbindView() {
        ButterKnife.unbind(this);
    }

    @Override
    public void setStatusBar() {
        if (!isHideBar()) {
            //初始化统一bar
            CommonUtils.initSystemBar(this);
        }
    }


}
