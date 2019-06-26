package com.xywy.askforexpert.module.main.service.linchuang;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.CommentFistInfo;
import com.xywy.askforexpert.module.main.service.linchuang.adapter.BaseCommentFistAdapter;
import com.xywy.askforexpert.module.main.service.linchuang.utils.HtmlRegexpUtil;
import com.xywy.askforexpert.widget.PasteEditText;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 临床指南评论 列表 页面 stone
 *
 * @author 王鹏
 * @2015-5-11下午8:06:47
 */
public class CommentInfoActivity extends YMBaseActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {

    // private PullToRefreshListView mPullToRefreshListView;
    private SwipeRefreshLayout swip;
    private MyLoadMoreListView comm_list;
    private CommentFistInfo comminfo;
    private CommentFistInfo comminfo_down;
    public int page = 1;
    private String id = 35511 + "";
    private int position;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private InputMethodManager manager;
    private TextView id_allcommnum;
    private BaseCommentFistAdapter adapter;

    private LinearLayout rl_bottom;
    public static PasteEditText et_sendmmot;
    private Map<String, String> map;
    private RelativeLayout main;
    public static boolean isTiez = true;
    public String type;
    private String URL;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 200:
                    if (comminfo != null && comminfo.getCode().equals("0")) {
                        // updateList(comminfo.getList(), position);
                        // adapter.notifyDataSetChanged();
                        adapter = new BaseCommentFistAdapter(
                                CommentInfoActivity.this, type);
                        adapter.setData(comminfo.getList());
                        comm_list.setAdapter(adapter);
                        id_allcommnum.setText("共有" + comminfo.getCommentNum()
                                + "条评论");
                        if (comminfo.getList().size() < 10) {
                            // mPullToRefreshListView.setScrollLoadEnabled(false);
                            // mPullToRefreshListView.setHasMoreData(false);
                            // mPullToRefreshListView.getFooterLoadingLayout().show(
                            // false);
                            comm_list.setLoading(true);
                            comm_list.noMoreLayout();
                        } else {
                            // mPullToRefreshListView.setScrollLoadEnabled(true);
                            // mPullToRefreshListView.setHasMoreData(true);
                            comm_list.setLoading(false);
                        }
                    }
                    break;
                case 300:
                    if (comminfo_down != null
                            && comminfo_down.getCode().equals("0")) {
                        comminfo.getList().addAll(comminfo_down.getList());
                        // updateList(comminfo.getList(), position);
                        adapter.setData(comminfo.getList());
                        adapter.notifyDataSetChanged();
                        if (comminfo_down.getList().size() == 0) {
                            // T.showNoRepeatShort(CommentInfoActivity.this,
                            // "没有更多数据");
                            page--;
                            comm_list.LoadingMoreText("已经到底了");
                            comm_list.setLoading(true);
                        }
                    }
                    break;
                case 400:
                    if (map != null & map.get("code").equals("0")) {
                        ToastUtils.shortToast(
                                map.get("msg"));
                        getData(id, 1);

                        page = 1;

                    } else {
                        ToastUtils.shortToast("评论失败");
                    }
                    isTiez = true;
                    et_sendmmot.setText("");
                    et_sendmmot.setHint("请输入你的评论");
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }

                    break;
                default:
                    break;
            }
        }
    };


    public AjaxParams getParams(String userid, String content, String toUserid,
                                String pid) {

        return null;
    }

    public void updateList(List<CommentFistInfo> list, int position) {

        // comm_list.setSelection(position);
    }

    public void setData(AjaxParams params) {
        final ProgressDialog dialog = new ProgressDialog(this, "加载中。。。");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        FinalHttp fh = new FinalHttp();
        fh.post(URL, params, new AjaxCallBack() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                super.onFailure(t, errorNo, strMsg);

            }

            @Override
            public void onSuccess(String t) {
                dialog.dismiss();
                map = ResolveJson.R_Action(t.toString());
                handler.sendEmptyMessage(400);

                super.onSuccess(t);
            }
        });
    }

    /**
     * 获取
     *
     * @param themeid
     * @param page
     */
    public void getData(String themeid, final int page) {

        // YMApplication.Trace("打印themid" + themeid);
        // final ProgressDialog dialog = new ProgressDialog(this, "正在加载中...");
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.showProgersssDialog();
        String sign = MD5Util.MD5(themeid + Constants.MD5_KEY);

        FinalHttp fh = new FinalHttp();
        AjaxParams params = new AjaxParams();
        params.put("c", "comment");
        params.put("a", "list");
        params.put("themeid", themeid);
        params.put("page", page + "");
        params.put("pagesize", 10 + "");
        params.put("sign", sign);

        fh.post(URL, params, new AjaxCallBack() {

            @Override
            public void onSuccess(String t) {

                if (page == 1) {
                    comminfo = ResolveJson.R_commentfistlist(t.toString());
                    handler.sendEmptyMessage(200);
                    swip.setRefreshing(false);
                } else {
                    comminfo_down = ResolveJson.R_commentfistlist(t.toString());
                    handler.sendEmptyMessage(300);
                    comm_list.onLoadComplete();
                }
                // dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                // dialog.dismiss();
                swip.setRefreshing(false);
                comm_list.onLoadComplete();
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub

                super.onLoading(count, current);
            }
        });
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn_send:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(CommentInfoActivity.this).context);
                } else {
                    if (isTiez) {
                        String userid = YMApplication.getLoginInfo().getData()
                                .getPid();
                        String toUserid = "0";
                        String themeid = id;
                        String sign = MD5Util.MD5(userid + toUserid + themeid
                                + Constants.MD5_KEY);
                        String str_content = HtmlRegexpUtil.filterHtml(et_sendmmot
                                .getText().toString().trim());
                        AjaxParams params = new AjaxParams();
                        params.put("c", "comment");
                        params.put("a", "comment");
                        params.put("userid", userid);
                        params.put("content", str_content);
                        params.put("toUserid", "0");
                        params.put("pid", "0");
                        params.put("themeid", themeid);
                        params.put("sign", sign);
                        // YMApplication.Trace("空   "
                        // + TextUtils.isEmpty(et_sendmmot.getText()
                        // .toString().trim()));
                        if (!TextUtils.isEmpty(et_sendmmot.getText().toString()
                                .trim())
                                && str_content != null && !str_content.equals("")) {
                            if (str_content.length() > 150) {
                                ToastUtils.shortToast(
                                        "字数限制在150字以内");
                            } else {

                                setData(params);

                            }

                        } else {
                            ToastUtils.shortToast(
                                    "评论内容不能为空");
                            et_sendmmot.setText("");
                        }
                    } else {
                        String str_content = HtmlRegexpUtil.filterHtml(et_sendmmot
                                .getText().toString().trim());
                        if (!TextUtils.isEmpty(et_sendmmot.getText().toString()
                                .trim())
                                && str_content != null && !str_content.equals("")) {

                            String userid = adapter.map.get("userid");
                            String toUserid = adapter.map.get("toUserid");
                            if (toUserid.equals(userid)) {
                                ToastUtils.shortToast(
                                        "您不能回复自己!");
                                return;
                            }
                            String themeid = id;
                            // String str_content = HtmlRegexpUtil
                            // .filterHtml(et_sendmmot.getText().toString());
                            String sign = MD5Util.MD5(userid + toUserid + themeid
                                    + Constants.MD5_KEY);
                            AjaxParams params = new AjaxParams();
                            params.put("c", "comment");
                            params.put("a", "comment");
                            params.put("userid", userid);
                            params.put("content", str_content);
                            params.put("toUserid", toUserid);
                            params.put("pid", adapter.map.get("pid"));
                            params.put("themeid", themeid);
                            params.put("sign", sign);
                            if (str_content.length() > 150) {
                                ToastUtils.shortToast(
                                        "字数限制在150字以内");
                            } else {
                                if (YMUserService.isGuest()) {
                                    DialogUtil.LoginDialog(new YMOtherUtils(CommentInfoActivity.this).context);
                                } else {
                                    setData(params);
                                }
                            }
                        } else {
                            ToastUtils.shortToast(
                                    "评论内容不能为空");
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        isTiez = true;
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.commentinfo;
    }

    private void setLastUpdateTime() {
        // String text = formatDateTime(System.currentTimeMillis());
        // mPullToRefreshListView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event)
    // {
    //
    // if (event.getAction() == MotionEvent.ACTION_DOWN)
    // {
    // View view = getCurrentFocus();
    // if (isHideInput(view, event))
    // {
    // HideSoftInput(view.getWindowToken());
    // }
    // }
    // return super.onTouchEvent(event);
    // }

    /**
     * 判断是否需要隐藏
     */
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom);
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @Override
    protected void initView() {

        //stone
        titleBarBuilder.setTitleText("评论");

        main = (RelativeLayout) findViewById(R.id.main);
        type = getIntent().getStringExtra("type");
        if (type.equals("guide")) {
            URL = CommonUrl.Codex_Url;
        } else if (type.equals("consult")) {
            URL = CommonUrl.Consulting_Url;
        }
        id = getIntent().getStringExtra("id");
        // mPullToRefreshListView = (PullToRefreshListView)
        // findViewById(R.id.list_comment);
        // mPullToRefreshListView.setPullLoadEnabled(false);
        // mPullToRefreshListView.setScrollLoadEnabled(true);
        // comm_list = new ListView(CommentInfoActivity.this);

        comm_list = (MyLoadMoreListView) findViewById(R.id.list_comment);
        comm_list.setLoadMoreListen(this);
        comm_list.setFadingEdgeLength(0);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        rl_bottom = (LinearLayout) findViewById(R.id.rl_bottom);
        et_sendmmot = (PasteEditText) findViewById(R.id.et_sendmmot);

        id_allcommnum = (TextView) findViewById(R.id.id_allcommnum);
        // comm_list = mPullToRefreshListView.getRefreshableView();
        setLastUpdateTime();
        if (NetworkUtil.isNetWorkConnected()) {
            // swip.setRefreshing(true);
            getData(id, 1);

        } else {
            ToastUtils.shortToast("网络连接失败");
        }

        // main.setOnTouchListener(new OnTouchListener()
        // {
        //
        // @Override
        // public boolean onTouch(View arg0, MotionEvent arg1)
        // {
        // // TODO Auto-generated method stub
        // hideKeyboard();
        // return true;
        // }
        // });
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
        // getData(id, page);
        // mPullToRefreshListView.onPullDownRefreshComplete();
        // }
        //
        // @Override
        // public void onPullUpToRefresh(
        // PullToRefreshBase<ListView> refreshView)
        // {
        // // TODO Auto-generated method stub
        //
        // getData(id, ++page);
        // mPullToRefreshListView.onPullUpRefreshComplete();
        //
        // }
        // });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void loadMore() {
        if (NetworkUtil.isNetWorkConnected()) {
            getData(id, ++page);
        } else {
            ToastUtils.shortToast("网络连接失败");
        }
    }

    @Override
    public void onRefresh() {
        if (NetworkUtil.isNetWorkConnected()) {
            page = 1;
            getData(id, page);
        } else {
            ToastUtils.shortToast("网络连接失败");
        }
    }
}
