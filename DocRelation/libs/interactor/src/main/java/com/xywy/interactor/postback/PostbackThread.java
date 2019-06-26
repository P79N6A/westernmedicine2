package com.xywy.interactor.postback;

public interface PostbackThread {

    /**
     * @param runnable The runnable to run.
     */
    void post(final Runnable runnable);
}
