package com.xywy.askforexpert.module.my.collection;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.ItemList;
import com.xywy.askforexpert.model.MyConlecListBean;
import com.xywy.askforexpert.module.main.service.codex.BookWebActivity;
import com.xywy.askforexpert.module.main.service.document.PhysicLiteratureDetaileActivty;
import com.xywy.askforexpert.module.main.service.linchuang.fragment.GuideMainFragment;
import com.xywy.askforexpert.module.main.service.media.InfoDetailActivity;
import com.xywy.askforexpert.module.my.adapter.MyConlectListAdapter;
import com.xywy.base.view.CircleProgressBar;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 我的收藏列表
 *
 * @author LG
 */
public class MyConlectListActivity extends Activity implements OnClickListener {

    private static final String TAG = "MyConlectListActivity";
    private ImageView iv_back;
    private Activity context;
    private TextView tv_more_manager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String channel;
    private String uuid;
    private int page = 1;
    private int onclicItem = 0;
    private String collecid;
    private MyConlectListAdapter myConlectListAdapter;
    private List<ItemList> list;
    private boolean isdelteAll = false;
    private boolean isloading = true;
    private TextView tv_cancell;
    private StringBuffer sb = new StringBuffer();
    private ProgressDialog pro;
    private String nameTitle;
    private ListView mlistview;
    private View view;
    private CircleProgressBar pb;
    private TextView tv_more;
    private View lin_nodata;
    private TextView tv_nodata_title;
    private ImageView img_nodate;
    private MyConlecListBean fromJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        view = View.inflate(this, R.layout.loading_more, null);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.activity_conlect_list);
        channel = getIntent().getStringExtra("channel");
        nameTitle = getIntent().getStringExtra("name");
        pro = new ProgressDialog(this, getString(R.string.loadig_more));
        pro.showProgersssDialog();
        uuid = YMApplication.getLoginInfo().getData().getPid();
        initView();
        setLisenr();

    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_type)).setText(nameTitle);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_cancell = (TextView) findViewById(R.id.tv_cancell);
        tv_more_manager = (TextView) findViewById(R.id.tv_more_manager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);

        lin_nodata = findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        img_nodate = (ImageView) findViewById(R.id.img_nodate);

        mlistview = (ListView) findViewById(R.id.id_listview);
        mlistview.addFooterView(view);
        view.setVisibility(View.INVISIBLE);
        mlistview.setDividerHeight(0);
        pb = (CircleProgressBar) view.findViewById(R.id.pb_more);
        tv_more = (TextView) view.findViewById(R.id.tv_more);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_scheme_2_1, R.color.color_scheme_2_2, R.color.color_scheme_2_3, R.color.color_scheme_2_4);
    }

    private void iniDada() {
        FinalHttp fh = new FinalHttp();
        AjaxParams params = new AjaxParams();
        String url = "";
        String sign = MD5Util.MD5(uuid + Constants.MD5_KEY);
        if ("100".equals(channel)) { //医学文献
            url = CommonUrl.wenxian;
            params.put("uid", uuid);
            params.put("bind", uuid);
            params.put("a", "literature");
            params.put("m", "zpClist");
        } else {
            params.put("c", "collection");
            params.put("a", "list");
            if ("-1".equals(channel)) {
                url = CommonUrl.zixun;
            } else {
                url = CommonUrl.connetUrl;
                params.put("channel", channel);
            }
            params.put("userid", uuid);
        }
        params.put("page", page + "");
        params.put("pagesize", "20");
        params.put("sign", sign);//http://yimai.api.xywy.com/app/1.1/shouce/index.interface.php
        fh.post(url, params, new AjaxCallBack() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();
                }

                view.setVisibility(View.GONE);
                isloading = true;
                ToastUtils.shortToast( "连接网络超时");
                mSwipeRefreshLayout.setRefreshing(false);
                System.out.println("----" + strMsg);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();
                }
                boolean jsonData = YMOtherUtils.isJsonData(t.toString());
                if (!jsonData) {
                    noConnect();
                    return;
                }

                isloading = true;
                mSwipeRefreshLayout.setRefreshing(false);
                getDataSusess(t.toString());
            }
        });
    }

    private void getDataSusess(String t) {
        Gson mGson = new Gson();
        fromJson = mGson.fromJson(t, MyConlecListBean.class);
        if ("0".equals(fromJson.code)) {
            if (fromJson == null || fromJson.list == null || fromJson.list.size() == 0) {
                if (1 == page) {
                    noConnect();
                }
                isloading = false;
                pb.setVisibility(View.GONE);
                tv_more.setText("没有数据了");//
                return;
            }
            list = fromJson.list;
            if (1 == page) {
                if (list.size() == 0) {
                    noConnect();
                } else {
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    lin_nodata.setVisibility(View.GONE);
                }

                if (list.size() < 20 && list.size() > 10) {
                    isloading = false;
                    pb.setVisibility(View.GONE);
                    tv_more.setText("没有数据了");//

                } else if (list.size() <= 10) {
                    isloading = false;
                    view.setVisibility(View.GONE);
                } else if (list.size() >= 20) {
                    isloading = true;
                    view.setVisibility(View.VISIBLE);
                }
                myConlectListAdapter = null;
            } else {
                if (list.size() == 0) {
                    isloading = false;
                    pb.setVisibility(View.GONE);
                    tv_more.setText("没有数据了");//"没有数据了"
                    page--;
                }
            }
            if (myConlectListAdapter == null) {
                sb.delete(0, sb.length());
                isdelteAll = false;
                tv_more_manager.setText("批量操作");
                tv_cancell.setVisibility(View.INVISIBLE);
                iv_back.setVisibility(View.VISIBLE);
                myConlectListAdapter = new MyConlectListAdapter(this, list);
                mlistview.setAdapter(myConlectListAdapter);
            } else {
                myConlectListAdapter.addData(list);
            }
        }
    }

    /**
     * "暂无收藏"
     */
    private void noConnect() {
        mSwipeRefreshLayout.setVisibility(View.GONE);
        lin_nodata.setVisibility(View.VISIBLE);
        tv_nodata_title.setText("暂无收藏");
        img_nodate.setBackgroundResource(R.drawable.service_more_none);
    }

    private void setLisenr() {
        tv_cancell.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_more_manager.setOnClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                tv_more.setText("正在加载...");//
                isloading = true;
                if (isloading) {
                    isloading = false;
                    page = 1;
                    iniDada();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mlistview.setOnScrollListener(new OnScrollListener() {


            private int lastItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

                if (lastItem == myConlectListAdapter.getCount() && arg1 == OnScrollListener.SCROLL_STATE_IDLE) {

                    if (isloading) {
                        isloading = false;
                        if (myConlectListAdapter.getCount() > 19) {
                            if (!NetworkUtil.isNetWorkConnected()) {
                                view.setVisibility(View.GONE);
                                ToastUtils.shortToast( "网络异常，请检查网络连接");
                                return;
                            }

                            view.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.VISIBLE);
                            tv_more.setText("正在加载中...");//
                            page++;
                            iniDada();
                        }

                    }
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;

            }
        });
        mlistview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0,
                                           View arg1, int arg2, long arg3) {
                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast( "网络连接异常,请检查网络连接");//
                    return true;
                }
                ItemList itemList = (ItemList) arg0.getAdapter()
                        .getItem(arg2);
                if ("100".equals(channel)) { //医学文献
                    collecid = itemList.lid;

                } else {

                    collecid = itemList.id;
                }
                onclicItem = arg2;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("删除该收藏");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        deleteConlect("");


                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        mlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast( "网络连接异常,请检查网络连接");
                    return;
                }
                if (tv_cancell.getVisibility() == View.VISIBLE) {
                    ((ItemList) arg0.getAdapter().getItem(arg2)).isSeclect = !((ItemList) arg0
                            .getAdapter().getItem(arg2)).isSeclect;
                    myConlectListAdapter.notifyDataSetChanged();
                } else {
                    if (arg2 == (arg0.getAdapter().getCount() - 1)) {
                        return;
                    }
                    // 1是检查手册 ，2药典，3是指南 -1 资讯
                    ItemList codex = (ItemList) arg0.getAdapter().getItem(arg2);
                    switch (Integer.parseInt(channel)) {
                        case 1://CodexSecondActivity
                        case 2:
                            Intent intent = new Intent(context, BookWebActivity.class);
                            intent.putExtra("url", codex.url);
                            intent.putExtra("msg", codex.msg_iscollection);
                            intent.putExtra("title", codex.title);
                            intent.putExtra("channel", channel);
                            intent.putExtra("iscollection", codex.iscollection);
                            intent.putExtra("collecid", codex.id);
                            startActivity(intent);
                            break;
                        case 3:
                            Intent yaodian = new Intent(context, GuideMainFragment.class);
                            yaodian.putExtra("url", codex.url);
                            yaodian.putExtra("ids", codex.id);
                            yaodian.putExtra("title", codex.title);
                            yaodian.putExtra("filesize", codex.filesize);
                            yaodian.putExtra("fileurl", codex.downloadurl);
                            yaodian.putExtra("channel", "3");
                            yaodian.putExtra("iscollection", codex.iscollection);
                            startActivity(yaodian);
                            break;
                        case -1:
                            Intent zixun = new Intent(context, InfoDetailActivity.class);
                            zixun.putExtra("url", codex.url);
                            zixun.putExtra("ids", codex.id);
                            zixun.putExtra("title", codex.title);
                            zixun.putExtra("imageurl", codex.image);//分享图片
                            startActivity(zixun);
                            break;
                        case 100:
                            String articleID = myConlectListAdapter.getList().get(arg2).lid;
                            String DBID = myConlectListAdapter.getList().get(arg2).DBID;
                            Intent intentDoc = new Intent(context, PhysicLiteratureDetaileActivty.class);
                            intentDoc.putExtra("ArticleID", articleID);
                            intentDoc.putExtra("DBID", DBID);
                            startActivity(intentDoc);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.tv_more_manager:
                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast( "网络连接异常,请检查网络连接");
                    return;
                }
                if (myConlectListAdapter == null || myConlectListAdapter.getCount() == 0) {
                    return;
                }
                isdelteAll = !isdelteAll;
                if (isdelteAll) {
                    tv_more_manager.setText("删除");
                    tv_cancell.setVisibility(View.VISIBLE);
                    iv_back.setVisibility(View.INVISIBLE);
                } else {
                    list = myConlectListAdapter.getList();
                    sb.delete(0, sb.length());
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSeclect) {
                            if ("100".equals(channel)) { //医学文献
                                sb.append(list.get(i).lid).append(",");
                            } else {
                                sb.append(list.get(i).id).append(",");
                            }
                        }
                    }
                    if (TextUtils.isEmpty(sb.toString())) {
                        ToastUtils.shortToast( "删除内容为空");
                        isdelteAll = !isdelteAll;
                        return;
                    }
                    DLog.i(TAG, sb.toString().substring(0, sb.length() - 1) + "-------->>>>" + sb.toString());
                    deleteMoreData(sb.toString().substring(0, sb.length() - 1));
                    tv_more_manager.setText("批量操作");
                    tv_cancell.setVisibility(View.INVISIBLE);
                    iv_back.setVisibility(View.VISIBLE);
                }
                if (myConlectListAdapter != null) {
                    myConlectListAdapter.setAlldelte(isdelteAll);
                }
                break;
            case R.id.tv_cancell:
                sb.delete(0, sb.length());
                isdelteAll = !isdelteAll;
                tv_more_manager.setText("批量操作");
                tv_cancell.setVisibility(View.INVISIBLE);
                iv_back.setVisibility(View.VISIBLE);
                if (myConlectListAdapter != null) {
                    myConlectListAdapter.setAlldelte(isdelteAll);
                }
                break;
        }
    }

    private void deleteMoreData(String sb) {
        deleteConlect(sb);
    }

    /**
     * 删除收藏
     */
    private void deleteConlect(final String sb) {
        if (pro != null) {
            pro.showProgersssDialog();
        }
        FinalHttp fh = new FinalHttp();
        AjaxParams params = new AjaxParams();
        String sign = MD5Util.MD5(uuid + Constants.MD5_KEY);
        String url = "";
        if ("100".equals(channel)) { //医学文献
            if (TextUtils.isEmpty(sb)) {// 单个删除
                params.put("lid", collecid);
                params.put("type", "del");
            } else {//批量
                params.put("type", "all");
                params.put("lid", sb);
            }
            url = CommonUrl.wenxian;
            params.put("uid", uuid);
            params.put("bind", uuid);
            params.put("a", "literature");
            params.put("m", "zpDelshoucang");
        } else {
            if (TextUtils.isEmpty(sb)) {// 单个删除
                params.put("collecid", collecid);
            } else {//批量
                params.put("collecid", sb);
            }
            params.put("c", "collection");
            params.put("userid", uuid);
            if (!"-1".equals(channel)) {
                params.put("channel", channel);
                params.put("a", "delete");
                url = CommonUrl.connetUrl;
            } else {
                url = CommonUrl.zixun;
                params.put("a", "actionDel");
            }
        }
        params.put("sign", sign);
        fh.post(url, params, new AjaxCallBack() {
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtils.shortToast( "删除失败");
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();
                }
            }

            @Override
            public void onSuccess(String t) {
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();
                }
                super.onSuccess(t);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(t.toString());{
                        if ("0".equals(jsonObject.getString("code"))) {
                            if (TextUtils.isEmpty(sb)) {
                                list.remove(onclicItem);
                                myConlectListAdapter.deleteItem(list);
                            } else {
                                String[] split = sb.split(",");
                                for (int i = 0; i < list.size(); i++) {
                                    for (int j = 0; j < split.length; j++) {
                                        if ("100".equals(channel)) { //医学文献
                                            if (list.get(i).lid.equals(split.clone()[j])) {
                                                list.remove(i);
                                            }
                                        } else {
                                            if (list.get(i).id.equals(split.clone()[j])) {
                                                list.remove(i);
                                            }
                                        }
                                    }
                                }
                            }
                            myConlectListAdapter.notifyDataSetChanged();
                            ToastUtils.shortToast( "删除成功");
                            if (myConlectListAdapter.getCount() == 0) {
                                noConnect();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniDada();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (pro != null && pro.isShowing()) {
            pro.closeProgersssDialog();
        }
        super.onDestroy();
    }
}
