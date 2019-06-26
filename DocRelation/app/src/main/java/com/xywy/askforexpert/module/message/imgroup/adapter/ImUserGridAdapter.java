package com.xywy.askforexpert.module.message.imgroup.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.im.group.ContactBean;

import java.util.List;

/**
 * Created by bailiangjin on 16/7/5.
 */
public class ImUserGridAdapter extends YMBaseAdapter<ContactBean> {


    public ImUserGridAdapter(Activity activity, List<ContactBean> dataList) {
        super(activity, dataList);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_im_user_selected_gridview;
    }

    @Override
    public BaseViewHolder getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    /**
     * Created by bailiangjin on 16/7/5.
     */
    public class ViewHolder extends BaseViewHolder<ContactBean> {
        public final TextView tv_name;
        public final ImageView iv_avatar;

        public ViewHolder(View root) {
            super(root);
            this.tv_name = (TextView) this.rootView.findViewById(R.id.tv_name);
            this.iv_avatar = (ImageView) this.rootView.findViewById(R.id.iv_avatar);
        }

        @Override
        public void show(int position, ContactBean contactBean) {
            tv_name.setText(TextUtils.isEmpty(contactBean.getUserName()) ? "" : contactBean.getUserName());
            ImageLoadUtils.INSTANCE.loadRoundedImageView(iv_avatar, contactBean.getHeadUrl());

        }
    }
}
