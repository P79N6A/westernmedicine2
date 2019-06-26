package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.appcommon.utils.SPUtils;
import com.xywy.askforexpert.model.doctor.DoctorCircleRealNameBean;
import com.xywy.askforexpert.model.doctor.RealNameItem;
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

public class RecycleViewRealNameModel extends BaseRecycleViewModel<RealNameItem> {
    public RecycleViewRealNameModel(IViewRender view, Activity context) {
        super(view);
        setItemClickListener(new RealNameItemClickListener(context,this));
    }
    @Override
    public void registerEvent(Object tag) {
         YmRxBus.registerRefreshRealCircleMessage(new EventSubscriber<Object>() {
            @Override
            public void onNext(Event messagesEvent) {
                onRefresh();
            }
        }, tag);
        YmRxBus.registerDeleteDoctorCircleMsgListener(new EventSubscriber<DCDeleteMsgEventBean>() {
            @Override
            public void onNext(Event<DCDeleteMsgEventBean> messagesEvent) {
                if (!messagesEvent.getData().getType().equals(DCMsgType.REAL_NAME))
                    return;
                Iterator<RealNameItem> it=data.iterator();
                while (it.hasNext()){
                    RealNameItem item=it.next();
                    if (messagesEvent.getData().getMsgId().equals(item.id)){
                        deleteItem(item);
                    }
                }
            }
        }, tag);
    }
    protected void request(final boolean loadmore){
        String uuid = YMApplication.getUUid();
        String oldid = SPUtils.getUser().getStringDef("oldid" + uuid, "0");
        String noldid;
        if (loadmore) {
            noldid= SPUtils.getUser().getStringDef("noldid" + uuid, "0");
        }else {
            noldid=null;
        }
        String type = PublishType.Realname;//实名
        RetrofitServiceProvider.getInstance().getRealMsg(computePage(loadmore) +"", YMApplication.getUUid(), oldid, noldid, type)
                .subscribe(new CommonResponse<DoctorCircleRealNameBean>(YMApplication.getInstance()) {
                    @Override
                    public void onNext(DoctorCircleRealNameBean data) {
                        if (data.list!=null&&data.list.size()>0){
                            SPUtils.getUser().putString("noldid" + YMApplication.getUUid(), data.list.get(0).id);
                        }
                        if (!RetrofitCache.isResponseFromCache(data)) {
                            if (data.message != null){
                                YmRxBus.notifyMessageCountChanged(data.message);
                            }
                        }
                        handleSuccess(data.list, loadmore,RetrofitCache.isResponseFromCache(data));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        handleError();
                    }
                });
    }

}
