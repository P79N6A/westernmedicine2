package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.adapter;

import android.content.Context;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.PersonInfoItemEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/4/1.
 */

public class PersonInfoAdapter extends XYWYRVMultiTypeBaseAdapter<PersonInfoItemEntity> {

    private OnItemClickListener onItemClickListener;

    public PersonInfoAdapter(Context context) {
        super(context);
        addItemViewDelegate(new PersonInfoItemDelegate());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(PersonInfoItemEntity entity);
    }

    public class PersonInfoItemDelegate implements ItemViewDelegate<PersonInfoItemEntity> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_person_info_center;
        }

        @Override
        public boolean isForViewType(PersonInfoItemEntity item, int position) {
            return item.getType() == PersonInfoItemEntity.TYPE_NORMAL;
        }

        @Override
        public void convert(ViewHolder holder, final PersonInfoItemEntity personInfoItemEntity, int position) {
            holder.setImageResource(R.id.iv_left, personInfoItemEntity.getLeftImgId());
            holder.setText(R.id.tv_left, personInfoItemEntity.getLeftText());
            holder.setText(R.id.tv_right, personInfoItemEntity.getRightText());
            holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(personInfoItemEntity);
                    }
                }
            });
        }
    }
}
