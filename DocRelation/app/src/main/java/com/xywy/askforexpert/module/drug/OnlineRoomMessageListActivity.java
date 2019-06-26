package com.xywy.askforexpert.module.drug;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.consultentity.IMQuestionBean;
import com.xywy.askforexpert.model.consultentity.QuestionInHandleRspEntity;
import com.xywy.askforexpert.model.consultentity.QuestionsListEntity;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.discovery.medicine.common.ViewCallBack;
import com.xywy.askforexpert.module.drug.adapter.OnlineRoomMessageListAdapter;
import com.xywy.askforexpert.module.drug.bean.DrugBean;
import com.xywy.askforexpert.module.drug.request.DrugAboutRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.LogUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * 消息列表 在线诊室IM stone
 */
public class OnlineRoomMessageListActivity extends YMBaseActivity {

    private String mDoctorId = YMUserService.getCurUserId();
    private List<IMQuestionBean> data = new ArrayList<>();
    private SwipeRefreshLayout refresh_view;
    private OnlineRoomMessageListAdapter adapter;
    private RecyclerView recycler_view;

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("消息");
        refresh_view = (SwipeRefreshLayout) findViewById(R.id.refresh_view);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        refresh_view.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        refresh_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        adapter = new OnlineRoomMessageListAdapter(this, data);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                final IMQuestionBean entity = (IMQuestionBean) data.get(position);
                if (entity != null) {
                    // TODO: 2018/6/25
                    OnlineChatDetailActivity.startActivity(OnlineRoomMessageListActivity.this,entity.getQid(),entity.getUid(),entity.getPatient_name(),false,0);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recycler_view.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        ServiceProvider.getQuestionsList(mDoctorId, new Subscriber<QuestionInHandleRspEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                refresh_view.setRefreshing(false);
                LogUtils.e("onError");
            }

            @Override
            public void onNext(QuestionInHandleRspEntity questionInHandleRspEntity) {
                refresh_view.setRefreshing(false);
                if (questionInHandleRspEntity != null) {
                    if (questionInHandleRspEntity.getData() == null ||
                            questionInHandleRspEntity.getData().getList() == null ||
                            questionInHandleRspEntity.getData().getList().isEmpty()
                            || questionInHandleRspEntity.getCode() == ConsultConstants.CODE_EMPTY_DATA) {
                        //空数据
                    } else if (questionInHandleRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                        data.clear();
                        adapter.getDatas().clear();
                        data.addAll(questionInHandleRspEntity.getData().getList());
//                        adapter.getDatas().addAll(data);
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.shortToast(questionInHandleRspEntity.getMsg());
                    }

                }
            }
        });
    }




    @Override
    protected int getLayoutResId() {
        return R.layout.online_message_list_layout;
    }
}