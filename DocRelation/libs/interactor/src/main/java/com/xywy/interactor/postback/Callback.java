package com.xywy.interactor.postback;

public interface Callback<T> {
    public void handleCallback(T result);
}