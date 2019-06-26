package com.xywy.askforexpert.module.main.service.recruit.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.model.RecrutiCenterInfo;
import com.xywy.askforexpert.module.doctorcircle.adapter.SwipListViewAdapter.OnItemDeleteClickListener;
import com.xywy.askforexpert.module.message.msgchat.GlobalContent;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 学校名字 适配器
 *
 * @author 王鹏
 * @2015-5-7下午5:49:25
 */
public class BaseRecruitResumeAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    FinalBitmap fb;
    private List<RecrutiCenterInfo> list;
    private Context context;
    private LayoutInflater inflater;
    private OnItemDeleteClickListener listener;

    public BaseRecruitResumeAdapter(Context context,
                                    OnItemDeleteClickListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.notifyDataSetChanged();
        fb = FinalBitmap.create(context, false);
        this.listener = listener;

    }

    public void initView(View view) {
        view.scrollTo(0, 0);
    }

    public void setData(List<RecrutiCenterInfo> list) {
        this.list = list;
        if (list != null) {
            init();
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater
                    .inflate(R.layout.recruit_myresume_item, null);
            holder.tv_title_1 = (TextView) convertView
                    .findViewById(R.id.tv_news_title);
            holder.tv_title_2 = (TextView) convertView
                    .findViewById(R.id.tv_news_title_2);
            holder.tv_data = (TextView) convertView.findViewById(R.id.tv_date);

            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.img_head);
//			holder.relRightDelete = (RelativeLayout) convertView
//					.findViewById(R.id.rel_right_delete);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RelativeLayout.LayoutParams llParam1 = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llParam1.setMargins(DensityUtils.dp2px(10), 0, 0, 0);
        holder.tv_title_1.setLayoutParams(llParam1);
        GlobalContent.viewMap.put(list.get(position).getId(), convertView);
//		holder.relRightDelete.setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				if (null != listener)
//				{
//					listener.onRightClick(v, position);
//				}
//			}
//		});
        if (list != null) {
            holder.tv_title_1.setText(list.get(position).getTitle());
            holder.tv_title_2.setText(list.get(position).getEntename());
            holder.tv_data.setText(list.get(position).getUpdatetime());
            // fb.display(holder.imageView, list.get(position).getLogo());
            holder.tv_type.setText("收藏日期："
                    + list.get(position).getCollectiontime());
        }
        return convertView;
    }

    public void init() {
        for (int i = 0; i < list.size(); i++) {
            selectionMap = new SparseBooleanArray();
            selectionMap.put(i, false);
        }

    }

    private class ViewHolder {
        TextView tv_title_1;
        TextView tv_title_2;
        ImageView imageView;
        TextView tv_data;
        //		RelativeLayout relRightDelete;
        TextView tv_type;
    }

}
