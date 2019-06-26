package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.adapter;

import android.content.Context;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.PersonInfoSettingItemEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/4/1.
 */

public class PersonInfoSettingAdapter extends XYWYRVMultiTypeBaseAdapter<PersonInfoSettingItemEntity> {

    private OnItemClickListener onItemClickListener;

    public PersonInfoSettingAdapter(Context context) {
        super(context);
        addItemViewDelegate(new SettingNormalDelegate());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(PersonInfoSettingItemEntity entity);
    }

    public class SettingNormalDelegate implements ItemViewDelegate<PersonInfoSettingItemEntity> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_person_center_setting;
        }

        @Override
        public boolean isForViewType(PersonInfoSettingItemEntity item, int position) {
            return item.getType() == PersonInfoSettingItemEntity.TYPE_NORMAL;
        }

        @Override
        public void convert(ViewHolder holder, final PersonInfoSettingItemEntity personInfoSettingItemEntity, int position) {
            holder.setText(R.id.tv_left, personInfoSettingItemEntity.getLeftText());
            holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(personInfoSettingItemEntity);
                    }
                }
            });
        }
    }
}
