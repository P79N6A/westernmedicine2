package com.xywy.askforexpert.module.my.pause;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.SectionedBaseAdapter;
import com.xywy.askforexpert.model.MyPurse.BillDayInfo;
import com.xywy.askforexpert.model.MyPurse.BillTimeInfo;

import java.util.List;

public class BillAdapter extends SectionedBaseAdapter {

    private Context context;

    private List<BillDayInfo> billDayInfos;
    private TextView tv_title;
    private TextView tv_price;
    private TextView tv_time;

    public BillAdapter(Context context, List<BillDayInfo> billDayInfos) {
        this.context = context;
        this.billDayInfos = billDayInfos;
    }

    public void bindData(List<BillDayInfo> billDayInfos) {
        this.billDayInfos = billDayInfos;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int section, int position) {
        return getItem(getSectionForPosition(position),
                getPositionInSectionForPosition(position));
    }

    @Override
    public long getItemId(int section, int position) {
        return section;
    }

    @Override
    public int getSectionCount() {
        return billDayInfos.size();
    }

    @Override
    public int getCountForSection(int section) {
        return billDayInfos.get(section).bill.size();
    }

    @Override
    public View getSectionHeaderView(int section, View convertView,
                                     ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.phd_list_item,
                    null);
        } else {
            layout = (LinearLayout) convertView;
        }
        if (billDayInfos.get(section) != null) {
            ((TextView) layout.findViewById(R.id.tv_header)).setText(billDayInfos.get(section).date);
        }
        return layout;
    }

    @Override
    public View getItemView(int section, int position, View convertView,
                            ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            layout = (LinearLayout) LayoutInflater.from(context).inflate(
                    R.layout.phd_content_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }

        tv_title = (TextView) layout.findViewById(R.id.tv_title);
        tv_price = (TextView) layout.findViewById(R.id.tv_price);
        tv_time = (TextView) layout.findViewById(R.id.tv_time);
        BillDayInfo billDayInfo = billDayInfos.get(section);
        if(null != billDayInfo ){
            List<BillTimeInfo> bill = billDayInfo.bill;
            if(null != bill){
                BillTimeInfo billItem = bill.get(position);
                if(null != billItem ){
                    tv_title.setText(TextUtils.isEmpty(billItem.hreason)?"":billItem.hreason);
                    tv_price.setText(TextUtils.isEmpty(billItem.hnum)?"":billItem.hnum);
                    tv_time.setText(TextUtils.isEmpty(billItem.htime)?"":billItem.htime);
                }else {
                    setEmpty(tv_title, tv_price, tv_time);
                }
            }else {
                setEmpty(tv_title, tv_price, tv_time);
            }
        }else {
            setEmpty(tv_title, tv_price, tv_time);
        }
        return layout;
    }

    private void setEmpty(TextView tv_title,TextView tv_price,TextView tv_time){
        tv_title.setText("");
        tv_price.setText("");
        tv_time.setText("");
    }
}
