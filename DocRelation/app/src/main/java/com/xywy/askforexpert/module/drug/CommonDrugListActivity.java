package com.xywy.askforexpert.module.drug;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.discovery.medicine.common.ViewCallBack;
import com.xywy.askforexpert.module.discovery.medicine.module.web.WebActivity;
import com.xywy.askforexpert.module.discovery.medicine.util.RSAUtils;
import com.xywy.askforexpert.module.drug.adapter.DrugListAdapter;
import com.xywy.askforexpert.module.drug.bean.DrugBean;
import com.xywy.askforexpert.module.drug.bean.RrescriptionData;
import com.xywy.askforexpert.module.drug.request.DrugAboutRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.xywy.uilibrary.recyclerview.wrapper.CustomRecyclerViewLoadMoreWrapper;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用药列表 在线诊室IM stone
 */
public class CommonDrugListActivity extends YMBaseActivity implements View.OnClickListener {

    private EmptyWrapper mEmptyWrapper;
    private CustomRecyclerViewLoadMoreWrapper mLoadMoreWrapper;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private int DRUG_CODE = 1;

    private DrugListAdapter adapter;
    private List<DrugBean> mList = new ArrayList<>();

    private String mDoctorId = YMUserService.getCurUserId();
    private int mPage = 1;
    private int pageSize = 10;
    private int mEmptyLayoutId;
    private boolean mIsLoadingMore;

    //是否是进行常用药操作  true 加入常用药和移除操作 false 进行开处方操作 加入处方
    private boolean mIsCommonOpt;
    //是否是请求常用药列表
    private boolean mIsCommonDrug;
    private String typeName;
    private String cateid;
    private String drugid;
    private TextView prescription_tv;
    //    private String drugid;
//    boolean drugFlag = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.common_recyclerview_list;
    }


    @Override
    protected void initView() {
        prescription_tv = (TextView) findViewById(R.id.prescription_tv);
        prescription_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2,new Intent());
                finish();
            }
        });
        mIsCommonOpt = getIntent().getBooleanExtra(Constants.KEY_VALUE, true);
        cateid = getIntent().getStringExtra(Constants.KEY_CITY);
//        drugid = getIntent().getStringExtra(Constants.KEY_PROVINCE);
        typeName = getIntent().getStringExtra(Constants.KEY_MESSAGE);
        drugid = getIntent().getStringExtra("drugid");
        mIsCommonDrug = "我的常用药".equals(typeName);

        titleBarBuilder.setTitleText(typeName);
//        drugFlag = getIntent().getBooleanExtra("drugFlag",false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                loadData(mPage, mDoctorId, State.ONREFRESH.getFlag());
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        itemDecoration.setDividerDrawble(getResources().getDrawable(R.drawable.item_divider_f2f2f2_8dp));
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        adapter = new DrugListAdapter(this, mList, mIsCommonOpt, mIsCommonDrug);

        mEmptyWrapper = new EmptyWrapper(adapter);
        mEmptyLayoutId = R.layout.layout_list_no_data_common;

        mLoadMoreWrapper = new CustomRecyclerViewLoadMoreWrapper(mEmptyWrapper, mRecyclerView);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //避免上来就加载
                if (!mIsLoadingMore
                        && mList.size() > 0
                        && mLoadMoreWrapper.getStatus() == CustomRecyclerViewLoadMoreWrapper.LoadingMoreViewStatus.SHOWLOADING.getFlag()) {
                    mPage++;
                    mIsLoadingMore = true;
                    loadData(mPage, mDoctorId, State.LOADMORE.getFlag());
                }
            }
        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);

        adapter.setViewCallBack(new ViewCallBack() {
            @Override
            public void onClick(int tag, final int position, Object data,boolean delete) {
                final DrugBean entity = (DrugBean) data;
                if (entity != null) {
                    String product_id = mIsCommonDrug?entity.getPid():entity.id;//商品id
                    if (tag == 0) {
                        //进入药品详情页
                        long doctor_id = Long.parseLong(YMUserService.getCurUserId());

                        String url = MyConstant.H5_BASE_URL + "prescription/drug-detail?doctorid="+ RSAUtils.encodeUserid(doctor_id+"")+"&id="+product_id;
                        WebActivity.startActivity(CommonDrugListActivity.this, url,"药品详情");
                    } else if (tag == 1) {
                        //操作 jia
                        if (mIsCommonOpt) {
                            //常用药列表
                            if (mIsCommonDrug || ((DrugBean) data).isCommon == 1) {
                                DrugAboutRequest.getInstance().getCommonDrugRemove(mDoctorId, product_id).subscribe(new BaseRetrofitResponse<BaseData>() {
                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(BaseData entry) {
                                        if (entity != null) {
                                            DrugBean drug = adapter.getDatas().get(position);
                                            //常用药直接移除 非常用药修改状态
                                            if (mIsCommonDrug) {
                                                adapter.getDatas().remove(drug);
                                                if(adapter.getDatas().size()==0){
                                                    mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                                                }
                                            } else {
                                                drug.isCommon = 0;
                                            }
                                            mLoadMoreWrapper.notifyDataSetChanged();
                                        }
                                    }
                                });

                            } else {
                                DrugAboutRequest.getInstance().getCommonDrugAdd(mDoctorId, product_id).subscribe(new BaseRetrofitResponse<BaseData>() {
                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(BaseData entry) {
                                        if (entity != null) {
                                            DrugBean drug = adapter.getDatas().get(position);
                                            drug.isCommon = 1;
                                            mLoadMoreWrapper.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }

                        } else {
                            if (delete){
                                RrescriptionData.getInstance().removeMedicine(product_id);
                                mLoadMoreWrapper.notifyDataSetChanged();
                            }else {
                                //跳转到处方设置页面
                                if (RrescriptionData.getInstance().getMedicineList().size()<5){
                                    startActivity(new Intent(CommonDrugListActivity.this, DrugSettingActivity.class).
                                            putExtra("drugInfo", entity).putExtra("mIsCommonDrug",mIsCommonDrug));
                                }else{
                                    shortToast("最多只能添加5种药品");
                                }

                            }
                        }
                    }
                }
            }
        });


//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        mSwipeRefreshLayout.setClipChildren(true);

//        adapter.setOnItemClickListener(new QueItemAdapter.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(int position, Object object) {
//                if (TYPEURL.equals("del")) {
//                    ToastUtils.shortToast("该问题已删除");
//                } else {
//                    Intent intent = new Intent(CommonDrugListActivity.this,
//                            QueDetailActivity.class);
//                    QuestionNote data = (QuestionNote) object;
//                    intent.putExtra("tag", "myreply");
//                    intent.putExtra("type", data.type);
//                    intent.putExtra("index", 0);
//                    intent.putExtra(INTENT_KEY_ISFROM, MYREPLY);//从历史回复的帖子列表跳转
//                    intent.putExtra("id", data.qid + "");
//                    startActivity(intent);
//
//
//                    intent.putExtra(INTENT_KEY_Q_TYPE, data.q_type);
//                }
//            }
//        });
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (RrescriptionData.getInstance().getMedicineList().size()==0){
//            prescription_tv.setVisibility(View.GONE);
//        }else{
//            prescription_tv.setVisibility(View.VISIBLE);
//        }
        if (RrescriptionData.getInstance().BACK_FLAG){
            finish();
            return;
        }
        mLoadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        mPage = 1;
        loadData(mPage, mDoctorId, State.ONREFRESH.getFlag());
    }


    private void loadData(int page, String doctorId, final int state) {
        if (mIsCommonDrug) {
            DrugAboutRequest.getInstance().getCommonDrugList(doctorId, page).subscribe(new BaseRetrofitResponse<BaseData<List<DrugBean>>>() {
                @Override
                public void onError(Throwable e) {
                    handleList(state, null);
                }

                @Override
                public void onNext(BaseData<List<DrugBean>> entry) {
                    handleList(state, entry);
                }
            });
        } else {
            DrugAboutRequest.getInstance().getGoodsList(mIsCommonOpt ? doctorId : null, cateid, drugid, page).subscribe(new BaseRetrofitResponse<BaseData<List<DrugBean>>>() {
                @Override
                public void onError(Throwable e) {
                    handleList(state, null);
                }

                @Override
                public void onNext(BaseData<List<DrugBean>> entry) {
                    handleList(state, entry);
                }
            });
        }

    }

    /**
     * 处理列表请求结果
     *
     * @param state
     * @param entry
     */
    private void handleList(int state, BaseData<List<DrugBean>> entry) {
        if (entry == null) {
            //失败的处理
            if (state == State.LOADMORE.getFlag()) {
                mIsLoadingMore = false;
                mPage--;
                if (mPage <= 1) {
                    mPage = 1;
                }
            } else {
                mList.clear();
                adapter.setData(mList);
            }
            mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
            mLoadMoreWrapper.loadDataFailed();
            mLoadMoreWrapper.notifyDataSetChanged();
        } else {
            //成功的处理
            mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条

            if (entry != null
                    && entry.getData() != null) {
                int size = entry.getData().size();
                if (state == State.LOADMORE.getFlag()) {
                    mIsLoadingMore = false;
                    if (size == 0) {
                        //stone 后台一页展示10个数据 数据过于少,就直接不展示没有最多数据的view
                        if (mList.size() > 10) {
                            mLoadMoreWrapper.showNoMoreData();
                        } else {
                            mLoadMoreWrapper.loadDataFailed();
                        }
                    } else {
                        mLoadMoreWrapper.showLoadMore();
                        mList.addAll(entry.getData());
                    }
                    adapter.setData(mList);
                    mLoadMoreWrapper.notifyDataSetChanged();
                } else {
                    if (size == 0) {
                        mList.clear();
                        mLoadMoreWrapper.loadDataFailed();
                        mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                    } else {
                        mLoadMoreWrapper.showLoadMore();
                        mList = entry.getData();
                    }
                    adapter.setData(mList);
                    mLoadMoreWrapper.notifyDataSetChanged();
                }
            } else {
                mLoadMoreWrapper.loadDataFailed();
                if (state == State.LOADMORE.getFlag()) {
                    mIsLoadingMore = false;
                } else {
                    mList.clear();
                    mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                }
                adapter.setData(mList);
                mLoadMoreWrapper.notifyDataSetChanged();
            }

            // TODO: 2018/6/25 不分页加载
//            mLoadMoreWrapper.loadDataFailed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            default:
                break;
        }
    }

    private enum State {
        ONREFRESH(1), LOADMORE(2);
        private int flag;

        private State(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return this.flag;
        }
    }
}
