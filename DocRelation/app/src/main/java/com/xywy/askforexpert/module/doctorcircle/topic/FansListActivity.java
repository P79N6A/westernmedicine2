package com.xywy.askforexpert.module.doctorcircle.topic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.topics.FansData;
import com.xywy.askforexpert.module.doctorcircle.topic.adapter.FansListAdapter;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 话题粉丝列表
 *
 * @author Jack Fang
 */
public class FansListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TOPICID_INTENT_KEY = "topicId";
    private static final String LOG_TAG = FansListActivity.class.getSimpleName();
    private static final String PARAMS_M = "theme_myfans";
    private static final String PARAMS_A = "theme";
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    @Bind(R.id.title_fans_count)
    TextView titleFansCount;
    @Bind(R.id.fans_list_toolbar)
    Toolbar fansListToolbar;
    @Bind(R.id.fans_list)
    ListView fansList;
    @Bind(R.id.fans_list_refresh)
    SwipeRefreshLayout fansListRefresh;
    @Bind(R.id.fans_list_empty_view)
    TextView fansListEmptyView;
    private String userid;
    private String topicId;

    private View footerView;
    private int currentPage = DEFAULT_PAGE;
    private boolean isLoading = false;

    private List<FansData.DataBean> mDatas = new ArrayList<>();
    private FansListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_list);
        ButterKnife.bind(this);

        CommonUtils.initSystemBar(this);
        CommonUtils.setToolbar(this, fansListToolbar);
        CommonUtils.setRefresh(fansListRefresh);

        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getPID();
        }

        getParams();
        initFooterView();
        registerListeners();

        adapter = new FansListAdapter(FansListActivity.this, mDatas, R.layout.fans_list_item_layout);
        fansList.setAdapter(adapter);

        if (NetworkUtil.isNetWorkConnected()) {
            requestData();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        }
    }


    private void getParams() {
        topicId = getIntent().getStringExtra(TOPICID_INTENT_KEY);
    }

    @SuppressLint("InflateParams")
    private void initFooterView() {
        footerView = getLayoutInflater().inflate(R.layout.loading_more, null);
        if (fansList != null && fansList.getFooterViewsCount() == 0) {
            fansList.addFooterView(footerView, null, false);
        }
        footerView.setVisibility(View.GONE);
    }

    private void registerListeners() {
        if (fansListRefresh != null) {
            fansListRefresh.setOnRefreshListener(this);
        }

        fansList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && view.getLastVisiblePosition() == view.getCount() - 1) {
                    if (!isLoading) {
                        currentPage++;
                        requestData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        fansList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DLog.d(LOG_TAG, "onItemClick");
                Object o = parent.getAdapter().getItem(position);
                FansData.DataBean dataBean = (FansData.DataBean) o;
                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast("网络异常，请检查网络连接");
                } else {
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(FansListActivity.this).context);
                    } else {
                        Intent intent = new Intent(FansListActivity.this, PersonDetailActivity.class);
                        DLog.d(LOG_TAG, "fans userId onItemClick = " + dataBean.getUserid() + ", relation = " + dataBean.getRelation());
                        intent.putExtra("uuid", String.valueOf(dataBean.getUserid()));
                        FansListActivity.this.startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (fansListRefresh != null) {
            fansListRefresh.setRefreshing(true);
            currentPage = DEFAULT_PAGE;
            requestData();
        }
    }

    private void requestData() {
        String sign = MD5Util.MD5(topicId + Constants.MD5_KEY);
        String bind = topicId;
        String url = CommonUrl.FOLLOW_LIST + "m=" + PARAMS_M + "&a=" + PARAMS_A + "&bind=" + bind
                + "&themeid=" + topicId + "&sign=" + sign + "&userid=" + userid + "&page=" + currentPage
                + "&pagesize=" + DEFAULT_PAGE_SIZE;
        DLog.d(LOG_TAG, "fans list url = " + url);
        new FinalHttp().get(url, new AjaxCallBack() {
            @SuppressLint("InflateParams")
            @Override
            public void onStart() {
                super.onStart();

                isLoading = true;

                if (currentPage == DEFAULT_PAGE) {
                    if (fansListRefresh != null && !fansListRefresh.isRefreshing()) {
                        fansListRefresh.setRefreshing(true);
                        fansListRefresh.setProgressViewOffset(false, 0,
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
                    }
                } else {
                    if (fansList != null) {
                        if (footerView == null) {
                            footerView = getLayoutInflater().inflate(R.layout.loading_more, null);
                        }

                        if (fansList.getFooterViewsCount() == 0) {
                            fansList.addFooterView(footerView, null, false);
                        }

                        if (footerView.getVisibility() == View.GONE) {
                            footerView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                DLog.d(LOG_TAG, "fans_list_s = " + s);

                isLoading = false;
                if (fansListRefresh != null) {
                    fansListRefresh.setRefreshing(false);
                }

                if (fansList.getEmptyView() == null) {
                    fansList.setEmptyView(fansListEmptyView);
                }

                FansData fansData = new Gson().fromJson(s, FansData.class);
                List<FansData.DataBean> data = fansData.getData();
                if (currentPage == DEFAULT_PAGE) {
                    titleFansCount.setText(fansData.getTotal());
                }
                if (data != null) {
                    if (data.isEmpty()) {
                        currentPage--;
                        if (footerView != null && fansList.getFooterViewsCount() == 1) {
                            fansList.removeFooterView(footerView);
                        }
                    } else {
                        if (currentPage == DEFAULT_PAGE) {
                            if (!mDatas.isEmpty()) {
                                mDatas.clear();
                            }
                        }
                        mDatas.addAll(data);
                        adapter.notifyDataSetChanged();

                        fansList.post(new Runnable() {
                            @Override
                            public void run() {
                                if (fansList.getHeight() <= AppUtils.getScreenHeight(FansListActivity.this)) {
                                    if (footerView != null && fansList.getFooterViewsCount() == 1) {
                                        fansList.removeFooterView(footerView);
                                    }
                                }
                            }
                        });
                    }
                } else {
                    if (footerView != null && fansList.getFooterViewsCount() == 1) {
                        fansList.removeFooterView(footerView);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                isLoading = false;
                currentPage--;
                if (fansListRefresh != null) {
                    fansListRefresh.setRefreshing(false);
                }
                if (footerView != null && fansList.getFooterViewsCount() == 1) {
                    fansList.removeFooterView(footerView);
                }
                if (fansList.getEmptyView() == null) {
                    fansList.setEmptyView(fansListEmptyView);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
