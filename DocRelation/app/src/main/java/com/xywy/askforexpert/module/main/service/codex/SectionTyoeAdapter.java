package com.xywy.askforexpert.module.main.service.codex;

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
import com.xywy.askforexpert.model.SectionSoftInfo;

import java.util.List;

/**
 * 药典 适配器 stone
 *
 * @author 王鹏
 * @2015-5-10下午4:09:34
 */
public class SectionTyoeAdapter extends BaseExpandableListAdapter {

    // List<ArrayList<String>> child_list;
    public SparseBooleanArray selectionMap;
    private Context context;
    private List<SectionSoftInfo> group_list;
    private LayoutInflater inflater;

    public SectionTyoeAdapter(Context context) {
        this.context = context;

        // this.child_list = child_list;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<SectionSoftInfo> group_list) {
        this.group_list = group_list;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return group_list.get(groupPosition).getChild_list()
                .get(childPosition);
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
            convertView.setTag(gholder);
        } else {
            gholder = (GroupHolder) convertView.getTag();
        }
//		boolean isCheck = selectionMap.get(groupPosition);
        if (isExpanded) {
            gholder.txt.setTextColor(context.getResources().getColor(
                    R.color.color_00c8aa));
        } else {
            gholder.txt.setTextColor(context.getResources().getColor(
                    R.color.my_textcolor));

        }
        gholder.txt.setText(group_list.get(groupPosition).getName());

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

            convertView = inflater.inflate(R.layout.medu_child_item, null);
            gholder.main = (RelativeLayout) convertView.findViewById(R.id.main);
            gholder.txt = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(gholder);
        } else {
            gholder = (ItemHolder) convertView.getTag();
        }
        if (childPosition == 0) {
            gholder.main.setBackgroundResource(R.drawable.codex_expand_child2_selector);
        } else {
            gholder.main.setBackgroundResource(R.drawable.codex_expand_child_selector);
        }
        gholder.txt.setText(group_list.get(groupPosition).getChild_list().get(childPosition).getName());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return group_list.get(groupPosition).getChild_list().size();
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

    public void init() {
        selectionMap = new SparseBooleanArray();
        for (int i = 0; i < group_list.size(); i++) {
            selectionMap.put(i, false);
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
