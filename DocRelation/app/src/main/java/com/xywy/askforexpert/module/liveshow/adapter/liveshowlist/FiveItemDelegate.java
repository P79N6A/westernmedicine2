package com.xywy.askforexpert.module.liveshow.adapter.liveshowlist;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.liveshow.LiveShowListPageBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static com.xywy.askforexpert.appcommon.utils.ToastUtils.shortToast;

/**
 * Created by bailiangjin on 2017/3/4.
 */

public class FiveItemDelegate implements ItemViewDelegate<LiveShowItem> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_live_show_list_five_cell;
    }

    @Override
    public boolean isForViewType(LiveShowItem item, int position) {
        return LiveShowItem.Type.MULTI == item.getType();
    }

    @Override
    public void convert(ViewHolder holder, LiveShowItem item, int position) {

        View item1 = holder.getView(R.id.item1);
        View item2 = holder.getView(R.id.item2);
        View item3 = holder.getView(R.id.item3);
        View item4 = holder.getView(R.id.item4);
        View item5 = holder.getView(R.id.item5);

        LiveShowListPageBean.DataBean dataItem1 = item.getMultiList().size() >= 1 ? item.getMultiList().get(0) : null;
        LiveShowListPageBean.DataBean dataItem2 = item.getMultiList().size() >= 2 ? item.getMultiList().get(1) : null;
        LiveShowListPageBean.DataBean dataItem3 = item.getMultiList().size() >= 3 ? item.getMultiList().get(2) : null;
        LiveShowListPageBean.DataBean dataItem4 = item.getMultiList().size() >= 4 ? item.getMultiList().get(3) : null;
        LiveShowListPageBean.DataBean dataItem5 = item.getMultiList().size() >= 5 ? item.getMultiList().get(4) : null;

        fillItemData(item1, dataItem1);
        fillItemData(item2, dataItem2);
        fillItemData(item3, dataItem3);
        fillItemData(item4, dataItem4);
        fillItemData(item5, dataItem5);

    }

    private void fillItemData(final View itemRootView, final LiveShowListPageBean.DataBean dataBean) {
        if (null == dataBean) {
            return;
        }
        itemRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YMUserService.isLoginUser((Activity) itemRootView.getContext())) {
                    if (null == dataBean) {
                        shortToast("条目数据为空");
                        return;
                    }
                    try {
                        LogUtils.e("跳转直播详情页:" + GsonUtils.toJson(dataBean));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    int type = dataBean.getState();
                    String liveShowId = dataBean.getId();
                    String chatRoomId = dataBean.getChatroomsid();
                    String rtmp = dataBean.getRtmp();
                    List<String> vodList = dataBean.getVod_list();

                    if (1 == type) {
                       // LiveShowUtils.watchLive((Activity) itemRootView.getContext(), liveShowId, chatRoomId, rtmp);
                    } else {
                        //LiveShowUtils.watchLiveRecord((Activity) itemRootView.getContext(), liveShowId, chatRoomId, vodList);
                    }

                }

            }
        });
        TextView tv_title = (TextView) itemRootView.findViewById(R.id.tv_title);
        TextView tv_user_name = (TextView) itemRootView.findViewById(R.id.tv_user_name);
        TextView tv_view_number = (TextView) itemRootView.findViewById(R.id.tv_view_number);

        ImageView iv_bg = (ImageView) itemRootView.findViewById(R.id.iv_bg);
        ImageView iv_user_head = (ImageView) itemRootView.findViewById(R.id.iv_user_head);

        TextView tv_live_show = (TextView) itemRootView.findViewById(R.id.tv_live_show);
        TextView tv_record = (TextView) itemRootView.findViewById(R.id.tv_record);

        tv_live_show.setVisibility(1 == dataBean.getState() ? View.VISIBLE : View.GONE);
        tv_record.setVisibility(0 == dataBean.getState() ? View.VISIBLE : View.GONE);

        tv_title.setText(dataBean.getName());
        tv_user_name.setText(dataBean.getUser().getName());
        tv_view_number.setText("" + dataBean.getAmount());

        ImageLoadUtils.INSTANCE.loadImageView(iv_bg, dataBean.getCover(), R.drawable.live_show_item_default_bg);
        if (iv_user_head != null) {
            ImageLoadUtils.INSTANCE.loadCircleImageView(iv_user_head, dataBean.getUser().getPortrait());
        }
    }
}
