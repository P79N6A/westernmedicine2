package com.xywy.askforexpert.module.discovery.service;

import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiConstants;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiService;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.discover.DiscoverNoticeNumberBean;
import com.xywy.datarequestlibrary.RxRequestHelper;
import com.xywy.datarequestlibrary.service.XywyApiServiceProvider;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by bailiangjin on 2016/12/29.
 */

public class DiscoverService {

    public static void getDiscoverNoticeService(final Subscriber<DiscoverNoticeNumberBean> subscriber){
//        RetrofitServiceProvider.getInstance().getDiscoverNoticeNumber(YMUserService.getCurUserId(), new CommonResponse<DiscoverNoticeNumberBean>() {
//            @Override
//            public void onNext(DiscoverNoticeNumberBean discoverNoticeNumberBean) {
//                if (null!=discoverNoticeNumberBean){
//                    subscriber.onNext(discoverNoticeNumberBean);
//                }else {
//                    ToastUtils.shortToast("服务端服务提示数接口返回数据异常");
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                LogUtils.e("请求服务端接口数据异常:"+e.getMessage());
//                //do nothing
//            }
//        });

        String userId= YMUserService.getCurUserId();

        Map<String, String> bundle = new HashMap<>();
        bundle.put("command", "quesMenu");
        bundle.put("userid", userId);
        bundle.put("getCount", "1");
        RetrofitServiceProvider.addSign(bundle, userId);

        Observable<DiscoverNoticeNumberBean> observable= XywyApiServiceProvider.getApiService(ApiService.class).getDiscoverServiceNoticeNumber2(CommonUrl.BASE_HOST+ File.separator+ ApiConstants.CLUB_URL,bundle);

        RxRequestHelper.requestNotDealCommonError(observable, new CommonResponse<DiscoverNoticeNumberBean>() {
            @Override
            public void onNext(DiscoverNoticeNumberBean discoverNoticeNumberBean) {
                if (null!=discoverNoticeNumberBean){
                    subscriber.onNext(discoverNoticeNumberBean);
                }else {
                    ToastUtils.shortToast("服务端服务提示数接口返回数据异常");
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("请求服务端接口数据异常:"+e.getMessage());
                //do nothing
            }
        });
    }
}
