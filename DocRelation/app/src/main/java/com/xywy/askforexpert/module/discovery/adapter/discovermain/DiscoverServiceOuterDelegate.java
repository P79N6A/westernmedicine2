package com.xywy.askforexpert.module.discovery.adapter.discovermain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.activity.WebViewActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.module.discovery.DoctorOneDayActivity;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverItemBean;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverServiceInnerItem;
import com.xywy.askforexpert.module.discovery.answer.AnswerMainActivity;
import com.xywy.askforexpert.module.main.service.codex.CheckBookActivity;
import com.xywy.askforexpert.module.main.service.codex.CodexFragActivity;
import com.xywy.askforexpert.module.main.service.linchuang.GuideActivity;
import com.xywy.uilibrary.recyclerview.adapter.HSItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by bailiangjin on 2017/3/24.
 */

public class DiscoverServiceOuterDelegate implements ItemViewDelegate<DiscoverItemBean> {

    DiscoverServiceInnerAdapter innerGridItemAdapter;
    private boolean isAddedDecoration = false;
    private Context mContext;

    public DiscoverServiceOuterDelegate(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_discovery_service_grid_outer;
    }

    @Override
    public boolean isForViewType(DiscoverItemBean item, int position) {
        return item.getType() == DiscoverItemBean.TYPE_SERVICE;
    }

    @Override
    public void convert(ViewHolder holder, DiscoverItemBean discoverItemBean, int position) {
        final RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        if (!isAddedDecoration) {
            recyclerView.addItemDecoration(new HSItemDecoration(recyclerView.getContext(), R.color.transparent));
            isAddedDecoration=true;
        }
        final List<DiscoverServiceInnerItem> innerItemList = discoverItemBean.getServiceItem().getInnerItemList();

        innerGridItemAdapter = new DiscoverServiceInnerAdapter(context);

        innerGridItemAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                DiscoverServiceInnerItem item = innerGridItemAdapter.getItem(position);
                onListItemClick(item);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        innerGridItemAdapter.setData(innerItemList);

        recyclerView.setAdapter(innerGridItemAdapter);
    }

    private void onListItemClick(DiscoverServiceInnerItem item) {
        switch (item.getItemType()) {
            case EXAM_PAPER:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(mContext).context);
                } else {
                    StatisticalTools.eventCount(mContext, "Exam");
                    AnswerMainActivity.start((Activity) mContext);
                }
                break;

            case DOCTOR_ONE_DAY:
                //中国医生的一天 stone
                StatisticalTools.eventCount(mContext, Constants.ONEDAYOFCHINESEDOCTOR);
                mContext.startActivity(new Intent(YMApplication.getAppContext(),
                        DoctorOneDayActivity.class));
                break;

            case CLINIC_BOOK:
                mContext.startActivity(new Intent(YMApplication.getAppContext(), GuideActivity.class));
                break;
            case CHECK_BOOK:
                mContext.startActivity(new Intent(YMApplication.getAppContext(),
                        CheckBookActivity.class));
                break;
            case MEDICINE_HELPER:
                mContext.startActivity(new Intent(YMApplication.getAppContext(),
                        CodexFragActivity.class));
                break;
            case FIND_JOB:
                //new stone 找工作逻辑
                WebViewActivity.start((Activity) mContext, "找工作", Constants.FIND_JOB_URL);
                break;
            default:
                break;

        }
    }
}
