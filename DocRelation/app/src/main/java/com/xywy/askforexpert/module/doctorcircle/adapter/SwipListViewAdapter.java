package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.AskPatientReplyInfo;
import com.xywy.askforexpert.module.message.msgchat.GlobalContent;

import java.util.List;


/**
 * 医患快捷回复
 *
 * @author 王鹏
 * @2015-6-4上午11:13:10
 */
public class SwipListViewAdapter extends BaseAdapter {

    private List<AskPatientReplyInfo> list;
    private Context context;
    private int mRightWidth = 0;
    private OnItemDeleteClickListener listener;

    public SwipListViewAdapter(List<AskPatientReplyInfo> list, Context context, int rightWidth, OnItemDeleteClickListener listener) {
        this.list = list;
        this.context = context;
        this.mRightWidth = rightWidth;
        this.listener = listener;
    }

    public void initView(View view) {
        view.scrollTo(0, 0);
    }

    public void setList(List<AskPatientReplyInfo> list) {
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
            convertView = View.inflate(context, R.layout.ask_patient_reply_item, null);
            holder.tvTextView = (TextView) convertView.findViewById(R.id.tv_text);
//			holder.relRightDelete = (RelativeLayout) convertView.findViewById(R.id.rel_right_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTextView.setText(list.get(position).getWord());
        GlobalContent.viewMap.put(list.get(position).getWord(), convertView);
        // 右侧删除按钮数据设置
//		LinearLayout.LayoutParams llParam1 = new LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		holder.tvTextView.setLayoutParams(llParam1);
//		LinearLayout.LayoutParams llParam2 = new LayoutParams(mRightWidth,
//				LayoutParams.MATCH_PARENT);
//		holder.relRightDelete.setLayoutParams(llParam2);
//		holder.relRightDelete.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (null != listener) {
//					listener.onRightClick(v, position);
//				}
//			}
//		});

        return convertView;
    }

    public interface OnItemDeleteClickListener {
        void onRightClick(View v, int position);
    }

    class ViewHolder {
        public TextView tvTextView;
//		public RelativeLayout relRightDelete;
    }

}
