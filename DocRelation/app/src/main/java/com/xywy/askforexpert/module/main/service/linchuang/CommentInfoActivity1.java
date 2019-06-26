package com.xywy.askforexpert.module.main.service.linchuang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.social.UMSocialService;
import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.CommentFistInfo;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.main.service.linchuang.adapter.BaseCommentFistAdapter1;
import com.xywy.askforexpert.module.main.service.linchuang.utils.HtmlRegexpUtil;
import com.xywy.askforexpert.module.main.service.media.InfoDetailActivity;
import com.xywy.askforexpert.module.message.share.ShareUtil;
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
 * 评论 列表 页面 stone
 *
 * @author 王鹏
 * @2015-5-11下午8:06:47
 */
public class CommentInfoActivity1 extends YMBaseActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {

    // private PullToRefreshListView mPullToRefreshListView;
    private MyLoadMoreListView comm_list;
    private SharedPreferences sp;
    private SwipeRefreshLayout swip;
    private CommentFistInfo comminfo;
    private CommentFistInfo comminfo_down;
    public int page = 1;
    private String id = 35511 + "";
    private int position;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private InputMethodManager manager;
    private TextView id_allcommnum;
    private BaseCommentFistAdapter1 adapter;

    public static LinearLayout rl_bottom;
    public static PasteEditText et_sendmmot;
    public static LinearLayout rl_menu;
    public static PasteEditText et_sendmmot_tiz;
    public static LinearLayout rl_bottom_tiez;

    private Map<String, String> map;
    private RelativeLayout main;
    public static boolean isTiez = true;
    public String type;
    private String URL;

    String url;
    String title;
    String imageUrl;

    private UMSocialService mController;
    private TextView tv_max;
    private TextView tv_min;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 200:
                    if (comminfo != null && comminfo.getCode().equals("0")) {
                        // updateList(comminfo.getList(), position);
                        // adapter.notifyDataSetChanged();
                        adapter = new BaseCommentFistAdapter1(
                                CommentInfoActivity1.this, type);
                        adapter.setData(comminfo.getList());
                        comm_list.setAdapter(adapter);
                        id_allcommnum.setText("共有" + comminfo.getCommentNum()
                                + "条评论");
                        if (comminfo.getList().size() < 10) {
                            comm_list.setLoading(true);
                            comm_list.noMoreLayout();
                            // mPullToRefreshListView.setScrollLoadEnabled(false);
                            // mPullToRefreshListView.setHasMoreData(false);
                            // mPullToRefreshListView.getFooterLoadingLayout().show(
                            // false);
                        } else {
                            comm_list.setLoading(false);
                            // mPullToRefreshListView.setScrollLoadEnabled(true);
                            // mPullToRefreshListView.setHasMoreData(true);
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
                            // T.showNoRepeatShort(CommentInfoActivity1.this,
                            // "没有更多数据");
                            page--;
                            comm_list.LoadingMoreText("已经到底了");
                            comm_list.setLoading(true);
                        }
                    }
                    break;
                case 400:
                    isTiez = true;
                    et_sendmmot.setText("");
                    et_sendmmot.setHint("请输入你的评论");
                    et_sendmmot_tiz.setText("");
                    et_sendmmot_tiz.setHint("请输入你的评论");
                    hideKeyboard();
                    // hideKey();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    if (map != null & map.get("code").equals("0")) {
                        ToastUtils.shortToast(
                                map.get("msg"));
                        getData(id, 1);

                        page = 1;

                    } else {
                        ToastUtils.shortToast("评论失败");
                        hideKeyboard();
                        // hideKey();
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
                if (rl_bottom != null & rl_bottom_tiez != null
                        & rl_menu != null) {

                    handler.sendEmptyMessage(400);
                    rl_bottom.setVisibility(View.GONE);
                    rl_bottom_tiez.setVisibility(View.GONE);
                    rl_menu.setVisibility(View.VISIBLE);
                    hideKeyboard();
                    // hideKey();

                }
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
        DLog.d("=======", "comment detail list url = " + URL + "?" + params.toString());
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
                handler.sendEmptyMessage(300);
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
                hideKeyboard();
                Intent intent = new Intent(CommentInfoActivity1.this, InfoDetailActivity.class);
                setResult(1228, intent);
                finish();
                break;
            case R.id.btn_send_real:// 实名
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {


                        String userid = YMApplication.getLoginInfo().getData().getPid();
                        String toUserid = "0";
                        String themeid = id;
                        String sign = MD5Util.MD5(userid + toUserid + themeid
                                + Constants.MD5_KEY);
                        String str_content = HtmlRegexpUtil.filterHtml(et_sendmmot_tiz
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
                        if (!TextUtils.isEmpty(et_sendmmot_tiz.getText().toString()
                                .trim())
                                && str_content != null && !str_content.equals("")) {
                            if (str_content.length() > 150) {
                                ToastUtils.shortToast(
                                        "字数限制在150字以内");
                            } else {

                                setData(params);

                            }

                        } else {
                            ToastUtils.shortToast("评论内容不能为空");
                            et_sendmmot_tiz.setText("");
                        }

                    }
                }, null, null);
                break;
            case R.id.btn_send_anony: // 匿名
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {


                        String userid = YMApplication.getLoginInfo().getData().getPid();
                        String toUserid = "0";
                        String themeid = id;
                        String sign = MD5Util.MD5(userid + toUserid + themeid
                                + Constants.MD5_KEY);
                        String str_content = HtmlRegexpUtil.filterHtml(et_sendmmot_tiz
                                .getText().toString().trim());
                        AjaxParams params = new AjaxParams();
                        params.put("c", "comment");
                        params.put("a", "comment");
                        params.put("userid", userid);
                        params.put("content", str_content);
                        params.put("toUserid", "0");
                        params.put("pid", "0");
                        params.put("level", "2");
                        params.put("themeid", themeid);
                        params.put("sign", sign);
                        // YMApplication.Trace("空   "
                        // + TextUtils.isEmpty(et_sendmmot.getText()
                        // .toString().trim()));
                        if (!TextUtils.isEmpty(et_sendmmot_tiz.getText().toString()
                                .trim())
                                && str_content != null && !str_content.equals("")) {
                            if (str_content.length() > 150) {
                                ToastUtils.shortToast(
                                        "字数限制在150字以内");
                            } else {

                                setData(params);

                            }

                        } else {
                            ToastUtils.shortToast("评论内容不能为空");
                            et_sendmmot_tiz.setText("");
                        }

                    }
                }, null, null);
                hideKey();
                hideKeyboard();
                break;
            case R.id.tv_com_menu:
                isTiez = true;
                rl_menu.setVisibility(View.GONE);
                rl_bottom_tiez.setVisibility(View.VISIBLE);
                et_sendmmot_tiz.requestFocus();
                et_sendmmot_tiz.setFocusableInTouchMode(true);
                et_sendmmot_tiz.setFocusable(true);
                ShowKeyboard(CommentInfoActivity1.et_sendmmot_tiz);
                break;
            case R.id.btn_send:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(CommentInfoActivity1.this).context);
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
                                .trim()) && !str_content.equals("")) {
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
                                .trim()) && !str_content.equals("")) {

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
                                    DialogUtil.LoginDialog(new YMOtherUtils(CommentInfoActivity1.this).context);
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
                hideKey();
                hideKeyboard();
                break;
            case R.id.btn_share:
                StatisticalTools.eventCount(CommentInfoActivity1.this, "zxshare");

                new ShareUtil.Builder()
                        .setTitle("医学资讯")
                        .setText(title)
                        .setTargetUrl(url)
                        .setImageUrl(imageUrl)
                        .setShareId(id)
                        .setShareSource("1")
                        .build(this).outerShare();

                break;

            case R.id.btn_backs:
                finish();
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
        return R.layout.commentinfo1;
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

    // 显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event)
    // {
    // YMApplication.Trace("点击了。。");
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

    private void hideKey() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        // InputMethodManager imm = (InputMethodManager)
        // getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(et_sendmmot.getWindowToken(), 0);
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        // if (getWindow().getAttributes().softInputMode !=
        // WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        // {
        // if (getCurrentFocus() != null)
        // manager.hideSoftInputFromWindow(getCurrentFocus()
        // .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(et_sendmmot.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et_sendmmot_tiz.getWindowToken(), 0);
    }


    @Override
    protected void onPause() {
        if (mController != null) {
            mController = null;
        }
        super.onPause();
    }

    @Override
    protected void initView() {

        titleBarBuilder.setTitleText("评论");

        sp = getSharedPreferences("save_user", MODE_PRIVATE);

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        ((TextView) findViewById(R.id.tv_title)).setText("评论");
        main = (RelativeLayout) findViewById(R.id.main);
        type = getIntent().getStringExtra("type");
        if (type.equals("guide")) {
            URL = CommonUrl.Codex_Url;
        } else if (type.equals("consult")) {
            URL = CommonUrl.Consulting_Url;
        }

        id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        imageUrl = getIntent().getStringExtra("imageUrl");
//        initSocialSDK(title, url, imageUrl);
        // mPullToRefreshListView = (PullToRefreshListView)
        // findViewById(R.id.list_comment);
        // mPullToRefreshListView.setPullLoadEnabled(false);
        // mPullToRefreshListView.setScrollLoadEnabled(true);
        // comm_list = new ListView(CommentInfoActivity1.this);
        comm_list = (MyLoadMoreListView) findViewById(R.id.list_comment);
        comm_list.setLoadMoreListen(this);
        comm_list.setFadingEdgeLength(0);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        tv_max = (TextView) findViewById(R.id.tv_max);
        tv_min = (TextView) findViewById(R.id.tv_min);
        rl_bottom = (LinearLayout) findViewById(R.id.rl_bottom);
        rl_menu = (LinearLayout) findViewById(R.id.rl_menu);
        rl_bottom_tiez = (LinearLayout) findViewById(R.id.rl_bottom_tiez);

        et_sendmmot_tiz = (PasteEditText) findViewById(R.id.et_sendmmot_tiz);

        et_sendmmot = (PasteEditText) findViewById(R.id.et_sendmmot);

        id_allcommnum = (TextView) findViewById(R.id.id_allcommnum);
        // comm_list = mPullToRefreshListView.getRefreshableView();
        setLastUpdateTime();
        if (NetworkUtil.isNetWorkConnected()) {
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

        main.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (rl_bottom.getVisibility() == View.VISIBLE) {
                            rl_bottom.setVisibility(View.GONE);
                            rl_menu.setVisibility(View.VISIBLE);
                            hideKeyboard();

                        } else if (rl_bottom_tiez.getVisibility() == View.VISIBLE) {
                            rl_bottom_tiez.setVisibility(View.GONE);
                            rl_menu.setVisibility(View.VISIBLE);

                            hideKeyboard();
                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    default:
                        break;
                }

                return false;

            }
        });
        comm_list.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (rl_bottom.getVisibility() == View.VISIBLE) {
                            rl_bottom.setVisibility(View.GONE);
                            rl_menu.setVisibility(View.VISIBLE);

                            hideKeyboard();
                        } else if (rl_bottom_tiez.getVisibility() == View.VISIBLE) {
                            rl_bottom_tiez.setVisibility(View.GONE);
                            rl_menu.setVisibility(View.VISIBLE);

                            hideKeyboard();
                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
        et_sendmmot_tiz.setText(sp.getString("tiezi" + id, ""));
        et_sendmmot_tiz.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                tv_min.setText(arg0.length() + "");
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                sp.edit().putString("tiezi" + id, arg0.toString()).commit();

            }
        });
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

    // 初始化分享平台
//    public void initSocialSDK(String shareTitle, String webUrl, String imageUrl) {
//        // System.out.println("---share-->>" + shareTitle + webUrl + imageUrl);
//        // 首先在您的Activity中添加如下成员变量
//        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT,
//                SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN);
//        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID ,
//        // http://dev.umeng.com/social/android/operation 分享平台不显示友盟
//        // 注册时候签名33a19e7cf5d223a88c83688b96dacb0b
//        String appID = "wx7adc9ead4e1de7ef";
//        String appSecret = "c2958650c92241d801f105a2d1eaa4d9";
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(CommentInfoActivity1.this,
//                appID, appSecret);
//        wxHandler.addToSocialSDK();
//
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(
//                CommentInfoActivity1.this, appID, appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//
//        WeiXinShareContent weixinContent = new WeiXinShareContent();
//        // 设置分享文字
//        weixinContent.setShareContent(shareTitle);
//        // 设置title
//        weixinContent.setTitle(shareTitle);
//        // 设置分享内容跳转URL
//        weixinContent.setTargetUrl(webUrl);
//        // 设置分享图片
//        weixinContent.setShareImage(new UMImage(CommentInfoActivity1.this,
//                imageUrl));
//        mController.setShareMedia(weixinContent);
//
//        // 设置微信朋友圈分享内容
//        CircleShareContent circleMedia = new CircleShareContent();
//        circleMedia.setShareContent(shareTitle);
//        // 设置朋友圈title
//        circleMedia.setTitle(shareTitle);
//        circleMedia.setShareImage(new UMImage(CommentInfoActivity1.this,
//                imageUrl));
//        circleMedia.setTargetUrl(webUrl);
//        mController.setShareMedia(circleMedia);
//        // QQ平台
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
//                CommentInfoActivity1.this, "1101752729", "LgrXeXE27j0PFDlU");
//        qqSsoHandler.addToSocialSDK();
//
//        QQShareContent qqShareContent = new QQShareContent();
//        // 设置分享文字
//        qqShareContent.setShareContent(shareTitle);
//        // 设置分享title
//        qqShareContent.setTitle(shareTitle);
//        // 设置分享图片
//        qqShareContent.setShareImage(new UMImage(CommentInfoActivity1.this,
//                imageUrl));
//        // 设置点击分享内容的跳转链接
//        qqShareContent.setTargetUrl(webUrl);
//        mController.setShareMedia(qqShareContent);
//
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
//                CommentInfoActivity1.this, "1101752729",
//                "LgrXeXE27j0PFDlU");
//        qZoneSsoHandler.addToSocialSDK();
//
//        // qq空间
//        QZoneShareContent qzone = new QZoneShareContent();
//        // 设置分享文字
//        qzone.setShareContent(shareTitle);
//        // 设置点击消息的跳转URL
//        qzone.setTargetUrl(webUrl);
//        // 设置分享内容的标题
//        qzone.setTitle(shareTitle);
//        // 设置分享图片
//        qzone.setShareImage(new UMImage(CommentInfoActivity1.this, imageUrl));
//        mController.setShareMedia(qzone);
//
//        // // 新浪微博平台
////		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        SinaShareContent sinaContent = new SinaShareContent();
//        sinaContent.setTitle(shareTitle);
//        sinaContent.setShareContent(shareTitle + webUrl);
//        sinaContent.setTargetUrl(webUrl);
//        sinaContent.setShareImage(new UMImage(CommentInfoActivity1.this,
//                imageUrl));
//        mController.setShareMedia(sinaContent);
//        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
//                SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
//
//    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//			Intent intent = new Intent(CommentInfoActivity1.this,InfoDetailActivity.class);
            setResult(1228, getIntent());
            this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
