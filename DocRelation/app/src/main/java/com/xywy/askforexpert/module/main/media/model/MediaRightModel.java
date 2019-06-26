package com.xywy.askforexpert.module.main.media.model;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.model.media.MediaNumberBean;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;
import com.xywy.askforexpert.module.docotorcirclenew.model.BaseRecycleViewModel;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/9 16:34
 * 多个科室的数据缓存起来
 */

public class MediaRightModel extends BaseRecycleViewModel<MediaNumberBean> {
    private static final int pageSize=10;

    public MediaRightModel(IViewRender view) {
        super(view);
        YmRxBus.registerMediaRecommendData(new EventSubscriber<List<MediaNumberBean>>() {
            @Override
            public void onNext(Event<List<MediaNumberBean>> listEvent) {
                cacheData(listEvent.getData(), false, "1");
            }
        }, YMApplication.getInstance().getTopActivity());
    }

    List<MediaNumberBean> getCacheDataById(String id){
        String key = "id" + id;
        Object extra = getExtra(key);
        if(extra==null){
            putExtra(key,new ArrayList<MediaNumberBean>());
        }
        return (List<MediaNumberBean>) getExtra(key);
    }

    protected void request(final boolean loadmore) {
        //请求时科室id
        final String id = getId();
        //有缓存则不请求刷新
        if (!loadmore&& getCacheDataById(id).size()>0){
            //返回缓存数据
            handleSuccess(getCacheDataById(getId()), loadmore, true);
            return;
        }
        RetrofitServiceProvider.getInstance().getMediaByDepartId(YMUserService.getCurUserId(), id,computePage(loadmore))
                .subscribe(new CommonResponse<BaseData<List<MediaNumberBean>>>(){
                    @Override
                    public void onNext(BaseData<List<MediaNumberBean>> listBaseData) {
                        List<MediaNumberBean> mediaNumberBeanList = listBaseData.getData();
                        //按原有请求id更新数据
                            //1、重置data
                            resetItem(getCacheDataById(id));
                            //2、按正常逻辑处理数据
                            updateState(mediaNumberBeanList);
                            updateData(mediaNumberBeanList, loadmore, false);
                            //3、更新缓存中的数据
                            getCacheDataById(id).clear();
                            getCacheDataById(id).addAll(data);


                        String newId = getId();
                        handleSuccess(getCacheDataById(newId), false, true);
                    }
                });
    }
    public int computePage(boolean loadmore) {
        if (loadmore){
            //根据当前已有数据计算下一页页数
            int size = getCacheDataById(getId()).size();
            page = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
            page++;
        }else {
            page = 1;
        }
        return page;
    }

    private String getId() {
        return  getExtra("id")==null?"1":(String)getExtra("id");
    }
    //注意此时缓存的是请求时科室ID，服务器返回时id可能已发生变化
    private void cacheData(List<MediaNumberBean> recommend, boolean loadmore, String oldId) {
        //如果服务器返回数据时，当前请求科室发生变更,丢弃数据
        String newId = getId();
        if (oldId.equals(newId)){
            resetItem(getCacheDataById(oldId));
            handleSuccess(recommend,loadmore, false);
            getCacheDataById(oldId).clear();
            getCacheDataById(oldId).addAll(data);
        }
    }
}

