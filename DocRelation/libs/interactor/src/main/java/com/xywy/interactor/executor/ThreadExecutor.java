package com.xywy.interactor.executor;

import com.xywy.interactor.interactors.AbstractInteractor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadExecutor implements Executor {
    private static volatile ThreadExecutor sThreadExecutor;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<Runnable>(128);

    private ThreadPoolExecutor mThreadPoolExecutor;
    private static final Object mLock = new Object();

    private ThreadExecutor() {
        long keepAlive = KEEP_ALIVE_TIME;
        mThreadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                keepAlive,
                TIME_UNIT,
                WORK_QUEUE);
    }

    @Override
    public void execute(final AbstractInteractor interactor) {
        mThreadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                interactor.run();
            }
        });
    }

    @Override
    public void execute(final Runnable run) {
        mThreadPoolExecutor.submit(run);
    }

    public static Executor getInstance() {
        synchronized (mLock) {
            if (sThreadExecutor == null) {
                sThreadExecutor = new ThreadExecutor();
            }
        }
        return sThreadExecutor;
    }
}
