package com.xywy.askforexpert.module.my.pause;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.uilibrary.titlebar.ItemClickListener;

/**
 * Created by xugan on 2018/5/21.
 */

public class BankCardActivity extends YMBaseActivity implements IBindBankCardView{
    private IBindBankCardPresenter iBindBankCardPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bank_card;
    }

    @Override
    protected void initView() {
        iBindBankCardPresenter = new BindBankCardPresenterImpl(this);
        titleBarBuilder.setTitleText("添加银行卡");
        TextView tv_card_number_str = (TextView) findViewById(R.id.tv_card_number_str);
        TextView tv_instruction_one = (TextView) findViewById(R.id.tv_instruction_one);
        TextView tv_instruction_two = (TextView) findViewById(R.id.tv_instruction_two);
        SpannableString spannableString = new SpannableString(Constants.CRAD_NUMBER_STR);
        spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 1, spannableString.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为透明色
        tv_card_number_str.append(spannableString);
        spannableString = new SpannableString(Constants.CRAD_INSTRUCTION_ONE_STR);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c999)), 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置为设置为c999
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c_fc5348)), 12, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置为浅红色
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c999)), 21, Constants.CRAD_INSTRUCTION_ONE_STR.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置为设置为c999
        tv_instruction_one.append(spannableString);
        spannableString = new SpannableString(Constants.CRAD_INSTRUCTION_TWO_STR);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c999)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置为设置为c999
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c_fc5348)), 5, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置为浅红色
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c999)), 12, Constants.CRAD_INSTRUCTION_TWO_STR.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置为c999
        tv_instruction_two.append(spannableString);

        final EditText et_card_num = (EditText) findViewById(R.id.et_card_num);
        final EditText et_card_dress = (EditText) findViewById(R.id.et_card_dress);
        final EditText et_card_name = (EditText) findViewById(R.id.et_card_name);
        final EditText et_card_id = (EditText) findViewById(R.id.et_card_id);

        titleBarBuilder.addItem("提交", new ItemClickListener() {
            @Override
            public void onClick() {
                StatisticalTools.eventCount(BankCardActivity.this,"submitBankcard");
                String card_num = et_card_num.getText().toString().trim();
                String card_dress = et_card_dress.getText().toString().trim();
                String card_name = et_card_name.getText().toString().trim();
                String card_id = et_card_id.getText().toString().trim();
                iBindBankCardPresenter.bindBankCard(card_id,card_name,card_dress,card_num,YMUserService.getCurUserId());
            }
        }).build();
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onSuccessResultView(Object o, String flag) {
        startActivity(new Intent(BankCardActivity.this,BankCardStateActivity.class));
        YmRxBus.notifyFinshBandCardInfo();
        finish();
    }

    @Override
    public void onErrorResultView(Object o, String flag, Throwable e) {
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
    public void showDialogue(String msg) {
        DialogUtil.showCustomDialogOnlyOneButton(BankCardActivity.this,R.layout.custom_dialogue,"",msg,"",
                "确定","",new MyCallBack<Object>(){

                    @Override
                    public void onClick(Object data) {
                    }
                });
    }

    @Override
    public void showToast(String str) {
        ToastUtils.shortToast(str);
    }
}
