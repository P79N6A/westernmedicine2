package com.xywy.askforexpert.module.message.healthrecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.interfaces.callback.HttpCallback;
import com.xywy.askforexpert.appcommon.utils.DateUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.healthrecord.BloodSugarModel;
import com.xywy.askforexpert.module.message.healthrecord.adapter.BloodSugarAdapterModel;
import com.xywy.askforexpert.module.message.healthrecord.adapter.BloodSugarRecordAdapter;
import com.xywy.askforexpert.module.message.healthrecord.utils.DataUtils;
import com.xywy.askforexpert.module.message.healthrecord.utils.Errors;
import com.xywy.askforexpert.widget.module.healthrecord.CalibrationView;
import com.xywy.askforexpert.widget.module.healthrecord.CustomScrollView;
import com.xywy.askforexpert.widget.module.healthrecord.TrendView;
import com.xywy.retrofit.net.BaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yuwentao on 16/5/30.
 */
public class BloodSugarRecordActivity extends BaseActivity {

    public static final String PATIENTID_INTENT_KEY = "PATIENTID_INTENT_KEY";

    @Bind(R.id.scrollView)
    CustomScrollView mScrollView;

    @Bind(R.id.trendviewContainer)
    LinearLayout mTrendviewContainer;

    @Bind(R.id.calibrationView)
    CalibrationView mCalibrationView;

    @Bind(R.id.blood_pressure_listview)
    ListView mBloodPressureListview;

    @Bind(R.id.kongfu)
    LinearLayout mKongfu;

    @Bind(R.id.zaocanhou)
    LinearLayout mZaocanhou;

    @Bind(R.id.wucanqian)
    LinearLayout mWucanqian;

    @Bind(R.id.wucanhou)
    LinearLayout mWucanhou;

    @Bind(R.id.wancanqian)
    LinearLayout mWancanqian;

    @Bind(R.id.wancanhou)
    LinearLayout mWancanhou;

    @Bind(R.id.shuiqian)
    LinearLayout mShuiqian;

    @Bind(R.id.noDataTv)
    TextView mNoDataTv;

    private TrendView mTrendview;

    private List<Boolean> which;

    private List<ArrayList<Float>> pointList = new ArrayList<>();


    private List<Float> kongfuList = new ArrayList<Float>();

    private List<Float> zaocanhouList = new ArrayList<Float>();

    private List<Float> wucanqianList = new ArrayList<Float>();

    private List<Float> wucanhouList = new ArrayList<Float>();

    private List<Float> wancanqianList = new ArrayList<Float>();

    private List<Float> wancanhouList = new ArrayList<Float>();

    private List<Float> shuiqianList = new ArrayList<Float>();

    private BloodSugarRecordAdapter adapter;


    private String patientId;

    public static void startActivity(Activity activity, String patientId) {
        Intent i = new Intent();
        i.putExtra(PATIENTID_INTENT_KEY, patientId);
        i.setClass(activity, BloodSugarRecordActivity.class);
        activity.startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.blood_sugar_record_activity);
        ButterKnife.bind(this);
        initView();
        initMyData();

        requestData();

        initChart(new ArrayList<ArrayList<Float>>(), 11);

        adapter = new BloodSugarRecordAdapter(BloodSugarRecordActivity.this, new ArrayList<BloodSugarAdapterModel>());
        mBloodPressureListview.setAdapter(adapter);

    }

    private void initMyData() {
        patientId = getIntent().getStringExtra(PATIENTID_INTENT_KEY);
    }

    private void initChart(List<ArrayList<Float>> pointl, int maxValue) {


        //服务返回的最大值过小，刻度线就会重叠在一起
        if (maxValue < 8) {
            maxValue = 8;
        }
        which = new ArrayList<>();
        which.add(true);
        which.add(true);
        which.add(true);
        which.add(true);
        which.add(true);
        which.add(true);
        which.add(true);

        ArrayList colorList = new ArrayList();
        colorList.add(getResources().getColor(R.color.color_fffb8c));
        colorList.add(getResources().getColor(R.color.color_87f358));
        colorList.add(getResources().getColor(R.color.color_26ea9e));
        colorList.add(getResources().getColor(R.color.color_26d5ea));
        colorList.add(getResources().getColor(R.color.color_2667ea));
        colorList.add(getResources().getColor(R.color.color_b026ea));
        colorList.add(getResources().getColor(R.color.color_ea266b));

        final List<Float> lines = new ArrayList();
        lines.add(0f);
        lines.add(3.9f);
        lines.add(4.4f);
        lines.add(6.1f);
        lines.add(7.8f);

        mTrendview = new TrendView(BloodSugarRecordActivity.this, pointl);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .MATCH_PARENT);
        mTrendview.setMaxValue(maxValue);
        mTrendview.setLayoutParams(params);
        mTrendview.setColorList(colorList);
        //左右的空隙
        mTrendview.paddingLeftRightSpace = 16;
        mTrendview.setShowAxisList(lines);
        mTrendview.setShowIntegerText(false);
        mTrendview.setMaxValue(maxValue);

        mTrendviewContainer.removeAllViews();
        mTrendviewContainer.addView(mTrendview);
        mTrendview.setVisibility(View.INVISIBLE);
        mTrendview.registListener();

        Handler handler = new Handler() {
        };
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                mTrendview.setVisibility(View.VISIBLE);
                mTrendview.initCalibrationYValue();
                mTrendview.setShowWhichLine(which);
                mTrendview.invalidate();

                mCalibrationView.setShowIntegerText(false);
                mCalibrationView.setAxisLineValueList(lines);
                mCalibrationView.setShowAxisList(mTrendview.getCalibrationYValue());
                mCalibrationView.invalidate();
            }
        }, 100);
    }

    private void initView() {
//        commonTitleView.setBackgroundColor(getResources().getColor(R.color.color_EF9854));
        titleCommonTv.setVisibility(View.VISIBLE);
        titleCommonTv.setText("血糖");
        leftImgBtn.setVisibility(View.VISIBLE);
        leftCommonImgBtn.setVisibility(View.VISIBLE);
        rightCommonImgBtn.setVisibility(View.VISIBLE);
        rightCommonImgBtn.setImageResource(R.drawable.icon_blood_pressure_selector);
        leftImgBtn.setImageResource(R.drawable.icon_blood_pressure_selector);
        leftImgBtn.setVisibility(View.GONE);
    }

    private void requestData() {

//        String patientId = "1";
        long curTimeMillis = System.currentTimeMillis();
        String startTime = DateUtils.getTimeBeaforAMoth();
        String endTime = DateUtils.getRecordTime("" + curTimeMillis);

        HealthService.requestBloodSugar(patientId, startTime, endTime, new HttpCallback() {
            @Override
            public void onSuccess(Object obj) {
                BaseData res = DataUtils.parseData(obj);
                if (null != res.getData() && res.getData() instanceof String) {
                    if (res.getCode() == 10000) {
                        LogUtils.e("显示数据");
                        //T.shortToast( "显示数据");
                        parseData((String) res.getData());
                    } else if (res.getCode() == Errors.BASE_GSON_ERROR) {
                        ToastUtils.shortToast( "血糖数据格式错误");
                    } else if (res.getCode() == Errors.BASE_PARSER_ERROR) {
                        ToastUtils.shortToast( "血糖数据格式错误");
                    } else {
                        ToastUtils.shortToast( "未知错误:" + res.getCode() + " msg:" + res.getMsg());
                    }
                } else {
                    ToastUtils.shortToast( "血糖数据为空");
                }
            }

            @Override
            public void onFailed(Throwable throwable, int errorNo, String strMsg) {
                ToastUtils.shortToast( "网络连接不可用，请检查网络");
            }
        });

    }

    private void parseData(String result) {
        JSONObject jsonObject = null;
        try {
            ArrayList<String> adapterMonthList = new ArrayList<>();
            LinkedHashMap<String, ArrayList<BloodSugarAdapterModel>> hashMap = new LinkedHashMap();
            String currentMonth = "";

            jsonObject = new JSONObject(result);
            JSONObject list_data = jsonObject.optJSONObject("list");
            if (null == list_data) {
                mNoDataTv.setVisibility(View.VISIBLE);
                mBloodPressureListview.setVisibility(View.GONE);
                return;
            }
            JSONObject max_data = jsonObject.optJSONObject("max");
            Iterator iterator = list_data.keys();
            ArrayList<String> keyList = new ArrayList<String>();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Log.d("NETWORK_TAG", "add key  " + key);
                keyList.add(key);
            }

            kongfuList.clear();
            zaocanhouList.clear();
            wucanqianList.clear();
            wucanhouList.clear();
            wancanqianList.clear();
            wancanhouList.clear();
            shuiqianList.clear();

            Collections.sort(keyList);

            for (String key : keyList) {

                JSONArray dayArray = list_data.optJSONArray(key);
                float temp_kongfu = 0;
                float temp_zaocanhou = 0;
                float temp_wucanqian = 0;
                float temp_wucanhou = 0;
                float temp_wancanqian = 0;
                float temp_wancanhou = 0;
                float temp_shuiqian = 0;

                if (dayArray.length() > 0) {

                    ArrayList<BloodSugarAdapterModel> everyDayItem = new ArrayList<>();

                    for (int i = 0; i < dayArray.length(); i++) {
                        BloodSugarModel.BloodSugarRecordItem everyeasurement = new BloodSugarModel.BloodSugarRecordItem();

                        JSONObject jsonObject1 = (JSONObject) dayArray.get(i);
                        everyeasurement.setClsj((String) jsonObject1.opt("clsj"));
                        everyeasurement.setClz((String) jsonObject1.opt("clz"));
                        everyeasurement.setSjd((String) jsonObject1.opt("sjd"));

                        if ("kongfu".equals(everyeasurement.getSjd())) {
                            temp_kongfu = Float.parseFloat(everyeasurement.getClz());
                        } else if ("zaocanhou".equals(everyeasurement.getSjd())) {
                            temp_zaocanhou = Float.parseFloat(everyeasurement.getClz());

                        } else if ("wucanqian".equals(everyeasurement.getSjd())) {
                            temp_wucanqian = Float.parseFloat(everyeasurement.getClz());

                        } else if ("wucanhou".equals(everyeasurement.getSjd())) {
                            temp_wucanhou = Float.parseFloat(everyeasurement.getClz());

                        } else if ("wancanqian".equals(everyeasurement.getSjd())) {
                            temp_wancanqian = Float.parseFloat(everyeasurement.getClz());

                        } else if ("wancanhou".equals(everyeasurement.getSjd())) {
                            temp_wancanhou = Float.parseFloat(everyeasurement.getClz());

                        } else if ("shuiqian".equals(everyeasurement.getSjd())) {
                            temp_shuiqian = Float.parseFloat(everyeasurement.getClz());
                        }

                        //获得列表中的数据
                        String tempKey = key.substring(0, 7);
                        if (!currentMonth.equals(tempKey)) {
                            currentMonth = tempKey;
                            adapterMonthList.add(tempKey);
                            hashMap.put(tempKey, new ArrayList<BloodSugarAdapterModel>());

                        }

                        BloodSugarAdapterModel bloodSugarAdapterModel = new BloodSugarAdapterModel();
                        bloodSugarAdapterModel.setBloodSugarRecordItem(everyeasurement);
                        bloodSugarAdapterModel.setType("1");
                        everyDayItem.add(bloodSugarAdapterModel);
                        Log.d("NETWORK_TAG", "add HASHMAP key  " + bloodSugarAdapterModel.getType() + "  " +
                                "   content   " + everyeasurement.toString());

                    }

                    hashMap.get(currentMonth)
                            .addAll(0, everyDayItem);

                }

                //else {
                //    String tempKey = key.substring(0, 7);
                //    if (!currentMonth.equals(tempKey)) {
                //        currentMonth = tempKey;
                //        adapterMonthList.add(tempKey);
                //        hashMap.put(tempKey, new ArrayList<BloodSugarAdapterModel>());
                //
                //    }
                //    BloodSugarAdapterModel bloodSugarAdapterModel = new BloodSugarAdapterModel();
                //    bloodSugarAdapterModel.setBloodSugarRecordItem(new BloodSugarRecordItem());
                //    bloodSugarAdapterModel.setType("1");
                //    hashMap.get(tempKey)
                //            .add(bloodSugarAdapterModel);
                //
                //}

                kongfuList.add(temp_kongfu);
                zaocanhouList.add(temp_zaocanhou);
                wucanqianList.add(temp_wucanqian);
                wucanhouList.add(temp_wucanhou);
                wancanqianList.add(temp_wancanqian);
                wancanhouList.add(temp_wancanhou);
                shuiqianList.add(temp_shuiqian);
            }

            String maxKongfu = max_data.optString("kongfu");
            String maxZaocanhou = max_data.optString("zaocanhou");
            String maxWucanqian = max_data.optString("wucanqian");
            String maxWucanhou = max_data.optString("wucanhou");
            String maxWancanqian = max_data.optString("wancanqian");
            String maxWancanhou = max_data.optString("wancanhou");
            String maxShuiqian = max_data.optString("shuiqian");

            float intMaxKongfu = 0;
            float intMaxZaocanhou = 0;
            float floatMaxWucanqian = 0;
            float floatMaxWucanhou = 0;
            float floatMaxWancanqian = 0;
            float floatMaxWancanhou = 0;
            float intMaxShuiqian = 0;
            if (!TextUtils.isEmpty(maxKongfu)) {
                intMaxKongfu = Float.parseFloat(maxKongfu);
            }
            if (!TextUtils.isEmpty(maxZaocanhou)) {
                intMaxZaocanhou = Float.parseFloat(maxZaocanhou);
            }
            if (!TextUtils.isEmpty(maxWucanqian)) {
                floatMaxWucanqian = Float.parseFloat(maxWucanqian);
            }
            if (!TextUtils.isEmpty(maxWucanhou)) {
                floatMaxWucanhou = Float.parseFloat(maxWucanhou);
            }
            if (!TextUtils.isEmpty(maxWancanqian)) {
                floatMaxWancanqian = Float.parseFloat(maxWancanqian);
            }
            if (!TextUtils.isEmpty(maxWancanhou)) {
                floatMaxWancanhou = Float.parseFloat(maxWancanhou);
            }
            if (!TextUtils.isEmpty(maxShuiqian)) {
                intMaxShuiqian = Float.parseFloat(maxShuiqian);
            }

            float maxValue = Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(intMaxKongfu,
                    intMaxZaocanhou), floatMaxWucanqian), floatMaxWucanhou), floatMaxWancanqian),
                    floatMaxWancanhou), intMaxShuiqian);

            //显示折线图
            pointList.clear();
            pointList.add((ArrayList<Float>) kongfuList);
            pointList.add((ArrayList<Float>) zaocanhouList);
            pointList.add((ArrayList<Float>) wucanqianList);
            pointList.add((ArrayList<Float>) wucanhouList);
            pointList.add((ArrayList<Float>) wancanqianList);
            pointList.add((ArrayList<Float>) wancanhouList);
            pointList.add((ArrayList<Float>) shuiqianList);
            DecimalFormat df = new DecimalFormat("#");

            if (hasNetData(pointList)) {
                initChart(pointList, Integer.valueOf(df.format(maxValue)));

            }

            /**
             * 准备底部列表的数据
             */
            ArrayList<BloodSugarAdapterModel> adapterDataList = new ArrayList<>();

            for (int i = adapterMonthList.size() - 1; i >= 0; i--) {
                String key = adapterMonthList.get(i);
                BloodSugarAdapterModel bloodSugarAdapterModel = new BloodSugarAdapterModel();
                bloodSugarAdapterModel.setTime(key);
                bloodSugarAdapterModel.setType("0");
                adapterDataList.add(bloodSugarAdapterModel);
                //Collections.reverse(hashMap.get(key));
                adapterDataList.addAll(hashMap.get(key));
            }
            if (adapterDataList.size() == 0) {
                //展示无数据的ui
                mBloodPressureListview.setVisibility(View.GONE);
                mNoDataTv.setVisibility(View.VISIBLE);
            } else {
                adapter.setDataList(adapterDataList);
                mBloodPressureListview.setVisibility(View.VISIBLE);
                mNoDataTv.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setShowWhichLine(int index) {
        which.set(index, !which.get(index));
        mTrendview.setShowWhichLine(which);
        mTrendview.invalidate();
    }

    @OnClick({R.id.kongfu, R.id.zaocanhou, R.id.wucanqian, R.id.wucanhou, R.id.wancanqian, R.id.wancanhou, R.id
            .shuiqian, R.id.right_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.kongfu:
                setShowWhichLine(0);
                updateTopTab();
                break;
            case R.id.zaocanhou:
                setShowWhichLine(1);
                updateTopTab();
                break;
            case R.id.wucanqian:
                setShowWhichLine(2);
                updateTopTab();
                break;
            case R.id.wucanhou:
                setShowWhichLine(3);
                updateTopTab();
                break;
            case R.id.wancanqian:
                setShowWhichLine(4);
                updateTopTab();
                break;
            case R.id.wancanhou:
                setShowWhichLine(5);
                updateTopTab();
                break;
            case R.id.shuiqian:
                setShowWhichLine(6);
                updateTopTab();
                break;
            case R.id.right_btn:
                StatisticalTools.eventCount(BloodSugarRecordActivity.this, "BloodPressure");
                finish();
                BloodPresureRecordActivity.startActivity(BloodSugarRecordActivity.this, patientId);
                break;
            default:
                break;
        }
    }

    private void updateTopTab() {
        mKongfu.setBackgroundResource(which.get(0) ? R.drawable.white_border_bg : R.drawable.ffb48c_border_bg);
        mZaocanhou.setBackgroundResource(which.get(1) ? R.drawable.white_border_bg : R.drawable.ffb48c_border_bg);
        mWucanqian.setBackgroundResource(which.get(2) ? R.drawable.white_border_bg : R.drawable.ffb48c_border_bg);
        mWucanhou.setBackgroundResource(which.get(3) ? R.drawable.white_border_bg : R.drawable.ffb48c_border_bg);
        mWancanqian.setBackgroundResource(which.get(4) ? R.drawable.white_border_bg : R.drawable.ffb48c_border_bg);
        mWancanhou.setBackgroundResource(which.get(5) ? R.drawable.white_border_bg : R.drawable.ffb48c_border_bg);
        mShuiqian.setBackgroundResource(which.get(6) ? R.drawable.white_border_bg : R.drawable.ffb48c_border_bg);
    }

    private boolean hasNetData(List<ArrayList<Float>> lists) {
        if (lists.size() > 0) {
            if (lists.get(0).size() == 0) {
                return false;
            }
        } else {
            return false;

        }
        return true;
    }

}
