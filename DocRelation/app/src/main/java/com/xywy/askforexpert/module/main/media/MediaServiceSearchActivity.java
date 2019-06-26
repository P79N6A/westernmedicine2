package com.xywy.askforexpert.module.main.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.fragment.LoadingDialogFragment;
import com.xywy.askforexpert.appcommon.interfaces.IRecyclerView;
import com.xywy.askforexpert.appcommon.interfaces.TextWatcherImpl;
import com.xywy.askforexpert.appcommon.net.retrofitTools.HttpRequestCallBack;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitServices;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitUtil;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
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

public class MediaServiceSearchActivity extends AppCompatActivity
        implements IRecyclerView.OnItemClickListener,
        IRecyclerView.OnLoadMoreListener, HttpRequestCallBack<List<ServicesMediasListBean>> {
    private static final String TAG = "MediaServiceSearchActivity";

    private static final String SEARCH = "搜索";
    private static final String CANCEL = "取消";

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private static final String REQUEST_PARAM_A = "sub";
    private static final String REQUEST_PARAM_M = "mediasearch";

    @Bind(R.id.media_service_search_input)
    EditText mediaServiceSearchInput;
    @Bind(R.id.media_service_search)
    TextView mediaServiceSearch;
    @Bind(R.id.media_service_search_toolbar)
    Toolbar mediaServiceSearchToolbar;
    @Bind(R.id.media_service_search_result)
    RecyclerView mediaServiceSearchResult;
    @Bind(R.id.no_search_result)
    TextView noSearchResult;
    @Bind(R.id.clear_search_key_word)
    ImageView clearSearchKeyWord;

    private int type;
    private int currentPage = DEFAULT_PAGE;
    private Map<String, Object> sMap = new HashMap<>();

    private List<ServicesMediasListBean> mDatas = new ArrayList<>();
    private MediaServiceListAdapter adapter;
    private View footerView;
    private LoadingDialogFragment loadingDialog;
    private String preKeyWord;
    private String searchKeyWord;
    private Call<BaseBean<List<ServicesMediasListBean>>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meida_service_search);
        ButterKnife.bind(this);

        CommonUtils.initSystemBar(this);

        loadingDialog = LoadingDialogFragment.newInstance("搜索中……");

        registerListeners();
        initRequestParams();

        mediaServiceSearchResult.setLayoutManager(new LinearLayoutManager(this));
        footerView = LayoutInflater.from(this).inflate(R.layout.loading, mediaServiceSearchResult, false);
        adapter = new MediaServiceListAdapter(this, mDatas, R.layout.media_service_item_layout, mediaServiceSearchResult);
        adapter.setOnItemClickListener(this);
        adapter.setOnLoadMoreListener(this);
        adapter.addFooterView(footerView);
        footerView.setVisibility(View.GONE);
        adapter.setEmptyView(noSearchResult);
        mediaServiceSearchResult.setAdapter(adapter);
    }

    private void initRequestParams() {
        type = getIntent().getIntExtra(MediaServiceListActivity.TYPE_INTENT_KEY, TYPE_MEDIA);
        sMap.put("a", REQUEST_PARAM_A);
        sMap.put("m", REQUEST_PARAM_M);
        sMap.put("pagesize", DEFAULT_PAGE_SIZE);
        sMap.put("bind", type);
        sMap.put("type", type);
        sMap.put("sign", CommonUtils.computeSign(String.valueOf(type)));
    }

    public static void start(Context activity,int type){
        Intent intent = new Intent(activity,MediaServiceSearchActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    private void registerListeners() {
        mediaServiceSearchInput.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);

                if (s.length() > 0) {
                    clearSearchKeyWord.setVisibility(View.VISIBLE);
                    mediaServiceSearch.setText(SEARCH);
                } else {
                    clearSearchKeyWord.setVisibility(View.GONE);
                    mediaServiceSearch.setText(CANCEL);
                }
            }
        });

        mediaServiceSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch();
                    return true;
                }

                return false;
            }
        });
    }

    @OnClick({R.id.media_service_search, R.id.clear_search_key_word})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.media_service_search:
                if (SEARCH.equals(mediaServiceSearch.getText())) {
                    // search
                    startSearch();
                } else {
                    this.finish();
                }
                break;

            case R.id.clear_search_key_word:
                mediaServiceSearchInput.setText("");
                break;
        }
    }

    private void startSearch() {
        searchKeyWord = mediaServiceSearchInput.getText().toString().trim();
        if ("".equals(searchKeyWord)) {
            Toast.makeText(this, "请输入正确的搜索关键词", Toast.LENGTH_SHORT).show();
            return;
        }
        // 默认搜索第一页数据
        currentPage = DEFAULT_PAGE;
        requestData();
    }

    @Override
    public void OnItemClick(View view) {
        int position = mediaServiceSearchResult.getChildAdapterPosition(view);
        ServicesMediasListBean servicesMediasListBean = mDatas.get(position);
        Map<String, String> map = new HashMap<>();
        map.put("channelId", servicesMediasListBean.getId());
        map.put("name", servicesMediasListBean.getName());
        Intent intent = new Intent();
        switch (type) {
            case TYPE_MEDIA:
//                CommonUtils.UMengAnalytics(this, "AMediaNumber");
                MobclickAgent.onEventValue(this, "AMediaNumber", map, 1);
                intent.setClass(this, MediaDetailActivity.class);
                intent.putExtra("mediaId", servicesMediasListBean.getId());
                break;

            case TYPE_SERVICE:
//                CommonUtils.UMengAnalytics(this, "AServiceNumber");
                MobclickAgent.onEventValue(this, "AServiceNumber", map, 1);
                intent.setClass(this, DiscussSettingsActivity.class);
                intent.putExtra("uuid", servicesMediasListBean.getId());
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        if (NetworkUtil.isNetWorkConnected()) {
            if (!adapter.hasFooterView(footerView)) {
                adapter.addFooterView(footerView);
            }
            footerView.setVisibility(View.VISIBLE);
            requestData();
        } else {
            Toast.makeText(this, "请检查您的网络连接", Toast.LENGTH_SHORT).show();
            adapter.removeFooterView(footerView);
        }
    }

    private void requestData() {
        DLog.d(TAG, "pre key word = " + preKeyWord);
        DLog.d(TAG, "search keyword = " + searchKeyWord);
        if (preKeyWord != null && !preKeyWord.equals(searchKeyWord)) // 关键词改变，重置页数
        {
            currentPage = DEFAULT_PAGE;
        }
        if (currentPage == DEFAULT_PAGE) {
            showLoadingDialog(true);
        }
        sMap.put("page", currentPage);
        sMap.put("keyword", searchKeyWord);
        RetrofitServices.SearchMediaService service = RetrofitUtil.createService(RetrofitServices.SearchMediaService.class);
        call = service.getData(sMap);
        RetrofitUtil.getInstance().enqueueCall(call, this);
        preKeyWord = searchKeyWord;
    }

    @Override
    public void onSuccess(BaseBean<List<ServicesMediasListBean>> baseBean) {
        CommonUtils.showOrHideSoftKeyboard(this, false, getCurrentFocus());
        mediaServiceSearch.setText(CANCEL);
        showLoadingDialog(false);
        List<ServicesMediasListBean> list = baseBean.getList();
        if (currentPage == DEFAULT_PAGE) {
            if (!mDatas.isEmpty()) {
                mDatas.clear();
            }
        }
        if (!list.isEmpty()) {
            CommonUtils.showOrHideViews(new View[]{mediaServiceSearchResult, noSearchResult},
                    new boolean[]{true, false});

            mDatas.addAll(list);
            adapter.notifyDataSetChanged();
            if (list.size() < DEFAULT_PAGE_SIZE) {
                footerView.setVisibility(View.GONE);
            }
            currentPage++;
        } else {
            footerView.setVisibility(View.GONE);
        }
        adapter.setIsLoading(false);
    }

    @Override
    public void onFailure(RequestState state, String msg) {
        CommonUtils.showOrHideSoftKeyboard(this, false, getCurrentFocus());
        showLoadingDialog(false);
        if ("数据不存在".equals(msg)) {
            if (currentPage == DEFAULT_PAGE) {
                if (!mDatas.isEmpty()) {
                    mDatas.clear();
                }
                adapter.notifyDataSetChanged();
                msg = "未搜索到相关结果";
            } else {
                msg = "暂无更多数据";
            }
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        footerView.setVisibility(View.GONE);
        adapter.setIsLoading(false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (call != null && !call.isCanceled()) {
            call.cancel();
        }


    }
}
