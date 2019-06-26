package com.xywy.askforexpert.appcommon.net.rxretrofitoktools.service;

import android.text.TextUtils;

import com.xywy.askforexpert.appcommon.YMConfig;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiService;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.rxretrofitoktools.collection.CommonServiceProvider;
import com.xywy.askforexpert.model.liveshow.AlipayResultBean;
import com.xywy.askforexpert.model.liveshow.FollowUNFollowResultBean;
import com.xywy.askforexpert.model.liveshow.HealthCoinOrderBean;
import com.xywy.askforexpert.model.liveshow.LiveShowHostInfo;
import com.xywy.askforexpert.model.liveshow.LiveShowListPageBean;
import com.xywy.askforexpert.model.liveshow.LiveShowStateInfoBean;
import com.xywy.askforexpert.model.liveshow.MyFansPageBean;
import com.xywy.askforexpert.model.liveshow.MyFollowedPageBean;
import com.xywy.datarequestlibrary.RxRequestHelper;
import com.xywy.datarequestlibrary.paramtools.XywyParamUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;


/**
 * WWSXYWY 网络请求服务提供者
 * Created by bailiangjin on 2017/2/15.
 */
public class WWSXYWYService {


    private static ApiService getWWSApiService() {
        return CommonServiceProvider.getWWSXYWYApiService();
    }

    /**
     * 获取用户直播状态信息 1574
     *
     * @param subscriber
     */
    public static void getliveStatusInfo(String user_id, final Subscriber<LiveShowStateInfoBean> subscriber) {

        Map<String, String> getParamMap = new HashMap<>();
        XywyParamUtils.addMustParam(getParamMap, "1574", "1.0");
        getParamMap.put("source", YMConfig.SOURCE);
        getParamMap.put("user_id", user_id);

        String sign = XywyParamUtils.getSign(getParamMap);

        RxRequestHelper.requestNotDealCommonError(getWWSApiService().getliveStatusInfo(user_id,sign), new CommonResponse<LiveShowStateInfoBean>() {
            @Override
            public void onNext(LiveShowStateInfoBean liveShowStateInfoBean) {
                subscriber.onNext(liveShowStateInfoBean);
            }
        });

    }


    /**
     * 获取直播列表 中间层 1568
     * user_id=18732252&page=1&is_list=1
     *
     * @param subscriber
     */
    public static void getLiveShowHostInfo(String user_id, String hostId, int page, final Subscriber<LiveShowHostInfo> subscriber) {

        int is_list = 1;

        Map<String, String> getParamMap = new HashMap<>();
        XywyParamUtils.addMustParam(getParamMap, "1568", "1.0");
        getParamMap.put("id", hostId);
        getParamMap.put("user_id", user_id);
        getParamMap.put("page", "" + page);
        getParamMap.put("is_list", "" + is_list);

        String sign = XywyParamUtils.getSign(getParamMap);

        RxRequestHelper.requestNotDealCommonError(getWWSApiService().getLiveShowHostInfo(user_id, hostId, page, is_list, sign), new CommonResponse<LiveShowHostInfo>() {
            @Override
            public void onNext(LiveShowHostInfo liveShowHostInfo) {
                subscriber.onNext(liveShowHostInfo);
            }
        });

    }

    /**
     * 获取直播列表 中间层 1562
     * cate_id=1&state=1&page=1&page_size=1
     *
     * @param subscriber
     */
    public static void getLiveShowList(String state, String cate_id, int page, int page_size, final Subscriber<LiveShowListPageBean> subscriber) {

        Map<String, String> getParamMap = new HashMap<>();
        XywyParamUtils.addMustParam(getParamMap, "1562", "1.0");
        Map<String, String> postParamMap = new HashMap<>();

        if(!TextUtils.isEmpty(state)){
            postParamMap.put("state", TextUtils.isEmpty(state) ? "" : state);
        }
        if(!TextUtils.isEmpty(cate_id)){
            postParamMap.put("cate_id", TextUtils.isEmpty(cate_id) ? "" : cate_id);
        }

        postParamMap.put("page", "" + page);
        postParamMap.put("page_size", "" + page_size);

        String sign = XywyParamUtils.getSign(getParamMap, postParamMap);
        RxRequestHelper.requestNotDealCommonError(getWWSApiService().getLiveShowList(state, cate_id, page, page_size, sign), new CommonResponse<LiveShowListPageBean>() {
            @Override
            public void onNext(LiveShowListPageBean liveShowListPageBean) {
                subscriber.onNext(liveShowListPageBean);
            }
        });
    }


    /**
     * @param userId     用户id
     * @param touserid   主播id
     * @param type       int	选填	当action=1时，必传；用户类型：1.医生 2.普通用户用户
     * @param subscriber
     */
    public static void follow(String userId, String touserid, int type, final Subscriber<FollowUNFollowResultBean> subscriber) {
        followOrUnfollow(userId, touserid, 1, type, subscriber);
    }

    /**
     * @param userId     用户id
     * @param touserid   主播id
     * @param type       int	选填	当action=1时，必传；用户类型：1.医生 2.普通用户用户
     * @param subscriber
     */
    public static void unfollow(String userId, String touserid, int type, final Subscriber<FollowUNFollowResultBean> subscriber) {
        followOrUnfollow(userId, touserid, 2, type, subscriber);
    }


    /**
     * 关注和取消关注接口 中间层 1551
     * action	1	int	必填	行为：1.关注 2.取消关注
     * user_id	123	int	必填	用户id
     * touserid	123123	int	必填	主播id
     * type	1	int	选填	当action=1时，必传；用户类型：1.医生 2.普通用户用户
     *
     * @param userId
     * @param action
     * @param type
     * @param subscriber
     */
    private static void followOrUnfollow(String userId, String touserid, int action, int type, final Subscriber<FollowUNFollowResultBean> subscriber) {

        Map<String, String> getParamMap = new HashMap<>();
        XywyParamUtils.addMustParam(getParamMap, "1551", "1.0");
        Map<String, String> postParamMap = new HashMap<>();
        postParamMap.put("action", "" + action);
        postParamMap.put("user_id", userId);
        postParamMap.put("touserid", touserid);
        postParamMap.put("type", "" + type);

        String sign = XywyParamUtils.getSign(getParamMap, postParamMap);
        RxRequestHelper.requestNotDealCommonError(getWWSApiService().followOrUnFollow(postParamMap, sign), new CommonResponse<FollowUNFollowResultBean>() {
            @Override
            public void onNext(FollowUNFollowResultBean followUNFollowResultBean) {
                subscriber.onNext(followUNFollowResultBean);
            }
        });
    }

    /**
     * 获取我的关注的直播页面信息 中间层 1552
     *
     * @param subscriber
     */
    public static void getMyFollowedLiveShowPageInfo(String userId, int page, final Subscriber<MyFollowedPageBean> subscriber) {


        Map<String, String> getParamMap = new HashMap<>();
        XywyParamUtils.addMustParam(getParamMap, "1552", "1.0");
        getParamMap.put("user_id", userId);
        getParamMap.put("page", "" + page);

        String sign = XywyParamUtils.getSign(getParamMap);


        RxRequestHelper.requestNotDealCommonError(getWWSApiService().getMyFollowedLiveShowPageInfo(userId, page, sign), new CommonResponse<MyFollowedPageBean>() {
            @Override
            public void onNext(MyFollowedPageBean myFollowedPageBean) {
                subscriber.onNext(myFollowedPageBean);
            }
        });
    }


    /**
     * 获取我的粉丝列表 中间层 1553
     *
     * @param subscriber
     */
    public static void getMyFansPageInfo(String userId, final Subscriber<MyFansPageBean> subscriber) {

        Map<String, String> getParamMap = new HashMap<>();

        XywyParamUtils.addMustParam(getParamMap, "1553", "1.0");

        getParamMap.put("user_id", userId);
        String sign = XywyParamUtils.getSign(getParamMap);

        RxRequestHelper.requestNotDealCommonError(getWWSApiService().getMyFansPageInfo(userId, sign), new CommonResponse<MyFansPageBean>() {
            @Override
            public void onNext(MyFansPageBean myFansPageBean) {
                subscriber.onNext(myFansPageBean);
            }
        });
    }



    /**
     *  生成订单接口 中间层 1516
     * http://test.api.wws.xywy.com/api.php/user/userCreateOrder/index?sign=7f8454acd49a1db96c617980ac1529bf&source=yimai_app&pro=xywyf32l24WmcqquqqTdhXZ4kg&os=android&api=1516&version=1.0
     * postbody是goods_id=1001, type=2, userid=75494116
     * @param userId
     * @param goodsId 固定面值所需，1001=>6元；1002=>60元，1003=》98元；
     * @param money
     * @param type 充值类型 1、随机面值 2、固定面值
     * @param subscriber
     */
    public static void generateOrder(String userId, String goodsId, String money,String type, final Subscriber<HealthCoinOrderBean> subscriber) {


        Map<String, String> getParamMap = new HashMap<>();
        XywyParamUtils.addMustParam(getParamMap, "1516", "1.0");

        Map<String, String> postParamMap = new HashMap<>();
        postParamMap.put("userid", userId);
        postParamMap.put("type", type);
        if("2".equals(type)){
            postParamMap.put("goods_id", goodsId);
        }else {
            postParamMap.put("money", money);
        }
        String sign = XywyParamUtils.getSign(getParamMap, postParamMap);

        RxRequestHelper.requestNotDealCommonError(getWWSApiService().generateOrder(sign, postParamMap), new CommonResponse<HealthCoinOrderBean>() {
            @Override
            public void onNext(HealthCoinOrderBean string) {
                subscriber.onNext(string);
            }
        });
    }


    /**
     * 支付宝支付接口  中间层 1062
     * http://test.api.wws.xywy.com/api.php/pay/AppAliPay/index?version=1.0&source=yimai_app&os=android&api=1062&pro=xywyf32l24WmcqquqqTdhXZ4ng&service_code=xywy_app_tel&user_id=65246946&order=8734&sign=f2397e8c239b0fc9851d1d71f7a6efb6
     *
     * @param subscriber
     */
    public static void payByAliPay(String userId, String orderId, final Subscriber<AlipayResultBean> subscriber) {

        Map<String, String> getParamMap = new HashMap<>();

        XywyParamUtils.addMustParam(getParamMap, "1062", "1.0");
        getParamMap.put("service_code", "yimai_xywy_radio");
        getParamMap.put("user_id", userId);
        getParamMap.put("order", orderId);

        String sign = XywyParamUtils.getSign(getParamMap);

        RxRequestHelper.requestNotDealCommonError(getWWSApiService().payByAliPay(userId, orderId, sign), new CommonResponse<AlipayResultBean>() {
            @Override
            public void onNext(AlipayResultBean string) {
                subscriber.onNext(string);
            }
        });
    }


}
