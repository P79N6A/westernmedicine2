package com.xywy.askforexpert.module.my.pause;

import com.xywy.askforexpert.model.bankCard.BankCard;
import com.xywy.askforexpert.module.my.pause.request.PurseRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

/**
 * Created by xugan on 2018/5/25.
 * 银行卡审核状态业务逻辑
 */

public class BankCardStatePresenterImpl implements IBankCardStatePresenter {

    private IBankCardStateView iBankCardStateView;

    public BankCardStatePresenterImpl(IBankCardStateView iBankCardStateView){
        this.iBankCardStateView = iBankCardStateView;
    }

    @Override
    public void setProgressBar(int visibility) {

    }

    @Override
    public void getBankCardState(String userId) {
        PurseRequest.getInstance().getBankCardState(userId).
                subscribe(new BaseRetrofitResponse<BaseData<BankCard>>(){
                    @Override
                    public void onStart() {
                        iBankCardStateView.showProgressBar();
                    }

                    @Override
                    public void onNext(BaseData<BankCard> bankCardBaseData) {
                        iBankCardStateView.onSuccessResultView(bankCardBaseData,"");
                    }

                    @Override
                    public void onCompleted() {
                        iBankCardStateView.hideProgressBar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iBankCardStateView.hideProgressBar();
                        iBankCardStateView.onErrorResultView(null,"",e);
                        super.onError(e);
                    }
                });
    }
}
