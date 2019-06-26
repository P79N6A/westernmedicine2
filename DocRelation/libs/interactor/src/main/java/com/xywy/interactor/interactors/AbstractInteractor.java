package com.xywy.interactor.interactors;

import com.xywy.interactor.executor.Executor;
import com.xywy.interactor.postback.Callback;
import com.xywy.interactor.postback.PostbackThread;
import com.xywy.interactor.utils.InteractorUtilGlobal;

public abstract class AbstractInteractor implements Interactor {

    public final static int USE_FAKE_DATA_NO = 1;
    public final static int USE_FAKE_DATA_YES = 2;

    protected Executor   mThreadExecutor;
    protected PostbackThread mThread;
    protected Callback mCallback;
    protected Object mCallbackData;

    protected volatile boolean mIsCanceled;
    protected volatile boolean mIsRunning;

    private int mIsUseFakeData = 0;

    public AbstractInteractor(Executor threadExecutor, PostbackThread thread, Callback callback) {
        mThreadExecutor = threadExecutor;
        mThread = thread;
        mCallback = callback;
    }

    public void doCallback(Object obj) {
        mCallbackData = obj;
        if(mThread == null) {
            mCallback.handleCallback(mCallbackData);
        } else {
            mThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.handleCallback(mCallbackData);
                }
            });
        }
    }

    /**
     * User can control if to use fake data on this api
     */
    public void setIsUseFakeData(int is) {
        mIsUseFakeData = is;
    }

    /**
     * InteractorImpl can override this function to control
     * if to use fake data on itself.
     */
    public boolean isUseFakeData() {
        if(InteractorUtilGlobal.getIsUseFakeDataByDefault()) {
            switch (mIsUseFakeData) {
                case USE_FAKE_DATA_NO:
                    return false;
                case USE_FAKE_DATA_YES:
                    return true;
                default:
                    return InteractorUtilGlobal.getIsUseFakeDataByDefault();
            }
        } else {
            return false;
        }
    }

    public abstract void syncData();
    public abstract void syncFakeData();

    public void cancel() {
        mIsCanceled = true;
        mIsRunning = false;
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    public void run() {
        if(isUseFakeData()) {
            syncFakeData();
        } else {
            syncData();
        }
        onFinished();
    };

    public void execute() {
        synchronized (this) {
            if (mThreadExecutor == null) {
                run();
            } else {
                this.mIsRunning = true;
                mThreadExecutor.execute(this);
            }
        }
    }

    protected void onFinished() {
        mIsRunning = false;
        mIsCanceled = false;
    }
}
