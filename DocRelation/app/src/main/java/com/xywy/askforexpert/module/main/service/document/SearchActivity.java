package com.xywy.askforexpert.module.main.service.document;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.doctor.Messages;
import com.xywy.askforexpert.model.wenxian.WXSeachBean;
import com.xywy.askforexpert.widget.view.PopWX_select;
import com.xywy.askforexpert.widget.view.PopWxNumberP;
import com.xywy.base.view.CircleProgressBar;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * 搜索页面
 *
 * @author apple
 */
public class SearchActivity extends Activity {

    private static final String TAG = "SearchActivity";
    private ImageView back;
    private Activity context;
    private TextView tv_strat_search;
    private ListView listview;
    private ImageButton search_clear;
    private EditText et_text;
    private RelativeLayout re_count, re_guanlian;//
    private TextView tv_count, tv_guanlian, tv_time;//   排序 ，     时间  ， 全部论文
    private String curenttv_count = "时间";
    private String curenttv_guanlian;
    public static String curenttv_time;
    String curentallPeriodcal = "全部论文";//
    private String sortby = "relevance";// 默认排序方式
    private String DBID = "";
    public static String search = "search";
    public static String key;

    private String start, end;//开始年份
    private ProgressDialog dialog;
    private View lin_nodata;// 没数据
    private int page = 1;
    private int startIndex = 1;
    LinearLayout ll;
    private TextView tv_nodata_title, tv_all_filed;
    private View footview;
    private boolean isloading = true;
    private CircleProgressBar pb_more;
    private TextView tv_more, tv_number;
    private SeachAdapter seachAdapter;
    final String[] allStr = {"全部字段 ", "标题 ", "关键词 ", "作者 ", "刊名 ", "第一作者 ", "作者机构 ", "中图分类号 ", "CN "};
    final String[] allFiled = {"search", "Title", "KeyWords", "Creator", "Source", "CreatorFirst", "Organization", "CLCNumber", "cn"};
    final String[] allPeriodcal = {"全部论文 ", "期刊论文 ", "会议论文 ", "学位论文 "};//WF_QK(期刊),WF_HY(会议),WF_XW(学位),WF_NSTL(外文)
    final String[] allPeriodcalFiled = {"", "WF_QK", "WF_HY", "WF_XW"};
    final String[] allSort = {"时间 ", "相关度 ", "被引次数 "};
    final String[] sortData = {"date", "relevance", "CitedCount"};
    private PopWxNumberP numberP;
    private PopWX_select popWX_select;
    private PopupWindow pp;
    private PopWX_select popWX_select2;
    private WXSeachBean mBean = null;
    private FinalDb db;
    private List<Messages> al;
    /**
     * 刷新控件
     */
    private SwipeRefreshLayout lv_srefresh;

    private boolean isFirst = true; //是否首次进入

    private InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        CommonUtils.initSystemBar(this);
        curenttv_time = "全部字段 ";//
        dialog = new ProgressDialog(context, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        search = "search";
        setContentView(R.layout.activity_physicseach);
        CommonUtils.initSystemBar(this);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        inidata();
        initLisener();//0 bukou
    }

    private void initView() {
        ll = (LinearLayout) findViewById(R.id.ll);
        lin_nodata = findViewById(R.id.lin_nodata);
        back = (ImageView) findViewById(R.id.iv_back);
        findViewById(R.id.img_nodate).setBackgroundResource(R.drawable.service_more_none);
        et_text = (EditText) findViewById(R.id.et_text);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_guanlian = (TextView) findViewById(R.id.tv_guanlian);
        re_count = (RelativeLayout) findViewById(R.id.re_count);
        search_clear = (ImageButton) findViewById(R.id.search_clear);
        re_guanlian = (RelativeLayout) findViewById(R.id.re_guanlian);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        lv_srefresh = (SwipeRefreshLayout) findViewById(R.id.lv_srefresh);
        lv_srefresh.setClipChildren(true);
        lv_srefresh.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        listview = (ListView) findViewById(R.id.lv);
        listview.setFadingEdgeLength(0);
        tv_all_filed = (TextView) findViewById(R.id.tv_all_filed);
        tv_strat_search = (TextView) findViewById(R.id.tv_strat_search);
        footview = View.inflate(context, R.layout.loading_more, null);
        listview.addFooterView(footview);
        footview.setVisibility(View.INVISIBLE);
        tv_more = (TextView) footview.findViewById(R.id.tv_more);
        pb_more = (CircleProgressBar) footview.findViewById(R.id.pb_more);

    }

    private void inidata() {
        db = FinalDb.create(context, "history.db");
        al = db.findAll(Messages.class);
        search();
    }

    private void initLisener() {
        MyOnclick myOnclick = new MyOnclick();
        back.setOnClickListener(myOnclick);
        et_text.setOnClickListener(myOnclick);
        search_clear.setOnClickListener(myOnclick);
        tv_strat_search.setOnClickListener(myOnclick);
        re_count.setOnClickListener(myOnclick);
        re_guanlian.setOnClickListener(myOnclick);
        tv_time.setOnClickListener(myOnclick);
        tv_all_filed.setOnClickListener(myOnclick);
        et_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stuarg0arg0b
                if ("".equals(et_text.getText().toString().trim())) {
                    search_clear.setVisibility(View.GONE);
                } else {
                    if (mBean != null) {
                        return;
                    }
                    search_clear.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable e) {
                // TODO Auto-generated method stub

            }
        });

        et_text.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 先隐藏键盘
                    ((InputMethodManager) et_text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(et_text
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == listview.getAdapter().getCount() - 1) {
                    return;
                }

//				if(YMApplication.isGuest){
//					new YMOtherUtils(context).LoginDialog_back();
//					return;
//				}
                String articleID = seachAdapter.getData().get(arg2).getArticleID();
                Intent intent = new Intent(context, PhysicLiteratureDetaileActivty.class);
                intent.putExtra("ArticleID", articleID);
                intent.putExtra("DBID", seachAdapter.getData().get(arg2).getDBID());
                startActivity(intent);
            }
        });

        listview.setOnScrollListener(new OnScrollListener() {
            int item;

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                // TODO Auto-generated method stub

                if (item == listview.getAdapter().getCount() && arg1 == SCROLL_STATE_IDLE) {
                    if (isloading) {
                        isloading = false;
                        pb_more.setVisibility(View.VISIBLE);
                        footview.setVisibility(View.VISIBLE);
                        ++page;
                        startIndex += 20;
                        getSeachData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-arg1generated method stub
                item = arg1 + arg2;
            }
        });
        lv_srefresh.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                startIndex = 1;
                page = 1;
                getSeachData();
            }
        });
    }


    /**
     * 开始搜索
     */
    private void getSeachData() {
        //隐藏软键盘
        manager.hideSoftInputFromWindow(et_text.getWindowToken(), 0);
        if (!NetworkUtil.isNetWorkConnected()) {
            ToastUtils.shortToast( "网络连接异常，请检查网络连接");
            return;
        }
        key = et_text.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            ToastUtils.shortToast( "关键字不能为空！");
            return;
        }

        if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end)) {
            if (Integer.parseInt(start) > Integer.parseInt(end)) {
                ToastUtils.shortToast( "开始年份不能比结束年份大");
                return;
            }
        }
        checkHasKey(key);
        FinalHttp fh = new FinalHttp();

        AjaxParams params = new AjaxParams();
        if (!TextUtils.isEmpty(DBID)) {
            params.put("DBID", DBID);
        }
        if (!TextUtils.isEmpty(search)) {
            params.put(search, key);

        } else {
            params.put("KeyWords", key);
        }
        if (!TextUtils.isEmpty(start)) {
            params.put("start", start);
        }
        if (!TextUtils.isEmpty(end)) {
            params.put("end", end);
        }
        if (!TextUtils.isEmpty(sortby)) {
            params.put("type", sortby);
        }

        params.put("startIndex", startIndex + "");
        params.put("a", "literature");
        params.put("m", "zpSearch");
        params.put("pageSize", 20 + "");
        fh.post(CommonUrl.wenxian, params, new AjaxCallBack() {//http://test.yimai.api.xywy.com/app/1.1/index.interface.php?a=literature&search=感冒&startIndex=1&pageSize=10&type=date&m=zpSearch
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (dialog.isShowing()) {
                    dialog.closeProgersssDialog();
                }
                footview.setVisibility(View.INVISIBLE);
                page--;
                startIndex -= 20;
                isloading = true;
                ToastUtils.shortToast( "网络链接超时");
                lv_srefresh.setRefreshing(false);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                lv_srefresh.setRefreshing(false);
                if (dialog.isShowing()) {
                    dialog.closeProgersssDialog();
                }

                isloading = true;
                getSeverDataSucess(t.toString());
            }
        });
    }

    /**
     * 获取数据成功
     *
     * @param
     */
    private void getSeverDataSucess(String t) {
        Gson mGson = new Gson();

        try {
            mBean = mGson.fromJson(t, WXSeachBean.class);

        } catch (Exception e) {
            // TODO: handle exception

            return;
        }
        if (mBean == null) {
            return;
        }

        search_clear.setVisibility(View.GONE);
        if ("0".equals(mBean.getCode())) {// 成功
            et_text.setCursorVisible(false);
            if (mBean.getTotal() > 0) {
                lin_nodata.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
                tv_number.setText("共搜到" + mBean.getTotal() + "篇文章");
                tv_number.setVisibility(View.VISIBLE);
                ll.setVisibility(View.VISIBLE);
                if (page == 1) {
                    seachAdapter = null;
                }
                if (seachAdapter == null) {
                    if (mBean.getRecords().size() < 20) {
                        isloading = false;
//						pb_more.setVisibility(View.GONE);
                        footview.setVisibility(View.VISIBLE);
                        tv_more.setText("没有更多数据啦");
                        pb_more.setVisibility(View.GONE);

                    }
                    seachAdapter = new SeachAdapter(context, mBean.getRecords());
                    listview.setAdapter(seachAdapter);
                } else {

                    if (mBean.getRecords().size() < 20) {

                        pb_more.setVisibility(View.GONE);
                        tv_more.setText("没有更多数据啦");
                        isloading = false;
//						return;
                    }

                    // 判断加载的数据和totle  一共的
                    seachAdapter.addData(mBean.getRecords());

                }

            } else {
                if (seachAdapter != null && mBean.getTotal() == seachAdapter.getCount()) {

                    pb_more.setVisibility(View.GONE);
                    tv_more.setText("没有更多数据啦");
                    isloading = false;

                    return;
                }
                lin_nodata.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
                tv_number.setVisibility(View.GONE);
                tv_nodata_title.setText("对不起,没有检索到相关文献");
                ll.setVisibility(View.GONE);
            }

        }
    }


    /***
     * 检查集合中是否包含key
     *
     * @param key
     */
    private void checkHasKey(String key) {


        db.deleteAll(Messages.class);

        for (int i = 0; i < al.size(); i++) {
            if (al.get(i).is_doctor.equals(key)) {
                al.remove(i);
            }

        }
        Messages messages = new Messages();
        messages.is_doctor = key;
        al.add(0, messages);
        for (int i = 0; i < al.size(); i++) {
            if (i < 10) {
                db.save(al.get(i));
            }
        }
//		al.clear();
    }


    /**
     * 排序
     */
    private void showPopSort() {


        popWX_select2 = new PopWX_select(curenttv_count, context, allSort, new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                sortby = sortData[arg2];
                curenttv_count = allSort[arg2];
                popWX_select2.dismiss();
                tv_count.setText(allSort[arg2]);
                startIndex = 1;
                page = 1;
                getSeachData();

            }
        }, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popWX_select2.dismiss();
            }
        });
        popWX_select2.showAsDropDown(ll, 0, 2);
        popWX_select2.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {

                // TODO Auto-generated method stub
                tv_count.setSelected(false);
            }
        });


    }

    /**
     * 时间pop  tv_guanlian
     */
    private void showTimePop() {

        numberP = new PopWxNumberP(context, new OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.tv_ok:

                        //start,end
                        if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(numberP.getWxnp1() + "")) {
                            if (Integer.parseInt(start) > Integer.parseInt(numberP.getWxnp2() + "")) {
                                ToastUtils.shortToast( "开始年份不能比结束年份大");
                                return;
                            }
                        }
                        start = numberP.getWxnp1() + "";
                        end = numberP.getWxnp2() + "";
                        startIndex = 1;
                        page = 1;
                        if (numberP != null) {
                            numberP.dismiss();
                        }
                        tv_guanlian.setText(numberP.getWxnp1() + "-" + numberP.getWxnp2());
                        getSeachData();
                        break;

                    case R.id.tv_cancel:

                        numberP.dismiss();
                        break;


                    default:

                        numberP.dismiss();
                        break;
                }

            }
        });
        numberP.showAsDropDown(ll, 0, 2);
        numberP.setOnDismissListener(new OnDismissListener() {

                                         @Override
                                         public void onDismiss() {
                                             tv_guanlian.setSelected(false);
                                             //start,end
//				if(!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end) ){
//					if(Integser.parseInt(start) > Integer.parseInt(end)) {
//						T.showLong(context, "开始年分不能比结束年份大");
//						return;
//					}
//				}


                                         }
                                     }
        );

    }


    /**
     * 全部论文
     */
    private void showPop() {


        popWX_select = new PopWX_select(curentallPeriodcal, context, allPeriodcal, new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                DBID = allPeriodcalFiled[arg2];
                curentallPeriodcal = allPeriodcal[arg2];
                tv_time.setText(allPeriodcal[arg2]);
                popWX_select.dismiss();

                switch (arg2) {
                    case 0:
                        StatisticalTools.eventCount(context, "quanbulunwen");
                        break;
                    case 1:
                        StatisticalTools.eventCount(context, "qikanlunwen");
                        break;
                    case 3:
                        StatisticalTools.eventCount(context, "xueweilunwen");
                        break;
                    case 2:
                        StatisticalTools.eventCount(context, "huiyilunwen");

                        break;

                    default:
                        break;
                }
                startIndex = 1;
                page = 1;
                getSeachData();
            }
        }, new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                popWX_select.dismiss();

            }
        });
        popWX_select.showAsDropDown(ll, 0, 2);

        popWX_select.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                tv_time.setSelected(false);
            }
        });

    }


    /***
     * 全部字段
     */
    private void showPopAllFiled() {


        View view = View.inflate(context, R.layout.pop_listview, null);
        ListView listview = (ListView) view.findViewById(R.id.lv);
        listview.setAdapter(new Allfieldadapter(curenttv_time, allStr, context));
        pp = new PopupWindow(context);
        pp.setContentView(view);
        pp.setHeight(LayoutParams.WRAP_CONTENT);
        pp.setWidth(DensityUtils.dp2px(130));
        pp.setFocusable(true);
        pp.setAnimationStyle(R.style.popALLFilled);
        pp.setTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        pp.setBackgroundDrawable(dw);
        pp.showAsDropDown(tv_all_filed, -30, 20);
        pp.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                tv_all_filed.setSelected(false);
            }
        });
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                tv_all_filed.setText(allStr[arg2]);
                pp.dismiss();
                curenttv_time = allStr[arg2];
                search = allFiled[arg2];

                switch (arg2) {
                    case 0: //全部字段
                        StatisticalTools.eventCount(context, "quanbuziduan");
                        break;
                    case 1:// 标题
                        StatisticalTools.eventCount(context, "biaoti");
                        break;
                    case 2://关键词
                        StatisticalTools.eventCount(context, "guanjianci");
                        break;
                    case 3://作者
                        StatisticalTools.eventCount(context, "zuozhe");
                        break;

                    default:
                        break;
                }


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == 100) {
            page = 1;
            startIndex = 1;
            al = db.findAll(Messages.class);
            et_text.setText(key);
            tv_all_filed.setText(curenttv_time);

            et_text.setSelection(key.length());

            tv_count.setText("相关度 ");
            tv_guanlian.setText("时间 ");
            tv_time.setText("全部论文 ");
            DBID = "";
            start = "";
            end = "";
            curentallPeriodcal = "全部论文";
            curenttv_count = "相关度";
            sortby = "relevance";
            getSeachData();
        }
        if (requestCode == 101 && resultCode == 1001) {
            al = db.findAll(Messages.class);
        }


    }

    class MyOnclick implements OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.iv_back:
                    context.finish();
                    break;

                case R.id.et_text:

                    if (!TextUtils.isEmpty(et_text.getText().toString().trim()) && (mBean != null)) {
                        StatisticalTools.eventCount(context, "sousuo2");
                        startActivityForResult(new Intent(context, HistroyDocmentActivity.class), 101);
                    }

                    break;

                case R.id.tv_strat_search:// 搜索
                    //点击搜索，需将 论文类型、时间 、排序 设置默认模式
                    startIndex = 1;
                    page = 1;
                    tv_count.setText("相关度 ");
                    tv_guanlian.setText("时间 ");
                    tv_time.setText("全部论文 ");
                    DBID = "";
                    start = "";
                    end = "";
                    curentallPeriodcal = "全部论文";
                    curenttv_count = "相关度";
                    sortby = "relevance";

                    getSeachData();
                    break;
                case R.id.search_clear://  清除editext
                    et_text.setText("");
                    break;
                case R.id.re_count://  // 排序
                    tv_count.setSelected(true);
                    tv_guanlian.setSelected(false);
                    tv_time.setSelected(false);
                    showPopSort();
                    break;
                case R.id.re_guanlian:// 时间
                    tv_count.setSelected(false);
                    tv_guanlian.setSelected(true);
                    tv_time.setSelected(false);
                    showTimePop();
                    break;
                case R.id.tv_time:// 全部论文
                    tv_count.setSelected(false);
                    tv_guanlian.setSelected(false);
                    tv_time.setSelected(true);
                    showPop();
                    StatisticalTools.eventCount(context, "quanbulunwen");

                    break;
                case R.id.tv_all_filed://全部字段
                    //显示软键盘
                    manager
                            .showSoftInputFromInputMethod(getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.SHOW_FORCED);
                    if (!TextUtils.isEmpty(et_text.getText().toString().trim()) && (mBean != null)) {

                        startActivityForResult(new Intent(context, HistroyDocmentActivity.class), 101);
                        return;
                    }

                    tv_all_filed.setSelected(true);
                    StatisticalTools.eventCount(context, "quanbuziduan");
                    showPopAllFiled();

                    break;
            }
        }


    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (dialog != null && dialog.isShowing()) {
            dialog.closeProgersssDialog();
        }
        db.setConfig(null);
        db = null;

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
        // TODO Auto-generated method stub
        DLog.i(TAG, "onResume()" + isFirst);
        if (!isFirst) {
//			if (manager.isActive()) {
//				//如果开启
//				manager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//				//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
//				}
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        isFirst = false;
        StatisticalTools.onPause(this);
        super.onPause();
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void search() {
        page = 1;
        startIndex = 1;
        al = db.findAll(Messages.class);
        et_text.setText(key);
        tv_all_filed.setText(curenttv_time);

        try {
            if (key.length() > 0) {
                et_text.setSelection(key.length());
            }

            tv_count.setText("相关度 ");
            tv_guanlian.setText("时间 ");
            tv_time.setText("全部论文 ");
            DBID = "";
            start = "";
            end = "";
            curentallPeriodcal = "全部论文";
            curenttv_count = "相关度";
            sortby = "relevance";
            getSeachData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

