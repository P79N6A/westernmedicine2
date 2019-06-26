package com.xywy.askforexpert.module.message.notice;

import com.xywy.askforexpert.model.notice.Notice;
import com.xywy.askforexpert.module.message.notice.request.GetNoticeListRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

import java.util.List;

/**
 * Created by xugan on 2018/5/28.
 */

public class GetNoticeListImpl implements IGetNoticeListPresenter {
    private IGetNoticeListView iGetNoticeListView;
    public GetNoticeListImpl(IGetNoticeListView iGetNoticeListView){
        this.iGetNoticeListView = iGetNoticeListView;
    }
    @Override
    public void setProgressBar(int visibility) {
    }

    @Override
    public void getNoticeList(String userId) {
        GetNoticeListRequest.getInstance().getNoticeList(userId).subscribe(new BaseRetrofitResponse<BaseData<List<Notice>>>(){
            @Override
            public void onStart() {
                super.onStart();
                iGetNoticeListView.showProgressBar();
            }

            @Override
            public void onNext(BaseData<List<Notice>> listBaseData) {
                super.onNext(listBaseData);
                iGetNoticeListView.onSuccessResultView(listBaseData,"");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                iGetNoticeListView.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                iGetNoticeListView.hideProgressBar();
                iGetNoticeListView.onErrorResultView(null,"",e);
                super.onError(e);
            }
        });
    }
}
