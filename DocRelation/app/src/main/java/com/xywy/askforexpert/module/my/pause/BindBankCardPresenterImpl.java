package com.xywy.askforexpert.module.my.pause;

import android.text.TextUtils;

import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.module.discovery.medicine.util.RegUtils;
import com.xywy.askforexpert.module.my.pause.request.PurseRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

/**
 * Created by xugan on 2018/5/25.
 */

public class BindBankCardPresenterImpl implements IBindBankCardPresenter {
    private IBindBankCardView iBindBankCardView;

    public BindBankCardPresenterImpl(IBindBankCardView iBindBankCardView){
        this.iBindBankCardView = iBindBankCardView;
    }

    @Override
    public void bindBankCard(String card_id,String card_name,String card_dress,String card_num,String userId) {
        if(!checkCardNum(card_num)){
            return;
        }

        if(!checkCardAddress(card_dress)){
            return;
        }

        if(!checkCardName(card_name)){
            return;
        }

        if(!checkCardId(card_id)){
            return;
        }

        PurseRequest.getInstance().bindBankCard(card_id,card_name,card_dress,card_num,userId)
                .subscribe(new BaseRetrofitResponse<BaseData>(){
                    @Override
                    public void onStart() {
                        iBindBankCardView.showProgressBar();
                    }

                    @Override
                    public void onNext(BaseData baseData) {
                        iBindBankCardView.showToast(baseData.getMsg());
                        iBindBankCardView.onSuccessResultView(null,"");
                    }

                    @Override
                    public void onCompleted() {
                        iBindBankCardView.hideProgressBar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBindBankCardView.hideProgressBar();
                        super.onError(e);
                    }
                });
    }

    private boolean checkCardId(String card_id) {
        boolean result = true;
        if(TextUtils.isEmpty(card_id)){
            iBindBankCardView.showDialogue(Constants.CRAD_IDENTIFY_MSG_STR);
            result = false;
        }else {
            if(!RegUtils.isCard(card_id)){
                iBindBankCardView.showToast("请填写有效的身份证号码");
                result = false;
            }
        }
        return result;
    }

    private boolean checkCardNum(String card_num){
        boolean result = true;
        if(TextUtils.isEmpty(card_num)){
            iBindBankCardView.showDialogue(Constants.CRAD_NUM_MSG_STR);
            result = false;
        }else {
            if(card_num.length()!=19){
                iBindBankCardView.showToast(Constants.CRAD_NUM_MSG_STR_19);
                result = false;
            }else {
                if(!RegUtils.checkBankCard(card_num)){
                    iBindBankCardView.showToast(Constants.CRAD_NUM_MSG_STR);
                    result = false;
                }
            }
        }
        return result;
    }

    private boolean checkCardAddress(String card_dress){
        boolean result = true;
        if(TextUtils.isEmpty(card_dress)){
            iBindBankCardView.showDialogue(Constants.CRAD_ADDRESS_MSG_STR);
            result = false;
        }
        return result;
    }

    private boolean checkCardName(String card_name){
        boolean result = true;
        if(TextUtils.isEmpty(card_name)){
            iBindBankCardView.showDialogue(Constants.CRAD_NAME_MSG_STR);
            result = false;
        }
        return result;
    }

    @Override
    public void setProgressBar(int visibility) {

    }
}
