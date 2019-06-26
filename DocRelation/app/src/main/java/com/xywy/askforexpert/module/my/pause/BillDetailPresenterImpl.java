package com.xywy.askforexpert.module.my.pause;

import com.xywy.askforexpert.model.MyPurse.BillInfo;
import com.xywy.askforexpert.module.my.pause.request.PurseRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

/**
 * Created by xugan on 2018/6/21.
 */

public class BillDetailPresenterImpl implements IBillDetailPresenter {
    private IBillDetailView iBillDetailView;
    public BillDetailPresenterImpl(IBillDetailView iBillDetailView){
        this.iBillDetailView = iBillDetailView;
    }

    @Override
    public void setProgressBar(int visibility) {
    }

    @Override
    public void getBillDetail(String userid, int month, int mtype, int page, int psize) {
        PurseRequest.getInstance().getBillDetail(userid, month,mtype,page, psize).subscribe(new BaseRetrofitResponse<BaseData<BillInfo>>(){
            @Override
            public void onStart() {
                iBillDetailView.showProgressBar();
            }

            @Override
            public void onNext(BaseData<BillInfo> purseInfoBaseData) {
                iBillDetailView.onSuccessResultView(purseInfoBaseData,"");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                iBillDetailView.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                iBillDetailView.hideProgressBar();
                iBillDetailView.onErrorResultView(null,"",e);
                super.onError(e);
            }
        });
    }
}
