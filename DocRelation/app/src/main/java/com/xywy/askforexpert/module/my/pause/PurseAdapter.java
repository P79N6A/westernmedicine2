package com.xywy.askforexpert.module.my.pause;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.SectionedBaseAdapter;
import com.xywy.askforexpert.model.PurseInfo;

import java.util.List;

public class PurseAdapter extends SectionedBaseAdapter {

    private Context context;

    private List<PurseInfo> puresInfos;

    public PurseAdapter(Context context, List<PurseInfo> puresInfos) {
        this.context = context;
        this.puresInfos = puresInfos;
    }

    public void bindData(List<PurseInfo> puresInfos) {
        this.puresInfos = puresInfos;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int section, int position) {
        return getItem(getSectionForPosition(position),
                getPositionInSectionForPosition(position));
    }

    @Override
    public long getItemId(int section, int position) {
        // TODO Auto-generated method stub
        return section;
    }

    @Override
    public int getSectionCount() {
        // TODO Auto-generated method stub
        return puresInfos.size();
    }

    @Override
    public int getCountForSection(int section) {
        // TODO Auto-generated method stub
        return puresInfos.get(section).getBillLists().size();
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
        if (puresInfos.get(section) != null) {
            ((TextView) layout.findViewById(R.id.tv_header)).setText(puresInfos
                    .get(section).getDate());
        }

        return layout;
    }

    @Override
    public View getItemView(int section, int position, View convertView,
                            ViewGroup parent) {
        RelativeLayout layout = null;
        if (convertView == null) {

            layout = (RelativeLayout) LayoutInflater.from(context).inflate(
                    R.layout.phd_content_item, null);
        } else {
            layout = (RelativeLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.tv_title)).setText(puresInfos
                .get(section).getBillLists().get(position).getHreason());
        ((TextView) layout.findViewById(R.id.tv_price)).setText(puresInfos
                .get(section).getBillLists().get(position).getHnum());
        ((TextView) layout.findViewById(R.id.tv_time)).setText(puresInfos
                .get(section).getBillLists().get(position).getDtime());
        return layout;
    }

}
