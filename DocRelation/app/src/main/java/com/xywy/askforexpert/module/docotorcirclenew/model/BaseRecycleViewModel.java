package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.os.Message;
import android.view.View;

import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/9 16:34
 */

public abstract class BaseRecycleViewModel<T> implements IRecycleViewModel<T> {
    protected IViewRender view;
    protected List<T> data = new ArrayList();
    protected int page = 1;
    private View.OnClickListener ItemClickListener;
    private State state = new State();
    private Map extra = new HashMap();

    //    protected Object headData;
    public BaseRecycleViewModel(IViewRender view) {
        this.view = view;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public Object getExtra(Object key) {
        return extra.get(key);
    }

    @Override
    public void putExtra(Object key, Object value) {
        extra.put(key, value);
    }

    @Override
    public void registerEvent(Object tag) {
    }

    public int computePage(boolean loadmore) {
        if (loadmore) {
            page++;
        } else {
            page = 1;
        }
        return page;
    }

    public void onRefresh() {
        if (state.isLoadingData()){
            return;
        }
        state.setRefreshing(true);
        state.setLoadingData(true);
        request(false);
    }


    public void loadMore() {
        if (state.isLoadingData()){
            return;
        }
        state.setLoadingData(true);
        request(true);
    }

    protected abstract void request(boolean loadmore);

    /**
     * 成功获取网络数据后回调
     *
     * @param list
     * @param loadmore
     * @param isCache
     */
    protected void handleSuccess(List<T> list, boolean loadmore, boolean isCache) {
        updateState(list);
        updateData(list, loadmore, isCache);
        notifyDataChanged();
    }

    protected void updateData(List<T> list, boolean loadmore, boolean isCache) {
        //修改数据，通知页面更新
        if (isCache) {
            //只有刷新允许使用缓存
            if (!loadmore) {
                resetItem(list);

            }
        } else {
            if (loadmore) {
                addItem(list);
            } else {
                resetItem(list);
            }
        }
    }

    protected void updateState(List<T> list) {
        //先更新状态
        if (list == null || list.size() == 0) {
            state.setHasMore(false);
        } else {
            state.setHasMore(true);
        }
        state.setFirstLoading(false);
        state.setLoadingData(false);
        state.setRefreshing(false);
    }

    private boolean isFirst() {
        return data.size() == 0;
    }

    protected void handleError() {
        page--;
        state.setLoadingData(false);
        notifyDataChanged();
    }

    @Override
    public View.OnClickListener getItemClickListener() {
        return ItemClickListener;
    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        ItemClickListener = itemClickListener;
    }

    public void resetItem(List contentItem) {
        data.clear();
        addItem(contentItem);
    }

    public void deleteItem(T contentItem) {
        data.remove(contentItem);
    }

    public void addItem(List<T> contentItem) {
        if (contentItem != null) {
            data.addAll(contentItem);
        }
    }

    public void notifyDataChanged() {
        sendMsg(IViewRender.DataChanged);
    }

    private void sendMsg(int what) {
        Message message = Message.obtain();
        message.what = what;
        view.handleModelMsg(message);
    }

    public  class State {
        protected boolean hasMore = true;//是否还有更多数据
        //当前是否正在请求数据
        private boolean isRefreshing = false;
        //当前是否正在请求数据
        private boolean isLoadingData = false;
        private boolean isFirstLoading = true;

        public boolean isRefreshing() {
            return isRefreshing;
        }

        public void setRefreshing(boolean refreshing) {
            isRefreshing = refreshing;
            if(refreshing){
                sendMsg(IViewRender.Refreshing);
            }else {
                sendMsg(IViewRender.StopRefreshing);
            }
        }

        public boolean isLoadingData() {
            return isLoadingData;
        }

        public void setLoadingData(boolean loadingData) {
            this.isLoadingData = loadingData;
            if(loadingData){
                sendMsg(IViewRender.isLoadingData);
            }else {
                sendMsg(IViewRender.stopLoadingData);
            }
        }

        public boolean hasMore() {
            return hasMore;
        }

        /**
         * 显示正在加载或者没有更多
         *
         * @param hasMore
         */
        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
            LogUtils.e("显示加载更多：" + hasMore);
            if(hasMore){
                sendMsg(IViewRender.HasMore);
            }else {
                sendMsg(IViewRender.NoMore);
            }

        }

        public boolean isFirstLoading() {
            return isFirstLoading;
        }

        public void setFirstLoading(boolean firstLoading) {
            isFirstLoading = firstLoading;
            if(firstLoading){
                sendMsg(IViewRender.isFirstLoading);
            }else {
                sendMsg(IViewRender.notFirstLoading);
            }
        }
    }
}
