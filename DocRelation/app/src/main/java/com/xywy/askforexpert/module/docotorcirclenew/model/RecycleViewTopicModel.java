package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.model.topics.TopicDetailData;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;
import com.xywy.askforexpert.module.docotorcirclenew.utils.ConvertUtil;
import com.xywy.retrofit.net.RetrofitCache;


/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/9 16:34
 */

public class RecycleViewTopicModel extends RecycleViewRealNameModel {
//    private String topicId;

    public RecycleViewTopicModel(IViewRender view, Activity context, String topicId) {
        super(view,context);
        setItemClickListener(new TopicItemClickListener(context, this));
        putExtra("topicId", topicId);
//        this.topicId = topicId;
    }

    protected void request(final boolean loadmore) {
        String uuid = YMApplication.getUUid();
        RetrofitServiceProvider.getInstance().getTopicMsg((String) getExtra("topicId"), uuid, computePage(loadmore))
                .subscribe(new CommonResponse<TopicDetailData>(YMApplication.getInstance()) {
                    @Override
                    public void onNext(TopicDetailData data) {
                        if (computePage(loadmore) == 1) {
                            YmRxBus.notifyTopicHeadMessage(data.getList().get(0));
                            putExtra("head", data.getList().get(0));
                        }
                        handleSuccess(ConvertUtil.convert(data.getDynamiclist()), loadmore, RetrofitCache.isResponseFromCache(data));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        handleError();
                    }
                });
    }

}
