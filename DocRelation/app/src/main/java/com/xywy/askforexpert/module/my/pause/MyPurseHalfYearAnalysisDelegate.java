package com.xywy.askforexpert.module.my.pause;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.MyPurse.BillMonthInfo;
import com.xywy.askforexpert.model.MyPurse.MyPurseItemBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *  Created by xgxg on 2018/6/20.
 */

public class MyPurseHalfYearAnalysisDelegate implements ItemViewDelegate<MyPurseItemBean> {
    Context context;
    private PieChart mPieChart;
    private LineChart mLineChart;
    private List<BillMonthInfo> atrend;
    private ArrayList<Integer> colors;
    protected DecimalFormat mFormat;

    public MyPurseHalfYearAnalysisDelegate(Context context){
        this.context = context;
        mFormat = new DecimalFormat("#0.00");
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_my_purse_recent_half_year_analysis;
    }

    @Override
    public boolean isForViewType(MyPurseItemBean item, int position) {
        return MyPurseItemBean.TYPE_LIST == item.getType();
    }

    @Override
    public void convert(ViewHolder holder, MyPurseItemBean myPurseItemBean, int position) {
        if(null != myPurseItemBean){
//            TextView tv_html = holder.getView(R.id.tv_html);

//            tv_html.setText(
//                    Html.fromHtml(
//                            "5.劳务报酬税计算方法，均是按照国家规定。可通过以下网址，直接计算出具体所缴劳务税费。细则请点击" +"<br>"+
//                                    "<a href='http://shiju.tax861.gov.cn/bnss/gr_002.asp'>http://shiju.tax861.gov.cn/bnss/gr_002.asp</a> "+"；")
//            );
//            tv_html.setMovementMethod(LinkMovementMethod.getInstance());
            atrend = myPurseItemBean.atrend;
            if(null !=atrend && atrend.size() == 0){
                holder.getView(R.id.tv_analysis).setVisibility(View.GONE);
                holder.getView(R.id.ll_analysis).setVisibility(View.GONE);
                holder.getView(R.id.tv_atrend).setVisibility(View.GONE);
                holder.getView(R.id.mLineChart).setVisibility(View.GONE);
                ViewStub viewstub = holder.getView(R.id.viewstub);
                viewstub.inflate();
                return;
            }
            TextView tv_imwd = holder.getView(R.id.tv_imwd);
            TextView tv_question_department = holder.getView(R.id.tv_question_department);
            TextView tv_home_doctor = holder.getView(R.id.tv_home_doctor);
            TextView tv_call_doctor = holder.getView(R.id.tv_call_doctor);
            TextView tv_other = holder.getView(R.id.tv_other);
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            //数据和颜色
            colors = new ArrayList<Integer>();
            if(0!=myPurseItemBean.club_percent){
                entries.add(new PieEntry(myPurseItemBean.club_percent, ""));
                colors.add(context.getResources().getColor(R.color.color_50ceff));
                tv_question_department.setText("问题广场 "+myPurseItemBean.club_percent+"%");
            }else {
                holder.getView(R.id.ll_question_departmen).setVisibility(View.GONE);
            }

            if(0!=myPurseItemBean.immediate_percent){
                entries.add(new PieEntry(myPurseItemBean.immediate_percent, ""));
                colors.add(context.getResources().getColor(R.color.color_717acb));
                tv_imwd.setText("即时问答 "+myPurseItemBean.immediate_percent+"%");
            }else {
                holder.getView(R.id.ll_imwd).setVisibility(View.GONE);

            }

            if(0!=myPurseItemBean.familydoc_percent){
                entries.add(new PieEntry(myPurseItemBean.familydoc_percent, ""));
                colors.add(context.getResources().getColor(R.color.color_8dd06b));
                tv_home_doctor.setText("家庭医生 "+myPurseItemBean.familydoc_percent+"%");
            }else {
                holder.getView(R.id.ll_home_doctor).setVisibility(View.GONE);
            }

            if(0!=myPurseItemBean.dhysdoc_percent){
                entries.add(new PieEntry(myPurseItemBean.dhysdoc_percent, ""));
                colors.add(context.getResources().getColor(R.color.color_ffb760));
                tv_call_doctor.setText("电话医生 "+myPurseItemBean.dhysdoc_percent+"%");
            }else {
                holder.getView(R.id.ll_call_doctor).setVisibility(View.GONE);
            }


            if(0!=myPurseItemBean.other_percent){
                entries.add(new PieEntry(myPurseItemBean.other_percent, ""));
                colors.add(context.getResources().getColor(R.color.c_00c8aa));
                tv_other.setText("其他 "+myPurseItemBean.other_percent+"%");
            }else {
                holder.getView(R.id.ll_other).setVisibility(View.GONE);
            }

            mPieChart = holder.getView(R.id.mPieChart);
            mLineChart = holder.getView(R.id.mLineChart);

            initPieChart(mPieChart,entries);
            if(null != atrend){
                mMonths = new String[atrend.size()];
                for (int i = 0; i < atrend.size(); i++) {
                    if(null !=atrend.get(i)){
                        mMonths[i] = atrend.get(i).month+"月";
                    }
                }
                initChart(mLineChart);
                // add data
                setData(atrend);
            }

        }
    }

    private String[] mMonths = new String[] {};

    private void setData(List<BillMonthInfo> atrend) {
        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < atrend.size(); i++) {
            if(null != atrend.get(i)){
                values.add(new Entry(i, atrend.get(i).jixiao));
            }
        }
        LineDataSet set1;
        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mLineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");
            set1.setDrawIcons(false);
            // set the line to be drawn like this "- - - - - -"
            // 不显示坐标点的数据
            set1.setDrawValues(true);
            // 不显示定位线
            set1.setHighlightEnabled(false);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            // set data
            mLineChart.setData(data);
        }
    }

    private void initPieChart(PieChart mPieChart,ArrayList<PieEntry> entries) {
        mPieChart.setNoDataText("暂无数据");
        mPieChart.setUsePercentValues(true);//使用百分比显示
        mPieChart.getDescription().setEnabled(false);
//        mPieChart.setExtraOffsets(5, 10, 5, 5);//设置pieChart图表上下左右的偏移，类似于外边距

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
//        mPieChart.setCenterText(generateCenterSpannableText());
        mPieChart.setDrawHoleEnabled(false);
//        mPieChart.setHoleColor(Color.WHITE);
//
//        mPieChart.setTransparentCircleColor(Color.WHITE);
//        mPieChart.setTransparentCircleAlpha(110);
//
//        mPieChart.setHoleRadius(58f);
//        mPieChart.setTransparentCircleRadius(61f);
        if(entries.size() == 1){
            mPieChart.setDrawCenterText(true);
            mPieChart.setCenterText("100.0 %");
            mPieChart.setCenterTextColor(context.getResources().getColor(R.color.white));
        }else {
            mPieChart.setDrawCenterText(false);
        }

        mPieChart.setRotationAngle(0);
        if(entries.size()!=0){
            //设置数据
            setData(entries);
        }
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mPieChart.getLegend().setEnabled(false);

        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);
    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


//        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new IValueFormatter(){
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                return mFormat.format(value)+ " %";
            }
        });
        data.setValueTextSize(11f);
        if(entries.size() == 1){
            data.setValueTextColor(Color.TRANSPARENT);
        }else {
            data.setValueTextColor(Color.WHITE);
        }

        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }

    /**
     * 初始化图表
     *
     * @param chart 原始图表
     * @return 初始化后的图表
     */
    public LineChart initChart(LineChart chart) {
        // 不显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        // 不显示表格颜色
        chart.setDrawGridBackground(false);
        // 不可以缩放
        chart.setScaleEnabled(false);
        // 不显示y轴右边的值
        chart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        // 显示x轴
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTextSize(12);//这里设置了字体大小，导致x轴上的字显示不全
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                LogUtils.i("value="+value+"----(int) value="+(int) value);
                return mMonths[(int) value % mMonths.length];
            }
        });
        xAxis.setGranularity(1f);
        YAxis yAxis = chart.getAxisLeft();
        // 显示y轴
        yAxis.setDrawAxisLine(true);
        yAxis.setTextSize(12);
        yAxis.setAxisMinimum(0);
        return chart;
    }
}
