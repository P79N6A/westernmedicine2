package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.text.TextUtils;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyRecordEntity;
import com.xywy.util.TimeUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 用药记录adapter stone
 * Created by xgxg on 17/4/5.
 */

public class PharmacyRecordDelegate implements ItemViewDelegate<PharmacyRecordEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_pharmacy_record_item;
    }

    @Override
    public boolean isForViewType(PharmacyRecordEntity item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, PharmacyRecordEntity pharmacyRecordEntity, final int position) {
        if (pharmacyRecordEntity != null) {
            ((TextView) holder.getView(R.id.medicine_name)).setText(pharmacyRecordEntity.getUname());
//            DisplayImageOptions options = ImageLoaderUtils.getInstance().getDisplayImageOptions(R.drawable.medicine);
//            ImageLoaderUtils.getInstance().displayImage(pharmacyRecordEntity.getImage(),(ImageView) holder.getView(R.id.medicine_pic),options);
            ((TextView) holder.getView(R.id.time)).setText(TimeUtils.getPhpStrDate(pharmacyRecordEntity.getTime(), "yyyy/MM/dd"));
            PharmacyRecordEntity.Drug drug = pharmacyRecordEntity.getDrug_list();
            if (null != drug) {
                ((TextView) holder.getView(R.id.num)).setText("x" + drug.getNum());

                ((TextView) holder.getView(R.id.medicine_spec)).setText(drug.getGname() + " " + drug.getSpecification());

                StringBuffer sb = new StringBuffer("用法：");
                if (!TextUtils.isEmpty(drug.getTake_rate())) {
                    sb.append("每日" + drug.getTake_rate() + "次");
                }

                if ((!TextUtils.isEmpty(drug.getTake_num())) && drug.getTake_unit() >= 0) {
                    sb.append("，一次" + drug.getTake_num());

                    final String[] medicine_time = holder.getConvertView().getResources().getStringArray(R.array.medicine_time);
                    if (medicine_time != null && medicine_time.length > drug.getTake_unit()) {
                        //stone 从1开始
                        if(0==drug.getTake_unit()){
                            sb.append("");
                        }else {
                            sb.append(medicine_time[drug.getTake_unit() - 1]);
                        }

                    }
                }

                //stone 添加服用时间
                if ((!TextUtils.isEmpty(drug.getTake_time()))) {
                    sb.append("，" + drug.getTake_time());
                }

                if (drug.getTake_method() >= 0) {
                    final String[] medicine_method = holder.getConvertView().getResources().getStringArray(R.array.take_method);
                    if (medicine_method != null && medicine_method.length > drug.getTake_method()) {
                        //stone 从1开始
                        if(drug.getTake_method()==0){
                            sb.append("，");
                        }else {
                            sb.append("，" + medicine_method[drug.getTake_method() - 1]);
                        }

                    }

                }

                if (!TextUtils.isEmpty(drug.getRemark())) {
                    sb.append("，" + drug.getRemark());
                }
                ((TextView) holder.getView(R.id.remark)).setText(sb.toString());

            }

            if (1 == pharmacyRecordEntity.getOrder_status()) {
                ((TextView) holder.getView(R.id.status)).setText("未支付");
            } else if (2 == pharmacyRecordEntity.getOrder_status()) {
                ((TextView) holder.getView(R.id.status)).setText("待发货");
            } else if (3 == pharmacyRecordEntity.getOrder_status()) {
                ((TextView) holder.getView(R.id.status)).setText("待收货");
            } else if (4 == pharmacyRecordEntity.getOrder_status()) {
                ((TextView) holder.getView(R.id.status)).setText("已完成");
            } else if (5 == pharmacyRecordEntity.getOrder_status()) {
                ((TextView) holder.getView(R.id.status)).setText("已取消");
            } else if (6 == pharmacyRecordEntity.getOrder_status()) {
                ((TextView) holder.getView(R.id.status)).setText("-已退款");
            } else if (7 == pharmacyRecordEntity.getOrder_status()) {
                ((TextView) holder.getView(R.id.status)).setText("-待退款");
            }

        }
    }
}
