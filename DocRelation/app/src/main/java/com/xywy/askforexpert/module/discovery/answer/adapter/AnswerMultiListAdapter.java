package com.xywy.askforexpert.module.discovery.answer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.answer.show.BaseItem;
import com.xywy.askforexpert.module.discovery.answer.utils.DataProvider;

import java.util.List;

import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListAdapter;

/**
 * Created by bailiangjin on 16/4/20.
 */
public class AnswerMultiListAdapter extends MultiLevelListAdapter {

    protected Context mContext;

    public AnswerMultiListAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public List<?> getSubObjects(Object object) {
        return DataProvider.getSubItems((BaseItem) object);
    }

    @Override
    public boolean isExpandable(Object object) {
        return DataProvider.isExpandable((BaseItem) object);
    }

    @Override
    public View getViewForObject(Object object, View convertView, ItemInfo itemInfo) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_multi_level_list, null);
            viewHolder.rl_root = (RelativeLayout) convertView.findViewById(R.id.rl_root);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
            viewHolder.arrowView = (ImageView) convertView.findViewById(R.id.dataItemArrow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameView.setText(((BaseItem) object).getName());

        if (itemInfo.isExpandable()) {
            viewHolder.arrowView.setVisibility(View.VISIBLE);
            viewHolder.arrowView.setImageResource(itemInfo.isExpanded() ?
                    R.drawable.arrow_up : R.drawable.arrow_down);
            viewHolder.rl_root.setBackgroundColor(mContext.getResources().getColor(R.color.multi_list_view_group_bg));
        } else {
            viewHolder.arrowView.setVisibility(View.GONE);
            viewHolder.rl_root.setBackgroundColor(mContext.getResources().getColor(R.color.multi_list_view_item_bg));

        }


        return convertView;
    }

    private class ViewHolder {
        RelativeLayout rl_root;
        TextView nameView;
        ImageView arrowView;
    }


}
