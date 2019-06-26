package com.xywy.askforexpert.module.doctorcircle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.retrofitTools.HttpRequestCallBack;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitServices;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitUtil;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.base.BaseBean;
import com.xywy.askforexpert.model.followList.FollowListData;
import com.xywy.askforexpert.module.doctorcircle.adapter.FollowAdapter;
import com.xywy.askforexpert.module.main.media.MediaSettingActivity;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.usermsg.DiscussSettingsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 关注列表 页
 *
 * @author Jack Fang
 */
public class FollowListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, HttpRequestCallBack<List<FollowListData>> {
    private static final String LOG_TAG = "FollowListActivity";

    private static final int DEFAULT_PAGE = 1;
    private static final String A = "doctor";
    private static final String M = "doctor_watched";
    private static int page = DEFAULT_PAGE;

    private Toolbar toolbar;
    private ListView followList;
    private FollowAdapter adapter;
    private List<FollowListData> mDatas;

    private SwipeRefreshLayout followRefresh;

    /**
     * 标记是否正在加载更多，避免跳页加载
     */
    private boolean isLoadingMoreNow = false;

    /**
     * 无数据显示页面
     */
    private TextView noDataText;

    /**
     * 加载更多布局
     */
    private View footerView;

    private String userid;

    private Map<String, Object> sMap = new HashMap<>();
    private Call<BaseBean<List<FollowListData>>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.activity_follow_list);

        initView();
        CommonUtils.setToolbar(this, toolbar);
        CommonUtils.setRefresh(followRefresh);
        followRefresh.setOnRefreshListener(this);
        getParams();
        registerListener();

        sMap.put("a", A);
        sMap.put("m", M);
        sMap.put("touserid", YMApplication.getPID());
        sMap.put("userid", userid);
        sMap.put("bind", userid);
        sMap.put("sign", CommonUtils.computeSign(userid));

        followList.setVisibility(View.VISIBLE);
        noDataText.setVisibility(View.GONE);

        adapter = new FollowAdapter(this, mDatas, R.layout.follow_list_item);
        if (followList.getFooterViewsCount() == 0) {
            followList.addFooterView(footerView, null, false);
        }
        footerView.setVisibility(View.GONE);
        followList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initUrl(DEFAULT_PAGE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (followList != null && footerView != null && followList.getFooterViewsCount() > 0) {
            followList.removeFooterView(footerView);
        }

        if (call != null && !call.isCanceled()) {
            call.cancel();
        }


    }

    private void getParams() {
        userid = getIntent().getStringExtra("userid");
    }

    private void registerListener() {
        if (followList != null) {
            followList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == SCROLL_STATE_IDLE && view.getLastVisiblePosition() == view.getCount() - 1) {
                        // 加载更多
                        if (!isLoadingMoreNow) {
                            initUrl(++page);
                            isLoadingMoreNow = true;
                            if (followList.getFooterViewsCount() == 0) {
                                followList.addFooterView(footerView);
                            }

                            if (footerView.getVisibility() == View.GONE) {
                                footerView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                }
            });
        }

        if (followList != null) {
            followList.setOnItemClickListener(this);
        }
    }

    private void initUrl(int pageNum) {
        if (pageNum == DEFAULT_PAGE) {
            page = DEFAULT_PAGE;
            CommonUtils.showLoadingIndicator(followRefresh, true);

            if (adapter != null) {
                Map<Integer, Integer> checkPositionList = adapter.getCheckPositionList();
                if (checkPositionList != null) {
                    checkPositionList.clear();
                }
            }
        }

        sMap.put("page", pageNum);
        RetrofitServices.FollowListService service = RetrofitUtil.createService(RetrofitServices.FollowListService.class);
        call = service.getData(sMap);
        RetrofitUtil.getInstance().enqueueCall(call, this);
    }

    @SuppressLint("InflateParams")
    private void initView() {
        mDatas = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        followRefresh = (SwipeRefreshLayout) findViewById(R.id.follow_refresh);
        followList = (ListView) findViewById(R.id.follow_list);
        noDataText = (TextView) findViewById(R.id.nodata_text);

        footerView = LayoutInflater.from(this).inflate(R.layout.loading_more, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        if (followRefresh != null) {
            followRefresh.setRefreshing(true);

            DLog.d(LOG_TAG, "START REFRESH");
            initUrl(DEFAULT_PAGE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 个人主页
        if (mDatas != null) {
            FollowListData followListData = mDatas.get(position);
            String user_type = followListData.getUsertype();
            Intent intent;
            DLog.d(LOG_TAG, "user_type = " + user_type);
            if (user_type != null && (user_type.equals("seminar") || user_type.equals("zixun"))) {      // 病例研讨班
                intent = new Intent(this, DiscussSettingsActivity.class);
            } else if (user_type != null && user_type.equals("media")) { // 媒体号
                intent = new Intent(this, MediaSettingActivity.class);
            } else {                                                     // 其他
                intent = new Intent(this, PersonDetailActivity.class);
            }
            intent.putExtra("uuid", followListData.getId());
            intent.putExtra("isDoctor", followListData.getIs_doctor());
            this.startActivity(intent);
        }
    }

    @Override
    public void onSuccess(BaseBean<List<FollowListData>> baseBean) {
        CommonUtils.showLoadingIndicator(followRefresh, false);

        isLoadingMoreNow = false;
        if (followList.getFooterViewsCount() > 0) {
            followList.removeFooterView(footerView);
        }

        List<FollowListData> datas = baseBean.getData();
        if (datas.size() > 0) {
            followList.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.GONE);
            if (page == DEFAULT_PAGE) {
                if (mDatas != null) {
                    // page = 1，下拉刷新或第一次加载数据，清空原数据，避免数据重复
                    if (mDatas.size() > 0) {
                        mDatas.clear();
                    }

                    mDatas.addAll(datas);
                }
            } else {
                mDatas.addAll(datas);
            }
            adapter.notifyDataSetChanged();
        } else {
            if (followList.getFooterViewsCount() > 0) {
                followList.removeFooterView(footerView);
            }
            if (page != DEFAULT_PAGE) {
                followList.setVisibility(View.VISIBLE);
                // 重置page，防止无数据时一直下滑导致page无限增大，导致永远无法下滑加载出新数据
                page--;
                DLog.d(LOG_TAG, String.valueOf(page));
            } else {// 只有当page=1并且原来没有数据时才显示无数据页面，避免当只有一页数据下滑时导致无数据页与数据重叠显示
                DLog.d(LOG_TAG, "this");
                followList.setVisibility(View.GONE);
                noDataText.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onFailure(RequestState state, String msg) {
        CommonUtils.showLoadingIndicator(followRefresh, false);

        isLoadingMoreNow = false;
        if (followList.getFooterViewsCount() > 0) {
            followList.removeFooterView(footerView);
        }

        Toast.makeText(FollowListActivity.this, getString(R.string.loading_failed), Toast.LENGTH_SHORT).show();
    }
}
