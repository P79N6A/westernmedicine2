package com.xywy.askforexpert.module.main.media;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.fragment.LoadingDialogFragment;
import com.xywy.askforexpert.appcommon.interfaces.IRecyclerView;
import com.xywy.askforexpert.appcommon.net.retrofitTools.HttpRequestCallBack;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitServices;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitUtil;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.base.BaseBean;
import com.xywy.askforexpert.model.media.ServicesMediasListBean;
import com.xywy.askforexpert.module.main.media.adapter.MediaServiceListAdapter;
import com.xywy.askforexpert.module.message.usermsg.DiscussSettingsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static com.xywy.askforexpert.appcommon.old.Constants.TYPE_MEDIA;
import static com.xywy.askforexpert.appcommon.old.Constants.TYPE_SERVICE;

/**
 * 媒体号/服务号列表
 *
 * @author Jack Fang
 */
public class MediaServiceListActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener,
        HttpRequestCallBack<List<ServicesMediasListBean>>,
        IRecyclerView.OnItemClickListener, IRecyclerView.OnLoadMoreListener {
    public static final String TYPE_INTENT_KEY = "type";
    private static final String TAG = "MediaServiceListActivity";
    private static final String REQUEST_PARAM_A = "sub";
    private static final String REQUEST_PARAM_M = "medialist";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE = 1;
    @Bind(R.id.media_service_list_toolbar_title)
    TextView mediaServiceListToolbarTitle;
    @Bind(R.id.media_service_list_toolbar)
    Toolbar mediaServiceListToolbar;
    @Bind(R.id.media_service_list_search)
    TextView mediaServiceListSearch;
    @Bind(R.id.media_service_recycler_view)
    RecyclerView mediaServiceRecyclerView;
    @Bind(R.id.media_service_list_refresh)
    SwipeRefreshLayout mediaServiceListRefresh;

    private List<ServicesMediasListBean> mDatas = new ArrayList<>();

    private int type;

    private int currentPage = DEFAULT_PAGE;

    private Map<String, Object> sMap = new HashMap<>();
    private MediaServiceListAdapter adapter;
    private View footerView;
    private LoadingDialogFragment loadingDialog;
    private Call<BaseBean<List<ServicesMediasListBean>>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_service_list);
        ButterKnife.bind(this);

        CommonUtils.initSystemBar(this);
        CommonUtils.setToolbar(this, mediaServiceListToolbar);
        CommonUtils.setRefresh(mediaServiceListRefresh);

        loadingDialog = LoadingDialogFragment.newInstance("加载中……");

        type = getIntent().getIntExtra(TYPE_INTENT_KEY, TYPE_MEDIA);
        switch (type) {
            case TYPE_MEDIA:
                mediaServiceListToolbarTitle.setText("媒体号");
                break;

            case TYPE_SERVICE:
                mediaServiceListToolbarTitle.setText("服务号");
                break;
        }

        sMap.put("a", REQUEST_PARAM_A);
        sMap.put("m", REQUEST_PARAM_M);
        sMap.put("pagesize", DEFAULT_PAGE_SIZE);
        sMap.put("type", type);
        sMap.put("bind", type);
        sMap.put("sign", CommonUtils.computeSign(String.valueOf(type)));

        mediaServiceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        footerView = LayoutInflater.from(this).inflate(R.layout.loading, mediaServiceRecyclerView, false);
        mediaServiceListRefresh.setOnRefreshListener(this);
        adapter = new MediaServiceListAdapter(this, mDatas, R.layout.media_service_item_layout, mediaServiceRecyclerView);
        adapter.setOnItemClickListener(this);
        adapter.setOnLoadMoreListener(this);
        adapter.addFooterView(footerView);
        footerView.setVisibility(View.GONE);
        mediaServiceRecyclerView.setAdapter(adapter);

        if (!adapter.hasFooterView(footerView)) {
            adapter.addFooterView(footerView);
        }

        if (NetworkUtil.isNetWorkConnected()) {
            requestData();
        } else {
            Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
            mediaServiceListRefresh.setRefreshing(false);
            adapter.removeFooterView(footerView);
        }
    }

    private void requestData() {
        if (currentPage == DEFAULT_PAGE && mDatas.isEmpty()) {
            showLoadingDialog(true);
        }
        DLog.d(TAG, "media list current page = " + currentPage);
        sMap.put("page", currentPage);
        RetrofitServices.ServicesMediasListService service = RetrofitUtil.createService(RetrofitServices.ServicesMediasListService.class);
        call = service.getData(sMap);
        RetrofitUtil.getInstance().enqueueCall(call, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (call != null && !call.isCanceled()) {
            call.cancel();
        }


    }

    @Override
    public void onRefresh() {
        currentPage = DEFAULT_PAGE;
        requestData();
    }

    @Override
    public void onSuccess(BaseBean<List<ServicesMediasListBean>> baseBean) {
        showLoadingDialog(false);
        List<ServicesMediasListBean> list = baseBean.getList();
        mediaServiceListRefresh.setRefreshing(false);
        if (currentPage == DEFAULT_PAGE) {
            mDatas.clear();
        }
        DLog.d(TAG, "media list total = " + baseBean.getTotal());
        if (!list.isEmpty()) {
            mDatas.addAll(list);
            adapter.notifyDataSetChanged();
            currentPage++;
            if (list.size() < DEFAULT_PAGE_SIZE) {
                adapter.removeFooterView(footerView);
            }
        } else {
            adapter.removeFooterView(footerView);
        }
        adapter.setIsLoading(false);
    }

    @Override
    public void onFailure(RequestState state, String msg) {
        showLoadingDialog(false);
        mediaServiceListRefresh.setRefreshing(false);
        if ("数据不存在".equals(msg)) {
            msg = "暂无更多数据";
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        footerView.setVisibility(View.GONE);
        adapter.removeFooterView(footerView);
        adapter.setIsLoading(false);
    }

    @Override
    public void OnItemClick(View view) {
        int position = mediaServiceRecyclerView.getChildAdapterPosition(view);
        ServicesMediasListBean servicesMediasListBean = mDatas.get(position);
        Intent intent = new Intent();
        switch (type) {
            case TYPE_MEDIA:
                intent.setClass(this, MediaDetailActivity.class);
                intent.putExtra("mediaId", servicesMediasListBean.getId());
                break;

            case TYPE_SERVICE:
                intent.setClass(this, DiscussSettingsActivity.class);
                intent.putExtra("uuid", servicesMediasListBean.getId());
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        DLog.d(TAG, "onLoadMore");
        if (NetworkUtil.isNetWorkConnected()) {
            if (!adapter.hasFooterView(footerView)) {
                adapter.addFooterView(footerView);
            }
            footerView.setVisibility(View.VISIBLE);
            requestData();
        } else {
            Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
            mediaServiceListRefresh.setRefreshing(false);
            adapter.removeFooterView(footerView);
        }
    }

    private void showLoadingDialog(boolean b) {
        if (loadingDialog != null) {
            if (b) {
                loadingDialog.show(getSupportFragmentManager(), TAG);
            } else {
                loadingDialog.dismiss();
            }
        }
    }

    @OnClick(R.id.media_service_list_search)
    public void onClick() {
        // 搜索媒体号/服务号
        if (type == TYPE_MEDIA) {
            StatisticalTools.eventCount(this, "MediaNumberSearchBox");
        } else if (type == TYPE_SERVICE) {
            StatisticalTools.eventCount(this, "ServiceNumberSearchBox");
        }
        MediaServiceSearchActivity.start(this,type);
    }
}
