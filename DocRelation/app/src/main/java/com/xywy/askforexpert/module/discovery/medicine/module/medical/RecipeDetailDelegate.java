package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ArithUtils;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineCartEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineEntity;
import com.xywy.askforexpert.module.discovery.medicine.view.AmountView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

//import static com.xywy.medicine_super_market.R.id.price;

/**
 * Created by bobby on 17/3/21.
 */

public class RecipeDetailDelegate implements ItemViewDelegate<MedicineCartEntity> {
    private Context mContext;
    private String[] medicine_time,take_method;
    RecipeDetailDelegate(Context context){
        mContext = context;
        medicine_time = mContext.getResources().getStringArray(R.array.medicine_time);
        take_method = mContext.getResources().getStringArray(R.array.take_method);
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_recipe_item;
    }

    @Override
    public boolean isForViewType(MedicineCartEntity item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, final MedicineCartEntity entity, final int position) {
        if(entity!=null) {
            holder.getConvertView().setTag(entity);
            final MedicineEntity medicineEntity = entity.getEntity();
            if(null != medicineEntity){
                String productName = medicineEntity.getName();
                String spec = medicineEntity.getSpec();
                if(TextUtils.isEmpty(productName)){
                    productName = "";
                }

                if(TextUtils.isEmpty(spec)){
                    spec = "";
                }
                ((TextView) holder.getView(R.id.medicine_title)).setText(productName + " " + spec);

                StringBuilder sb = new StringBuilder();
                sb.append("用法：每日"+medicineEntity.getDayCount()+"次");
                sb.append(",一次"+medicineEntity.getTimeCount());
                if(!TextUtils.isEmpty(medicineEntity.getTimeCountDesc())){
                    //stone 从1开始 -1
                    sb.append(medicine_time[Integer.parseInt(medicineEntity.getTimeCountDesc())-1]);

                }
                if(!TextUtils.isEmpty(medicineEntity.getDayCountDesc())){
                    sb.append(","+medicineEntity.getDayCountDesc());
                }
                //服用方式
                //stone 从1开始 -1
                sb.append(","+take_method[Integer.parseInt(medicineEntity.getTakeMethod())-1]);

                if(!TextUtils.isEmpty(medicineEntity.getRemark())){
                    sb.append(","+medicineEntity.getRemark());
                }
                ((TextView) holder.getView(R.id.medicine_intro)).setText(sb.toString());

                final TextView medicinePrice = holder.getView(R.id.medicine_price);
                if(!TextUtils.isEmpty(medicineEntity.getWksmj())){
                    float price = Float.parseFloat(medicineEntity.getWksmj());
                    //stone 准确计算
                    medicinePrice.setText("¥"+ArithUtils.mulForBigDecimal(String.valueOf(price),String.valueOf(medicineEntity.getCount())));
                }

                //猜测是选择的药品的数量
                AmountView amountViewCount = holder.getView(R.id.amount_view_count);
                amountViewCount.setTag(position);
                amountViewCount.setCount(medicineEntity.getCount());
                amountViewCount.setGoods_storage(medicineEntity.getStock());
                amountViewCount.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                    @Override
                    public void onAmountChange(View view, int amount) {
                        AmountView currentView = (AmountView)view;
                        if(!currentView.getClickAdd() && (position == (int)currentView.getTag())){
                            if(1>=amount && currentView.getOldAmount()<=0){
                                Intent intent = new Intent(mContext,DialogueDecreaseActivity.class);
                                Bundle b = new Bundle();
                                b.putInt("productId",medicineEntity.getProductId());
                                intent.putExtras(b);
                                mContext.startActivity(intent);
                            }
                        }

                        if(!TextUtils.isEmpty(medicineEntity.getWksmj())){
                            float price = Float.parseFloat(medicineEntity.getWksmj());
                            //stone 准确计算
                            medicinePrice.setText("¥"+ ArithUtils.mulForBigDecimal(String.valueOf(price),String.valueOf(amount)));
                        }
                        medicineEntity.setCount(amount);
                    }
                });
            }

        }
    }
}
