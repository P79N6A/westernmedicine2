package com.xywy.askforexpert.appcommon.base;

import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.base.view.ProgressDialog;
import com.xywy.uilibrary.fragment.XywySuperBaseFragment;

import butterknife.ButterKnife;

/**
 * 医脉 BaseFragment
 * Created by bailiangjin on 2016/10/24.
 */

public abstract class YMBaseFragment extends XywySuperBaseFragment {
    private ProgressDialog progressDialog;
    @Override
    protected void beforeViewBind() {

    }

    //// TODO: 2016/12/22 改为全局的
    protected  void initListener(){

    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
        titleBarBuilder
                .setBackGround(R.drawable.toolbar_bg_no_alpha_new)
                .setBackIcon("返回",R.drawable.fanhui)
//                .setBackIcon("返回",R.drawable.back_btn_selector)
                .build();
    }

    @Override
    public void unBindView() {
        //ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume(getStatisticalPageName());
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgressDialog();
        StatisticalTools.fragmentOnPause(getStatisticalPageName());
    }

    public abstract String getStatisticalPageName() ;

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
            progressDialog = new ProgressDialog(getActivity(), content);
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


}
