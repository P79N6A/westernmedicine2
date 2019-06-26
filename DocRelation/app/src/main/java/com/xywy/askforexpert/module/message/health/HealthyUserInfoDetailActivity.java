package com.xywy.askforexpert.module.message.health;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.retrofitTools.HttpRequestCallBack;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitServices;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitUtil;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.base.BaseBean;
import com.xywy.askforexpert.model.healthy.HealthyUserInfoDetailBean;
import com.xywy.askforexpert.module.message.healthrecord.BloodPresureRecordActivity;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 签约居民详细资料
 *
 * @author Jack Fang
 */
public class HealthyUserInfoDetailActivity extends AppCompatActivity {
    public static final String PATIENT_ID_INTENT_KEY = "patientId";
    private static final String TAG = "HealthyUserInfoDetailActivity";
    private static final String PARAM_A = "areamedical";
    private static final String PARAM_M = "patientinfor";
    @Bind(R.id.healthy_user_info_detail_toolbar)
    Toolbar healthyUserInfoDetailToolbar;
    @Bind(R.id.healthy_user_info_avatar)
    ImageView healthyUserInfoAvatar;
    @Bind(R.id.healthy_user_info_name)
    TextView healthyUserInfoName;
    @Bind(R.id.healthy_user_info_gender)
    TextView healthyUserInfoGender;
    @Bind(R.id.healthy_user_info_age)
    TextView healthyUserInfoAge;
    @Bind(R.id.to_healthy_doc)
    RelativeLayout toHealthyDoc;
    @Bind(R.id.to_healthy_dynamic)
    RelativeLayout toHealthyDynamic;
    @Bind(R.id.healthy_user_info_button)
    AppCompatButton healthyUserInfoButton;
    private Map<String, String> sMap = new HashMap<>();

    private String userId;
    private String patientId;
    private String patientHXId;
    private DisplayImageOptions mOptions;

    /**
     * 是否有健康档案
     */
    private boolean hasHealthyDoc;

    /**
     * 是否有血压图
     */
    private boolean hasBloodPressure;

    /**
     * 是否有血糖图
     */
    private boolean hasBloodSugar;

    private String avatarUrl;
    private String name;
    private Call<BaseBean<HealthyUserInfoDetailBean>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_user_info_detail);
        ButterKnife.bind(this);

        CommonUtils.initSystemBar(this);
        CommonUtils.setToolbar(this, healthyUserInfoDetailToolbar);

        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        if (YMUserService.isGuest()) {
            userId = "0";
        } else {
            userId = YMApplication.getPID();
        }

        getParams();

        sMap.put("a", PARAM_A);
        sMap.put("m", PARAM_M);
        sMap.put("timestamp", String.valueOf(System.currentTimeMillis()));

        toHealthyDoc.setClickable(false);
        toHealthyDynamic.setClickable(false);
        if (NetworkUtil.isNetWorkConnected()) {
            requestData();
        } else {
            Toast.makeText(this, "网络不给力", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 请求数据
     */
    private void requestData() {
        String bind = patientId;
        String sign = CommonUtils.computeSign(sMap.get("timestamp") + bind);
        sMap.put("bind", bind);
        sMap.put("sign", sign);
        sMap.put("patientid", patientId);

        RetrofitServices.HealthyUserInfoDetailService service =
                RetrofitUtil.createService(RetrofitServices.HealthyUserInfoDetailService.class);
        call = service.getData(sMap);
        RetrofitUtil.getInstance().enqueueCall(call, new HttpRequestCallBack<HealthyUserInfoDetailBean>() {
            @Override
            public void onSuccess(BaseBean<HealthyUserInfoDetailBean> baseBean) {
                toHealthyDoc.setClickable(true);
                toHealthyDynamic.setClickable(true);
                updatePageData(baseBean.getData());
            }

            @Override
            public void onFailure(RequestState state, String msg) {
                Toast.makeText(HealthyUserInfoDetailActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 刷新页面数据
     */
    private void updatePageData(HealthyUserInfoDetailBean data) {
        DLog.d(TAG, "XM = " + data.getXm() + ", xb = " + data.getXb() + ", nl = " + data.getNl()
                + ", id = " + data.getId() + ", hxid = " + data.getHxid());
        avatarUrl = TextUtils.isEmpty(data.getTxdz()) ? "" : data.getTxdz();
        name = TextUtils.isEmpty(data.getXm()) ? "" : data.getXm();
        String gender = TextUtils.isEmpty(data.getXb()) ? "" : data.getXb();
        String age = TextUtils.isEmpty(data.getNl()) ? "" : data.getNl();

        ImageLoader.getInstance().displayImage(avatarUrl, healthyUserInfoAvatar, mOptions);
        healthyUserInfoName.setText(name);
        healthyUserInfoGender.setText(gender);
        if (age == null || age.equals("")) {
            healthyUserInfoAge.setVisibility(View.GONE);
        } else {
            healthyUserInfoAge.setVisibility(View.VISIBLE);
            healthyUserInfoAge.setText(age + "岁");
        }

        hasHealthyDoc = data.getSfjd() == 1;
        hasBloodPressure = data.getXy() == 1;
        hasBloodSugar = data.getXt() == 1;

        patientHXId = data.getHxid().replaceAll(Constants.QXYL_USER_HXID_MARK, "");
    }

    private void getParams() {
        patientId = getIntent().getStringExtra(PATIENT_ID_INTENT_KEY);
        DLog.d(TAG, "healthy patient id = " + patientId);
    }

    @OnClick({R.id.to_healthy_doc, R.id.to_healthy_dynamic, R.id.healthy_user_info_button})
    public void onClick(View view) {
        if (!NetworkUtil.isNetWorkConnected()) {
            Toast.makeText(this, "网络不给力", Toast.LENGTH_SHORT).show();
            return;
        }

        if (YMUserService.isGuest()) {
            DialogUtil.LoginDialog(new YMOtherUtils(this).context);
            return;
        }

        switch (view.getId()) {
            // 查看健康档案
            case R.id.to_healthy_doc:
                StatisticalTools.eventCount(this, "HealthFile");
                Intent intent = new Intent(this, HealthyDocActivity.class);
                intent.putExtra(PATIENT_ID_INTENT_KEY, patientId);
                intent.putExtra(HealthyDocActivity.HAS_HEALTHY_DOC_INTENT_KEY, hasHealthyDoc);
                startActivity(intent);
                break;

            // 查看健康动态
            case R.id.to_healthy_dynamic:
                StatisticalTools.eventCount(this, "HealthDynamics");
                BloodPresureRecordActivity.startActivity(this, patientId);
                break;

            // 发消息
            case R.id.healthy_user_info_button:
                if (name != null && avatarUrl != null) {
                    StatisticalTools.eventCount(this, "HomeSend");
                    DLog.d(TAG, "healthy chat info = " + patientHXId + ", " + name + ", " + patientId);
                    Intent chatIntent = new Intent(this, ChatMainActivity.class);
                    chatIntent.putExtra("userId", "did_" + patientHXId);
                    chatIntent.putExtra("username", name);
                    chatIntent.putExtra("toHeadImge", avatarUrl);
                    chatIntent.putExtra(ChatMainActivity.IS_HEALTHY_USER_KEY, true);
                    chatIntent.putExtra(ChatMainActivity.DIAL_ID, patientId);
                    startActivity(chatIntent);
                } else {
                    Toast.makeText(this, "数据正在刷新，请稍后……", Toast.LENGTH_SHORT).show();
                }
                break;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (call != null && !call.isCanceled()) {
            call.cancel();
        }


    }
}
