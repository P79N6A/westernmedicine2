package com.xywy.askforexpert.module.discovery.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.IdNameBean;

import java.util.List;


/**
 * 医院列表 adapter stone
 */
public class HospitalAdapter extends BaseAdapter {

    private List<IdNameBean> list;
    private Context context;

    public HospitalAdapter(List<IdNameBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void initView(View view) {
        view.scrollTo(0, 0);
    }

    public void setList(List<IdNameBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_hospital, null);
            holder.tvTextView = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTextView.setText(list.get(position).name);

        return convertView;
    }

//    public interface OnItemDeleteClickListener {
//        void onRightClick(View v, int position);
//    }

    class ViewHolder {
//        public TextView tv_edit;
        public TextView tvTextView;
//		public RelativeLayout relRightDelete;
    }

}
