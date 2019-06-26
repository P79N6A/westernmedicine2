package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.SearchResultEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.request.MedicineRequest;
import com.xywy.askforexpert.module.discovery.medicine.util.GsonUtil;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.askforexpert.module.drug.CommonDrugListActivity;
import com.xywy.base.XywyBaseActivity;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.xywy.util.LogUtils;
import com.xywy.util.T;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SearchActivity extends XywyBaseActivity {
    private RecyclerView mResultView,mHistoryView;
    private SearchResultAdapter mResultAdapter;
    private SearchHistoryAdapter mHistoryAdapter;
    private EditText mSearch;
    private View mCancel;
    private List<SearchResultEntity> mSearchResultList = new ArrayList<>();
    private List<SearchResultEntity> mSearchHistoryList = new ArrayList<>();
    private static final String PRE_SEARCH_HISTORY = "pre_search_history";
    private static final String KEY_SEARCH_HISTORY_KEYWORD = "search_history";
    private static final int mMaxHistoryNum = 10;

    private  JSONArray arr = new JSONArray();
    SharedPreferences mSharePreference;
    private LinearLayout mHistory;

    private static final String IS_FROM = "isFrom";
    private String mIsFrom;
    private boolean mIsCommonOpt;
    private boolean drug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //初始化统一bar，将系统栏的背景设置成透明色
        CommonUtils.initSystemBar(this);
        Intent intent = getIntent();
        if(null != intent){
            if(intent.hasExtra(IS_FROM)){
                mIsFrom = intent.getStringExtra(IS_FROM);
            }
        }
        mIsCommonOpt = intent.getBooleanExtra("mIsCommonOpt",false);
        drug = intent.getBooleanExtra("drug",false);
        initHistoryData();
        initView();
    }

    private void initHistoryData() {
        mSharePreference = getSharedPreferences(PRE_SEARCH_HISTORY, 0);
        String history = mSharePreference.getString(KEY_SEARCH_HISTORY_KEYWORD,"");
        if (!TextUtils.isEmpty(history)){
            mSearchHistoryList = GsonUtil.jsonToList(history, SearchResultEntity.class);
        }
        //进行降序排列
        Collections.sort(mSearchHistoryList,Collections.reverseOrder());
    }

    private void initView() {
        hideCommonBaseTitle();
        mSearch = (EditText) findViewById(R.id.pharmacy_search);
        mHistory = (LinearLayout) findViewById(R.id.ll_history);
        mHistoryView = (RecyclerView) findViewById(R.id.search_history);
        mHistory.setVisibility(mSearchHistoryList.size()>0?View.VISIBLE:View.GONE);

        mHistoryView.setLayoutManager(new LinearLayoutManager(this));

        mHistoryAdapter = new SearchHistoryAdapter(this);
        mHistoryAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(view.getTag()!=null && view.getTag() instanceof SearchResultEntity) {
                    SearchResultEntity entity = (SearchResultEntity) view.getTag();
                    entity.setTime(System.currentTimeMillis());
                    addHistory(entity);
                    saveSearchHistory(mSearchHistoryList);
                    if (drug){
                        Intent intent = new Intent(SearchActivity.this, CommonDrugListActivity.class);
                        intent.putExtra(Constants.KEY_VALUE, mIsCommonOpt);//是否是常用药操作 否则就是加入处方操作
                        intent.putExtra(Constants.KEY_CITY, "");//分类id
                        intent.putExtra(Constants.KEY_MESSAGE, entity.getName());//标题
                        intent.putExtra("drugid", entity.getId()+"");
                        startActivity(intent);
                        finish();
                    }else {
                        MedicineListActivity.startActivityFromSearch(SearchActivity.this, entity.getId(), entity.getName(), mIsFrom);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mHistoryAdapter.setData(mSearchHistoryList);
        mHistoryView.setAdapter(mHistoryAdapter);

        Button btn_clear = (Button)findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharePreference.edit().clear().commit();
                mSearchHistoryList.clear();
                mHistoryAdapter.setData(mSearchHistoryList);
                mHistoryAdapter.notifyDataSetChanged();
                mHistory.setVisibility(View.GONE);
            }
        });

        mResultView = (RecyclerView) findViewById(R.id.search_result);
        mResultView.setVisibility(mSearchHistoryList.size()>0?View.GONE:View.VISIBLE);
        mResultView.setLayoutManager(new LinearLayoutManager(this));
        mResultView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mResultAdapter = new SearchResultAdapter(this);
        mResultAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(view.getTag()!=null && view.getTag() instanceof SearchResultEntity) {
                    SearchResultEntity entity = (SearchResultEntity) view.getTag();
                    entity.setTime(System.currentTimeMillis());
                    addHistory(entity);
                    saveSearchHistory(mSearchHistoryList);
                    LogUtils.i("startActivityFromSearch");
                    if (drug){
                        Intent intent = new Intent(SearchActivity.this, CommonDrugListActivity.class);
                        intent.putExtra(Constants.KEY_VALUE, mIsCommonOpt);//是否是常用药操作 否则就是加入处方操作
                        intent.putExtra(Constants.KEY_CITY, "");//分类id
                        intent.putExtra(Constants.KEY_MESSAGE, entity.getName());//标题
                        intent.putExtra("drugid", entity.getId()+"");
                        startActivity(intent);
                    }else {
                        MedicineListActivity.startActivityFromSearch(SearchActivity.this, entity.getId(), entity.getName(), mIsFrom);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mResultAdapter.setData(mSearchResultList);
        mResultView.setAdapter(mResultAdapter);
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString().trim());
            }
        });

        mSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER){
                    String s=mSearch.getText().toString().trim();
                    search(s);

                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if(imm.isActive()) {
//                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                        if(s != null && s.length() > 0) {
                            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        mCancel = findViewById(R.id.cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void addHistory( SearchResultEntity entity) {
        int size = mSearchHistoryList.size();
        int exsistsIndex = -1;//存在相同的元素的index
        for (int i = 0; i < size; i++) {
            if(mSearchHistoryList.get(i).getId() == entity.getId()){
                exsistsIndex = i;
            }
        }
        if(-1 != exsistsIndex){
            mSearchHistoryList.remove(exsistsIndex);
        }
        size = mSearchHistoryList.size();//重新计算集合的大小
        if(size==mMaxHistoryNum){
            mSearchHistoryList.remove(size-1);
        }
        mSearchHistoryList.add(entity);
        //进行降序排列
        Collections.sort(mSearchHistoryList,Collections.reverseOrder());
    }

    private void saveSearchHistory(List<SearchResultEntity> list) {
        arr = new JSONArray();
        int size = list.size();
        JSONObject jsonObj = null;
        SearchResultEntity entity = null;
        for (int i = 0; i <size; i++) {
            try {
                entity = list.get(i);
                jsonObj = new JSONObject();
                jsonObj.put("id",entity.getId());
                jsonObj.put("name",entity.getName());
                jsonObj.put("commonName",entity.getCommonName());
                jsonObj.put("time",entity.getTime());
                arr.put(jsonObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences.Editor editor = mSharePreference.edit();
        editor.clear();
        editor.putString(KEY_SEARCH_HISTORY_KEYWORD,arr.toString()).commit();
    }

    private void search(final String keywork) {
        if(keywork == null || keywork.length() == 0) {
            ToastUtils.shortToast("输入的内容不能为空");
            return;
        }

        MedicineRequest.getInstance().medicineSearch(keywork).subscribe(new BaseRetrofitResponse<BaseData<List<SearchResultEntity>>>(){
            @Override
            public void onNext(BaseData<List<SearchResultEntity>> entry) {
                super.onNext(entry);
                if(entry!=null && entry.getData()!=null) {
                    if(entry.getData().size() == 0) {
                        T.show(SearchActivity.this, "未找到该药品", Toast.LENGTH_SHORT);
                    }
                    mHistory.setVisibility(entry.getData().size()>0?View.GONE:View.VISIBLE);
                    mResultView.setVisibility(entry.getData().size()>0?View.VISIBLE:View.GONE);
                    mSearchResultList = entry.getData();
                    mResultAdapter.setData(mSearchResultList);
                    mResultAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHistoryData();
        mHistoryAdapter.setData(mSearchHistoryList);
        mHistoryAdapter.notifyDataSetChanged();
    }
}
