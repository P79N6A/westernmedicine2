package com.xywy.askforexpert.module.message.notice;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.model.notice.Notice;
import com.xywy.askforexpert.module.discovery.medicine.module.web.WebActivity;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugan on 2018/5/28.
 * 公告消息列表
 */

public class NoticeListActivity extends YMBaseActivity implements IGetNoticeListView{
    private NoticeListDataAdapter mAdapter;
    private EmptyWrapper mEmptyWrapper;
    private RecyclerView recyclerView;
    private List<Notice> data = new ArrayList();
    private DividerItemDecoration  itemDecoration;
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * 是否在刷新
     */
    private boolean mIsRefreshing = false;
    private IGetNoticeListPresenter iGetNoticeListPresenter;
    private String nid;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_notice_list;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("网站公告");
        iGetNoticeListPresenter = new GetNoticeListImpl(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setClipChildren(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(NoticeListActivity.this));
        if (itemDecoration==null){
            itemDecoration = new DividerItemDecoration(NoticeListActivity.this, LinearLayoutManager.VERTICAL);
            itemDecoration.setDividerDrawble(getResources().getDrawable(R.drawable.item_divider_common));
        }
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mIsRefreshing;
            }
        });
        mAdapter = new NoticeListDataAdapter(this);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Notice notice = data.get(position);
                String userid = YMUserService.getCurUserId();
                nid = notice.id;
                String url = CommonUrl.BASE_HOST + "/app/other/index.interface.php?a=invitation&m=notice&userid="+userid+"&nid="+ nid;
                WebActivity.startActivity(NoticeListActivity.this, url,"公告详情",notice.title,notice.content);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                iGetNoticeListPresenter.getNoticeList(YMUserService.getCurUserId());
            }
        });

        YmRxBus.registerWebViewLoadingStateSuccess(new EventSubscriber<Object>() {
            @Override
            public void onNext(Event<Object> ObjectEvent) {
                String url = (String) ObjectEvent.getData();
                if(url.contains("&nid="+nid)){
                    Notice notice = null;
                    for (int i = 0; i < data.size(); i++) {
                        notice = data.get(i);
                        if(nid.equals(notice.id)){
                            notice.hs_read = 1;
                            break;
                        }
                    }
                    mAdapter.setData(data);
                    mEmptyWrapper.notifyDataSetChanged();
                }
            }
        }, this);
    }

    @Override
    protected void initData() {
        this.data = YMApplication.getInstance().getNoticeList();
        mAdapter.setData(this.data);
        mAdapter.setData(this.data);
        mEmptyWrapper = new EmptyWrapper(mAdapter);
        recyclerView.setAdapter(mEmptyWrapper);
    }

    @Override
    public void onSuccessResultView(Object o, String flag) {
        mIsRefreshing = false;
        swipeRefreshLayout.setRefreshing(mIsRefreshing);
        BaseData<List<Notice>> baseData = (BaseData<List<Notice>>)o;
        List<Notice> noticeList = baseData.getData();
        this.data.clear();
        for (int i = 0; i < noticeList.size(); i++) {
            this.data.add(noticeList.get(i));
        }
        mAdapter.setData(this.data);
        mEmptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void onErrorResultView(Object o, String flag, Throwable e) {
        mIsRefreshing = false;
        swipeRefreshLayout.setRefreshing(mIsRefreshing);
    }

    @Override
    public void showProgressBar() {
        showProgressDialog("");
    }

    @Override
    public void hideProgressBar() {
        hideProgressDialog();
    }

    @Override
    public void showToast(String str) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        iGetNoticeListPresenter.getNoticeList(YMUserService.getCurUserId());
    }
}
