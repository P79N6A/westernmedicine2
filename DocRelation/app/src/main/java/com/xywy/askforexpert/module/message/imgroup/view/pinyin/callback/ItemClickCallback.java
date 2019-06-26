package com.xywy.askforexpert.module.message.imgroup.view.pinyin.callback;

public interface ItemClickCallback<T> {
    public void onItemClick(T data);

    public void onItemDelete(T data);
}
