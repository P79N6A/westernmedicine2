package com.xywy.askforexpert.module.message.imgroup.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.im.group.ContactModel;

import java.util.List;

/**
 * 群信息也 群成员grid列表
 * Created by bailiangjin on 16/7/5.
 */
public class GroupMemberGridAdapter extends YMBaseAdapter<ContactModel> {


    public GroupMemberGridAdapter(Activity activity, List<ContactModel> dataList) {
        super(activity, dataList);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_group_memeber_gridview;
    }

    @Override
    public BaseViewHolder getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    /**
     * Created by bailiangjin on 16/7/5.
     */
    public class ViewHolder extends BaseViewHolder<ContactModel> {
        public final ImageView iv_avatar;
        public final ImageView iv_group_master_star;
        public final TextView tv_name;

        public ViewHolder(View root) {
            super(root);
            this.iv_avatar = (ImageView) this.rootView.findViewById(R.id.iv_avatar);
            this.iv_group_master_star = (ImageView) this.rootView.findViewById(R.id.iv_group_master_star);
            this.tv_name = (TextView) this.rootView.findViewById(R.id.tv_name);

        }

        @Override
        public void show(int position, ContactModel contactModel) {
            //tv_name.setText(TextUtils.isEmpty(contactModel.getUserName()) ? "" : contactModel.getUserName());
            //设置是否为群主 标识
            iv_group_master_star.setVisibility(contactModel.isMaster() ? View.VISIBLE : View.GONE);

            if (position == dataList.size() - 1) {
                //加号图标
                iv_avatar.setImageResource(R.drawable.icon_groupinfo_add);
            } else {
                //设置头像
                ImageLoadUtils.INSTANCE.loadRoundedImageView(iv_avatar, contactModel.getHeadUrl());
            }

        }
    }


}
