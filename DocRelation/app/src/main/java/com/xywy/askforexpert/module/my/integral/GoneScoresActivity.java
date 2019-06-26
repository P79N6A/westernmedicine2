package com.xywy.askforexpert.module.my.integral;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.MyScoresInfo;
import com.xywy.askforexpert.module.main.service.que.QuePerActivity;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 过期积分
 *
 * @author 王鹏
 * @2015-10-27上午11:22:41
 */
public class GoneScoresActivity extends Activity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore

{
    private static final String TAG = "GoneScoresActivity";
    SwipeRefreshLayout swip;
    private MyLoadMoreListView list_scores;
    int page;
    BaseMyScoresAdapter adapter;

    private MyScoresInfo scoreinfo;
    private MyScoresInfo scoreinfo_down;
    private TextView tv_myscore, tv_guoqi_title;
    private String userid;
    private LinearLayout no_data, linear_chongzhi;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (scoreinfo != null) {
                        if (scoreinfo.getData().getDetail() == null) {
                            list_scores.setAdapter(null);
                            list_scores.setLoading(false);
                            list_scores.noMoreLayout();
                            return;
                        }
                        if ("0".equals(scoreinfo.getCode())) {
                            initview(scoreinfo.getData().getPoints()
                                    .getGone_score(), scoreinfo.getData()
                                    .getPoints().getEndtime());
                            if (scoreinfo.getData().getDetail().size() == 0) {
                                // List<MyScoresInfo> list = new
                                // ArrayList<MyScoresInfo>();
                                // adapter = new BaseMyScoresAdapter(
                                // MyScoresActivity.this,list);
                                // list_scores.setAdapter(adapter);
                                list_scores.setAdapter(null);
                                list_scores.setLoading(false);
                                list_scores.noMoreLayout();
                                return;
                            }
                            if (scoreinfo.getData().getDetail().size() < 20) {
                                // mPullToRefreshListView.setHasMoreData(false);

                                list_scores.setLoading(true);
                                list_scores.noMoreLayout();
                            } else {
                                // mPullToRefreshListView.setHasMoreData(true);
                                list_scores.setLoading(false);
                            }
                            // updateList(codex.getList());
                            adapter = new BaseMyScoresAdapter(
                                    GoneScoresActivity.this, scoreinfo.getData()
                                    .getDetail());
                            adapter.setData(scoreinfo.getData().getDetail());
                            list_scores.setAdapter(adapter);
                        }
                    }
                    break;
                case 200:
                    if (scoreinfo_down != null
                            && scoreinfo_down.getCode().equals("0")) {
                        scoreinfo.getData().getDetail()
                                .addAll(scoreinfo_down.getData().getDetail());
                        adapter.setData(scoreinfo.getData().getDetail());
                        adapter.notifyDataSetChanged();
                        if (scoreinfo_down.getData().getDetail().size() == 0) {
                            // T.showNoRepeatShort(CodexSecondActivity.this,
                            // "没有更多数据了");
                            page--;
                            list_scores.LoadingMoreText(getResources().getString(
                                    R.string.no_more));
                            list_scores.setLoading(true);
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
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.gone_scores);
        ((TextView) findViewById(R.id.tv_title)).setText("即将过期的积分");
        userid = getIntent().getStringExtra("userid");
        list_scores = (MyLoadMoreListView) findViewById(R.id.list_scores);
        list_scores.setLoadMoreListen(this);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        View layout = LayoutInflater.from(GoneScoresActivity.this).inflate(
                R.layout.gone_scores_head, null);
        tv_myscore = (TextView) layout.findViewById(R.id.tv_gone_count);
        tv_guoqi_title = (TextView) layout.findViewById(R.id.tv_gong_date);
        layout.findViewById(R.id.tv_guizs).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoneScoresActivity.this,
                        QuePerActivity.class);
                intent.putExtra("isfrom", "积分规则");
                startActivity(intent);

            }
        });
        list_scores.addHeaderView(layout, null, false);
        if (!"".equals(userid)) {

            if (NetworkUtil.isNetWorkConnected()) {
                list_scores.setAdapter(null);
                page = 1;
                swip.post(new Runnable() {
                    @Override
                    public void run() {
                        swip.setRefreshing(true);
                        getData(page);

                    }
                });
            } else {
                ToastUtils.shortToast("网络连接失败");
                list_scores.setAdapter(null);
                list_scores.setLoading(false);
                list_scores.noMoreLayout();
            }

        } else {
            DialogUtil.LoginDialog_back(new YMOtherUtils(GoneScoresActivity.this).context);
        }

    }

    public void initview(String total, String gone) { // 即将过期积分2015/7/15过期，请及时消费

        tv_myscore.setText(total);
        if (!"0".equals(total)) {
            tv_guoqi_title.setText("即将过期积分将在" + gone + "过期,请及时消费");
        }
    }

    public void getData(final int page) {
//		String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("command", "detail");
        params.put("pagenum", page + "");
        params.put("pagesize", 20 + "");
        params.put("userid", userid);
        params.put("isold", "1");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        DLog.i(TAG, "积分详情地址" + CommonUrl.ScoresPointUrl
                + params.toString());
        fh.post(CommonUrl.ScoresPointUrl, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                DLog.i(TAG, "积分详情返回" + t.toString());
                Gson gson = new Gson();
                if (page == 1) {
                    scoreinfo = gson.fromJson(t.toString(), MyScoresInfo.class);
                    handler.sendEmptyMessage(100);
                    swip.setRefreshing(false);
                } else {
                    scoreinfo_down = gson.fromJson(t.toString(),
                            MyScoresInfo.class);
                    handler.sendEmptyMessage(200);
                    list_scores.onLoadComplete();
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
                DLog.i(TAG, "积分错误日志" + strMsg);
                if (page == 1) {
                    list_scores.setAdapter(null);
                    list_scores.setLoading(false);
                    list_scores.noMoreLayout();
                }
                swip.setRefreshing(false);
                list_scores.onLoadComplete();
                ToastUtils.shortToast("网络链接超时");
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    public void onClick_back(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                finish();
                break;
//		case R.id.tv_guizs:
//			T.showNoRepeatShort(GoneScoresActivity.this, "点击了");
//			Intent intent = new Intent(GoneScoresActivity.this,
//					QuePerActivity.class);
//			intent.putExtra("isfrom", "积分规则");
//			startActivity(intent);
//			break;
            default:
                break;
        }
    }

    @Override
    public void loadMore() {
        // TODO Auto-generated method stub
        if (NetworkUtil.isNetWorkConnected()) {
            getData(++page);
        } else {
            ToastUtils.shortToast("网络连接失败");
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        if (NetworkUtil.isNetWorkConnected()) {
            page = 1;
            getData(page);
        } else {
            ToastUtils.shortToast("网络连接失败");
            swip.setRefreshing(false);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
        if ("".equals(userid)) {
            userid = YMApplication.getLoginInfo().getData().getPid();
            if (NetworkUtil.isNetWorkConnected()) {
                list_scores.setAdapter(null);
                page = 1;
                swip.post(new Runnable() {
                    @Override
                    public void run() {
                        swip.setRefreshing(true);
                        getData(page);

                    }
                });
            } else {
                ToastUtils.shortToast("网络连接失败");
                list_scores.setAdapter(null);
                list_scores.setLoading(false);
                list_scores.noMoreLayout();
            }

        }

    }

    @Override
    protected void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
