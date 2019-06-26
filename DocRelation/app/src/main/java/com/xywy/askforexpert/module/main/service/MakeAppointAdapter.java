package com.xywy.askforexpert.module.main.service;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.RefreshBaseAdapter;
import com.xywy.askforexpert.model.QuestionSquareMsgItem;

import java.util.List;

/**
 * <p>
 * 功能：预约加号列表适配器
 * </p>
 *
 * @author liuxuejiao
 * @2015-5-13 下午14:05:57
 */
public class MakeAppointAdapter extends RefreshBaseAdapter {

    /**
     * mDataList: 预约加号列表
     */
    private List<QuestionSquareMsgItem> mDataList;
    /**
     * mContext: 上下文
     */
    private Context mContext;
    /** mInflater: 布局加载器 */
    // private LayoutInflater mInflater;
    /** mRes: 资源对象 */
    /**
     * mType: 数据源类型
     */
    private String mType;

    private boolean useFooter;

    private OnItemClickListener listener;

    public MakeAppointAdapter(Context context, String mType) {
        this.mContext = context;
        this.mType = mType;
    }

    public void bindData(List<QuestionSquareMsgItem> list) {
        this.mDataList = list;
    }

//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return mDataList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		final ViewHolder holder;
//		if (convertView == null) {
//			holder = new ViewHolder();
//			convertView = LayoutInflater.from(mContext).inflate(
//					R.layout.fragment_make_app_item, null);
//
//			holder.mTextViewNameAge = (TextView) convertView
//					.findViewById(R.id.textView_makeapp_item_nameAndAge);
//			holder.mTextViewOrderNum = (TextView) convertView
//					.findViewById(R.id.textView_makeapp_item_order_num);
//			holder.mTextViewTime = (TextView) convertView
//					.findViewById(R.id.textView_makeapp_item_order_time);
//			holder.mImageViewTop = (ImageView) convertView
//					.findViewById(R.id.imagView_makeapp_item_top);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		if (mDataList != null) {
//			holder.mTextViewNameAge.setText(mDataList.get(position).getName()
//					+ "    " + mDataList.get(position).getSex() + "    "
//					+ mDataList.get(position).getAge());
//			holder.mTextViewOrderNum.setText("预约单号："
//					+ mDataList.get(position).getId());
//			holder.mTextViewTime.setText("预约门诊时间："
//					+ mDataList.get(position).getCreateTime() + "    "
//					+ mDataList.get(position).getWeek() + "    "
//					+ mDataList.get(position).getHalfday());
//			if (mType.equals("1")) {
//				holder.mImageViewTop
//						.setImageResource(R.drawable.yuyue_wait_confirm);
//			} else if (mType.equals("2")) {
//				if (mDataList.get(position).getIsConfirm().equals("1")) {
//					holder.mImageViewTop
//							.setImageResource(R.drawable.yuyue_yilingqu);
//				} else {
//					holder.mImageViewTop
//							.setImageResource(R.drawable.yuyue_shenghe);
//				}
//
//			} else if (mType.equals("3")) {
//				holder.mImageViewTop.setImageResource(R.drawable.yuyue_success);
//			} else if (mType.equals("4")) {
//				holder.mImageViewTop
//						.setImageResource(R.drawable.yuyue_shuangyue);
//			}
//		}
//		return convertView;
//	}

//	private class ViewHolder {
//		/** mTextViewNameAge: 姓名、性别、年龄 */
//		TextView mTextViewNameAge;
//		/** mTextViewOrderNum: 预约订单号 */
//		TextView mTextViewOrderNum;
//		/** mTextViewTime: 预约订单时间 */
//		TextView mTextViewTime;
//		/** mImageViewTop: 订单状态 */
//		ImageView mImageViewTop;
//
//	}

    @Override
    public boolean useFooter() {
        // TODO Auto-generated method stub
        return useFooter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentItemViewHolder(
            ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(
                R.layout.fragment_make_app_item, null);
        MakeAppointViewHolder holder = new MakeAppointViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindContentItemView(
            RecyclerView.ViewHolder holder,
            final int position) {
        MakeAppointViewHolder makeHolder = (MakeAppointViewHolder) holder;
        if (mDataList != null) {
            makeHolder.mTextViewNameAge.setText(mDataList.get(position).getName()
                    + "    " + mDataList.get(position).getSex() + "    "
                    + mDataList.get(position).getAge());
            makeHolder.mTextViewOrderNum.setText("预约单号："
                    + mDataList.get(position).getId());
            makeHolder.mTextViewTime.setText("预约门诊时间："
                    + mDataList.get(position).getCreateTime() + "    "
                    + mDataList.get(position).getWeek() + "    "
                    + mDataList.get(position).getHalfday());
            if (mType.equals("1")) {
                makeHolder.mImageViewTop
                        .setImageResource(R.drawable.yuyue_wait_confirm);
            } else if (mType.equals("2")) {
                if (mDataList.get(position).getIsConfirm().equals("1")) {
                    makeHolder.mImageViewTop
                            .setImageResource(R.drawable.yuyue_yilingqu);
                } else {
                    makeHolder.mImageViewTop
                            .setImageResource(R.drawable.yuyue_shenghe);
                }

            } else if (mType.equals("3")) {
                makeHolder.mImageViewTop.setImageResource(R.drawable.yuyue_success);
            } else if (mType.equals("4")) {
                makeHolder.mImageViewTop
                        .setImageResource(R.drawable.yuyue_shuangyue);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onItemClick(position, mDataList.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getContentItemCount() {
        // TODO Auto-generated method stub
        return mDataList.size();
    }

    @Override
    public int getContentItemType(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 设置监听方法 * * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setUseFooter(boolean useFooter) {
        this.useFooter = useFooter;
    }

    /**
     * 内部接口回调方法
     */
    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }

    private class MakeAppointViewHolder extends RecyclerView.ViewHolder {


        private View parent;
        /**
         * mTextViewNameAge: 姓名、性别、年龄
         */
        private TextView mTextViewNameAge;
        /**
         * mTextViewOrderNum: 预约订单号
         */
        private TextView mTextViewOrderNum;
        /**
         * mTextViewTime: 预约订单时间
         */
        private TextView mTextViewTime;
        /**
         * mImageViewTop: 订单状态
         */
        private ImageView mImageViewTop;

        public MakeAppointViewHolder(View convertView) {
            super(convertView);
            parent = convertView;

            mTextViewNameAge = (TextView) convertView
                    .findViewById(R.id.textView_makeapp_item_nameAndAge);
            mTextViewOrderNum = (TextView) convertView
                    .findViewById(R.id.textView_makeapp_item_order_num);
            mTextViewTime = (TextView) convertView
                    .findViewById(R.id.textView_makeapp_item_order_time);
            mImageViewTop = (ImageView) convertView
                    .findViewById(R.id.imagView_makeapp_item_top);
        }
    }
}
