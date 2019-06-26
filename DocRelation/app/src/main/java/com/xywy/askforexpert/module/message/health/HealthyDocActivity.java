package com.xywy.askforexpert.module.message.health;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.fragment.LoadingDialogFragment;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitServices;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitUtil;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.healthy.HealthyDocBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 健康档案
 *
 * @author Jack Fang
 */
public class HealthyDocActivity extends AppCompatActivity {
    public static final String HAS_HEALTHY_DOC_INTENT_KEY = "hasDoc";
    private static final String TAG = "HealthyDocActivity";
    private static final String PARAMS_A = "areamedical";
    private static final String PARAMS_M = "patienthealthrecords";
    @Bind(R.id.healthy_doc_toolbar)
    Toolbar healthyDocToolbar;
    @Bind(R.id.no_healthy_doc)
    TextView noHealthyDoc;
    @Bind(R.id.healthy_doc_web)
    WebView healthyDocWeb;

    private Map<String, String> sMap = new HashMap<>();

    private String patientId;

    /**
     * 是否有健康档案，默认有健康档案
     */
    private boolean hasHealthyDoc = true;

    private Call<HealthyDocBean> call;
    private long value;
    private LoadingDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_doc);
        ButterKnife.bind(this);

        CommonUtils.initSystemBar(this);
        CommonUtils.setToolbar(this, healthyDocToolbar);
        CommonUtils.setWebView(healthyDocWeb);

        sMap.put("a", PARAMS_A);
        sMap.put("m", PARAMS_M);
        value = System.currentTimeMillis();
        sMap.put("timestamp", String.valueOf(value));

        getParams();

        if (!hasHealthyDoc) {
            CommonUtils.showOrHideView(noHealthyDoc, true);
        } else {
            if (NetworkUtil.isNetWorkConnected()) {
                requestData();
            } else {
                Toast.makeText(this, "网络不给力", Toast.LENGTH_SHORT).show();
                CommonUtils.showOrHideView(noHealthyDoc, true);
            }
        }
    }

    private void requestData() {
        showLoadingDialog();
        String bind = patientId;
        String sign = CommonUtils.computeSign(value + bind);
        sMap.put("bind", bind);
        sMap.put("sign", sign);
        sMap.put("patientid", patientId);

        RetrofitServices.HealthyDocService service =
                RetrofitUtil.createService(RetrofitServices.HealthyDocService.class);
        call = service.getData(sMap);
        call.enqueue(new Callback<HealthyDocBean>() {
            @Override
            public void onResponse(Call<HealthyDocBean> call, Response<HealthyDocBean> response) {
                if (BuildConfig.DEBUG) {
                    RetrofitUtil.checkRetrofitRequest(call.request());
                }

                hideLoadingDialog();
                if (response.isSuccessful()) {
                    HealthyDocBean bean = response.body();
                    updatePage(bean);
                } else {
                    Toast.makeText(HealthyDocActivity.this, "刷新数据失败",
                            Toast.LENGTH_SHORT).show();
                    CommonUtils.showOrHideView(noHealthyDoc, true);
                }
            }

            @Override
            public void onFailure(Call<HealthyDocBean> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    RetrofitUtil.checkRetrofitRequest(call.request());
                }

                Toast.makeText(HealthyDocActivity.this, "刷新数据失败",
                        Toast.LENGTH_SHORT).show();
                CommonUtils.showOrHideView(noHealthyDoc, true);
                hideLoadingDialog();
            }
        });
    }

    private void updatePage(HealthyDocBean bean) {
        if (bean != null) {
            if (bean.getCode() == 10000) { // 返回数据成功
                String data = bean.getData();
                if (data != null && !data.equals("")) {
                    CommonUtils.showOrHideView(noHealthyDoc, false);
                    DLog.d(TAG, "healthy doc data = " + data);
                    Document document = Jsoup.parse(data);
                    String content = document.select("body").first().text();
                    healthyDocWeb.loadData(content, "text/html", null);
                } else {
                    CommonUtils.showOrHideView(noHealthyDoc, true);
                }
            } else {
                Toast.makeText(HealthyDocActivity.this, bean.getMsg(),
                        Toast.LENGTH_SHORT).show();
                CommonUtils.showOrHideView(noHealthyDoc, true);
            }
        } else {
            Toast.makeText(HealthyDocActivity.this, "刷新数据失败",
                    Toast.LENGTH_SHORT).show();
            CommonUtils.showOrHideView(noHealthyDoc, true);
        }
    }

    private void getParams() {
        patientId = getIntent().getStringExtra(HealthyUserInfoDetailActivity.PATIENT_ID_INTENT_KEY);
        // 默认有健康档案
        hasHealthyDoc = getIntent().getBooleanExtra(HAS_HEALTHY_DOC_INTENT_KEY, true);
    }

    private void showLoadingDialog() {
        dialogFragment = LoadingDialogFragment.newInstance("加载中……");
        dialogFragment.show(getSupportFragmentManager(), TAG);
    }

    private void hideLoadingDialog() {
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (call != null && !call.isCanceled()) {
            call.cancel();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
