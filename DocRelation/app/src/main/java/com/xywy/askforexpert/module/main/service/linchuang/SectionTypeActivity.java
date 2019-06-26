package com.xywy.askforexpert.module.main.service.linchuang;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.SectionSoftInfo;
import com.xywy.askforexpert.module.main.service.codex.SectionTyoeAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;

/**
 * 科室分类 stone
 *
 * @author 王鹏
 * @2015-6-22上午11:38:05
 */
public class SectionTypeActivity extends YMBaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "SectionTypeActivity";
    private ExpandableListView expandlist;
    // private ListView mPullToRefreshListView;
    private SectionTyoeAdapter adapter;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private SectionSoftInfo sectioninfo;

    SwipeRefreshLayout swip;

    private Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (sectioninfo != null
                            & sectioninfo.getGroup_list().size() > 0) {
                        adapter = new SectionTyoeAdapter(SectionTypeActivity.this);
                        adapter.setData(sectioninfo.getGroup_list());
                        expandlist.setAdapter(adapter);
                    }
                    break;

                default:
                    break;
            }
        }
    };


    /**
     * 获取网络数据
     */
    public void getData() {
        AjaxParams params = new AjaxParams();
        String sign = MD5Util.MD5(3 + Constants.MD5_KEY);
        params.put("c", "guide");
        params.put("a", "offices");
        params.put("channel", 3 + "");
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Codex_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                DLog.i(TAG, "json==" + t.toString());
                sectioninfo = ResolveJson.R_section_soft(t.toString());
                hander.sendEmptyMessage(100);
                swip.setRefreshing(false);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

//    public void onClick_back(View view) {
//        switch (view.getId()) {
//            case R.id.btn1:
//                finish();
//                break;
//
//            default:
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.section_type;
    }

    @Override
    public void onRefresh() {
        if (NetworkUtil.isNetWorkConnected()) {

            getData();

        } else {
            ToastUtils.shortToast("网络连接失败");
        }
    }

    @Override
    protected void initView() {


        titleBarBuilder.setTitleText("科室分类");

//        ((TextView) findViewById(R.id.tv_title)).setText("科室分类");
        expandlist = (ExpandableListView) findViewById(R.id.expand_list);

//		expandlist = new ExpandableListView(SectionTypeActivity.this);
        expandlist.setGroupIndicator(null);
        //
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        // mPullToRefreshListView.setPullLoadEnabled(false);
        // mPullToRefreshListView.setScrollLoadEnabled(true);

        // expandlist = mPullToRefreshListView.getRefreshableView();
        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast("网络连接失败");
        }

        // mPullToRefreshListView
        // .setOnRefreshListener(new OnRefreshListener<ExpandableListView>()
        // {
        //
        // @Override
        // public void onPullDownToRefresh(
        // PullToRefreshBase<ExpandableListView> refreshView)
        // {
        // // TODO Auto-generated method stub
        // if (NetworkUtil
        // .isNetWorkConnected(SectionTypeActivity.this))
        // {
        //
        // getData();
        // mPullToRefreshListView.onPullDownRefreshComplete();
        // } else
        // {
        // T.shortToast("网络连接失败");
        // }
        // }
        //
        // @Override
        // public void onPullUpToRefresh(
        // PullToRefreshBase<ExpandableListView> refreshView)
        // {
        // // TODO Auto-generated method stub
        // }
        // });
        expandlist.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        int arg2, int arg3, long arg4) {

                Intent intent = new Intent(SectionTypeActivity.this,
                        GuideActivity.class);

                intent.putExtra("type", sectioninfo.getGroup_list().get(arg2)
                        .getChild_list().get(arg3).getId());
                startActivity(intent);
                finish();
                return true;
            }
        });
        expandlist.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView arg0, View arg1,
                                        int arg2, long arg3) {

                if (sectioninfo != null && sectioninfo.getGroup_list().size() > 0) {
                    if (sectioninfo.getGroup_list().get(arg2).getChild_list() != null && sectioninfo.getGroup_list().get(arg2).getChild_list().size() > 0) {
                        return false;
                    } else {
                        Intent intent = new Intent(SectionTypeActivity.this,
                                GuideActivity.class);

                        intent.putExtra("type",
                                sectioninfo.getGroup_list().get(arg2).getId());
                        startActivity(intent);
                        finish();
                        return true;
                    }
                } else {
                    return false;
                }

//                if (arg2 == 0) {
//
//                } else {
//
//                }

            }
        });
    }

    @Override
    protected void initData() {

    }
}
