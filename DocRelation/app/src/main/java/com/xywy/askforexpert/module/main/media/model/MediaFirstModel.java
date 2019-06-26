package com.xywy.askforexpert.module.main.media.model;

import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.model.media.MediaFirstItemBean;
import com.xywy.askforexpert.model.media.MediaTypeBean;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;
import com.xywy.askforexpert.module.docotorcirclenew.model.BaseRecycleViewModel;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.net.RetrofitCache;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/9 16:34
 */

public class MediaFirstModel extends BaseRecycleViewModel<MediaTypeBean> {
    public MediaFirstModel(IViewRender view) {
        super(view);
    }

    protected void request(final boolean loadmore) {
        RetrofitServiceProvider.getInstance().getMediaOrders(YMUserService.getCurUserId())
                .subscribe(new CommonResponse<BaseData<MediaFirstItemBean>>(){
                    @Override
                    public void onNext(BaseData<MediaFirstItemBean> mediaFirstItemBaseData) {
                        YmRxBus.notifyMediaRecommendData(mediaFirstItemBaseData.getData().getRecommend());
                        handleSuccess(mediaFirstItemBaseData.getData().getSubject(),loadmore,RetrofitCache.isResponseFromCache(data));
                    }
                });
    }

}

