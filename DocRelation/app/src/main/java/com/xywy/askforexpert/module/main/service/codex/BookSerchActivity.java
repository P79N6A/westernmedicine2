package com.xywy.askforexpert.module.main.service.codex;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.CodexSecondInfo;
import com.xywy.askforexpert.module.main.service.codex.adapter.BaseCodexSecondAdapter;
import com.xywy.askforexpert.module.main.service.linchuang.fragment.GuideMainFragment;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 检查手册 药典 搜索 stone
 *
 * @author 王鹏
 * @2015-6-5上午9:23:10
 */
public class BookSerchActivity extends YMBaseActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {
    // private ListView list_news_con;
    // private PullToRefreshListView mPullToRefreshListView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private InputMethodManager inputMethodManager;
    private String id;
    // private ListView list_codex;
    private MyLoadMoreListView list_codex;
    private int page = 1;
    private CodexSecondInfo codex = new CodexSecondInfo();
    private CodexSecondInfo codex_down = new CodexSecondInfo();
    private BaseCodexSecondAdapter adapter;
    private ImageButton clearSearch;
    private TextView tv_cancle;
    String type;
    private TextView tv_nodata;
    private EditText query;
    SwipeRefreshLayout swip;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:
                    if (codex != null & codex.getCode().equals("0")) {
                        // updateList(codex.getList());
                        if (codex.getList().size() != 0) {
                            tv_nodata.setVisibility(View.GONE);

                            adapter = new BaseCodexSecondAdapter(BookSerchActivity.this);
                            adapter.setData(codex.getList());
                            list_codex.setAdapter(adapter);
                            if (codex.getList().size() < 15) {
//						mPullToRefreshListView.setHasMoreData(false);
//						mPullToRefreshListView.getFooterLoadingLayout().show(
//								false);
                                list_codex.setLoading(true);
                                list_codex.noMoreLayout();
                            } else {
//						mPullToRefreshListView.setHasMoreData(true);
                                list_codex.setLoading(false);
                            }
                        } else {
                            tv_nodata.setVisibility(View.VISIBLE);
                        }
                    } else if (codex.getCode().equals("-2")) {
                        tv_nodata.setVisibility(View.VISIBLE);
                        // T.showNoRepeatShort(BookSerchActivity.this,
                        // codex.getMsg());
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
//						T.showNoRepeatShort(BookSerchActivity.this, "没有更多数据了");
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
    private TextView tv_search;


    public void getData(final int page, String type, String title) {
        final ProgressDialog dialog = new ProgressDialog(this, "搜索中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String sign = MD5Util.MD5(type + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("c", "search");
        params.put("a", "search");
        params.put("channel", type);
        params.put("page", page + "");
        params.put("pagesize", 15 + "");
        if (!YMUserService.isGuest()) {
            params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        }

        params.put("title", title);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Codex_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                if (page == 1) {
                    codex = gson.fromJson(t.toString(), CodexSecondInfo.class);
                    handler.sendEmptyMessage(100);
                    swip.setRefreshing(false);
                } else {
                    codex_down = gson.fromJson(t.toString(),
                            CodexSecondInfo.class);
                    handler.sendEmptyMessage(200);
                    list_codex.onLoadComplete();
                }
                dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                tv_nodata.setVisibility(View.VISIBLE);
                swip.setRefreshing(false);
                list_codex.onLoadComplete();
                // TODO Auto-generated method stub
                ToastUtils.shortToast("网络繁忙，请稍后重试");
                dialog.dismiss();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
//		mPullToRefreshListView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

    void hideSoftKeyboard() {
        if (BookSerchActivity.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (BookSerchActivity.this.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        BookSerchActivity.this.getCurrentFocus()
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


        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        query = (EditText) findViewById(R.id.query);

        type = getIntent().getStringExtra("type");
        if ("3".equals(type)) {
            titleBarBuilder.setTitleText("临床指南检索");
            query.setHint("请输入搜索内容");

        } else if ("0".equals(type)) {
            titleBarBuilder.setTitleText("药典项目检索");
            query.setHint("请输入药品名称");
        } else if ("2".equals(type)) {
            titleBarBuilder.setTitleText("检查项目检索");
            query.setHint("请输入检查名称");
        }

        // mPullToRefreshListView = (PullToRefreshListView)
        // findViewById(R.id.list_codex);
        // mPullToRefreshListView.setPullLoadEnabled(false);
        // mPullToRefreshListView.setScrollLoadEnabled(true);
        // list_codex = new ListView(BookSerchActivity.this);
        // list_codex = mPullToRefreshListView.getRefreshableView();
        list_codex = (MyLoadMoreListView) findViewById(R.id.list_codex);
        list_codex.setLoadMoreListen(this);

        list_codex.setFadingEdgeLength(0);

        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        setLastUpdateTime();

        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_search = (TextView) findViewById(R.id.tv_search);

//		mPullToRefreshListView
//				.setOnRefreshListener(new OnRefreshListener<ListView>()
//				{
//
//					@Override
//					public void onPullDownToRefresh(
//							PullToRefreshBase<ListView> refreshView)
//					{
//						// TODO Auto-generated method stub
//						if (NetworkUtil
//								.isNetWorkConnected(BookSerchActivity.this))
//						{
//							page = 1;
//							getData(page, type, query.getText().toString()
//									.trim());
//							mPullToRefreshListView.onPullDownRefreshComplete();
//						} else
//						{
//							T.shortToast( "网络连接失败");
//						}
//					}
//
//					@Override
//					public void onPullUpToRefresh(
//							PullToRefreshBase<ListView> refreshView)
//					{
//						// TODO Auto-generated method stub
//						if (NetworkUtil
//								.isNetWorkConnected(BookSerchActivity.this))
//						{
//							getData(++page, type, query.getText().toString()
//									.trim());
//							mPullToRefreshListView.onPullUpRefreshComplete();
//						} else
//						{
//							T.shortToast( "网络连接失败");
//						}
//					}
//				});

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.length() > 0) {
                    list_codex.setVisibility(View.VISIBLE);
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
//                    if (codex != null && codex.getList() != null) {
//                        codex.getList().clear();
//                        // adapter.setData(codex.getList());
//                        // adapter.notifyDataSetChanged();
//                        list_codex.setVisibility(View.GONE);
//                        page = 1;
//                    }
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
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
//                if (codex != null & codex.getList() != null) {
//                    codex.getList().clear();
//                    list_codex.setVisibility(View.GONE);
//
//                }

//                hideSoftKeyboard();
            }
        });
        if (tv_cancle != null) {
            tv_cancle.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (NetworkUtil.isNetWorkConnected()) {
                        String str = query.getText().toString().trim();
                        if (!TextUtils.isEmpty(str) && !str.equals("")) {
                            if (str.length() < 40) {
                                if (NetworkUtil.isNetWorkConnected()) {
                                    list_codex.setVisibility(View.VISIBLE);
                                    getData(page, type, str);
                                } else {
                                    ToastUtils.shortToast(
                                            "网络连接失败");
                                }
                            } else {
                                ToastUtils.shortToast(
                                        "搜索内容限制在40个字符以内");
                            }

                        } else {
                            ToastUtils.shortToast("搜索内容不能为空");
                        }
                    } else {
                        ToastUtils.shortToast( "网络连接失败");
                    }

                }
            });
        }
        tv_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isNetWorkConnected()) {
                    String str = query.getText().toString().trim();
                    if (!TextUtils.isEmpty(str) && !str.equals("")) {
                        if (str.length() < 40) {
                            if (NetworkUtil.isNetWorkConnected()) {
                                list_codex.setVisibility(View.VISIBLE);
                                page = 1;
                                getData(page, type, str);
                            } else {
                                ToastUtils.shortToast("网络连接失败");
                            }
                        } else {
                            ToastUtils.shortToast("搜索内容限制在40个字符以内");
                        }
                    } else {
                        ToastUtils.shortToast("搜索内容不能为空");
                    }
                } else {
                    ToastUtils.shortToast( "网络连接失败");
                }
            }
        });
        list_codex.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (type.equals("3")) {
                    Intent intent = new Intent(BookSerchActivity.this,
                            GuideMainFragment.class);
                    // intent.putExtra("url",
                    // codex.getList().get(arg2).getUrl());
                    // intent.putExtra("msg",
                    // codex.getList().get(arg2).getMsg());
                    // intent.putExtra("title",
                    // codex.getList().get(arg2).getTitle());
                    // intent.putExtra("iscollection",
                    // codex.getList().get(arg2).getIscollection());
                    // intent.putExtra("channel", "3");

                    intent.putExtra("url", codex.getList().get(arg2).getUrl());
                    intent.putExtra("ids", codex.getList().get(arg2).getId());
                    intent.putExtra("title", codex.getList().get(arg2)
                            .getTitle());
                    intent.putExtra("filesize", codex.getList().get(arg2)
                            .getFilesize());
                    intent.putExtra("fileurl", codex.getList().get(arg2)
                            .getDownloadurl());
                    // intent.putExtra("imageurl",
                    // consult_info.getList().get(arg2).getImage());
                    intent.putExtra("channel", "3");
                    intent.putExtra("iscollection", codex.getList().get(arg2)
                            .getIscollection());
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(BookSerchActivity.this,
                            BookWebActivity.class);
                    intent.putExtra("url", codex.getList().get(arg2).getUrl());
                    intent.putExtra("msg", codex.getList().get(arg2).getMsg());
                    intent.putExtra("title", codex.getList().get(arg2)
                            .getTitle());
                    String str_type = "";
                    if (type.equals("0")) {
                        str_type = 2 + "";
                    } else if (type.equals("2")) {
                        str_type = 1 + "";
                    }
                    intent.putExtra("channel", str_type);
                    intent.putExtra("iscollection", codex.getList().get(arg2)
                            .getIscollection());
                    intent.putExtra("collecid", codex.getList().get(arg2)
                            .getId());
                    startActivity(intent);

                }

            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    public void loadMore() {
        // TODO Auto-generated method stub
        if (NetworkUtil.isNetWorkConnected()) {
            getData(++page, type, query.getText().toString().trim());
        } else {
            ToastUtils.shortToast( "网络连接失败");
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

        if (NetworkUtil.isNetWorkConnected()) {
            page = 1;
            getData(page, type, query.getText().toString().trim());
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
