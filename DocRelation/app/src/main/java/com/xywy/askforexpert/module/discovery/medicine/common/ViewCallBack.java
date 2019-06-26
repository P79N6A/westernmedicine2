package com.xywy.askforexpert.module.discovery.medicine.common;

//stone 回调
public interface ViewCallBack<T> {
    void onClick(int tag, int position, T data,boolean delete);
}
