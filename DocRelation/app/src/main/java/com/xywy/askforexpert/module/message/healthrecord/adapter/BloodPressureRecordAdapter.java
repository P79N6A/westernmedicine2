package com.xywy.askforexpert.module.message.healthrecord.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xywy.askforexpert.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by yuwentao on 16/5/26.
 */
public class BloodPressureRecordAdapter extends BaseAdapter {

    /**
     * 标题的item
     */
    public static final int ITEM_MONTH = 0;

    /**
     * 二级菜单的item
     */
    public static final int ITEM_DAY = 1;

    private List<BloodPresureAdapterModel> mList = new ArrayList<>();

    private Context context;

    private LayoutInflater inflater;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd日 hh:mm");


    public BloodPressureRecordAdapter(Context context, List<BloodPresureAdapterModel> mList) {
        this.context = context;
        this.mList.addAll(mList);
        inflater = LayoutInflater.from(context);
    }

    public void setList(final List<BloodPresureAdapterModel> l) {
        Log.d("NETWORK_TAG", mList.size() + "     adapter   setList");
        mList.clear();
        mList.addAll(l);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {

        return arg0;
    }

    //返回 代表某一个样式 的 数值
    @Override
    public int getItemViewType(int position) {

        int i = Integer.valueOf(mList.get(position).type);
        return i;
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
        int type = getItemViewType(position);

        Log.d("NETWORK_TAG", mList.size() + "     adapter list size");

        BloodPresureAdapterModel bloodPresureAdapterModel = mList.get(position);
        HolderMonth holder1 = null;
        ViewHolderCell holder2 = null;

        if (convertView == null) {
            //选择某一个样式。。
            switch (type) {
                case ITEM_MONTH:
                    convertView = inflater.inflate(R.layout.time_line_month_item, null);
                    holder1 = new HolderMonth(convertView);

                    convertView.setTag(holder1);
                    break;
                case ITEM_DAY:
                    convertView = inflater.inflate(R.layout.blood_presure_listitem, null);
                    holder2 = new ViewHolderCell(convertView);

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
                    holder2 = (ViewHolderCell) convertView.getTag();

                    break;
                default:
                    break;

            }

        }

        switch (type) {
            case ITEM_MONTH:
                String date = bloodPresureAdapterModel.getTime();
                String dateArray[] = date.split("-");
                holder1.mYear.setText(dateArray[0]);
                holder1.mMonth.setText(dateArray[1]);
                break;

            case ITEM_DAY:

                if (getItemViewType(position - 1) == ITEM_MONTH) {
                    holder2.mSepLine.setVisibility(View.INVISIBLE);
                } else {
                    holder2.mSepLine.setVisibility(View.VISIBLE);
                }

                //血压
                int ssy = (int) Float.parseFloat(bloodPresureAdapterModel.getEveryeasurement()
                        .getSsy());
                int szy = (int) Float.parseFloat(bloodPresureAdapterModel.getEveryeasurement()
                        .getSzy());
                holder2.mBloodPresureTv.setText(ssy + "/" + szy);

                //心率
                int heartRate = (int) Float.parseFloat(bloodPresureAdapterModel.getEveryeasurement()
                        .getXl());
                holder2.mXlTv.setText(heartRate + "");

                //上传时间
                String time = "";
                if (!bloodPresureAdapterModel
                        .getEveryeasurement()
                        .getJlsj().equals("")) {

                    time = dateFormat.format(new Date(Integer.valueOf(bloodPresureAdapterModel
                            .getEveryeasurement()
                            .getJlsj()) * 1000L));

                }
                holder2.mTime.setText(time);

                //高低压标识
                setBloodPresureIndicators(Integer.valueOf(ssy), Integer.valueOf(szy), holder2
                        .mBloodPresureIndicators);

                setHeartRateIndicators(heartRate, holder2.mHeartRateIndicators);
                break;
            default:
                break;

        }

        return convertView;

    }

    private void setBloodPresureIndicators(int ssy, int szy, TextView tv) {
        tv.setVisibility(View.INVISIBLE);
        if (ssy >= 130 || szy >= 85) {
            tv.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.health_up_bg));
            tv.setText("血压偏高");
            tv.setTextColor(context.getResources()
                    .getColor(R.color.color_ffa147));

            Drawable rightDrawable = context.getResources()
                    .getDrawable(R.drawable.blood_sugar_up);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

            tv.setCompoundDrawables(null, null, rightDrawable, null);
            tv.setVisibility(View.VISIBLE);
        } else if (ssy <= 85 || szy <= 60) {

            Drawable rightDrawable = context.getResources()
                    .getDrawable(R.drawable.blood_sugar_down);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

            tv.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.health_down_bg));
            tv.setTextColor(context.getResources()
                    .getColor(R.color.color_44b790));
            tv.setText("血压偏低");
            tv.setCompoundDrawables(null, null, rightDrawable, null);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.INVISIBLE);
        }

    }

    private void setHeartRateIndicators(int xl, TextView tv) {
        tv.setVisibility(View.INVISIBLE);
        if (xl >= 100) {
            tv.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.health_up_bg));
            tv.setText("心率偏高");
            Drawable rightDrawable = context.getResources()
                    .getDrawable(R.drawable.blood_sugar_up);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());

            tv.setCompoundDrawables(null, null, rightDrawable, null);
            tv.setTextColor(context.getResources()
                    .getColor(R.color.color_ffa147));
            tv.setVisibility(View.VISIBLE);
        } else if (xl <= 60) {
            tv.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.health_down_bg));
            tv.setTextColor(context.getResources()
                    .getColor(R.color.color_44b790));
            tv.setText("心率偏低");
            Drawable rightDrawable = context.getResources()
                    .getDrawable(R.drawable.blood_sugar_down);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            tv.setCompoundDrawables(null, null, rightDrawable, null);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.INVISIBLE);
        }

    }


    static class ViewHolderCell {
        @Bind(R.id.sep_line)
        View mSepLine;

        @Bind(R.id.time)
        TextView mTime;

        @Bind(R.id.blood_presure_tv)
        TextView mBloodPresureTv;

        @Bind(R.id.blood_presure_indicators)
        TextView mBloodPresureIndicators;

        @Bind(R.id.xl_tv)
        TextView mXlTv;

        @Bind(R.id.heart_rate_indicators)
        TextView mHeartRateIndicators;


        ViewHolderCell(View view) {
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
}

















