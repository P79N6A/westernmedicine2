package com.xywy.askforexpert.module.main.media.model;

import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.model.media.MediaNumberBean;
import com.xywy.askforexpert.model.media.TodayRecommendBean;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;
import com.xywy.askforexpert.module.docotorcirclenew.model.BaseRecycleViewModel;
import com.xywy.retrofit.net.RetrofitCache;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/9 16:34
 */

public class TodayRecommendMediaModel extends BaseRecycleViewModel<MediaNumberBean> {
    public TodayRecommendMediaModel(IViewRender view) {
        super(view);
    }


    protected void request(final boolean loadmore) {
        RetrofitServiceProvider.getInstance().getToadayRecommendMedia(YMUserService.getCurUserId())
                .subscribe(new CommonResponse<TodayRecommendBean>(){
                    @Override
                    public void onNext(TodayRecommendBean todayRecommendBean) {
                        handleSuccess(todayRecommendBean.getData(), loadmore, RetrofitCache.isResponseFromCache(data));
                    }
                });
    }

}

