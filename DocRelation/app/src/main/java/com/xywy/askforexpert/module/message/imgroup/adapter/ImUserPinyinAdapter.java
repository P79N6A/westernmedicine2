package com.xywy.askforexpert.module.message.imgroup.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.im.group.ContactBean;
import com.xywy.askforexpert.module.message.imgroup.constants.UserPageShowType;
import com.xywy.askforexpert.module.message.imgroup.view.pinyin.callback.ItemClickCallback;

import java.util.List;

/**
 * Created by bailiangjin on 16/7/11.
 */
public class ImUserPinyinAdapter extends PinyinBaseAdapter<ContactBean> {

    private UserPageShowType type;
    private ItemClickCallback itemClickCallback;


    public ImUserPinyinAdapter(ExpandableListView listView, List<ContactBean> list, UserPageShowType type, boolean isCanScroll, ItemClickCallback itemClickCallback) {
        super(listView, list, isCanScroll);
        this.type = type;
        this.itemClickCallback = itemClickCallback;
    }

    @Override
    public int getFatherLayoutResId() {
        return R.layout.item_pinyin;
    }

    @Override
    public int getChildLayoutResId() {
        return R.layout.item_im_user_listview;
    }


    @Override
    public String getItemName(ContactBean itemData) {
        if (TextUtils.isEmpty(itemData.getUserName())) {
            return itemData.getUserId();
        }
        return itemData.getUserName();
    }

    /**
     * 返回viewholder持有
     */
    @Override
    public ViewHolder getViewHolder(View rootView, final ContactBean itemData) {
        /**View holder*/
        return new DataViewHolder(rootView, itemData);
    }

    @Override
    protected boolean canItemScroll(ContactBean itemData) {
        return isCanScroll && !itemData.isMaster();
    }

    class DataViewHolder extends PinyinBaseAdapter.ViewHolder implements View.OnClickListener {
        public final View rootView;
        public final View id_front;
        public final CheckBox cb_checked;
        public final TextView tv_name;
        public final ImageView iv_avatar;
        public final TextView tv_master_label;
        public final Button btn_delete;


        public DataViewHolder(View rootView, ContactBean contactBean) {
            super(contactBean);
            this.rootView = rootView;
            this.id_front = this.rootView.findViewById(R.id.id_front);
            this.cb_checked = (CheckBox) this.rootView.findViewById(R.id.cb_checked);
            this.tv_name = (TextView) this.rootView.findViewById(R.id.tv_name);
            this.iv_avatar = (ImageView) this.rootView.findViewById(R.id.iv_avatar);
            this.tv_master_label = (TextView) this.rootView.findViewById(R.id.tv_master_label);
            this.btn_delete = (Button) this.rootView.findViewById(R.id.btn_delete);
        }

        /**
         * 初始化
         */
        @Override
        public PinyinBaseAdapter.ViewHolder getHolder(View view) {
            /**在这里设置点击事件*/
            id_front.setOnClickListener(this);
            btn_delete.setOnClickListener(this);
            return this;
        }

        /**
         * 显示数据
         */
        @Override
        public void show() {
            ContactBean contactBean = (ContactBean) data;
            tv_name.setText(contactBean.getUserName());
            tv_name.setText(TextUtils.isEmpty(contactBean.getUserName()) ? contactBean.getUserId() : contactBean.getUserName());
            ImageLoadUtils.INSTANCE.loadRoundedImageView(iv_avatar, contactBean.getHeadUrl());
            tv_master_label.setVisibility(contactBean.isMaster() ? View.VISIBLE : View.GONE);
            switch (type) {
                case ADD_MEMBER:
                    cb_checked.setVisibility(View.VISIBLE);
                    if (contactBean.isGroupMember()) {
                        cb_checked.setEnabled(false);
                        cb_checked.setChecked(false);
                    } else {
                        cb_checked.setEnabled(true);
                        cb_checked.setChecked(contactBean.isChecked());
                    }
                    break;
                case CREATE_GROUP:
                    cb_checked.setVisibility(View.VISIBLE);
                    cb_checked.setEnabled(true);
                    cb_checked.setChecked(contactBean.isChecked());

                    break;
                case SHOW:
                    cb_checked.setVisibility(View.GONE);
                    break;

                case SELECT_NEW_MASTER:
                    //群主不可勾选
                    if (contactBean.isMaster()) {
                        cb_checked.setEnabled(false);
                        cb_checked.setChecked(false);
                    } else {
                        cb_checked.setEnabled(true);
                        cb_checked.setChecked(contactBean.isChecked());
                    }
                    break;

                default:
                    break;
            }
        }

        /**
         * 点击事件
         */
        @Override
        public void onClick(View v) {
            ContactBean contactBean = (ContactBean) data;
            switch (v.getId()) {
                case R.id.id_front:
                    itemClickCallback.onItemClick(contactBean);
                    //Toast.makeText(v.getContext(),contactBean.getUserName(), Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btn_delete:
                    //T.shortToast(getContext(),"删除"+contactBean.getUserName());
                    itemClickCallback.onItemDelete(contactBean);
                    break;
                default:
                    break;
            }

        }
    }
}
