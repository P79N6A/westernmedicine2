package com.xywy.askforexpert.module.my.clinic.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 家庭医生开通服务 价格
 *
 * @author 王鹏
 * @2015-5-26下午5:55:03
 */
public class BaseFamMoneyAdapter extends BaseExpandableListAdapter {

    public SparseBooleanArray selectionMap_week;
    public SparseBooleanArray selectionMap_month;
    private Context context;
    // private List<CodexInfo> group_list;
    private List<String> group_list;
    private List<ArrayList<String>> child_list;
    private LayoutInflater inflater;

    public BaseFamMoneyAdapter(Context context) {
        this.context = context;

        // this.child_list = child_list;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<String> group_list,
                        List<ArrayList<String>> child_list) {
        this.group_list = group_list;
        this.child_list = child_list;
        if (group_list != null) {
            init_week();
            init_moth();
        }

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return child_list.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final GroupHolder gholder;
        if (convertView == null) {
            gholder = new GroupHolder();
            // LayoutInflater inflater = (LayoutInflater) context
            // .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.medu_group_item, null);

            gholder.txt = (TextView) convertView.findViewById(R.id.tv_text);
            gholder.img = (ImageView) convertView.findViewById(R.id.img_right);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupHolder) convertView.getTag();
        }
        // boolean isCheck = selectionMap.get(groupPosition);
        if (isExpanded) {
            gholder.txt.setTextColor(context.getResources().getColor(
                    R.color.purse_blue));
            gholder.img.setBackgroundResource(R.drawable.enter_right);
        } else {
            gholder.txt.setTextColor(context.getResources().getColor(
                    R.color.my_textcolor));
            gholder.img.setBackgroundResource(R.drawable.enght_3);
        }
        gholder.txt.setText(group_list.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemHolder gholder;
        if (convertView == null) {
            gholder = new ItemHolder();
            // LayoutInflater inflater = (LayoutInflater) context
            // .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.fam_money_child_item, null);
            gholder.main = (RelativeLayout) convertView.findViewById(R.id.main);
            gholder.txt = (TextView) convertView.findViewById(R.id.tv_text);
            gholder.img = (ImageView) convertView.findViewById(R.id.img_ok);
            convertView.setTag(gholder);
        } else {
            gholder = (ItemHolder) convertView.getTag();
        }
        boolean ischeck = false;
        if (groupPosition == 0) {
            ischeck = selectionMap_week.get(childPosition);
        } else if (groupPosition == 1) {
            ischeck = selectionMap_month.get(childPosition);
        }

        if (ischeck) {
            gholder.img.setVisibility(View.VISIBLE);
        } else {
            gholder.img.setVisibility(View.GONE);
        }
        gholder.txt.setText(child_list.get(groupPosition).get(childPosition) + " 元");

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return child_list.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return group_list.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return group_list.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    public void init_week() {
        selectionMap_week = new SparseBooleanArray();

        for (int j = 0; j < child_list.get(0).size(); j++) {
            selectionMap_week.put(j, false);

        }
    }

    public void init_moth() {
        selectionMap_month = new SparseBooleanArray();
        for (int j = 0; j < child_list.get(1).size(); j++) {
            selectionMap_month.put(j, false);

        }

    }

    class GroupHolder {
        public TextView txt;
        public ImageView img;

    }

    class ItemHolder {
        public ImageView img;
        public TextView txt;
        public RelativeLayout main;

    }

}
