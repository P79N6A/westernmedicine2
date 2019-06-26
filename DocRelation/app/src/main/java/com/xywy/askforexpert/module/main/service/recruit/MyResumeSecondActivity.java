package com.xywy.askforexpert.module.main.service.recruit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.RecrutiCenterInfo;
import com.xywy.askforexpert.module.doctorcircle.adapter.SwipListViewAdapter.OnItemDeleteClickListener;
import com.xywy.askforexpert.module.main.service.recruit.adapter.BaseRecruitResumeAdapter;
import com.xywy.askforexpert.module.message.msgchat.GlobalContent;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 收藏的职位
 *
 * @author 王鹏
 * @2015-5-13下午4:16:36
 */

public class MyResumeSecondActivity extends Activity implements
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
    private RecrutiCenterInfo recrut_info = new RecrutiCenterInfo();
    private RecrutiCenterInfo recrut_down = new RecrutiCenterInfo();
    Map<String, String> map;
    private BaseRecruitResumeAdapter adapter;
    private TextView tv_total;
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;

    private SwipeRefreshLayout swip;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:
                    if (recrut_info != null & recrut_info.getCode().equals("0")
                            & recrut_info.getList_first().getList().size() > 0) {
                        // updateList(codex.getList());
                        no_data.setVisibility(View.GONE);
                        adapter = new BaseRecruitResumeAdapter(
                                MyResumeSecondActivity.this, listener);
                        adapter.setData(recrut_info.getList_first().getList());
                        list_codex.setAdapter(adapter);
                        if (recrut_info.getList_first().getList().size() < 10) {
//						mPullToRefreshListView.setHasMoreData(false);
//						mPullToRefreshListView.getFooterLoadingLayout().show(
//								false);
                            list_codex.setLoading(true);
                            list_codex.noMoreLayout();
                        } else {
//						mPullToRefreshListView.setHasMoreData(true);
                            list_codex.setLoading(false);
                        }
                        if (recrut_info.getList_first().getList().size() > 0) {
                            ((TextView) findViewById(R.id.tv_total))
                                    .setText(recrut_info.getList_first().getTotal());
                            tv_total.setText("共"
                                    + recrut_info.getList_first().getCount()
                                    + "个职位");

                        }

                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;
                case 200:
                    if (recrut_info != null & recrut_down != null
                            & "0".equals(recrut_down.getCode())) {
                        recrut_info.getList_first().getList()
                                .addAll(recrut_down.getList_first().getList());
                        adapter.setData(recrut_info.getList_first().getList());
                        adapter.notifyDataSetChanged();
                        if (recrut_down.getList_first().getList().size() == 0) {
//						T.shortToast( "没有更多数据了");
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
    private OnItemDeleteClickListener listener = new OnItemDeleteClickListener() {

        @Override
        public void onRightClick(View v, int position) {
            delete(recrut_info.getList_first().getList().get(position).getId(),
                    position);
            adapter.initView(GlobalContent.viewMap.get(recrut_info
                    .getList_first().getList().get(position).getId()));
            recrut_info.getList_first().getList().remove(position);

            adapter.setData(recrut_info.getList_first().getList());
            adapter.notifyDataSetChanged();

            if (recrut_info.getList_first().getList().size() > 0) {
                int items = Integer.valueOf(recrut_info.getList_first()
                        .getCount());
                recrut_info.getList_first().setCount(items - 1 + "");

                ((TextView) findViewById(R.id.tv_total)).setText(recrut_info
                        .getList_first().getTotal());
                tv_total.setText("共" + recrut_info.getList_first().getCount()
                        + "个职位");

            } else {
                no_data.setVisibility(View.VISIBLE);
            }
            if (recrut_info.getList_first().getList().size() < 10) {
//				mPullToRefreshListView.setHasMoreData(false);
//				mPullToRefreshListView.getFooterLoadingLayout().show(false);
                list_codex.setLoading(true);
                list_codex.noMoreLayout();
            } else {
//				mPullToRefreshListView.setHasMoreData(true);
                list_codex.setLoading(false);
            }
        }
    };

    public void delete(String id, final int position) {
        AjaxParams params = new AjaxParams();
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);
        params.put("id", id);
        params.put("uid", uid);
        params.put("type", "del");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Recruit_Coll_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                // TODO Auto-generated method stub
                map = ResolveJson.R_Action(t.toString());
                if (map.get("code").equals("0")) {
                    // recrut_info.getList_first().getList().remove(position);
                    // adapter.setData(recrut_info.getList_first().getList());
                    // adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.shortToast(
                            map.get("msg"));
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.myresume_second);

        findViewById(R.id.btn2).setVisibility(View.GONE);
        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无收藏的职位");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.service_more_none);

        // mPullToRefreshListView = (PullToRefreshListView)
        // findViewById(R.id.list_resume);
        // mPullToRefreshListView.setPullLoadEnabled(true);
        // mPullToRefreshListView.setScrollLoadEnabled(true);
        // list_codex = new ListView(MyResumeSecondActivity.this);
        // list_codex = mPullToRefreshListView.getRefreshableView();

        list_codex = (MyLoadMoreListView) findViewById(R.id.list_resume);
        list_codex.setLoadMoreListen(this);

        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        list_codex.setFadingEdgeLength(0);
        tv_total = (TextView) findViewById(R.id.tv_total);
        setLastUpdateTime();
        String title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        ((TextView) findViewById(R.id.tv_title)).setText(title);
        if (NetworkUtil.isNetWorkConnected()) {
            getData(page, type);
        } else {
            ToastUtils.shortToast("网络连接失败");
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");
        }

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
//								.isNetWorkConnected(MyResumeSecondActivity.this))
//						{
//							page = 1;
//							getData(page, type);
//							mPullToRefreshListView.onPullDownRefreshComplete();
//						} else
//						{
//							T.shortToast( "网络连接失败");
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
//								.isNetWorkConnected(MyResumeSecondActivity.this))
//						{
//							++page;
//							getData(page, type);
//							mPullToRefreshListView.onPullUpRefreshComplete();
//						} else
//						{
//							T.shortToast( "网络连接失败");
//						}
//
//					}
//				});

        list_codex.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent(MyResumeSecondActivity.this,
                        RecruitWebMianActivity.class);

                intent.putExtra("url", recrut_info.getList_first().getList()
                        .get(arg2).getUrl());
                intent.putExtra("id", recrut_info.getList_first().getList()
                        .get(arg2).getId());
                startActivity(intent);
                //
                // Intent intent = new Intent(MyResumeSecondActivity.this,
                // BookWebActivity.class);
                // intent.putExtra("url", recrut_info.getList_first().getList()
                // .get(arg2).getUrl());
                // intent.putExtra("msg", "");
                // intent.putExtra("title",
                // recrut_info.getList_first().getList()
                // .get(arg2).getTitle());
                // startActivity(intent);
            }
        });

        list_codex.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {
                // TODO Auto-generated method stub
                final android.support.v7.app.AlertDialog.Builder dialogs = new android.support.v7.app.AlertDialog.Builder(
                        MyResumeSecondActivity.this);
                dialogs.setTitle("删除职位收藏");

                dialogs.setMessage("是否确定删除？");

                dialogs.setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(recrut_info.getList_first().getList()
                                .get(position).getId(), position);
                        adapter.initView(GlobalContent.viewMap.get(recrut_info
                                .getList_first().getList().get(position)
                                .getId()));
                        recrut_info.getList_first().getList().remove(position);

                        adapter.setData(recrut_info.getList_first().getList());
                        adapter.notifyDataSetChanged();

                        if (recrut_info.getList_first().getList().size() > 0) {
                            int items = Integer.valueOf(recrut_info
                                    .getList_first().getCount());
                            recrut_info.getList_first()
                                    .setCount(items - 1 + "");

                            ((TextView) findViewById(R.id.tv_total))
                                    .setText(recrut_info.getList_first()
                                            .getTotal());
                            tv_total.setText("共"
                                    + recrut_info.getList_first().getCount()
                                    + "个职位");

                        } else {
                            no_data.setVisibility(View.VISIBLE);
                        }
                        if (recrut_info.getList_first().getList().size() < 10) {
//							mPullToRefreshListView.setHasMoreData(false);
//							mPullToRefreshListView.getFooterLoadingLayout()
//									.show(false);
                            list_codex.setLoading(true);
                            list_codex.noMoreLayout();
                        } else {
//							mPullToRefreshListView.setHasMoreData(true);
                            list_codex.setLoading(false);
                        }

                        dialog.dismiss();
                    }
                });
                dialogs.setNegativeButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                dialogs.create().show();

                // dialog = new DialogWindow(MyResumeSecondActivity.this,
                // "删除职位收藏", "是否确定删除？", "取消", "确定");
                // dialog.createDialogView(new OnClickListener()
                // {
                //
                // @Override
                // public void onClick(View arg0)
                // {
                // switch (arg0.getId())
                // {
                // case R.id.rl_channel:
                // dialog.channelDialog();
                // break;
                //
                // case R.id.rl_enter:
                //
                // delete(recrut_info.getList_first().getList().get(position).getId(),
                // position);
                // adapter.initView(GlobalContent.viewMap.get(recrut_info
                // .getList_first().getList().get(position).getId()));
                // recrut_info.getList_first().getList().remove(position);
                //
                // adapter.setData(recrut_info.getList_first().getList());
                // adapter.notifyDataSetChanged();
                //
                // if (recrut_info.getList_first().getList().size() > 0)
                // {
                // int items = Integer.valueOf(
                // recrut_info.getList_first().getCount());
                // recrut_info.getList_first().setCount(items-1+"");
                //
                // ((TextView) findViewById(R.id.tv_total)).setText(recrut_info
                // .getList_first().getTotal());
                // tv_total.setText("共" + recrut_info.getList_first().getCount()
                // + "个职位");
                //
                // } else
                // {
                // no_data.setVisibility(View.VISIBLE);
                // }
                // if (recrut_info.getList_first().getList().size() < 10)
                // {
                // mPullToRefreshListView.setHasMoreData(false);
                // mPullToRefreshListView.getFooterLoadingLayout().show(false);
                // } else
                // {
                // mPullToRefreshListView.setHasMoreData(true);
                // }
                // dialog.channelDialog();
                // break;
                // }
                // }
                // });

                return true;
            }
        });
    }

    public void getData(final int page, String type) {
//		final ProgressDialog dialog = new ProgressDialog(
//				MyResumeSecondActivity.this, "正在加载中...");
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.showProgersssDialog();

        String id = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("uid", id);
        params.put("page", page + "");
        params.put("pagesize", 10 + "");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(type, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                if (page == 1) {
                    recrut_info = ResolveJson.R_Mysume(t.toString());
                    if (recrut_info != null) {
                        handler.sendEmptyMessage(100);
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }


                    swip.setRefreshing(false);
                } else {
                    recrut_down = ResolveJson.R_Mysume(t.toString());
                    if (recrut_down != null) {
                        handler.sendEmptyMessage(200);
                    }

                    list_codex.onLoadComplete();
                }
//				dialog.dismiss();
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
//				dialog.dismiss();
                swip.setRefreshing(false);
                list_codex.onLoadComplete();
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
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(MyResumeSecondActivity.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(MyResumeSecondActivity.this);
        super.onPause();
    }

    @Override
    public void loadMore() {
        // TODO Auto-generated method stub
        if (NetworkUtil
                .isNetWorkConnected()) {
            ++page;
            getData(page, type);
        } else {
            ToastUtils.shortToast( "网络连接失败");
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        if (NetworkUtil
                .isNetWorkConnected()) {
            page = 1;
            getData(page, type);
        } else {
            ToastUtils.shortToast( "网络连接失败");

        }
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
