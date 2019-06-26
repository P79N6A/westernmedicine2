package com.xywy.askforexpert.module.message.friend;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.SerchNewCardInfo;
import com.xywy.askforexpert.module.message.friend.adapter.BaseSerchNewFriendAdapter;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 搜索好友
 *
 * @author 王鹏
 * @2015-4-21下午9:23:38
 */
public class SerchCardActivity extends Activity {
    BaseSerchNewFriendAdapter adapter;
    private LinearLayout view;
    private EditText query;
    private ImageButton clearSearch;
    private ListView serch_list;
    private InputMethodManager inputMethodManager;
    private SerchNewCardInfo serchinfo = new SerchNewCardInfo();
    private LinearLayout serch_no;
    private TextView tv_cancle;
    private TextView tv_nodata;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (serchinfo.getCode().equals("0")) {
                        adapter.setData(serchinfo.getData());
                        serch_list.setAdapter(adapter);
                        if ("0".equals(serchinfo.getHasdata())) {
//						T.showNoRepeatShort(SerchCardActivity.this, "抱歉，没有匹配到相关内容");
                            tv_nodata.setVisibility(View.VISIBLE);
                        } else {
                            tv_nodata.setVisibility(View.GONE);
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
        setContentView(R.layout.search_bar);

        query = (EditText) findViewById(R.id.query);
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        serch_list = (ListView) findViewById(R.id.serch_list);
        serch_no = (LinearLayout) findViewById(R.id.serch_no);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // serch_list.setAdapter(adapter);
        adapter = new BaseSerchNewFriendAdapter(SerchCardActivity.this, 1);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                    serch_no.setVisibility(View.GONE);
                    serch_list.setVisibility(View.VISIBLE);
                    tv_cancle.setText("搜索");
                } else {

                    if (serchinfo.getData() != null) {
                        serchinfo.getData().clear();
                        adapter.setData(serchinfo.getData());
                        adapter.notifyDataSetChanged();
//						page = 1;
                    }
                    clearSearch.setVisibility(View.INVISIBLE);
                    serch_no.setVisibility(View.VISIBLE);
                    serch_list.setVisibility(View.GONE);
                    tv_nodata.setVisibility(View.GONE);
                    tv_cancle.setText("取消");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });
        tv_cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String str = query.getText().toString().trim();
                if (tv_cancle.getText().equals("搜索")) {
                    if (!str.equals("")) {
                        getData(str);
                    } else {
                        ToastUtils.shortToast("请输入搜索内容");
                    }

                } else {
                    finish();
                }

            }
        });
        AddNewCardHolderActivity.ismove = true;
    }

    public void getData(String keyword) {
        final ProgressDialog dialog = new ProgressDialog(this, "正在搜索中...");

        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();

        String id = YMApplication.getLoginInfo().getData().getPid();
        String bind = id;
        Long st = System.currentTimeMillis();
        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("m", "searchFriend");
        params.put("a", "chat");
        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("id", id);
        params.put("keyword", keyword);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        Gson gson = new Gson();
                        serchinfo = gson.fromJson(t.toString(),
                                SerchNewCardInfo.class);
                        handler.sendEmptyMessage(100);
                        dialog.dismiss();
                        super.onSuccess(t);
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        // TODO Auto-generated method stub
                        super.onLoading(count, current);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    void hideSoftKeyboard() {
        if (SerchCardActivity.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (SerchCardActivity.this.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        SerchCardActivity.this.getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
