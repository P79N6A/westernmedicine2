package com.xywy.askforexpert.module.message.imgroup.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.im.group.GroupBean;
import com.xywy.askforexpert.module.message.imgroup.constants.UserPageShowType;
import com.xywy.askforexpert.module.message.imgroup.view.pinyin.callback.ItemClickCallback;

import java.util.List;

/**
 * Created by bailiangjin on 16/7/11.
 */
public class ImGroupPinyinAdapter extends PinyinBaseAdapter<GroupBean> {

    private UserPageShowType type;
    private ItemClickCallback itemClickCallback;


    public ImGroupPinyinAdapter(ExpandableListView listView, List<GroupBean> list, ItemClickCallback itemClickCallback) {
        super(listView, list, false);//不可滑动删除
        this.itemClickCallback = itemClickCallback;
    }

    @Override
    public int getFatherLayoutResId() {
        return R.layout.item_pinyin;
    }

    @Override
    public int getChildLayoutResId() {
        return R.layout.item_im_group_list;
    }


    @Override
    public String getItemName(GroupBean itemData) {
        if (TextUtils.isEmpty(itemData.getContactName())) {
            return itemData.getGroupId();
        }
        return itemData.getContactName();
    }

    /**
     * 返回viewholder持有
     */
    @Override
    public ViewHolder getViewHolder(View rootView, final GroupBean itemData) {
        /**View holder*/
        return new DataViewHolder(rootView, itemData);
    }


    class DataViewHolder extends ViewHolder implements View.OnClickListener {
        public final View rootView;
        public final ImageView iv_group_avatar;
        public final ImageView iv_group_no_disturb;
        public final TextView tv_group_name;


        public DataViewHolder(View rootView, GroupBean groupBean) {
            super(groupBean);
            this.rootView = rootView.findViewById(R.id.root_view);
            this.iv_group_avatar = (ImageView) this.rootView.findViewById(R.id.iv_group_avatar);
            this.iv_group_no_disturb = (ImageView) this.rootView.findViewById(R.id.iv_group_no_disturb);
            this.tv_group_name = (TextView) this.rootView.findViewById(R.id.tv_group_name);

        }

        @Override
        public ViewHolder getHolder(View view) {
            /**在这里设置点击事件*/
            rootView.setOnClickListener(this);
            return this;
        }

        /**
         * 显示数据
         */
        @Override
        public void show() {
            GroupBean groupBean = data;
            tv_group_name.setText(TextUtils.isEmpty(groupBean.getContactName()) ? "" : groupBean.getContactName());
            ImageLoadUtils.INSTANCE.loadRoundedImageView(iv_group_avatar, groupBean.getHeadUrl(), R.drawable.icon_group_default_head, 2);
            //sDisturb 1：免打扰 0：提示
            iv_group_no_disturb.setVisibility(groupBean.getIsDisturb() == 1 ? View.VISIBLE : View.GONE);
        }

        /**
         * 点击事件
         */
        @Override
        public void onClick(View v) {
            GroupBean groupBean = (GroupBean) data;
            switch (v.getId()) {
                case R.id.root_view:
                    itemClickCallback.onItemClick(groupBean);
                    //Toast.makeText(v.getContext(),contactBean.getUserName(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;


            }

        }
    }
}
