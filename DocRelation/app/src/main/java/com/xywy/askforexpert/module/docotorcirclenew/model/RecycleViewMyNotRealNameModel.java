package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.appcommon.utils.SPUtils;
import com.xywy.askforexpert.model.doctor.DoctorCirclrNotNameBean;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;
import com.xywy.retrofit.net.RetrofitCache;


/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/9 16:34
 */

public class RecycleViewMyNotRealNameModel extends RecycleViewNotRealNameModel {


    private String touserid;//默认选择自己的动态列表

    public RecycleViewMyNotRealNameModel(IViewRender view, Activity context, String touserid) {
        super(view,context);
        this.touserid = touserid;
    }

    protected void request(final boolean loadmore) {
        String uuid = YMApplication.getUUid();
        String type = PublishType.Anonymous;//匿名
        RetrofitServiceProvider.getInstance().getMyAnonymoDynamicMsg(touserid, computePage(loadmore) + "", uuid,
                type)
                .subscribe(new CommonResponse<DoctorCirclrNotNameBean>(YMApplication.getInstance()) {
                    @Override
                    public void onNext(DoctorCirclrNotNameBean data) {
                        if (data.list!=null&&data.list.size()>0) {
                            SPUtils.getUser().putString("noldid" + YMApplication.getUUid(), data.list.get(0).id);
                        }
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
