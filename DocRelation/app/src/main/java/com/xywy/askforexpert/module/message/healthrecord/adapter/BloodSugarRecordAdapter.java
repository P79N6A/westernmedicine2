package com.xywy.askforexpert.module.message.healthrecord.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by yuwentao on 16/5/26.
 */
public class BloodSugarRecordAdapter extends BaseAdapter {
    /**
     * 标题的item
     */
    public static final int ITEM_MONTH = 0;

    /**
     * 二级菜单的item
     */
    public static final int ITEM_DAY = 1;

    private List<BloodSugarAdapterModel> mDataList = new ArrayList<>();

    private Context context;

    private LayoutInflater inflater;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd日 hh:mm");


    public BloodSugarRecordAdapter(Context context, List<BloodSugarAdapterModel> mList) {
        this.context = context;
        this.mDataList.addAll(mList);
        inflater = LayoutInflater.from(context);
    }

    public void setDataList(final List<BloodSugarAdapterModel> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int arg0) {

        return arg0;
    }

    //返回 代表某一个样式 的 数值
    @Override
    public int getItemViewType(int position) {
//        int i = Integer.valueOf(mDataList.get(position).type);
        return Integer.valueOf(mDataList.get(position).type);
    }

    //两个样式 返回2
    @Override
    public int getViewTypeCount() {

        return 2;
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BloodSugarAdapterModel bloodSugarAdapterModel = mDataList.get(position);


        String content = "";
        if (bloodSugarAdapterModel.getBloodSugarRecordItem() != null) {
            content = bloodSugarAdapterModel.getBloodSugarRecordItem().toString();
        }
        Log.d("NETWORK_TAG", "getview key  " + bloodSugarAdapterModel.getType() + "     content   " + content);

        int type = getItemViewType(position);

        HolderMonth holder1 = null;
        ViewHolderDay holder2 = null;

        if (convertView == null) {
            //选择某一个样式。。
            switch (type) {
                case ITEM_MONTH:
                    convertView = inflater.inflate(R.layout.time_line_month_item, null);
                    holder1 = new HolderMonth(convertView);

                    convertView.setTag(holder1);
                    break;
                case ITEM_DAY:
                    convertView = inflater.inflate(R.layout.time_line_day_item, null);
                    holder2 = new ViewHolderDay(convertView);

                    convertView.setTag(holder2);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case ITEM_MONTH:
                    holder1 = (HolderMonth) convertView.getTag();
                    break;
                case ITEM_DAY:
                    holder2 = (ViewHolderDay) convertView.getTag();

                    break;

                default:
                    break;
            }

        }

        switch (type) {
            case ITEM_MONTH:
                String date = bloodSugarAdapterModel.getTime();
                String dateArray[] = date.split("-");
                holder1.mYear.setText(dateArray[0]);
                holder1.mMonth.setText(dateArray[1]);
                break;

            case ITEM_DAY:

                if (getItemViewType(position - 1) == ITEM_MONTH) {
                    holder2.mSepLine.setVisibility(View.INVISIBLE);
                } else if (getYploadTimeString(position - 1).equals(getYploadTimeString(position))) {
                    holder2.mSepLine.setVisibility(View.INVISIBLE);
                } else {
                    holder2.mSepLine.setVisibility(View.VISIBLE);
                }

                holder2.mContainer.removeAllViews();

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
                if (holder2.mContainer.getChildCount() != 0) {
                    params.setMargins(0, DensityUtils.dp2px(7), 0, 0);
                }

                View cellView = inflater.inflate(R.layout.day_item_cell, null);
                ViewHolderCell viewHolderCell = new ViewHolderCell(cellView);
                holder2.mContainer.addView(cellView);

                //上传时间
                String time = "";
                if (!TextUtils.isEmpty(bloodSugarAdapterModel.getBloodSugarRecordItem()
                        .getClsj())) {
                    time = dateFormat.format(new Date(Integer.valueOf(bloodSugarAdapterModel.getBloodSugarRecordItem()
                            .getClsj()) * 1000L));
                }
                viewHolderCell.mTime.setText(time);

                //血糖测量时间的描述
                viewHolderCell.mTimeDescription.setText(getDescription(bloodSugarAdapterModel.getBloodSugarRecordItem()
                        .getSjd()));

                //测量值
                viewHolderCell.mTvValue.setText(bloodSugarAdapterModel.getBloodSugarRecordItem()
                        .getClz());

                //血糖高低说明
                float clz = Float.parseFloat(bloodSugarAdapterModel.getBloodSugarRecordItem()
                        .getClz());
                setBloodSugarIndicators(clz, bloodSugarAdapterModel.getBloodSugarRecordItem().getSjd(), viewHolderCell.mIndicatorView);


                //cellView.setLayoutParams(params);

                break;

            default:
                break;
        }

        return convertView;
    }


    private void setBloodSugarIndicators(float clz, String sjd, TextView tv) {

        tv.setVisibility(View.INVISIBLE);
        if ("kongfu".equals(sjd)) {
            if (clz >= 6.1) {
                tv.setBackgroundDrawable(context.getResources()
                        .getDrawable(R.drawable.health_up_bg));
                tv.setText("血糖偏高");
                tv.setTextColor(context.getResources()
                        .getColor(R.color.color_ffa147));

                Drawable rightDrawable = context.getResources()
                        .getDrawable(R.drawable.blood_sugar_up);
                rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

                tv.setCompoundDrawables(null, null, rightDrawable, null);
                tv.setVisibility(View.VISIBLE);
            } else if (clz <= 3.9) {
                Drawable rightDrawable = context.getResources()
                        .getDrawable(R.drawable.blood_sugar_down);
                rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

                tv.setBackgroundDrawable(context.getResources()
                        .getDrawable(R.drawable.health_down_bg));
                tv.setTextColor(context.getResources()
                        .getColor(R.color.color_44b790));
                tv.setText("血糖偏低");
                tv.setCompoundDrawables(null, null, rightDrawable, null);
                tv.setVisibility(View.VISIBLE);
            }

        } else {
            if (clz >= 7.8) {
                tv.setBackgroundDrawable(context.getResources()
                        .getDrawable(R.drawable.health_up_bg));
                tv.setText("血糖偏高");
                tv.setTextColor(context.getResources()
                        .getColor(R.color.color_ffa147));

                Drawable rightDrawable = context.getResources()
                        .getDrawable(R.drawable.blood_sugar_up);
                rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

                tv.setCompoundDrawables(null, null, rightDrawable, null);
                tv.setVisibility(View.VISIBLE);
            } else if (clz <= 4.4) {
                Drawable rightDrawable = context.getResources()
                        .getDrawable(R.drawable.blood_sugar_down);
                rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

                tv.setBackgroundDrawable(context.getResources()
                        .getDrawable(R.drawable.health_down_bg));
                tv.setTextColor(context.getResources()
                        .getColor(R.color.color_44b790));
                tv.setText("血糖偏低");
                tv.setCompoundDrawables(null, null, rightDrawable, null);
                tv.setVisibility(View.VISIBLE);
            }


        }


    }

    private String getDescription(String sjd) {
        String temp = "";
        if ("kongfu".equals(sjd)) {
            temp = "空   腹";
        } else if ("zaocanhou".equals(sjd)) {
            temp = "早餐后";

        } else if ("wucanqian".equals(sjd)) {
            temp = "午餐前";

        } else if ("wucanhou".equals(sjd)) {
            temp = "午餐后";

        } else if ("wancanqian".equals(sjd)) {
            temp = "晚餐前";

        } else if ("wancanhou".equals(sjd)) {
            temp = "晚餐后";

        } else if ("shuiqian".equals(sjd)) {
            temp = "睡   前";

        }

        return temp + "  血糖";
    }

    /**
     * 获取测量的时间
     *
     * @return
     */
    private String getYploadTimeString(int position) {
        BloodSugarAdapterModel bloodSugarAdapterModel = mDataList.get(position);
        String time = "";
        if (!TextUtils.isEmpty(mDataList.get(position)
                .getBloodSugarRecordItem()
                .getClsj())) {
            time = dateFormat.format(new Date(Integer.valueOf(bloodSugarAdapterModel.getBloodSugarRecordItem()
                    .getClsj()) * 1000L));
        }
        if (time.length() >= 2) {
            time = time.substring(0, 2);
        }

        return time;

    }


    static class ViewHolderDay {
        @Bind(R.id.sep_line)
        View mSepLine;

        @Bind(R.id.sepTopView)
        View mSepTopView;


        @Bind(R.id.Container)
        LinearLayout mContainer;

        ViewHolderDay(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class HolderMonth {
        @Bind(R.id.month)
        TextView mMonth;

        @Bind(R.id.year)
        TextView mYear;

        HolderMonth(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderCell {


        @Bind(R.id.time)
        TextView mTime;

        @Bind(R.id.time_description)
        TextView mTimeDescription;

        @Bind(R.id.tv_value)
        TextView mTvValue;


        @Bind(R.id.indicator_view)
        TextView mIndicatorView;

        ViewHolderCell(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
