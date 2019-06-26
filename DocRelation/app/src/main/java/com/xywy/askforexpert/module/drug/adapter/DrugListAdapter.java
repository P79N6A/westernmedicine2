package com.xywy.askforexpert.module.drug.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.common.ViewCallBack;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.DrugCartCenter;
import com.xywy.askforexpert.module.drug.bean.DrugBean;
import com.xywy.askforexpert.module.drug.bean.RrescriptionData;
import com.xywy.util.ImageLoaderUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 药品列表的Adapter stone
 */
public class DrugListAdapter extends CommonAdapter<DrugBean> {

    //来自药房还是开处方操作 用来控制按钮的作用 加入常用药/移除常用药 加入处方/移除处方
    private boolean mIsCommonOpt;
    //来自常用药
    private boolean mIsFromCommonDrug;

    private ViewCallBack mMyCallBack;

    public void setViewCallBack(ViewCallBack viewCallBack) {
        mMyCallBack = viewCallBack;
    }

    public DrugListAdapter(Context context, List<DrugBean> list, boolean isCommonOpt, boolean isFromCommonDrug) {
        super(context, R.layout.item_drug_list, list);
        this.mIsCommonOpt = isCommonOpt;
        this.mIsFromCommonDrug = isFromCommonDrug;
    }

    public void setData(List<DrugBean> oneList) {
        mDatas.clear();
        if (oneList != null) {
            mDatas.addAll(oneList);
        }
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder holder, final DrugBean entity, final int position) {
        if (entity != null) {
            ((TextView)holder.getView(R.id.pharmacy_tv)).setText(entity.supplier);
            ((TextView) holder.getView(R.id.name)).setText(entity.name);
            ((TextView) holder.getView(R.id.spec)).setText(entity.specification);
            ((TextView) holder.getView(R.id.price)).setText("￥" + entity.wksmj);
            ((TextView) holder.getView(R.id.company)).setText(entity.company);
            ImageLoaderUtils.getInstance().displayImage(entity.image, (ImageView) holder.getView(R.id.img));

            final TextView go = holder.getView(R.id.go);

            if (mIsCommonOpt) {
                //常用药列表没有isCommon字段
                if (mIsFromCommonDrug) {
                    go.setSelected(true);
                    go.setText("移除常用药");
                } else {
                    if (entity.isCommon == 0) {
                        go.setSelected(false);
                        go.setText("加入常用药");
                    } else {
                        go.setSelected(true);
                        go.setText("移除常用药");
                    }
                }
            } else {
                go.setEnabled(true);
                if (!RrescriptionData.getInstance().isAdded(mIsFromCommonDrug?entity.getPid():entity.getId())) {
                    go.setText("加入处方笺");
                } else {
                    go.setText("移出处方笺");
                }
            }

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity != null && mMyCallBack != null) {
                        mMyCallBack.onClick(0, position, entity,false);
                    }
                }
            });

            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity != null && mMyCallBack != null) {
                        if (go.getText().equals("移出处方笺")){
                            mMyCallBack.onClick(1, position, entity, true);
                        }else {
                            mMyCallBack.onClick(1, position, entity, false);
                        }
                    }
                }
            });
        }
    }
}
