package com.xywy.askforexpert.module.discovery.medicine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyRecordEntity;
import com.xywy.util.TimeUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by xgxg on 2017/10/23.
 */

public class LastestMedicineRecordInnerAdapter extends YMRVSingleTypeBaseAdapter<PharmacyRecordEntity> {
    String[] mMedicineTime;
    String[] mTakeMethod;

    public LastestMedicineRecordInnerAdapter(Context context) {
        super(context);
        mMedicineTime = context.getResources().getStringArray(R.array.medicine_time);
        mTakeMethod = context.getResources().getStringArray(R.array.take_method);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_pharmacy_record_item;
    }

    @Override
    protected void convert(ViewHolder holder, PharmacyRecordEntity pharmacyRecordEntity, int position) {
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
                        if(0==drug.getTake_method()){
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
            //stone 添加用药记录跳转
            holder.getView(R.id.pharmacy_main_title_container).setTag(pharmacyRecordEntity);
            holder.getView(R.id.pharmacy_main_title_container).setOnClickListener(mOnClickListener);
        }

    }

    //stone 添加用药记录跳转
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //添加最新用药记录的统计
            StatisticalTools.eventCount(mContext, "YPRecentmedicationrecord");
            //stone 跳转
            PharmacyRecordEntity item = (PharmacyRecordEntity) view.getTag();
            if (item != null) {
                YMOtherUtils.skip2PrecsciptionDetail(mContext, item.getId());
            }
        }
    };
}
