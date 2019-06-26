package com.xywy.askforexpert.appcommon.base;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.im.group.ContactBean;

import java.util.List;

/**
 * Created by bailiangjin on 16/8/8.
 */
public class DemoAdaper extends YMBaseAdapter<ContactBean> {


    public DemoAdaper(Activity activity, List dataList) {
        super(activity, dataList);
    }

    @Override
    public int getItemLayoutResId() {
        //TODO:返回Item xml id
        return R.layout.item_im_user_selected_gridview;
    }

    @Override
    protected BaseViewHolder getViewHolder(View rootView) {
        //TODO:返回继承BaseViewHolder 的ViewHolder 示例
        return new ViewHolder(rootView);
    }

    /**
     * 具体的ViewHolder 继承BaseViewHolder
     * 完成View的初始化
     * 完成View的数据填充
     */
    public class ViewHolder extends BaseViewHolder<ContactBean> {
        //示例
        final TextView tv_name;

        public ViewHolder(View rootView) {
            super(rootView);
            //TODO:构造方法中完成 item中View的初始化
            tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        }

        @Override
        public void show(int position, ContactBean contactBean) {
            //TODO:完成View的数据填充
            tv_name.setText(TextUtils.isEmpty(contactBean.getUserName()) ? "" : contactBean.getUserName());
        }
    }
}
