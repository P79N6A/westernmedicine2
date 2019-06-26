package com.xywy.askforexpert.module.main.news;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdfdemo.AsyncTask;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseFragment;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMLinearRVLoadMoreWrapper;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.main.CommonNewsItemBean;
import com.xywy.askforexpert.model.main.NewsListPageBean;
import com.xywy.askforexpert.model.news.MediaRecommendItem;
import com.xywy.askforexpert.module.main.home.HomePageCacheUtils;
import com.xywy.askforexpert.module.main.home.HomeService;
import com.xywy.askforexpert.module.main.news.newpart.NewsRvBaseAdapter;
import com.xywy.askforexpert.module.main.news.newpart.bean.MainTabListItemBean;
import com.xywy.askforexpert.module.main.news.newpart.bean.MediaEnterItemBean;
import com.xywy.askforexpert.module.main.news.newpart.bean.MediaRecommendItemBean;
import com.xywy.askforexpert.module.main.news.newpart.bean.NewsItemBean;
import com.xywy.askforexpert.module.main.service.media.InfoDetailActivity;
import com.xywy.askforexpert.module.main.subscribe.video.VideoNewsActivity;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * 资讯列表
 *
 * @author Jack Fang
 *         <p>
 *         2017-01-11 blj 重构重写
 */
public class NewsListFragment extends YMBaseFragment {


    private static final String BUNDLE_ID_KEY = "BUNDLE_ID_KEY";

    private static final int DEFAULT_PAGE = 1;

    private static final int PAGE_SIZE = 20;

    private int pageNumber = DEFAULT_PAGE;

    @Bind(R.id.news_listView)
    RecyclerView recyclerView;
    private NewsRvBaseAdapter newsRvAdapter;
    private YMLinearRVLoadMoreWrapper loadMoreWrapper;
    private int id;

    private boolean isLoadingMore = false;
    private TextView emptyView;
    private boolean isLoading;

    private boolean isHasMoreData = true;
    private DividerItemDecoration  itemDecoration;

    public static NewsListFragment newInstance(int id) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID_KEY, id);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_another_news_rv_list;
    }

    @Override
    protected void initView() {

        id = this.getArguments().getInt(BUNDLE_ID_KEY);
        // 设置无数据时显示
        emptyView = new TextView(getActivity());
        // 屏蔽无数据点击
        emptyView.setOnClickListener(null);
        emptyView.setBackgroundColor(Color.WHITE);
        emptyView.setText("暂无数据");
        emptyView.setTextColor(Color.parseColor("#999999"));
        emptyView.setTextSize(16f);
        emptyView.setGravity(Gravity.CENTER_HORIZONTAL);
        emptyView.setPadding(0, DensityUtils.dp2px(16), 0, 0);
        emptyView.setVisibility(View.GONE);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        if (itemDecoration==null){
            itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
            itemDecoration.setDividerDrawble(getResources().getDrawable(R.drawable.item_divider_common));
        }
        recyclerView.addItemDecoration(itemDecoration);

        newsRvAdapter = new NewsRvBaseAdapter(getActivity());
        newsRvAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                MainTabListItemBean item = newsRvAdapter.getItem(position);

                switch (item.getType()) {
                    case MainTabListItemBean.TYPE_NEWS:
                    case MainTabListItemBean.TYPE_NEWS_MULTI_PICTURE:
                        onNewsItemClick(item);
                        break;
                    case MainTabListItemBean.TYPE_MEDIA_ENTER:
                    case MainTabListItemBean.TYPE_MEDIA_RECOMMEND:
                        //do nothing
                        break;
                    default:
                        break;
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        loadMoreWrapper = new YMLinearRVLoadMoreWrapper(newsRvAdapter, recyclerView);


        loadMoreWrapper.setOnLoadMoreListener(new YMLinearRVLoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isHasMoreData) {
                    pageNumber++;
                    LogUtils.d("加载更多" + id);
                    //shortToast("加载更多" + id);
                    initOrRefreshData();
                } else {
                    //shortToast("没有更多数据");
                    LogUtils.d("没有更多数据");
                    loadMoreWrapper.showNoMoreData();
                }
            }
        });

        recyclerView.setAdapter(loadMoreWrapper);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        if (NetworkUtil.isNetWorkConnected()) {
            initOrRefreshData();
        } else {
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
            if (emptyView != null && emptyView.getVisibility() == View.GONE) {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setId(int id) {
        this.id = id;
    }


    /**
     * 列表条目点击事件
     *
     * @param item
     */
    private void onNewsItemClick(MainTabListItemBean item) {
        if (!NetworkUtil.isNetWorkConnected()) {
            Toast.makeText(getActivity(), "请检查您的网络连接", Toast.LENGTH_SHORT).show();
            return;
        }


        CommonNewsItemBean data = item.getNewsItemBean().getCommonNewsItemBean();
        Intent intent;
        new SetReadTask(data).execute(String.valueOf(data.getId()));
        LogUtils.d("news_url = " + data.getUrl());
        if ("4".equals(data.getModel())) {
            // 原生视频资讯
            intent = new Intent(getActivity(), VideoNewsActivity.class);
            intent.putExtra(VideoNewsActivity.NEWS_ID_INTENT_KEY, String.valueOf(data.getId()));
            intent.putExtra(VideoNewsActivity.VIDEO_UUID_INTENT_KEY, data.getVideo().getUu());
            intent.putExtra(VideoNewsActivity.VIDEO_VUID_INTENT_KEY, data.getVideo().getVu());
            intent.putExtra(VideoNewsActivity.VIDEO_TITLE_INTENT_KEY, data.getTitle());
        } else {
            intent = new Intent(getActivity(), InfoDetailActivity.class);
            if ("0".equals(data.getModel())) {
                // 普通资讯
                StatisticalTools.eventCount(getActivity(), "SingleMapInformation");
            } else if ("1".equals(data.getModel())) {
                // 图片资讯
                StatisticalTools.eventCount(getActivity(), "ManyMapsInformation");
            }
            // TODO 站外链接
            intent.putExtra("url", data.getUrl());
            intent.putExtra("ids", String.valueOf(data.getId()));
            intent.putExtra("title", data.getTitle());
            intent.putExtra("imageurl", data.getImage());
        }
        startActivity(intent);
    }

    private void initOrRefreshData() {

        if (DEFAULT_PAGE == pageNumber) {

            new AsyncTask<Void, Void, NewsListPageBean>() {

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();
                }

                @Override
                protected NewsListPageBean doInBackground(Void... params) {
                    //首页获取缓存数据
                    NewsListPageBean newsListPageBean = HomePageCacheUtils.getNewsListBean("" + id);
                    return newsListPageBean;
                }

                @Override
                protected void onPostExecute(NewsListPageBean newsListPageBean) {
                    super.onPostExecute(newsListPageBean);

                    if (null != newsListPageBean) {
                        initOrUpdateRVNew(newsListPageBean);
                    } else {
                        loadMoreWrapper.showNoMoreData();
                    }
                }
            }.execute();

        }

        if (!NetworkUtil.isNetWorkConnected()) {
            if (pageNumber > DEFAULT_PAGE) {
                shortToast("您未连接网络 无法获取更多数据");
            }
        } else {
            HomeService.getMewsListPageBeanList("" + id, pageNumber, PAGE_SIZE, new CommonResponse<NewsListPageBean>() {
                @Override
                public void onNext(NewsListPageBean newsListPageBean) {
                    initOrUpdateRVNew(newsListPageBean);
                }
            });
        }
    }


    private void initOrUpdateRVNew(NewsListPageBean newsListPageBean) {

        if (null == newsListPageBean) {
            newsRvAdapter.setData(new ArrayList<MainTabListItemBean>());
            newsRvAdapter.notifyDataSetChanged();
            return;
        }
        List<CommonNewsItemBean> commonNewsItemBeanList = newsListPageBean.getList();

        if (null != commonNewsItemBeanList && !commonNewsItemBeanList.isEmpty()) {

            List<MainTabListItemBean> tabListItemBeanList = new ArrayList<>();
            if (6 == id && pageNumber == DEFAULT_PAGE) {
                tabListItemBeanList.add(new MainTabListItemBean(new MediaEnterItemBean()));
            }

            for (CommonNewsItemBean item : commonNewsItemBeanList) {
                if ("6".equals(item.getModel())) {

                    List<MediaRecommendItem> mediaRecommendItemList = item.getRecommend();
                    if (null != mediaRecommendItemList && !mediaRecommendItemList.isEmpty()) {
                        LogUtils.d("推荐条目：" + mediaRecommendItemList.size());
                        //媒体号推荐条目
                        tabListItemBeanList.add(new MainTabListItemBean(new MediaRecommendItemBean(mediaRecommendItemList)));
                    }else {
                        LogUtils.e("推荐条目列表为空");
                    }
                } else {
                    //普通条目
                    NewsItemBean newsItemBean = new NewsItemBean(item);
                    MainTabListItemBean mainTabListItemBean = new MainTabListItemBean(newsItemBean);
                    tabListItemBeanList.add(mainTabListItemBean);
                }

            }

            if (DEFAULT_PAGE == pageNumber) {
                newsRvAdapter.setData(tabListItemBeanList);
            } else {
                newsRvAdapter.addData(tabListItemBeanList);
            }

        } else {
            if (DEFAULT_PAGE == pageNumber) {
                newsRvAdapter.setData(new ArrayList<MainTabListItemBean>());
            } else {
                //shortToast("没有更多数据");
                LogUtils.e("没有更多数据");

                isHasMoreData = false;
                pageNumber = DEFAULT_PAGE == pageNumber ? DEFAULT_PAGE : pageNumber - 1;
            }
        }

        loadMoreWrapper.notifyDataSetChanged();

    }


    public void startRefresh() {
        if (!isLoading) {
            pageNumber = DEFAULT_PAGE;
            initOrRefreshData();
        }
    }

    @Override
    public String getStatisticalPageName() {
        return null;
    }

    /**
     * 标记已读
     */
    private class SetReadTask extends AsyncTask<String, Void, Boolean> {
        private CommonNewsItemBean data;

        public SetReadTask(CommonNewsItemBean data) {
            this.data = data;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return YMApplication.getNewsReadDataSource().readNews(String.valueOf(data.getId()));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            data.setRead(true);
            if (aBoolean) {
                loadMoreWrapper.notifyDataSetChanged();
            }
        }
    }
}
