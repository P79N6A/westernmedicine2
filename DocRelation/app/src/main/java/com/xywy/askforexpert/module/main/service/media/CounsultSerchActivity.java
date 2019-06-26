package com.xywy.askforexpert.module.main.service.media;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.ConsultingInfo;
import com.xywy.askforexpert.module.main.news.adapter.Con_NewsAdapter;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 资讯搜索
 *
 * @author 王鹏
 * @2015-6-5上午9:23:10 shihao 修改2015.12.3
 */
public class CounsultSerchActivity extends YMBaseActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {

    private static final String TAG = "CounsultSerchActivity";

    private InputMethodManager inputMethodManager;
    private String id;
//	private ListView list_codex;
    /**
     * 加载更多的listview
     */
    private MyLoadMoreListView list_codex;

    private int page = 1;
    private ConsultingInfo codex = new ConsultingInfo();
    private ConsultingInfo codex_down = new ConsultingInfo();
    private Con_NewsAdapter adapter;
    private ImageButton clearSearch;
    private TextView tv_search;

    private String lastTextString = "";
    /**
     * 搜索框
     */
    private EditText query;
    private TextView tv_nodata;
    SwipeRefreshLayout swip;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:
                    if (codex != null & codex.getCode().equals("0")) {
                        // updateList(codex.getList());
                        adapter = new Con_NewsAdapter(CounsultSerchActivity.this);
                        if (codex.getList().size() != 0) {
                            adapter.setData(codex.getList());
                            list_codex.setAdapter(adapter);
                            tv_nodata.setVisibility(View.GONE);
                            if (codex.getList().size() < 20) {
//							mPullToRefreshListView.setScrollLoadEnabled(false);
//							mPullToRefreshListView.setScrollLoadEnabled(false);
//							mPullToRefreshListView.setHasMoreData(false);
                                list_codex.setLoading(true);
                                list_codex.noMoreLayout();
                            } else {
//							mPullToRefreshListView.setScrollLoadEnabled(true);
//							mPullToRefreshListView.setHasMoreData(true);
                                list_codex.setLoading(false);

                            }
                        } else {
                            tv_nodata.setVisibility(View.VISIBLE);
                            //T.shortToast( "没有搜索到相关内容");
                        }

                    } else {
                        tv_nodata.setVisibility(View.VISIBLE);
                    }
                    break;
                case 200:
                    if (codex_down != null && codex_down.getCode().equals("0")) {
                        codex.getList().addAll(codex_down.getList());
                        adapter.setData(codex.getList());
                        adapter.notifyDataSetChanged();
                        if (codex_down.getList().size() == 0) {
//						T.showNoRepeatShort(CounsultSerchActivity.this, "没有更多数据了");
                            page--;
                            list_codex.LoadingMoreText(getResources().getString(R.string.no_more));
                            list_codex.setLoading(true);
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };


    public void getData(final int page, String title) {

        String sign = MD5Util.MD5(Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("c", "search");
        params.put("a", "list");
        params.put("page", page + "");
        params.put("pagesize", 20 + "");
        params.put("title", title);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Consulting_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Gson gson = new Gson();
                if (page == 1) {
                    codex = ResolveJson.R_Consult(t.toString());
                    handler.sendEmptyMessage(100);
                    swip.setRefreshing(false);
                } else {
                    DLog.i(TAG, "PAGE==" + page);
                    codex_down = ResolveJson.R_Consult(t.toString());
                    handler.sendEmptyMessage(200);
                    list_codex.onLoadComplete();
                }

            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DLog.i(TAG, "SEARCH=onFailure=" + strMsg);
                swip.setRefreshing(false);
                list_codex.onLoadComplete();

            }
        });
    }

    void hideSoftKeyboard() {
        if (CounsultSerchActivity.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (CounsultSerchActivity.this.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        CounsultSerchActivity.this.getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();

        ((TextView) findViewById(R.id.tv_title)).setText("医学资讯");
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);

        list_codex = (MyLoadMoreListView) findViewById(R.id.list_codex);
        list_codex.setLoadMoreListen(this);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        query = (EditText) findViewById(R.id.query);
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_search = (TextView) findViewById(R.id.tv_search);

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
//                    list_codex.setVisibility(View.VISIBLE);
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
//					if (codex!=null&codex.getList() != null)
//					{
//						codex.getList().clear();
//						list_codex.setVisibility(View.GONE);
//					}
                    clearSearch.setVisibility(View.INVISIBLE);
//					mPullToRefreshListView.setVisibility(View.GONE);
//                    list_codex.setVisibility(View.GONE);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastTextString = query.getText().toString().trim();
                query.getText().clear();
//				if (codex != null&codex.getList()!=null)
//				{
//					codex.getList().clear();
//					list_codex.setVisibility(View.GONE);
//
//				}

//				hideSoftKeyboard();
            }
        });
        tv_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String str = query.getText().toString().trim();
                hideSoftKeyboard();
                if (NetworkUtil.isNetWorkConnected()) {
                    if (!TextUtils.isEmpty(str) && !str.equals("")) {
                        if (str.length() < 40) {

                            if (list_codex.getVisibility() == View.GONE) {
                                list_codex.setVisibility(View.VISIBLE);
                            }

                            if (NetworkUtil.isNetWorkConnected()) {
                                lastTextString = str;
                                page = 1;
                                getData(page, str);
                            } else {
                                ToastUtils.shortToast("网络连接失败");
                            }
                        } else {
                            ToastUtils.shortToast(
                                    "搜索内容限制在40个字符以内");
                        }
                    } else {
                        query.getText().clear();
                        ToastUtils.shortToast(
                                "请输入搜索内容");
                    }

                } else {
                    ToastUtils.shortToast( "网络连接失败");

                }

            }
        });
        list_codex.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(CounsultSerchActivity.this,
                        InfoDetailActivity.class);
                intent.putExtra("url", codex.getList().get(arg2).getUrl());
                intent.putExtra("ids", codex.getList().get(arg2).getId());
                intent.putExtra("title", codex.getList().get(arg2).getTitle());
                intent.putExtra("imageurl", codex.getList().get(arg2)
                        .getImage());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    public void loadMore() {
        if (NetworkUtil
                .isNetWorkConnected()) {
            DLog.i(TAG, "loadMore" + lastTextString + "======");
            getData(++page, lastTextString);
        } else {
           ToastUtils.shortToast( "网络连接失败");

        }
    }

    @Override
    public void onRefresh() {
        if (NetworkUtil
                .isNetWorkConnected()) {
            page = 1;
            getData(page, lastTextString);
        } else {
           ToastUtils.shortToast( "网络连接失败");
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.book_serch;
    }
}
