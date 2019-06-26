package com.xywy.askforexpert.module.my.userinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.model.IdNameBean;
import com.xywy.askforexpert.module.discovery.adapter.HospitalAdapter;
import com.xywy.askforexpert.module.discovery.medicine.CertificationAboutRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索医院
 * stone
 * 2018/2/5 上午10:38
 */
public class SearchHosipitalActivity extends YMBaseActivity {
    private EditText editText;
    private View v_clear;
    private View v_no_search;
    private ListView listView;

    private HospitalAdapter mAdapter;
    private InputMethodManager mInputMethodManager;
    private List<IdNameBean> mList = new ArrayList<>();

    private IdNameBean mIdNameBean;


    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            mInputMethodManager.
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void searchData(final String key) {
        YMApplication.applicationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //防止多余的请求 200ms之内再输入新的内容,并不请求
                if (!SearchHosipitalActivity.this.isFinishing() && key.equals(editText.getText().toString().trim())) {
//                    showLoadDataDialog();
//                    hideKeyboard();
//                    mList.clear();
//                    mAdapter.notifyDataSetChanged();

                    CertificationAboutRequest.getInstance().getHospitalList(key).subscribe(new BaseRetrofitResponse<BaseData<List<IdNameBean>>>() {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
//                            hideProgressDialog();
                            mList.clear();
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onNext(BaseData<List<IdNameBean>> listBaseData) {
                            super.onNext(listBaseData);
//                            hideProgressDialog();
                            mList.clear();
                            if (key.equals(editText.getText().toString().trim())
                                    && listBaseData != null
                                    && listBaseData.getData() != null
                                    && listBaseData.getData().size() > 0) {
                                hideKeyboard();
                                mList.addAll(listBaseData.getData());
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }, 200);
    }


    //titlrbar 点击
    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.v_clear:
                mList.clear();
                mAdapter.notifyDataSetChanged();
                editText.getText().clear();
                break;
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
            case R.id.btn22:
                //确定
                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                    ToastUtils.shortToast("请输入医院");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.KEY_VALUE, new IdNameBean("", editText.getText().toString().trim()));
                    SearchHosipitalActivity.this.setResult(RESULT_OK, intent);
                    SearchHosipitalActivity.this.finish();
                }
            default:
                break;
        }
    }

    @Override
    protected void initView() {
        mIdNameBean = (IdNameBean) getIntent().getSerializableExtra(Constants.KEY_VALUE);

        //标题
        hideCommonBaseTitle();
        ((TextView) findViewById(R.id.tv_title)).setText("医院");

        TextView btn22 = (TextView) findViewById(R.id.btn22);
        btn22.setText(getString(R.string.complete));
        btn22.setVisibility(View.VISIBLE);

        editText = (EditText) findViewById(R.id.editText);
        v_clear = findViewById(R.id.v_clear);
        v_no_search = findViewById(R.id.v_no_search);
        listView = (ListView) findViewById(R.id.listView);


        if (mIdNameBean != null && !TextUtils.isEmpty(mIdNameBean.name)) {
            editText.setText(mIdNameBean.name);
            editText.setSelection(mIdNameBean.name.length());
        }


        mAdapter = new HospitalAdapter(mList, SearchHosipitalActivity.this);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        listView.setAdapter(mAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    //搜索 200ms内输入框内容一致就去搜索
                    searchData(s.toString().trim());

                    v_clear.setVisibility(View.VISIBLE);
                    v_no_search.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();

                    v_clear.setVisibility(View.INVISIBLE);
                    v_no_search.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //点击事件
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.KEY_VALUE, mList.get(position));
                YMOtherUtils.backForResult(SearchHosipitalActivity.this, bundle);
            }
        });



    }

    @Override
    protected void initData() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.search_hospital_activity;
    }
}
