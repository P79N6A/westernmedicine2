package com.xywy.askforexpert.module.message.friend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.CreateHXnameInfo;
import com.xywy.askforexpert.model.SerchNewCardInfo;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.friend.adapter.BaseSerchNewFriendAdapter;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 查找好友
 *
 * @author 王鹏
 * @2015-6-1下午7:59:14
 */
public class SerchNewCardHolderActivity extends Activity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {

    String type;
    BaseSerchNewFriendAdapter adapter;
    boolean isFirst = true;
    SwipeRefreshLayout swip;
    private ListView list_news_con;
    //	private PullToRefreshListView mPullToRefreshListView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    //	private ListView list_codex;
    private MyLoadMoreListView list_codex;
    private int page = 1;
    private EditText query;
    private ImageButton clearSearch;
    private SerchNewCardInfo serchinfo = new SerchNewCardInfo();
    private SerchNewCardInfo serchinfo_down;
    private InputMethodManager inputMethodManager;
    private TextView tv_title;
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:
                    if (serchinfo.getCode().equals("0")
                            & serchinfo.getData().size() > 0) {
                        if (serchinfo.getData().size() < 10) {
//						mPullToRefreshListView.setHasMoreData(false);
                            list_codex.setLoading(true);
                            list_codex.noMoreLayout();
                        } else {
//						mPullToRefreshListView.setHasMoreData(true);
                            list_codex.setLoading(false);
                        }
                        no_data.setVisibility(View.GONE);
                        adapter.setData(serchinfo.getData());
                        list_codex.setAdapter(adapter);

                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;
                case 200:
                    if (serchinfo_down.getCode().equals("0")) {
                        serchinfo.getData().addAll(serchinfo_down.getData());
                        adapter.setData(serchinfo.getData());
                        adapter.notifyDataSetChanged();
                        if (serchinfo_down.getData().size() == 0) {
                            page--;
//						T.showNoRepeatShort(SerchNewCardHolderActivity.this,
//								"没有更多数据了");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.serch_new_cardholder);

        type = getIntent().getStringExtra("type");
        tv_title = (TextView) findViewById(R.id.tv_title);

        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("抱歉，暂无数据");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.nofriend);
        if (type.equals("subject")) {
            tv_title.setText("同科室");
        } else if (type.equals("hospital")) {
            tv_title.setText("同医院");
        } else if (type.equals("area")) {
            tv_title.setText("同城医友");
        }
//		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list);
//		mPullToRefreshListView.setPullLoadEnabled(false);
//		mPullToRefreshListView.setScrollLoadEnabled(true);
//		list_codex = new ListView(SerchNewCardHolderActivity.this);
//		list_codex = mPullToRefreshListView.getRefreshableView();
        list_codex = (MyLoadMoreListView) findViewById(R.id.list);
        list_codex.setLoadMoreListen(this);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);
        setLastUpdateTime();
        if (NetworkUtil.isNetWorkConnected()) {
            getData(page, type);
        } else {
            ToastUtils.shortToast("网络连接失败");
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");
        }

        adapter = new BaseSerchNewFriendAdapter(
                SerchNewCardHolderActivity.this, 1);
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
//								.isNetWorkConnected(SerchNewCardHolderActivity.this))
//						{
//							page = 1;
//							getData(page, type);
//							mPullToRefreshListView.onPullDownRefreshComplete();
//						} else
//						{
//							T.showNoRepeatShort(
//									SerchNewCardHolderActivity.this, "网络连接失败");
//
//						}
//					}
//
//					@Override
//					public void onPullUpToRefresh(
//							PullToRefreshBase<ListView> refreshView)
//					{
//						// TODO Auto-generated method stub
//						if (NetworkUtil
//								.isNetWorkConnected(SerchNewCardHolderActivity.this))
//						{
//							getData(++page, type);
//							mPullToRefreshListView.onPullUpRefreshComplete();
//						} else
//						{
//							T.showNoRepeatShort(
//									SerchNewCardHolderActivity.this, "网络连接失败");
//
//						}
//					}
//				});

        // 搜索框
        query = (EditText) findViewById(R.id.query);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // 搜索框中清除button
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
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

            }
        });
        list_codex.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (serchinfo.getData().get(arg2).getHx_username().equals("")) {
                    getData(serchinfo.getData().get(arg2).getPid());
                }
                Intent intent = new Intent(SerchNewCardHolderActivity.this,
                        PersonDetailActivity.class);
                intent.putExtra("uuid", serchinfo.getData().get(arg2).getPid());
                intent.putExtra("isDoctor", 1 + "");
                startActivity(intent);

            }
        });
    }

    public void getData(String id) {

        String bind = id;
        Long st = System.currentTimeMillis();
        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("m", "addDocUser");
        params.put("a", "chat");
        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("id", id);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        CreateHXnameInfo hxinfo;
                        Gson gson = new Gson();
                        hxinfo = gson.fromJson(t.toString(),
                                CreateHXnameInfo.class);
                        if (hxinfo != null) {
                            if ("0".equals(hxinfo.getCode())) {
//								Intent intent = new Intent(context,
//										AddCardHoldVerifyActiviy.class);
//								intent.putExtra("toAddUsername", hxinfo.getData().getUsername());
//								context.startActivity(intent);
                            }
                        }


                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    void hideSoftKeyboard() {
        if (SerchNewCardHolderActivity.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (SerchNewCardHolderActivity.this.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        SerchNewCardHolderActivity.this.getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void getData(final int page, String type) {
        final ProgressDialog dialog = new ProgressDialog(this, "正在搜索中...");
        if (isFirst) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.showProgersssDialog();
        }

        String id = YMApplication.getLoginInfo().getData().getPid();
        String bind = id;
        Long st = System.currentTimeMillis();
        String sign = MD5Util.MD5(page + "10" + st + bind
                + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("m", "findfriend");
        params.put("a", "chat");
        params.put("page", page + "");
        params.put("pagesize", 10 + "");
        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("type", type);
        params.put("id", id);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        Gson gson = new Gson();
                        if (page == 1) {
                            serchinfo = gson.fromJson(t.toString(),
                                    SerchNewCardInfo.class);

                            handler.sendEmptyMessage(100);
                            swip.setRefreshing(false);

                        } else {
                            serchinfo_down = gson.fromJson(t.toString(),
                                    SerchNewCardInfo.class);
                            handler.sendEmptyMessage(200);
                            list_codex.onLoadComplete();
                        }
                        if (isFirst) {
                            dialog.dismiss();
                            isFirst = false;
                        }

                        super.onSuccess(t);
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        // TODO Auto-generated method stub
                        super.onLoading(count, current);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        dialog.dismiss();
                        swip.setRefreshing(false);
                        list_codex.onLoadComplete();
                        ToastUtils.shortToast("网络繁忙，请稍后重试");
                        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        if (NetworkUtil
                .isNetWorkConnected()) {
            getData(++page, type);
//			mPullToRefreshListView.onPullUpRefreshComplete();
        } else {
            ToastUtils.shortToast(
                    "网络连接失败");

        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        if (NetworkUtil
                .isNetWorkConnected()) {
            page = 1;
            getData(page, type);
//			mPullToRefreshListView.onPullDownRefreshComplete();
        } else {
            ToastUtils.shortToast(
                    "网络连接失败");

        }
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
