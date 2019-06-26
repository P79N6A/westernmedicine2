package com.xywy.askforexpert.module.message.healthrecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.xywy.askforexpert.model.healthrecord.BloodPresureModel;
import com.xywy.askforexpert.module.message.healthrecord.adapter.BloodPressureRecordAdapter;
import com.xywy.askforexpert.module.message.healthrecord.adapter.BloodPresureAdapterModel;
import com.xywy.askforexpert.module.message.healthrecord.utils.DataUtils;
import com.xywy.askforexpert.module.message.healthrecord.utils.Errors;
import com.xywy.askforexpert.widget.module.healthrecord.CalibrationView;
import com.xywy.askforexpert.widget.module.healthrecord.CustomScrollView;
import com.xywy.askforexpert.widget.module.healthrecord.TrendView;
import com.xywy.retrofit.net.BaseData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 血压记录页面
 */
public class BloodPresureRecordActivity extends BaseActivity {

    public static final String PATIENTID_INTENT_KEY = "PATIENTID_INTENT_KEY";

    @Bind(R.id.scrollView)
    CustomScrollView mScrollView;

    @Bind(R.id.trendviewContainer)
    LinearLayout mTrendviewContainer;

    @Bind(R.id.calibrationView)
    CalibrationView mCalibrationView;

    @Bind(R.id.blood_pressure_listview)
    ListView mBloodPressureListview;

    @Bind(R.id.selectedValue)
    TextView mSelectedValue;

    @Bind(R.id.noDataTv)
    TextView mNoDataTv;

    private TrendView mTrendview;

    private TrendView.OnPointClick mOnPointClick;


    private String patientId;


    private List<ArrayList<Float>> pointList = new ArrayList<>();

    private List<Float> ssyList = new ArrayList<Float>();

    private List<Float> szyList = new ArrayList<Float>();

    private List<Float> xlList = new ArrayList<Float>();

    private BloodPressureRecordAdapter bloodPresureAdapter;

    public static void startActivity(Activity activity, String patientId) {
        Intent i = new Intent();
        i.putExtra(PATIENTID_INTENT_KEY, patientId);
        i.setClass(activity, BloodPresureRecordActivity.class);
        activity.startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_presure_record_activity);
        CommonUtils.initSystemBar(this);
        ButterKnife.bind(this);
        initView();
        initMyData();

        requestData();
        addTrendView(new ArrayList<ArrayList<Float>>(), 100);

        bloodPresureAdapter = new BloodPressureRecordAdapter(BloodPresureRecordActivity.this, new
                ArrayList<BloodPresureAdapterModel>());
        mBloodPressureListview.setAdapter(bloodPresureAdapter);

    }

    private void addTrendView(List<ArrayList<Float>> pointl, int maxValue) {

        ArrayList colorList = new ArrayList();
        colorList.add(getResources().getColor(R.color.color_8526c2));
        colorList.add(getResources().getColor(R.color.color_19b48e));
        colorList.add(getResources().getColor(R.color.color_4b60f4));

        final ArrayList<Float> lines = new ArrayList();
        lines.add(0f);
        lines.add(60f);
        lines.add(90f);
        lines.add(120f);

        mTrendview = new TrendView(BloodPresureRecordActivity.this, pointl);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .MATCH_PARENT);
        mTrendview.setMaxValue(maxValue > 130 ? maxValue : 130);
        mTrendview.setLayoutParams(params);
        mTrendview.setColorList(colorList);
        mTrendview.setOnPointClick(mOnPointClick);
        //左右的空隙
        mTrendview.paddingLeftRightSpace = 16;
        mTrendview.setShowAxisList(lines);

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

                mCalibrationView.setAxisLineValueList(lines);
                mCalibrationView.setShowAxisList(mTrendview.getCalibrationYValue());
                mCalibrationView.invalidate();
            }
        }, 100);
    }

    private void requestData() {

//        String patientId = "1";
        long curTimeMillis = System.currentTimeMillis();
        String startTime = DateUtils.getTimeBeaforAMoth();
        String endTime = DateUtils.getRecordTime("" + curTimeMillis);

        HealthService.requestBloodPressure(patientId, startTime, endTime, new HttpCallback() {
            @Override
            public void onSuccess(Object obj) {
                BaseData res = DataUtils.parseData(obj);
                if (null != res.getData() && res.getData() instanceof String) {
                    if (res.getCode() == 10000) {
                        parseData((String) res.getData());
                        LogUtils.e("显示数据");
                        //T.shortToast( "显示数据");
                    } else if (res.getCode() == Errors.BASE_GSON_ERROR) {
                        ToastUtils.shortToast( "血压数据格式错误");
                    } else if (res.getCode() == Errors.BASE_PARSER_ERROR) {
                        ToastUtils.shortToast( "血压数据格式错误");
                    } else {
                        ToastUtils.shortToast( "未知错误:" + res.getCode() + " msg:" + res.getMsg());
                    }
                } else {
                    ToastUtils.shortToast( "血压数据为空");
                }
            }

            @Override
            public void onFailed(Throwable throwable, int errorNo, String strMsg) {
                ToastUtils.shortToast( "网络连接不可用，请检查网络");
            }
        });

    }

    /**
     * 解析数据 并显示
     *
     * @param result
     */
    private void parseData(String result) {
        try {

            LinkedHashMap<String, ArrayList<BloodPresureAdapterModel>> hashMap = new LinkedHashMap();
            ArrayList<String> adapterMonthList = new ArrayList<>();
            String currentMonth = "";

            JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonObject_max_data = jsonObject.getJSONObject("max");
            BloodPresureModel bloodPresureModel = new BloodPresureModel();
            bloodPresureModel.setSsy(jsonObject_max_data.optString("ssy"));
            bloodPresureModel.setSzy(jsonObject_max_data.optString("szy"));
            bloodPresureModel.setXl(jsonObject_max_data.optString("xl"));

            JSONObject list_data = jsonObject.optJSONObject("list");
//        if(null==list_data){
//            return;
//        }
            Iterator iterator = list_data.keys();
            ArrayList<String> keyList = new ArrayList<String>();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                //Log.d("network", "key  " + key);
                keyList.add(key);
            }

            Collections.sort(keyList);

            //Collections.reverse(keyList);

            ssyList.clear();
            szyList.clear();
            xlList.clear();
            for (String key : keyList) {


                String tempKey = key.substring(0, 7);


                JSONArray jsonArray = list_data.optJSONArray(key);

                BloodPresureModel.Everyeasurement everyeasurement = new BloodPresureModel.Everyeasurement();
                int time = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                    int jlsj = Integer.valueOf(jsonObject1.opt("jlsj")
                            .toString());
                    if (jlsj > time) {
                        time = jlsj;
                        everyeasurement.setJlsj((String) jsonObject1.opt("jlsj"));
                        everyeasurement.setSzy((String) jsonObject1.opt("szy"));
                        everyeasurement.setSsy((String) jsonObject1.opt("ssy"));
                        everyeasurement.setXl((String) jsonObject1.opt("xl"));
                    }


                    //用于展示血压页面底部列表的数据
                    BloodPresureModel.Everyeasurement everyeasurementForList = new BloodPresureModel.Everyeasurement();
                    everyeasurementForList.setJlsj((String) jsonObject1.opt("jlsj"));
                    everyeasurementForList.setSzy((String) jsonObject1.opt("szy"));
                    everyeasurementForList.setSsy((String) jsonObject1.opt("ssy"));
                    everyeasurementForList.setXl((String) jsonObject1.opt("xl"));

                    if (!currentMonth.equals(tempKey)) {
                        currentMonth = tempKey;
                        adapterMonthList.add(tempKey);
                        hashMap.put(tempKey, new ArrayList<BloodPresureAdapterModel>());
                        BloodPresureAdapterModel bloodPresureAdapterModel = new BloodPresureAdapterModel();
                        bloodPresureAdapterModel.setEveryeasurement(everyeasurementForList);
                        if (!everyeasurementForList.hasNoData()) {
                            hashMap.get(tempKey)
                                    .add(bloodPresureAdapterModel);
                        }

                    } else {
                        BloodPresureAdapterModel bloodPresureAdapterModel = new BloodPresureAdapterModel();
                        bloodPresureAdapterModel.setEveryeasurement(everyeasurementForList);
                        if (!everyeasurementForList.hasNoData()) {
                            hashMap.get(tempKey)
                                    .add(bloodPresureAdapterModel);
                        }

                    }

                }
                ssyList.add(Float.valueOf(everyeasurement.getSsy()));
                szyList.add(Float.valueOf(everyeasurement.getSzy()));
                xlList.add(Float.valueOf(everyeasurement.getXl()));


            }

            /**
             * 准备折线图的数据
             */
            //取得最大值
            float max = 0;
            if (Float.valueOf(bloodPresureModel.getSsy()) >= Float.valueOf(bloodPresureModel.getSzy())) {
                max = Float.valueOf(bloodPresureModel.getSsy());
            } else {
                if (Float.valueOf(bloodPresureModel.getXl()) >= max) {
                    max = Float.valueOf(bloodPresureModel.getXl());
                }
            }
            pointList.clear();
            pointList.add((ArrayList<Float>) ssyList);
            pointList.add((ArrayList<Float>) szyList);
            pointList.add((ArrayList<Float>) xlList);
            if (max <= 0) {
                max = 300;
            }
            if (max < 120) {
                max = 120;
            }
            addTrendView(pointList, (int) max);
            if (pointList.size() > 0) {
                OnPointClick(pointList.get(0).size() - 1);
            }

            /**
             * 准备底部列表的数据
             */
            ArrayList<BloodPresureAdapterModel> adapterDataList = new ArrayList<>();

            for (int i = adapterMonthList.size() - 1; i >= 0; i--) {
                String key = adapterMonthList.get(i);

                if (hashMap.get(key).size() == 0) {
                    continue;
                }
                BloodPresureAdapterModel bloodPresureAdapterModel = new BloodPresureAdapterModel();
                bloodPresureAdapterModel.setTime(key);
                bloodPresureAdapterModel.setType("0");
                adapterDataList.add(bloodPresureAdapterModel);
                Collections.reverse(hashMap.get(key));
                adapterDataList.addAll(hashMap.get(key));
            }

            if (adapterDataList.size() > 0) {
                bloodPresureAdapter.setList(adapterDataList);
                mBloodPressureListview.setVisibility(View.VISIBLE);
                mNoDataTv.setVisibility(View.GONE);
            } else {//展示无数据的ui
                mBloodPressureListview.setVisibility(View.GONE);
                mNoDataTv.setVisibility(View.VISIBLE);


            }

        } catch (Exception e) {
            mBloodPressureListview.setVisibility(View.GONE);
            mNoDataTv.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    private void initView() {
//        commonTitleView.setBackgroundColor(getResources().getColor(R.color.color_7066E2));
        titleCommonTv.setVisibility(View.VISIBLE);
        leftImgBtn.setVisibility(View.VISIBLE);
        titleCommonTv.setText("血压");
        leftCommonImgBtn.setVisibility(View.VISIBLE);
        rightCommonImgBtn.setVisibility(View.VISIBLE);
        rightCommonImgBtn.setImageResource(R.drawable.icon_blood_sugar_selector);
        leftImgBtn.setImageResource(R.drawable.icon_blood_sugar_selector);
        leftImgBtn.setVisibility(View.GONE);
    }

    private void initMyData() {
        patientId = getIntent().getStringExtra(PATIENTID_INTENT_KEY);
        mOnPointClick = new TrendView.OnPointClick() {
            @Override
            public void OnPointClickevent(final int index) {
                OnPointClick(index);
            }

        };
    }

    //折线图上的点，被点击之后的事件处理，
    private void OnPointClick(int index) {
        String value = ssyList.get(index)
                .intValue() + "/" + szyList.get(index)
                .intValue() + "mmHg" + " " + xlList.get(index)
                .intValue() + "次/分钟";
        mSelectedValue.setText(value);
    }

    @OnClick(R.id.right_btn)
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.right_btn:
                finish();
                StatisticalTools.eventCount(BloodPresureRecordActivity.this, "BloodSugar");
                BloodSugarRecordActivity.startActivity(BloodPresureRecordActivity.this, patientId);
                break;
            default:
                break;
        }
    }
}



























