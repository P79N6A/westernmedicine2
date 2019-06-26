package com.xywy.askforexpert.module.discovery.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.model.IdNameBean;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;

import java.util.List;


/**
 * IM快捷回复adapter stone
 */
public class StringListViewAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    //    private int mRightWidth = 0;
    private MyCallBack mMyCallBack;

    public StringListViewAdapter(List<String> list, Context context,MyCallBack callBack) {
        this.list = list;
        this.context = context;
        this.mMyCallBack = callBack;
    }
//    public StringListViewAdapter(List<String> list, Context context, int rightWidth, OnItemDeleteClickListener listener) {
//        this.list = list;
//        this.context = context;
//        this.mRightWidth = rightWidth;
//        this.listener = listener;
//    }

    public void initView(View view) {
        view.scrollTo(0, 0);
    }

    public void setList(List<String> list) {
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
            convertView = View.inflate(context, R.layout.im_fast_reply_item, null);
            holder.tvTextView = (TextView) convertView.findViewById(R.id.tv_text);
            holder.tv_edit = (TextView) convertView.findViewById(R.id.tv_edit);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTextView.setText(list.get(position));
//        GlobalContent.viewMap4IM.put(list.get(position), convertView);
        // 右侧删除按钮数据设置
//		LinearLayout.LayoutParams llParam1 = new LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		holder.tvTextView.setLayoutParams(llParam1);
//		LinearLayout.LayoutParams llParam2 = new LayoutParams(mRightWidth,
//				LayoutParams.MATCH_PARENT);
//		holder.relRightDelete.setLayoutParams(llParam2);
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mMyCallBack) {
                    mMyCallBack.onClick(new IdNameBean(Constants.EDIT, String.valueOf(position)));
                }
            }
        });

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mMyCallBack) {
                    mMyCallBack.onClick(new IdNameBean(Constants.DELETE, String.valueOf(position)));
                }
            }
        });


        return convertView;
    }


//    public interface OnItemDeleteClickListener {
//        void onRightClick(View v, int position);
//    }

    class ViewHolder {
        public TextView tv_edit;
        public TextView tvTextView;
        public TextView tv_delete;
    }

}
