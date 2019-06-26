package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.PersonIdentifyEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/4/11.
 */

public class PersonIdentifyAdapter extends XYWYRVMultiTypeBaseAdapter<PersonIdentifyEntity> {
    public static final int TYPE_NORMAL = 0;

    public PersonIdentifyAdapter(Context context) {
        super(context);
        addItemViewDelegate(new IdentityNormalItemDelegate());
    }

    class IdentityNormalItemDelegate implements ItemViewDelegate<PersonIdentifyEntity> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_identity_img;
        }

        @Override
        public boolean isForViewType(PersonIdentifyEntity item, int position) {
            return item.getType() == TYPE_NORMAL;
        }

        @Override
        public void convert(ViewHolder holder, PersonIdentifyEntity personIdentifyEntity, int position) {
            if (personIdentifyEntity.getText() == null || personIdentifyEntity.getText().equals("")) {
                holder.getView(R.id.tv_identify_text).setVisibility(View.GONE);
            } else {
                holder.getView(R.id.tv_identify_text).setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_identify_text, personIdentifyEntity.getText());
            }
            XYImageLoader.getInstance().displayImage(personIdentifyEntity.getImgUrl(), (ImageView) holder.getView(R.id.iv_identify_card));
        }
    }
}
