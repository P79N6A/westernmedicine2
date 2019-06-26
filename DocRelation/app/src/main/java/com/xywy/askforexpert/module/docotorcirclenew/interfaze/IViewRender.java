package com.xywy.askforexpert.module.docotorcirclenew.interfaze;

import android.os.Message;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/29 14:14
 */
public interface IViewRender {
     int Refreshing =1;
    int StopRefreshing =2;
    int DataChanged =3;
    int isLoadingData=4;
    int stopLoadingData=5;
    int HasMore=6;
    int NoMore=7;
    int isFirstLoading=8;
    int notFirstLoading=9;
    /**
     * 更新状态
     * @param msg
     */
    void handleModelMsg(Message msg);
}
