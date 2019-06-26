package com.xywy.askforexpert.module.drug.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.model.consultentity.IMQuestionBean;
import com.xywy.askforexpert.module.discovery.medicine.common.ViewCallBack;
import com.xywy.askforexpert.module.drug.bean.DrugBean;
import com.xywy.util.ImageLoaderUtils;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 消息列表的Adapter stone
 */
public class OnlineRoomMessageListAdapter extends CommonAdapter<IMQuestionBean> {


    public OnlineRoomMessageListAdapter(Context context, List<IMQuestionBean> list) {
        super(context, R.layout.item_onlineroom_message, list);
    }

    @Override
    protected void convert(ViewHolder holder, final IMQuestionBean entity, final int position) {
        XYImageLoader.getInstance().displayImage(entity.getUser_photo(), (ImageView) holder.getView(R.id.img));
        TextView name = holder.getView(R.id.name);
        name.setText(entity.getPatient_name());
        TextView content = holder.getView(R.id.content);
        content.setText(entity.getContent());
        TextView time = holder.getView(R.id.time);
        time.setText(entity.getCreated_time());
    }
}
