package com.xywy.askforexpert.module.main.service.codex;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.CodexSecondInfo;
import com.xywy.askforexpert.module.main.service.codex.adapter.BaseCodexSecondAdapter;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 药典二级列表药品详情 stone
 *
 * @author 王鹏
 * @2015-5-13下午4:16:36
 */

public class CodexSecondActivity extends YMBaseActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {

    // private ListView list_news_con;
    // private PullToRefreshListView mPullToRefreshListView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    private String id;
    // private ListView list_codex;
    private MyLoadMoreListView list_codex;
    private int page = 1;
    private String type;
    private CodexSecondInfo codex = new CodexSecondInfo();
    private CodexSecondInfo codex_down = new CodexSecondInfo();

    private BaseCodexSecondAdapter adapter;

    SwipeRefreshLayout swip;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:
                    if (codex != null & codex.getCode().equals("0")) {
                        if (codex.getList().size() < 15) {
                            // mPullToRefreshListView.setHasMoreData(false);
                            list_codex.setLoading(true);
                            list_codex.noMoreLayout();
                        } else {
                            // mPullToRefreshListView.setHasMoreData(true);
                            list_codex.setLoading(false);
                        }
                        // updateList(codex.getList());
                        adapter = new BaseCodexSecondAdapter(
                                CodexSecondActivity.this);
                        adapter.setData(codex.getList());
                        list_codex.setAdapter(adapter);

                    }
                    break;
                case 200:
                    if (codex_down != null && codex_down.getCode().equals("0")) {
                        codex.getList().addAll(codex_down.getList());
                        adapter.setData(codex.getList());
                        adapter.notifyDataSetChanged();
                        if (codex_down.getList().size() == 0) {
//						T.showNoRepeatShort(CodexSecondActivity.this, "没有更多数据了");
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


    private void updateList(List<CodexSecondInfo> list) {

    }

    public void getData(final int page, String type) {
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("c", type);
        params.put("a", "list");
        params.put("page", page + "");
        params.put("pagesize", 15 + "");
        if (!YMUserService.isGuest()) {
            String userid = YMApplication.getLoginInfo().getData().getPid();
            params.put("userid", userid);
        }

        params.put("id", id);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Codex_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                if (page == 1) {
                    codex = ResolveJson.R_codexSecond(t.toString());
                    handler.sendEmptyMessage(100);
                    swip.setRefreshing(false);
                } else {
                    codex_down = ResolveJson.R_codexSecond(t.toString());
                    handler.sendEmptyMessage(200);
                    list_codex.onLoadComplete();
                }
                super.onSuccess(t);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                swip.setRefreshing(false);
                list_codex.onLoadComplete();
                ToastUtils.shortToast("网络链接超时");
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        // mPullToRefreshListView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void initView() {
//        hideCommonBaseTitle();


        // mPullToRefreshListView = (PullToRefreshListView)
        // findViewById(R.id.list_codex);
        // mPullToRefreshListView.setPullLoadEnabled(false);
        // mPullToRefreshListView.setScrollLoadEnabled(true);
        // list_codex = new ListView(CodexSecondActivity.this);
        // list_codex = mPullToRefreshListView.getRefreshableView();
        list_codex = (MyLoadMoreListView) findViewById(R.id.list_codex);
        list_codex.setLoadMoreListen(this);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);
        setLastUpdateTime();
        id = getIntent().getStringExtra("ids");
        String title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        titleBarBuilder.setTitleText(title);
        if (NetworkUtil.isNetWorkConnected()) {
            getData(page, type);
        } else {
            ToastUtils.shortToast("网络连接失败");
        }

        // mPullToRefreshListView
        // .setOnRefreshListener(new OnRefreshListener<ListView>()
        // {
        //
        // @Override
        // public void onPullDownToRefresh(
        // PullToRefreshBase<ListView> refreshView)
        // {
        // // TODO Auto-generated method stub
        // page = 1;
        // getData(page, type);
        // mPullToRefreshListView.onPullDownRefreshComplete();
        // }
        //
        // @Override
        // public void onPullUpToRefresh(
        // PullToRefreshBase<ListView> refreshView)
        // {
        // // TODO Auto-generated method stub
        //
        // getData(++page, type);
        // mPullToRefreshListView.onPullUpRefreshComplete();
        // }
        // });

        list_codex.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(CodexSecondActivity.this,
                        BookWebActivity.class);
                intent.putExtra("url", codex.getList().get(arg2).getUrl());
                intent.putExtra("msg", codex.getList().get(arg2).getMsg());
                intent.putExtra("title", codex.getList().get(arg2).getTitle());
                String str_type = "";
                if (type.equals("codex")) {
                    str_type = 2 + "";
                } else if (type.equals("check")) {
                    str_type = 1 + "";
                }
                intent.putExtra("channel", str_type);
                intent.putExtra("iscollection", codex.getList().get(arg2)
                        .getIscollection());
                intent.putExtra("collecid", codex.getList().get(arg2).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

    }

    // viewsetOnPageChangeListener
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
    public void loadMore() {
        if (NetworkUtil.isNetWorkConnected()) {
            getData(++page, type);
        } else {
            ToastUtils.shortToast("网络连接失败");
        }

    }

    @Override
    public void onRefresh() {
        if (NetworkUtil.isNetWorkConnected()) {
            page = 1;
            getData(page, type);
        } else {
            ToastUtils.shortToast("网络连接失败");
        }


    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.codex_second;
    }
}
