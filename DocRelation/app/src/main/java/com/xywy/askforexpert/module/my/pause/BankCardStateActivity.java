package com.xywy.askforexpert.module.my.pause;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.bankCard.BankCard;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.dialog.bottompopupdialog.BottomBtnPopupWindow;
import com.xywy.uilibrary.dialog.bottompopupdialog.listener.BtnClickListener;
import com.xywy.uilibrary.titlebar.ItemClickListener;

/**
 * Created by xugan on 2018/5/25.
 */

public class BankCardStateActivity extends YMBaseActivity implements IBankCardStateView{

    private RelativeLayout rl_root;
    private ImageView iv;
    private TextView tv_state,tv,tv_apply;
    private TextView tv_1,tv_2,tv_3,tv_end_num;
    private RelativeLayout rl_bank_card;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bank_card_state;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("银行卡");
        rl_root = (RelativeLayout)findViewById(R.id.rl_root);
        rl_root.setVisibility(View.GONE);
        iv = (ImageView)findViewById(R.id.iv);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv = (TextView)findViewById(R.id.tv);
        tv_1 = (TextView)findViewById(R.id.tv_1);
        tv_2 = (TextView)findViewById(R.id.tv_2);
        tv_3 = (TextView)findViewById(R.id.tv_3);
        tv_end_num = (TextView)findViewById(R.id.tv_end_num);
        ViewGroup.MarginLayoutParams param_1= (ViewGroup.MarginLayoutParams) tv_1.getLayoutParams();
        ViewGroup.MarginLayoutParams param_2= (ViewGroup.MarginLayoutParams) tv_2.getLayoutParams();
        ViewGroup.MarginLayoutParams param_3= (ViewGroup.MarginLayoutParams) tv_3.getLayoutParams();
        tv_1.setText("****");
        tv_2.setText("****");
        tv_3.setText("****");
        param_1.topMargin+=8;
        param_2.topMargin+=8;
        param_3.topMargin+=8;
        tv_apply = (TextView) findViewById(R.id.tv_apply);
        rl_bank_card = (RelativeLayout) findViewById(R.id.rl_bank_card);
        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatisticalTools.eventCount(BankCardStateActivity.this,"Toresubmit");
                startActivity(new Intent(BankCardStateActivity.this,BankCardActivity.class));
                finish();
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatisticalTools.eventCount(BankCardStateActivity.this,"Contactcustomerservice");
                phone();
            }
        });
    }

    private void phone() {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008591200"));//跳转到拨号界面，同时传递电话号码
        startActivity(dialIntent);
    }

    @Override
    protected void initData() {
        IBankCardStatePresenter iBankCardStatePresenter = new BankCardStatePresenterImpl(this);
        iBankCardStatePresenter.getBankCardState(YMUserService.getCurUserId());
    }


    @Override
    public void showProgressBar() {
        showProgressDialog("");
    }

    @Override
    public void hideProgressBar() {
        hideProgressDialog();
    }

    @Override
    public void showToast(String str) {
        ToastUtils.shortToast(str);
    }

    @Override
    public void onSuccessResultView(Object o, String flag) {
        rl_root.setVisibility(View.VISIBLE);
        BaseData baseData = (BaseData) o;
        if(Constants.SUCCESS_CN.equals(baseData.getMsg())){
            iv.setVisibility(View.GONE);
            tv_state.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            tv_apply.setVisibility(View.GONE);
            rl_bank_card.setVisibility(View.VISIBLE);
            BankCard bankCard = (BankCard) baseData.getData();
            if(null != bankCard && !TextUtils.isEmpty(bankCard.cnum)){
                int length = bankCard.cnum.length();
                tv_end_num.setText(bankCard.cnum.substring(length-4,length));
                titleBarBuilder.addItem("", R.drawable.service_topque_right_btn, new ItemClickListener() {
                    @Override
                    public void onClick() {
                        showBottomPopuWindow();
                    }
                }).build();
            }
        }else if(Constants.CHECK_REFUSE.equals(baseData.getMsg())){
            iv.setImageResource(R.drawable.refuse);
            tv_state.setText("审核未通过");
            tv.setText(Constants.CUSTOMER_SERVICE);
            tv.setBackground(getResources().getDrawable(R.drawable.activity_card_state_item_normal_bg));
            tv_apply.setText("重新提交");
            tv_apply.setBackground(getResources().getDrawable(R.drawable.activity_card_state_item_normal_bg));
            tv_apply.setVisibility(View.VISIBLE);
            rl_bank_card.setVisibility(View.GONE);
        }else if(Constants.UNDER_REVIEW.equals(baseData.getMsg())){
            iv.setImageResource(R.drawable.commit_success);
            tv_state.setText("提交成功");
            tv.setText("银行卡审核中");
            tv.setBackground(null);
            tv_apply.setVisibility(View.GONE);
            rl_bank_card.setVisibility(View.GONE);
        }else if(Constants.COMMIT_NEVER.equals(baseData.getMsg())){
            startActivity(new Intent(BankCardStateActivity.this, BankCardActivity.class));
            finish();
        }
    }

    @Override
    public void onErrorResultView(Object o, String flag, Throwable e) {
    }

    private void showBottomPopuWindow() {
        new BottomBtnPopupWindow.Builder()
                //添加组Item 会自动分组 组第一个为上部圆角 组最后一个为底部圆角 中部item圆角
                .addGroupItem("联系客服解绑银行卡", new BtnClickListener() {
                    @Override
                    public void onItemClick() {
                        StatisticalTools.eventCount(BankCardStateActivity.this,"UnbundlingbankCARDS");
                        phone();
                    }
                }).build(BankCardStateActivity.this).show();
    }
}
