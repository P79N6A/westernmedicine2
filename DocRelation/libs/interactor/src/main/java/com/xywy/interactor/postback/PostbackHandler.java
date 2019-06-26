package com.xywy.interactor.postback;

import android.os.Handler;
import android.os.Looper;

import com.xywy.interactor.executor.ThreadExecutor;

public class PostbackHandler implements PostbackThread {

    private Looper mLooper;

    public PostbackHandler(Looper looper) {
        mLooper = looper;
    }

    @Override
    public void post(final Runnable runnable){
        if(mLooper == null) {
            ThreadExecutor.getInstance().execute(runnable);
        } else {
            new Handler(mLooper).post(runnable);
        }
    }
}