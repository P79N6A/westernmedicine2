package com.xywy.livevideo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.xywy.livevideo.entity.GiftListRespEntity;
import com.xywy.livevideolib.R;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVSingleTypeBaseAdapter;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/2/22.
 */
public class GiftAdapter extends XYWYRVSingleTypeBaseAdapter<GiftListRespEntity.DataBean> {

    public GiftAdapter(Context context) {
        super(context);
    }

    private GiftListRespEntity.DataBean selectedItem;

    @Override
    protected int getItemLayoutResId() {
        return R.layout.live_gift_item;
    }

    @Override
    protected void convert(ViewHolder holder, final GiftListRespEntity.DataBean item, final int position) {
        XYImageLoader.getInstance().displayImage(item.getImg(), (ImageView) holder.getView(R.id.iv_gift_img));
        holder.setText(R.id.tv_gift_name, item.getName());
        holder.setText(R.id.tv_gift_cost, item.getPrice() + "健康币");
        if (item == selectedItem) {
            holder.setBackgroundRes(R.id.ll_gift_item, R.drawable.gift_selected_bkg);
        } else {
            holder.setBackgroundColor(R.id.ll_gift_item, Color.TRANSPARENT);
        }
        holder.getView(R.id.ll_gift_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = item;
                GiftAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public GiftListRespEntity.DataBean getSelectedItem() {
        return selectedItem;
    }


}
