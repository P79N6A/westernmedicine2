package com.xywy.askforexpert.module.doctorcircle.topic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.db.DBManager;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.topics.MoreTopicItem;
import com.xywy.askforexpert.module.docotorcirclenew.activity.NewTopicDetailActivity;
import com.xywy.askforexpert.module.doctorcircle.DoctorCircleSendMessageActivty;
import com.xywy.askforexpert.module.doctorcircle.topic.adapter.TopicAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 话题搜索页
 *
 * @author shr
 *         created at 16/4/20 下午2:47
 */
public class TopicSearchActivity extends AppCompatActivity {

    private static final String TAG = "TopicSearchActivity";
    private final int HEADER_COUNT = 1;
    @Bind(R.id.topic_search_et)
    EditText topicSearchEt;
    @Bind(R.id.cancel_btn)
    TextView cancelBtn;
    @Bind(R.id.search_list)
    ListView searchList;
    @Bind(R.id.topic_swipe_refresh)
    SwipeRefreshLayout topicSwipeRefresh;
    TextView headerTv;
    /**
     * 来源 type=1 发表页 type=2 更多话题搜索
     */
    private int type = -1;
    /**
     * 用户id
     */
    private String userId = "";
    private TopicAdapter adapter;
    private List<MoreTopicItem.ListEntity> searchLists = new ArrayList<>();
    private List<MoreTopicItem.ListEntity> mLists = new ArrayList<>();
    /**
     * 历史记录
     */
    private List<MoreTopicItem.ListEntity> hisLists = new ArrayList<>();
    /**
     * 最近使用
     */
    private List<MoreTopicItem.ListEntity> recLists = new ArrayList<>();
    /**
     * 热门或者可变
     */
    private List<MoreTopicItem.ListEntity> hotLists = new ArrayList<>();
    private String tableHisName = "";
    private String tableRecName = "";
    private View footView;
    private View headerView;
    private boolean isBottom = false;
    private boolean isLoading = false;
    private int pageSize = 20;
    private int allPageNum;
    private int page = 1;
    private String createName = "";
    private Gson gson;
    private FinalHttp fh;
    /**
     * 是否为search
     */
    private boolean isSearch = false;
    private int sPage = 1;
    private int sAllPageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_search);

        CommonUtils.initSystemBar(this);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gson = new Gson();
        fh = new FinalHttp();
        initData();
        initView();
        initAction();

    }


    private void initView() {
        topicSwipeRefresh.setColorSchemeResources(R.color.color_scheme_2_1, R.color.color_scheme_2_2,
                R.color.color_scheme_2_3, R.color.color_scheme_2_4);
        headerView = LayoutInflater.from(this).inflate(R.layout.topic_sea_header, null);
        headerTv = (TextView) headerView.findViewById(R.id.header_tv);
        switch (type) {
            case 1:
                footView = LayoutInflater.from(this).inflate(R.layout.loading_more, null);
                if (searchList.getFooterViewsCount() == 0) {
                    searchList.addFooterView(footView);
                }
                if (footView != null && footView.getVisibility() == View.VISIBLE) {
                    footView.setVisibility(View.GONE);
                }

                getHotTopicData(false, page);
                break;
            case 2:
                footView = LayoutInflater.from(this).inflate(R.layout.topic_foot_view, null);
                adapter = new TopicAdapter(this, mLists);
                searchList.setAdapter(adapter);
                if (!mLists.isEmpty()) {
                    searchList.addFooterView(footView);
                    footView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DBManager.getManager().clearTableData(tableHisName);
                            mLists.clear();
                            adapter.notifyDataSetChanged();
                            if (searchList.getFooterViewsCount() != 0) {
                                searchList.removeFooterView(footView);
                            }
                        }
                    });
                }
                break;
        }

    }

    private void initData() {
        type = getIntent().getIntExtra("from", -1);
        if (!YMUserService.isGuest()) {
            userId = YMApplication.getLoginInfo().getData().getPid();
        }

        switch (type) {
            case 1:
                if (!mLists.isEmpty()) {
                    mLists.clear();
                }
                if (!"".equals(userId)) {
                    tableRecName = "rec_" + userId;
                    tableHisName = "his_" + userId;
                    recLists = DBManager.getManager().getTopicData(tableRecName);
                    hisLists = DBManager.getManager().getTopicData(tableHisName);
                    mLists.addAll(recLists);
                }
                break;
            case 2:
                if (!"".equals(userId)) {
                    tableHisName = "his_" + userId;
                    hisLists = DBManager.getManager().getTopicData(tableHisName);
                    mLists = hisLists;
                }

                break;

            default:
                DLog.e(TAG, "调转类型不正确");
                break;
        }


    }

    private void initAction() {
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type) {
                    case 1:
                        try {
                            if (searchList.getHeaderViewsCount() != 0 && headerView.getVisibility() == View.VISIBLE && position == 0) {
                                StatisticalTools.eventCount(TopicSearchActivity.this, "yqTopicSearchCreate");
                                Intent intent = new Intent(TopicSearchActivity.this, CreateEditTopicActivity.class);
                                intent.putExtra(CreateEditTopicActivity.TOPIC_NAME_KEY, createName);
                                startActivity(intent);
                                finish();
                                return;
                            }
                            DLog.i(TAG, "点击了第" + position);
                            MoreTopicItem.ListEntity topicItem = (MoreTopicItem.ListEntity) parent.getAdapter().getItem(position);

                            addRecToDb(topicItem);

                            Intent intent = new Intent(TopicSearchActivity.this, DoctorCircleSendMessageActivty.class);
                            intent.putExtra("topicName", topicItem.getTheme());
                            intent.putExtra("topicId", topicItem.getId() + "");
                            setResult(DoctorCircleSendMessageActivty.RESULT_OK, intent);
                            finish();
                        } catch (Exception e) {
                            DLog.e(TAG, e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            if (searchList.getHeaderViewsCount() != 0 && headerView.getVisibility() == View.VISIBLE && position == 0) {
                                StatisticalTools.eventCount(TopicSearchActivity.this, "yqTopicSearchCreate");
                                Intent intent = new Intent(TopicSearchActivity.this, CreateEditTopicActivity.class);
                                intent.putExtra(CreateEditTopicActivity.TOPIC_NAME_KEY, createName);
                                startActivity(intent);
                                finish();
                                return;
                            }
                            DLog.i(TAG, "是否搜索" + isSearch);
                            MoreTopicItem.ListEntity topicItem = (MoreTopicItem.ListEntity) parent.getAdapter().getItem(position);
                            if (isSearch) {
                                StatisticalTools.eventCount(TopicSearchActivity.this, "yqTopicSearchMatch");
                                Intent intent = new Intent(TopicSearchActivity.this, NewTopicDetailActivity.class);
                                intent.putExtra(NewTopicDetailActivity.TOPICID_INTENT_PARAM_KEY, topicItem.getId());
                                startActivity(intent);
//                                finish();
//                                return;
                            } else {
                                sPage = 1;
                                isSearch = true;
                                if (searchList.getFooterViewsCount() != 0) {
                                    searchList.removeFooterView(footView);
                                }
                                if (type == 2) {
                                    footView = null;
                                    footView = LayoutInflater.from(TopicSearchActivity.this).inflate(R.layout.loading_more, null);
                                }
                                topicSearchEt.setText(topicItem.getTheme());
                                topicSearchEt.setSelection(topicItem.getTheme().length());
                                searchTopic(topicItem.getTheme(), sPage, false);
                                StatisticalTools.eventCount(TopicSearchActivity.this, "yqTopicSearchHistroy");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            DLog.e(TAG, "越界" + e.getMessage());
                        } catch (Exception e) {
                            DLog.e(TAG, "更多话题搜索" + e.getMessage());
                        }

                        break;
                    default:

                        break;

                }

            }
        });
        switch (type) {
            case 1:
                topicSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        DLog.i(TAG, "刷新数据");
                        if (isSearch) {
                            sPage = 1;
                            searchLists.clear();
                            searchTopic(topicSearchEt.getText().toString(), sPage, true);
                        } else {
                            page = 1;
                            mLists.removeAll(hotLists);
                            hotLists.clear();
                            getHotTopicData(true, page);
                        }
                    }
                });
                searchList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (isBottom && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && !isLoading) {
                            DLog.i(TAG, "onScrollStateChanged加载更多");
                            isLoading = true;
                            if (searchList.getFooterViewsCount() == 0) {
                                searchList.addFooterView(footView);
                            }
                            if (footView.getVisibility() == View.GONE) {
                                footView.setVisibility(View.VISIBLE);
                            }
                            if (NetworkUtil.isNetWorkConnected()) {
                                if (isSearch) {
                                    sPage = sPage + 1;
                                    searchTopic(topicSearchEt.getText().toString(), sPage, false);
                                } else {
                                    page = page + 1;
                                    getHotTopicData(false, page);
                                }
                            } else {
                                isBottom = false;
                                Toast.makeText(TopicSearchActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                                if (footView != null && footView.getVisibility() == View.VISIBLE) {
                                    footView.setVisibility(View.GONE);
                                }
                            }

                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                        DLog.i(TAG, "firstVisibleItem=" + firstVisibleItem + "visibleItemCount=" + visibleItemCount + "totalItemCount=" + totalItemCount);
                        isBottom = ((firstVisibleItem + visibleItemCount) == totalItemCount);
                    }
                });
                break;
            case 2:
                topicSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (isSearch) {
                            sPage = 1;
                            searchLists.clear();
                            searchTopic(topicSearchEt.getText().toString(), sPage, true);
                        } else {
                            topicSwipeRefresh.setRefreshing(false);
                        }
                    }
                });
                searchList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (isBottom && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && !isLoading && isSearch) {
                            DLog.i(TAG, "onScrollStateChanged加载更多");
                            isLoading = true;
                            if (searchList.getFooterViewsCount() == 0) {
                                searchList.addFooterView(footView);
                            }
                            if (footView.getVisibility() == View.GONE) {
                                footView.setVisibility(View.VISIBLE);
                            }
                            if (NetworkUtil.isNetWorkConnected()) {
                                sPage = sPage + 1;
                                searchTopic(topicSearchEt.getText().toString(), sPage, false);
                            } else {
                                isBottom = false;
                                Toast.makeText(TopicSearchActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                                if (footView != null && footView.getVisibility() == View.VISIBLE) {
                                    footView.setVisibility(View.GONE);
                                }
                            }

                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        isBottom = ((firstVisibleItem + visibleItemCount) == totalItemCount);
                    }
                });
                break;
        }

        topicSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isSearch = true;
                    if (searchList.getHeaderViewsCount() != 0) {
                        searchList.removeHeaderView(headerView);
                    }
                    if (searchList.getFooterViewsCount() != 0) {
                        searchList.removeFooterView(footView);
                    }
                    if (type == 2) {
                        footView = null;
                        footView = LayoutInflater.from(TopicSearchActivity.this).inflate(R.layout.loading_more, null);
                    }
                    if (adapter != null) {
                        adapter.bindData(searchLists);
                    }
                } else {
                    if (type == 2) {
                        footView = null;
                        footView = LayoutInflater.from(TopicSearchActivity.this).inflate(R.layout.topic_foot_view, null);
                    }
                    if (searchList.getHeaderViewsCount() != 0) {
                        searchList.removeHeaderView(headerView);
                    }

                    isSearch = false;
                    searchLists.clear();
                    if (adapter != null) {
                        adapter.signText("");
                    }
                    if (type == 2 && searchList.getFooterViewsCount() == 0) {
                        searchList.addFooterView(footView);
                        footView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DBManager.getManager().clearTableData(tableHisName);
                                mLists.clear();
                                if (searchList.getFooterViewsCount() != 0) {
                                    searchList.removeFooterView(footView);
                                }
                            }
                        });
                    }
                    if (null!=adapter){
                        adapter.bindData(mLists);
                    }else{
                        adapter = new TopicAdapter(TopicSearchActivity.this, mLists);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        topicSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    StatisticalTools.eventCount(TopicSearchActivity.this, "yqTopicSearchBottom");
                    /*隐藏软键盘*/
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(TopicSearchActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }
                    sPage = 1;
                    searchLists.clear();
                    searchTopic(topicSearchEt.getText().toString(), sPage, false);
                    search(topicSearchEt.getText().toString());
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @OnClick(R.id.cancel_btn)
    public void onClick() {
        finish();
        StatisticalTools.eventCount(this, "yqTopicSearchCancel");
    }

    private void search(String content) {
        DLog.i(TAG, "开始搜索" + content);
        if (!content.equals("")) {
            //保存成历史记录
            checkHasKey(content);
        }
    }

    /**
     * 添加最近历史到数据库
     *
     * @param topicEntity
     */
    private void addRecToDb(MoreTopicItem.ListEntity topicEntity) {
        DBManager.getManager().clearTableData(tableRecName);
        for (int i = 0; i < recLists.size(); i++) {
            if (recLists.get(i).getTheme().equals(topicEntity.getTheme())) {
                recLists.remove(i);
            }
        }
        recLists.add(0, topicEntity);
        for (int i = 0; i < recLists.size(); i++) {
            recLists.get(i).setHeader("最近使用");
            if (i < 3) {
                if (!"".equals(tableRecName)) {
                    DBManager.getManager().addTopicData(recLists.get(i), tableRecName);
                }
            }
        }
    }

    private void checkHasKey(String key) {
        DBManager.getManager().clearTableData(tableHisName);
        for (int i = 0; i < hisLists.size(); i++) {
            if (hisLists.get(i).getTheme().equals(key)) {
                hisLists.remove(i);
            }
        }

        MoreTopicItem.ListEntity topicEntity = new MoreTopicItem.ListEntity();
        topicEntity.setTheme(key);
        topicEntity.setHeader("历史搜索");
        hisLists.add(0, topicEntity);
        for (int i = 0; i < hisLists.size(); i++) {
            if (i < 5) {
                if (!"".equals(tableHisName)) {
                    DBManager.getManager().addTopicData(hisLists.get(i), tableHisName);
                }
            }
        }
    }

    /**
     * 热门话题数据
     */
    private void getHotTopicData(final boolean isRefresh, final int page) {
        // 推荐1，热门2，最新3，可变传5
        int typeId = 5;
        DLog.i(TAG, "page ===" + page);
        if (!NetworkUtil.isNetWorkConnected()) {
            Toast.makeText(this, "请检查网络设置", Toast.LENGTH_SHORT).show();
            return;
        }
        AjaxParams params = new AjaxParams();
        params.put("m", "theme_list");
        params.put("a", "theme");
        params.put("bind", typeId + "");
        params.put("type", typeId + "");
        params.put("page", page + "");
        params.put("sign", MD5Util.MD5(typeId + Constants.MD5_KEY));
        DLog.i(TAG, "more topic url" + CommonUrl.doctor_circo_url + "?" + params.toString());
        fh.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                topicSwipeRefresh.setRefreshing(false);
                isLoading = false;
                DLog.i(TAG, "更多话题页" + s);
                try {
                    MoreTopicItem moreItem = gson.fromJson(s, MoreTopicItem.class);
                    int pageTotal = moreItem.getTotal();
                    String header = moreItem.getTitle();
                    allPageNum = (pageTotal + pageSize - 1) / pageSize;      //总条数
                    DLog.i(TAG, "总条数：" + allPageNum + "当前第" + page + "页");
                    if (page >= allPageNum) {
                        if (searchList.getFooterViewsCount() != 0) {
                            searchList.removeFooterView(footView);
                        }
                    }
                    List<MoreTopicItem.ListEntity> list = moreItem.getList();
                    DLog.i(TAG, "加载的list大小" + list.size());
                    if (list != null && !list.isEmpty()) {

                        for (MoreTopicItem.ListEntity entity : list) {
                            entity.setHeader(header);
                        }

                        if (moreItem.getCode() == 0) {
                            //在此页面刷新,mAdapter!=null
                            if (isRefresh && adapter != null) {
                                DLog.i(TAG, "在此页面刷新,mAdapter!=null");
                                hotLists.clear();
                                hotLists = list;
                                mLists.addAll(hotLists);
                                adapter.bindData(mLists);
                                return;
                            }
                            //进入页面 ，但是没有刷新
                            if (page == 1 && !isRefresh) {
                                DLog.i(TAG, "进入页面 ，但是没有刷新");
                                if (!hotLists.isEmpty()) {
                                    hotLists.clear();
                                }
                                hotLists.addAll(list);
                                mLists.addAll(hotLists);
                                adapter = new TopicAdapter(TopicSearchActivity.this, mLists);
                                searchList.setAdapter(adapter);
                                return;
                            }
                            //加载更多
                            if (page > 1 && page <= allPageNum) {
                                DLog.i(TAG, "加载更多");
                                hotLists.addAll(list);
                                mLists.addAll(list);
                                adapter.bindData(mLists);
                            } else {
                                if (footView != null && footView.getVisibility() == View.VISIBLE) {
                                    footView.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        if (adapter == null && !recLists.isEmpty() && hotLists.isEmpty()) {
                            adapter = new TopicAdapter(TopicSearchActivity.this, mLists);
                            searchList.setAdapter(adapter);
                        }
                        if (footView != null && footView.getVisibility() == View.VISIBLE) {
                            footView.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    DLog.e(TAG, e.getMessage());
                    if (recLists != null && !recLists.isEmpty()) {
                        adapter = new TopicAdapter(TopicSearchActivity.this, mLists);
                        searchList.setAdapter(adapter);
                    }
                    if (footView != null && footView.getVisibility() == View.VISIBLE) {
                        footView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(TopicSearchActivity.this, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                if (topicSwipeRefresh != null) {
                    topicSwipeRefresh.setRefreshing(false);
                }
                isLoading = false;
                if (recLists != null && !recLists.isEmpty()) {
                    adapter = new TopicAdapter(TopicSearchActivity.this, mLists);
                    searchList.setAdapter(adapter);
                }
                if (footView != null && footView.getVisibility() == View.VISIBLE) {
                    footView.setVisibility(View.GONE);
                }
            }
        });
    }


    /**
     * 话题搜索
     * http://test.yimai.api.xywy.com/app/1.2/index.interface.php?m=theme_search&a=theme&keyword=话
     */
    private void searchTopic(final String keyword, final int sPage, final boolean refresh) {
        if (keyword.equals("")) {
            Toast.makeText(this, "请输入文字", Toast.LENGTH_SHORT).show();
            return;
        }
        createName = keyword;
        if (!NetworkUtil.isNetWorkConnected()) {
            Toast.makeText(this, "请检查网络设置", Toast.LENGTH_SHORT).show();
            return;
        }
        AjaxParams params = new AjaxParams();
        params.put("m", "theme_search");
        params.put("a", "theme");
        params.put("keyword", keyword);
        params.put("page", sPage + "");
        DLog.i(TAG, "more topic url" + CommonUrl.doctor_circo_url + "?" + params.toString());
        fh.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                isLoading = false;
                topicSwipeRefresh.setRefreshing(false);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    int total = jsonObject.getInt("total");
                    sAllPageNum = (total + pageSize - 1) / pageSize;
                    DLog.i(TAG, "sAllPageNum==" + sAllPageNum + "===" + s);
                    if (sAllPageNum == 0) {
                        searchLists.clear();
                        if (searchList.getHeaderViewsCount() == 0) {
                            searchList.addHeaderView(headerView);
                        }
                        headerTv.setText("创建新话题" + "\"" + keyword + "\"");
                        if (adapter != null) {
                            adapter.bindData(searchLists);
                        }
                        if (searchList.getFooterViewsCount() != 0) {
                            searchList.removeFooterView(footView);
                        }
                        Toast.makeText(TopicSearchActivity.this, "未搜索到相关话题", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (sPage >= sAllPageNum) {
                        if (searchList.getFooterViewsCount() != 0) {
                            searchList.removeFooterView(footView);
                        }
                    }
                    if (code == 0) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonElement = jsonArray.getJSONObject(i);
                                MoreTopicItem.ListEntity listEntity = new MoreTopicItem.ListEntity();
                                listEntity.setHeader("");
                                listEntity.setId(jsonElement.getInt("id"));
                                listEntity.setTheme(jsonElement.getString("theme"));
                                searchLists.add(listEntity);
                            }
                            //规则匹配
                            if (!searchLists.get(0).getTheme().equals(keyword)) {
                                if (searchList.getHeaderViewsCount() == 0) {
                                    searchList.addHeaderView(headerView);
                                }
                                headerTv.setText("创建新话题" + "\"" + keyword + "\"");
                            }
                            //在此页面刷新,mAdapter!=null
                            if (refresh && adapter != null) {
                                DLog.i(TAG, "search在此页面刷新,!=null");
                                adapter.signText(keyword);
                                adapter.bindData(searchLists);
                                return;
                            }
                            //进入页面 ，但是没有刷新
                            if (sPage == 1 && !refresh) {
                                DLog.i(TAG, "search进入页面 ，但是没有刷新");
                                adapter = new TopicAdapter(TopicSearchActivity.this, searchLists);
                                adapter.signText(keyword);
                                searchList.setAdapter(adapter);
                                return;
                            }
                            //加载更多
                            if (sPage > 1 && sPage <= sAllPageNum) {
                                DLog.i(TAG, "search加载更多");
                                adapter.signText(keyword);
                                adapter.bindData(searchLists);
                            } else {
                                if (footView != null && footView.getVisibility() == View.VISIBLE) {
                                    footView.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            if (footView != null && footView.getVisibility() == View.VISIBLE) {
                                footView.setVisibility(View.GONE);
                            }
                        }

                    } else {
                        Toast.makeText(TopicSearchActivity.this, msg, Toast.LENGTH_SHORT).show();

                        if (footView != null && footView.getVisibility() == View.VISIBLE) {
                            footView.setVisibility(View.GONE);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                isLoading = false;
                topicSwipeRefresh.setRefreshing(false);
                searchLists.clear();
                if (searchList.getHeaderViewsCount() == 0) {
                    searchList.addHeaderView(headerView);
                }
                headerTv.setText("创建新话题" + "\"" + keyword + "\"");
                if (adapter != null) {
                    adapter.bindData(searchLists);
                }
                if (searchList.getFooterViewsCount() != 0) {
                    searchList.removeFooterView(footView);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DLog.i(TAG, "onResume === type==" + type);
        StatisticalTools.onResume(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
