package com.xywy.askforexpert.module.main.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.home.HomeItemBean;
import com.xywy.askforexpert.module.my.smallstation.MySite;
import com.xywy.base.view.ProgressDialog;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 *  Created by xgxg on 2018/4/4.
 */

public class RoomServiceMoreDelegate implements ItemViewDelegate<HomeItemBean> {
    Context context;
    private ProgressDialog progressDialog;
    public RoomServiceMoreDelegate(Context context){
        this.context = context;
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_home_room_service_more;
    }

    @Override
    public boolean isForViewType(HomeItemBean item, int position) {
        return HomeItemBean.TYPE_MORE == item.getType();
}

    @Override
    public void convert(ViewHolder holder, HomeItemBean homeItemBean, int position) {
        holder.getView(R.id.ll_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MySite.class));

                //stone 发现更多诊室
                StatisticalTools.eventCount(context, Constants.FINDMORESERVICE);
            }
        });
    }


}
