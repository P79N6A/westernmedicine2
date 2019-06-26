package com.xywy.component.no_test;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.google.gson.Gson;
import com.xywy.component.BuildConfig;
import com.xywy.component.datarequest.network.RequestManager;
import com.xywy.component.datarequest.neworkWrapper.BaseData;
import com.xywy.component.datarequest.neworkWrapper.IDataResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/6/30 9:29
 * 使用同步阻塞方式测试服务器接口,
 * DataRequest.ErrorListener对于Timeout和Noconnection等网络异常已经封装过，并返回
 * BaseData res = new BaseData();
 * res.setCode(-1);
 * res.setMsg("network error");
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)
public abstract class BaseServerInterfaceTest {
    protected static Context context;
    protected CountDownLatch signal;
    protected volatile String errorMsg = null;
    //期望的服务器返回code
    protected int expectedCode = 10000;
    //等待服务器返回时间秒
    protected int waitTime = 30;
    protected IDataResponse callback = new IDataResponse() {
        @Override
        public void onResponse(BaseData result) {
            if (!checkResult(result)) {
                errorMsg = result.getMsg();
            }
            System.out.println("BaseData--------" + new Gson().toJson(result));
            signal.countDown();
        }
    };

    private boolean checkResult(BaseData res) {
        if (res != null) {
            if (res.getCode() == expectedCode) {
                return true;
            }
        }
        return false;
    }

    @Before
    public void setUp() throws Exception {
        //        输出日志
        ShadowLog.stream = System.out;
        context = RuntimeEnvironment.application;
        RequestManager.initTest(context);
        signal = new CountDownLatch(1);
    }

    @After
    public void tearDown() throws Exception {
        if (!signal.await(waitTime, TimeUnit.SECONDS)) {
            Assert.fail("time out");
        } else {
            if (!TextUtils.isEmpty(errorMsg)) {
                Assert.fail(errorMsg);
            }
        }
    }

    protected RequestQueue initVolley() throws NoSuchFieldException, IllegalAccessException {
        HttpStack stack = new HurlStack();
//        HttpStack stack = new HttpClientStack(new DefaultHttpClient());

        Network network = new BasicNetwork(stack);

        ResponseDelivery responseDelivery = new ExecutorDelivery(Executors.newSingleThreadExecutor());

        RequestQueue queue = new RequestQueue(new NoCache(), network, 4, responseDelivery);

        queue.start();

        Field f = RequestManager.class.getDeclaredField("mRequestQueue");
        f.setAccessible(true);
        f.set(null, queue);
        return queue;
    }
}