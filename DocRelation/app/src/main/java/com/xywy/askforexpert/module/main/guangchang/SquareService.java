package com.xywy.askforexpert.module.main.guangchang;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.main.promotion.PromotionPageBean;

import rx.Subscriber;

/**
 * Created by bailiangjin on 2016/10/25.
 */

public class SquareService {

    /**
     * 获取晋级信息
     * @param subscriber
     */
    public static void getPromotionInfo(final Subscriber<PromotionPageBean> subscriber){
        RetrofitServiceProvider.getInstance().getPromotionInfo(YMUserService.getCurUserId(), new CommonResponse<PromotionPageBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(PromotionPageBean promotionPageBean) {
                LogUtils.d("jsonData:" + GsonUtils.toJson(promotionPageBean));
                if (null == promotionPageBean) {
                    return;
                }
                subscriber.onNext(promotionPageBean);
            }
        });
    }
}
