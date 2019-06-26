package com.xywy.askforexpert.module.message.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.RefreshBaseAdapter;
import com.xywy.askforexpert.model.TelItem;

import java.util.List;

/**
 * 电话医生适配器
 *
 * @author shihao
 */
public class TelDocAdapter extends RefreshBaseAdapter {

    private Context context;
    private List<TelItem> telItems;

    private OnItemClickListener listener;

    private boolean useFooter;

    public TelDocAdapter(Context context) {
        this.context = context;
    }

    public void bindData(List<TelItem> telItems) {
        this.telItems = telItems;
    }

    @Override
    public boolean useFooter() {
        // TODO Auto-generated method stub
        return useFooter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentItemViewHolder(
            ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(
                R.layout.tel_item_adapter, null);
        TelViewHolder holder = new TelViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindContentItemView(
            RecyclerView.ViewHolder holder,
            final int position) {
        TelViewHolder telHolder = (TelViewHolder) holder;

        telHolder.tvOrderNum.setText(telItems.get(position).getOrderNum());
        telHolder.tvFee.setText(telItems.get(position).getFee());
        telHolder.tvTelTime.setText(telItems.get(position).getTalkTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(position, telItems.get(position));
                }
            }
        });
    }

    @Override
    public int getContentItemCount() {
        // TODO Auto-generated method stub
        return telItems.size();
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

    private class TelViewHolder extends RecyclerView.ViewHolder {

        private View parent;
        private TextView tvOrderNum, tvFee, tvTelTime;

        public TelViewHolder(View itemView) {
            super(itemView);
            parent = itemView;

            tvOrderNum = (TextView) itemView
                    .findViewById(R.id.tv_order_num);
            tvFee = (TextView) itemView.findViewById(R.id.tv_fee);
            tvTelTime = (TextView) itemView
                    .findViewById(R.id.tv_tel_time);
        }

    }
}
