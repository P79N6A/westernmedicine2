package com.xywy.askforexpert.module.main.service.document;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.doctor.Messages;
import com.xywy.askforexpert.widget.view.MyListView;

import net.tsz.afinal.FinalDb;

import java.util.List;

/**
 * 搜索历史
 *
 * @author apple
 */
public class HistroyDocmentActivity extends Activity implements OnClickListener {

    final String[] allStr = {"全部字段", "标题", "关键词", "作者", "刊名", "第一作者", "作者机构",
            "中图分类号", "CN"};
    final String[] allFiled = {"search", "title", "KeyWords", "Creator",
            "Source", "CreatorFirst", "Organization", "CLCNumber", "cn"};

    private String curentStr = "全部字段";
    private ImageView iv_back, search_clear;
    private TextView tv_all_filed, tv_strat_search, tv_clean;
    private EditText et_text;
    private Context con;
    private MyListView lv_histroy;
    private MyAdapter adapter;
    private FinalDb db;
    private List<Messages> al;
    private PopupWindow pp;
    private TextView tv_history;
    private String key = "search";
    InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = this;
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setContentView(R.layout.activity_histroydocment);

        CommonUtils.initSystemBar(this);
        initView();
        initData();
        initlinsener();

    }

    private void initView() {

        et_text = (EditText) findViewById(R.id.et_text);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_clean = (TextView) findViewById(R.id.tv_clean);
        lv_histroy = (MyListView) findViewById(R.id.lv_histroy);
        tv_all_filed = (TextView) findViewById(R.id.tv_all_filed);
        search_clear = (ImageView) findViewById(R.id.search_clear);
        tv_history = (TextView) findViewById(R.id.tv_history);
        tv_strat_search = (TextView) findViewById(R.id.tv_strat_search);
    }

    private void initData() {
        db = FinalDb.create(this, "history.db");
        al = db.findAll(Messages.class);

        if (al != null && al.size() > 0) {
            tv_history.setVisibility(View.VISIBLE);
            tv_clean.setVisibility(View.VISIBLE);
        }
    }

    private void initlinsener() {
        iv_back.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
        tv_all_filed.setOnClickListener(this);
        search_clear.setOnClickListener(this);
        tv_strat_search.setOnClickListener(this);
        adapter = new MyAdapter();

        lv_histroy.setAdapter(adapter);
        lv_histroy.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        lv_histroy.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                et_text.setText(al.get(arg2).getIs_doctor());
                et_text.setSelection(al.get(arg2).getIs_doctor().length());

//				setResult(100);
                Intent intent = new Intent(HistroyDocmentActivity.this, SearchActivity.class);
                startActivity(intent);
                SearchActivity.key = et_text.getText().toString().trim();
                SearchActivity.search = key;
                SearchActivity.curenttv_time = curentStr;
                HistroyDocmentActivity.this.finish();
            }
        });

        et_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                if (TextUtils.isEmpty(et_text.getText().toString().trim())) {
                    search_clear.setVisibility(View.GONE);
                } else {
                    search_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_back:
                // 先隐藏键盘
                ((InputMethodManager) et_text.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        et_text.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                this.finish();

                break;

            case R.id.tv_all_filed:// 全部字段
                tv_all_filed.setSelected(true);
                StatisticalTools.eventCount(this, "quanbuziduan");
                showPopAllFiled();
                break;
            case R.id.search_clear:// 清空编辑框的内容
                et_text.setText("");

                break;
            case R.id.tv_strat_search:// 开始搜索
                if (TextUtils.isEmpty(et_text.getText().toString().trim())) {

                    ToastUtils.shortToast("关键字不能为空！");
                    return;
                }
//				setResult(100);
                Intent intent = new Intent(HistroyDocmentActivity.this, SearchActivity.class);
                startActivity(intent);
                SearchActivity.key = et_text.getText().toString().trim();
                SearchActivity.search = key;
                SearchActivity.curenttv_time = curentStr;

                this.finish();
                break;
            case R.id.tv_clean:// 清空历史记录

                setResult(1001);

                if (db != null && al.size() > 0 && adapter != null) {
                    Class<Messages> calss = Messages.class;
                    db.deleteAll(calss);
                    al.clear();
                    adapter.notifyDataSetChanged();
                    tv_history.setVisibility(View.GONE);
                    tv_clean.setVisibility(View.GONE);
                    // et_text.setText("");

                }

                break;
        }

    }

    /***
     * 全部字段
     */
    private void showPopAllFiled() {

        View view = View.inflate(con, R.layout.pop_listview, null);
        ListView listview = (ListView) view.findViewById(R.id.lv);
        listview.setAdapter(new Allfieldadapter(curentStr, allStr, con));
        pp = new PopupWindow(con);
        pp.setContentView(view);
        pp.setHeight(LayoutParams.WRAP_CONTENT);
        pp.setWidth(DensityUtils.dp2px(130));
        pp.setFocusable(true);
        pp.setTouchable(true);
        pp.setAnimationStyle(R.style.popALLFilled);
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
                curentStr = allStr[arg2];
                pp.dismiss();
                key = allFiled[arg2];

                switch (arg2) {
                    case 0: // 全部字段
                        StatisticalTools.eventCount(YMApplication.getInstance(), "quanbuziduan");
                        break;
                    case 1:// 标题
                        StatisticalTools.eventCount(YMApplication.getInstance(), "biaoti");
                        break;
                    case 2:// 关键词
                        StatisticalTools.eventCount(YMApplication.getInstance(), "guanjianci");
                        break;
                    case 3:// 作者
                        StatisticalTools.eventCount(YMApplication.getInstance(), "zuozhe");
                        break;

                    default:
                        break;
                }

            }
        });

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public View getView(int posion, View arg1, ViewGroup arg2) {
            TextView tv = new TextView(con);
            tv.setText(al.get(posion).getIs_doctor());
            tv.setPadding(DensityUtils.dp2px(16), DensityUtils.dp2px(10),
                    DensityUtils.dp2px(10), DensityUtils.dp2px(10));
            return tv;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
        // TODO Auto-generated method stub

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        db.setConfig(null);
        db = null;

        super.onDestroy();
    }
}
