package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.model.doctor.DoctorCircleRealNameBean;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;
import com.xywy.retrofit.net.RetrofitCache;


/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/9 16:34
 */

public class RecycleViewMyRealNameModel extends RecycleViewRealNameModel {


    private String visitedUserId;//默认选择自己的动态列表

    public RecycleViewMyRealNameModel(IViewRender view, Activity context, String visitedUserId) {
        super(view,context);
        this.visitedUserId = visitedUserId;
    }

    protected void request(final boolean loadmore) {
        String loginUserID = YMApplication.getUUid();
        String type = PublishType.Realname;//实名
        RetrofitServiceProvider.getInstance().getMydynamicMsg(loginUserID, computePage(loadmore) + "", visitedUserId,
                type)
                .subscribe(new CommonResponse<DoctorCircleRealNameBean>(YMApplication.getInstance()) {
                    @Override
                    public void onNext(DoctorCircleRealNameBean data) {
                        handleSuccess(data.list,loadmore,RetrofitCache.isResponseFromCache(data));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        handleError();
                    }
                });
    }

}
