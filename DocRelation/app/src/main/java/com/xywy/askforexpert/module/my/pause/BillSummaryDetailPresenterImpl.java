package com.xywy.askforexpert.module.my.pause;

import com.xywy.askforexpert.model.MyPurse.MyPurseBean;
import com.xywy.askforexpert.module.my.pause.request.PurseRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

/**
 * Created by xugan on 2018/6/21.
 */

public class BillSummaryDetailPresenterImpl implements IBillSummaryDetailPresenter {
    private IBillSummaryDetailView iBillSummaryDetailView;
    public BillSummaryDetailPresenterImpl(IBillSummaryDetailView iBillSummaryDetailView){
        this.iBillSummaryDetailView = iBillSummaryDetailView;
    }

    @Override
    public void setProgressBar(int visibility) {
    }

    @Override
    public void getBillSummaryDetail(String userid, int month) {
        PurseRequest.getInstance().getBillSummaryDetail(userid, month).subscribe(new BaseRetrofitResponse<BaseData<MyPurseBean>>(){
            @Override
            public void onStart() {
                iBillSummaryDetailView.showProgressBar();
            }

            @Override
            public void onNext(BaseData<MyPurseBean> purseInfoBaseData) {
                iBillSummaryDetailView.onSuccessResultView(purseInfoBaseData,"BillSummaryDetailPresenterImpl");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                iBillSummaryDetailView.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                iBillSummaryDetailView.hideProgressBar();
                iBillSummaryDetailView.onErrorResultView(null,"",e);
                super.onError(e);
            }
        });
    }
}
