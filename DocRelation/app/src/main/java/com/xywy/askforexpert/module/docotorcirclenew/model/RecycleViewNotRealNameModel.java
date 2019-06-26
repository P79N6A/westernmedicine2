package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.appcommon.utils.SPUtils;
import com.xywy.askforexpert.model.doctor.DoctorCirclrNotNameBean;
import com.xywy.askforexpert.model.doctor.NotRealNameItem;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.rxevent.DCDeleteMsgEventBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;
import com.xywy.retrofit.net.RetrofitCache;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;

import java.util.Iterator;


/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/9 16:34
 */

public class RecycleViewNotRealNameModel extends BaseRecycleViewModel<NotRealNameItem> {
    public RecycleViewNotRealNameModel(IViewRender view, Activity context) {
        super(view);
        setItemClickListener(new AnonymousItemClickListener(context,this));
    }
    @Override
    public void registerEvent(Object tag) {
        YmRxBus.registerRefreshAnonyCircleMessage(new EventSubscriber<Object>() {
            @Override
            public void onNext(Event messagesEvent) {
                onRefresh();
            }
        }, tag);
        YmRxBus.registerDeleteDoctorCircleMsgListener(new EventSubscriber<DCDeleteMsgEventBean>() {
            @Override
            public void onNext(Event<DCDeleteMsgEventBean> messagesEvent) {
                if (!messagesEvent.getData().getType().equals(DCMsgType.NO_NAME))
                    return;
                Iterator<NotRealNameItem> it=data.iterator();
                while (it.hasNext()){
                    NotRealNameItem item=it.next();
                    if (messagesEvent.getData().getMsgId().equals(item.id)){
                        deleteItem(item);
                    }
                }
            }
        }, tag);
    }
    protected void request(final boolean loadmore) {
        String uuid = YMApplication.getUUid();
        String oldid = SPUtils.getUser().getStringDef("oldid" + uuid, "0");
        String noldid;
        if (loadmore) {
            noldid= SPUtils.getUser().getStringDef("noldid" + uuid, "0");
        }else {
            noldid=null;
        }
        String type = "2";//匿名
        RetrofitServiceProvider.getInstance().getAnonymoMsg(computePage(loadmore) +"", YMApplication.getUUid(), oldid, noldid, type)
                .subscribe(new CommonResponse<DoctorCirclrNotNameBean>(YMApplication.getInstance()) {
                    @Override
                    public void onNext(DoctorCirclrNotNameBean data) {
                        if (data.list!=null&&data.list.size()>0) {
                            SPUtils.getUser().putString("noldid" + YMApplication.getUUid(), data.list.get(0).id);
                        }
                        if (!RetrofitCache.isResponseFromCache(data)&&data.message != null) {
                            YmRxBus.notifyMessageCountChanged(data.message);
                        }
                        handleSuccess(data.list,loadmore,RetrofitCache.isResponseFromCache(data));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        handleError();
                    }
                });
    }

}
