package com.xywy.askforexpert.module.liveshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.liveshow.AlipayResultBean;
import com.xywy.askforexpert.model.liveshow.HealthCoinOrderBean;

import java.util.Map;

/**
 * Created by bailiangjin on 2017/2/28.
 */

public class PayActivity extends YMBaseActivity {

    public static final String USER_ID_INTENT_KEY = "userId";
    public static final String GOODS_ID_INTENT_KEY = "goodsId";
    public static final String MONEY_COUNT_KEY = "money";
    public static final int SDK_PAY_FLAG = 10088;

    private String goodsId;
    private String userId;
    private int money;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    String result = (String) msg.obj;
                    LogUtils.e("支付结果：" + result);
                    //shortToast("支付成功" + result);
                    break;
                default:
                    break;
            }
        }
    };

    public static void start(Activity activity, String userId, String goodsId, int money) {
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra(USER_ID_INTENT_KEY, userId);
        intent.putExtra(GOODS_ID_INTENT_KEY, goodsId);
        intent.putExtra(MONEY_COUNT_KEY, money);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void beforeViewBind(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString(USER_ID_INTENT_KEY);
        goodsId = bundle.getString(GOODS_ID_INTENT_KEY);
        money = bundle.getInt(MONEY_COUNT_KEY);
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("生成订单并支付")
                .hideBackIcon()//隐藏返回按钮
                .build();
    }

    @Override
    protected void initData() {
        showProgressDialog("正在生成订单...");
        userId = YMUserService.getCurUserId();
        //goodsId="1001";
        //money = "0.01";
        LiveShowService.generateAnyAmountOrder(userId, ""+money, new CommonResponse<HealthCoinOrderBean>() {
            @Override
            public void onNext(HealthCoinOrderBean healthCoinOrderBean) {
                String notice = "生成订单返回结果：" + GsonUtils.toJson(healthCoinOrderBean);
                LogUtils.d(notice);
                //shortToast(notice);

                LiveShowService.payByAliPay("" + healthCoinOrderBean.getData().getUid(), healthCoinOrderBean.getData().getOrder_bn(), new CommonResponse<AlipayResultBean>() {
                    @Override
                    public void onNext(AlipayResultBean alipayResultBean) {
                        hideProgressDialog();
                        String notice = "获取支付宝支付接口返回结果：" + GsonUtils.toJson(alipayResultBean);
                        LogUtils.d(notice);
                        //shortToast(notice);
                        //跳转支付宝支付

                        final String orderInfo = alipayResultBean.getData().getStr();   // 订单信息

                        LogUtils.e("orderInfo:"+orderInfo);
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(PayActivity.this);
                                Map result = alipay.payV2(orderInfo, true);

                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result.toString();
                                mHandler.sendMessage(msg);
                            }
                        };
                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }
                });
            }
        });
    }

}
