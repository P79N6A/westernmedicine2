package com.xywy.askforexpert.module.liveshow;

import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.rxretrofitoktools.service.WWSXYWYService;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.liveshow.AlipayResultBean;
import com.xywy.askforexpert.model.liveshow.HealthCoinOrderBean;

import rx.Subscriber;

/**
 *
 * 直播请求服务端服务汇总
 * Created by bailiangjin on 2017/2/24.
 */
public class LiveShowService {

    public static void generateOrder(String userId, String goodsId, final Subscriber<HealthCoinOrderBean> subscriber){
        String type="2";
        WWSXYWYService.generateOrder(userId,goodsId,null,type, new CommonResponse<HealthCoinOrderBean>() {
            @Override
            public void onNext(HealthCoinOrderBean healthCoinOrderBean) {

                if (null!=healthCoinOrderBean){
                    LogUtils.d(GsonUtils.toJson(healthCoinOrderBean));
                    subscriber.onNext(healthCoinOrderBean);
                }else {
                    String logString="请求生成订单接口，服务端返回数据为空";
                    LogUtils.d(logString);
                    ToastUtils.shortToast(logString);
                }
            }
        });
    }

    /**
     * 生成任意金额订单
     * @param userId 用户id
     * @param money 金额钱数
     * @param subscriber
     */
    public static void generateAnyAmountOrder(String userId, String money, final Subscriber<HealthCoinOrderBean> subscriber){
        String type="1";
        WWSXYWYService.generateOrder(userId,null,money,type, new CommonResponse<HealthCoinOrderBean>() {
            @Override
            public void onNext(HealthCoinOrderBean healthCoinOrderBean) {

                if (null!=healthCoinOrderBean){
                    LogUtils.d(GsonUtils.toJson(healthCoinOrderBean));
                    subscriber.onNext(healthCoinOrderBean);
                }else {
                    String logString="请求生成订单接口，服务端返回数据为空";
                    LogUtils.d(logString);
                    ToastUtils.shortToast(logString);
                }
            }
        });
    }

    public static void payByAliPay(String userId,String orderId,final Subscriber<AlipayResultBean> subscriber){
//        userId = "";
//        orderId = "";
        WWSXYWYService.payByAliPay(userId,orderId, new CommonResponse<AlipayResultBean>() {
            @Override
            public void onNext(AlipayResultBean alipayResultBean) {
                if (null!=alipayResultBean){
                    LogUtils.d(GsonUtils.toJson(alipayResultBean));
                    subscriber.onNext(alipayResultBean);
                }else {
                    String logString="请求支付宝;付接口，服务端返回数据为空";
                    LogUtils.d(logString);
                    ToastUtils.shortToast(logString);

                }

            }
        });
    }

}
