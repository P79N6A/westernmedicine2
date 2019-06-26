package com.xywy.component.no_test;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.xywy.component.BuildConfig;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/7/7 13:50
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)
public abstract class BaseAndroidTest {
    protected Context context;
    protected Activity activity;
    protected CountDownLatch signal;
    protected volatile String errorMsg = null;
    protected PrintStream out = System.out;
    protected final static int waitTime = BuildConfig.DEBUG ? 24 * 60 * 60 : 30;

    @Before
    public void setUp() throws Exception {
        //        输出日志
        //ShadowLog.stream = System.out;
        context = RuntimeEnvironment.application;
        signal = new CountDownLatch(1);
    }

    @After
    public void tearDown() throws Exception {
        if (signal == null || signal.await(waitTime, TimeUnit.SECONDS)) {
            Assert.fail("time out");
        } else {
            if (!TextUtils.isEmpty(errorMsg)) {
                Assert.fail(errorMsg);
            }
        }
    }

    /**
     * 异步任务执行回调
     *
     * @param afterFinish 异步
     * @throws InterruptedException
     */
    protected void afterTaskFinished(Runnable afterFinish) throws InterruptedException {
        if (signal != null) {
            if (!signal.await(30, TimeUnit.SECONDS)) {
                Assert.fail("time out");
            } else {
                if (afterFinish != null)
                    afterFinish.run();
            }
        } else {
            if (!TextUtils.isEmpty(errorMsg)) {
                Assert.fail(errorMsg);
            }
        }
    }


}
