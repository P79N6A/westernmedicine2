package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineCartEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.request.MedicineRequest;
import com.xywy.askforexpert.module.discovery.medicine.module.web.WebActivity;
import com.xywy.askforexpert.module.discovery.medicine.util.RSAUtils;
import com.xywy.askforexpert.module.discovery.medicine.view.SortView;
import com.xywy.askforexpert.module.discovery.medicine.view.XYWYLoadMoreWrapper;
import com.xywy.base.XywyBaseActivity;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.dialog.bottompopupdialog.listener.BtnClickListener;
import com.xywy.uilibrary.dialog.middlelistpopupwindow.MiddleListPopupWindow;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.xywy.util.LogUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

public class MedicineListActivity extends XywyBaseActivity {

    public static String MEDICINE_TYPE = "medicine_type";
    public static String MEDICINE_DRUG_ID = "medicine_drug_id";
    public static String MEDICINE_DRUG_NAME = "medicine_drug_name";
    public static String FLAG = "flag";
    private SortView mSortView;
    private RecyclerView mMedicineListView;
    private MedicineListAdapter mMedicineListAdapter;
    private XYWYLoadMoreWrapper mLoadMoreWrapper;
    private List<MedicineEntity> mMedicineList = new ArrayList<>();
    private TextView mTv_total_price,mNum_text;
    private Button mSubmit;

    private int mCateId;
    private int mDrugId;
    private String mDrugName;
    private boolean mIsCommonMedicine;
    private int mOrder_by,mStort;

    private EditText mSearch;
    private View mBack;
    private int mPage ,mPagesize = 10;
    private EmptyWrapper mEmptyWrapper;
    private RelativeLayout mRootView;
    private LinearLayout mNetworkError,mCommonDrugEmptyLayout;
    private int mCurrentState;

    private static final String IS_FROM = "isFrom";
    private static final String IS_FROM_VALUE = "MedicineAssistantActivity";
    private String mIsFrom;

//    public static void startActivity(Context context, int type,boolean flag) {
//        Intent startIntent = new Intent(context, MedicineListActivity.class);
//        startIntent.putExtra(MEDICINE_TYPE, type);
//        startIntent.putExtra(FLAG,flag);
//        context.startActivity(startIntent);
//    }

    public static void startActivity(Context context, int type,boolean flag,String from) {
        Intent startIntent = new Intent(context, MedicineListActivity.class);
        startIntent.putExtra(MEDICINE_TYPE, type);
        startIntent.putExtra(FLAG,flag);
        startIntent.putExtra(IS_FROM,from);
        context.startActivity(startIntent);
    }

//    public static void startActivityFromSearch(Context context, int drugId,String drugName) {
//        Intent startIntent = new Intent(context, MedicineListActivity.class);
//        startIntent.putExtra(MEDICINE_DRUG_ID, drugId);
//        startIntent.putExtra(MEDICINE_DRUG_NAME, drugName);
//        context.startActivity(startIntent);
//    }

    public static void startActivityFromSearch(Context context, int drugId,String drugName,String from) {
        Intent startIntent = new Intent(context, MedicineListActivity.class);
        startIntent.putExtra(MEDICINE_DRUG_ID, drugId);
        startIntent.putExtra(MEDICINE_DRUG_NAME, drugName);
        startIntent.putExtra(IS_FROM,from);
        context.startActivity(startIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        //初始化统一bar，将系统栏的背景设置成透明色
        CommonUtils.initSystemBar(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNetworkError = (LinearLayout) findViewById(R.id.network_error);
        mCommonDrugEmptyLayout = (LinearLayout) findViewById(R.id.common_drug_empty_layout);
        initData();

        initView();
    }

    private void initView() {
        hideCommonBaseTitle();
        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRootView = (RelativeLayout)findViewById(R.id.rl_root);
        RelativeLayout medicine_bottom = (RelativeLayout)findViewById(R.id.medicine_bottom);
        if(IS_FROM_VALUE.equals(mIsFrom)){
            medicine_bottom.setVisibility(View.GONE);
        }else {
            medicine_bottom.setVisibility(View.VISIBLE);
        }
        mSearch = (EditText) findViewById(R.id.pharmacy_search);
        if(!TextUtils.isEmpty(mDrugName)){
            mSearch.setText(mDrugName);
            mSearch.setSelection(mDrugName.length());
        }

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicineListActivity.this, SearchActivity.class);
                intent.putExtra(IS_FROM,mIsFrom);
                startActivity(intent);
            }
        });
        mNetworkError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentState == State.INIT.getFlag()){
                    getData(mOrder_by,mStort,mPage,mPagesize,State.INIT.getFlag());
                }else if(mCurrentState == State.LOADMORE.getFlag()){
                    mPage--;
                    if(mPage<=1){
                        mPage=1;
                    }
                    getData(mOrder_by,mStort,mPage,mPagesize,State.LOADMORE.getFlag());
                }

            }
        });
        mSortView = (SortView)findViewById(R.id.sortview);
        mSortView.setListener(new SortView.OnSrotTypeChangeListener() {
            @Override
            public void onSortTypeChange(SortView.SORTED_TYPE type) {
//                LogUtils.i("type="+type);
                mSortView.setEnabled(false);
                switch (type){
                    case SORTED_TYPE_PRICE_UP:
                        mOrder_by = 2;
                        mStort = 1;
                        mPage = 1;
                        getData(mOrder_by,mStort,mPage,mPagesize,State.ONREFRESH.getFlag());
                        break;
                    case SORTED_TYPE_PRICE_DOWN:
                        mOrder_by = 2;
                        mStort = 2;
                        mPage = 1;
                        getData(mOrder_by,mStort,mPage,mPagesize,State.ONREFRESH.getFlag());
                        break;
                    case SORTED_TYPE_COUNT_UP:
                        mOrder_by = 3;
                        mStort = 1;
                        mPage = 1;
                        getData(mOrder_by,mStort,mPage,mPagesize,State.ONREFRESH.getFlag());
                        break;
                    case SORTED_TYPE_COUNT_DOWN:
                        mOrder_by = 3;
                        mStort = 2;
                        mPage = 1;
                        getData(mOrder_by,mStort,mPage,mPagesize,State.ONREFRESH.getFlag());
                        break;
                    case SORTED_TYPE_INDEX_UP:
                        mOrder_by = 4;
                        mStort = 1;
                        mPage = 1;
                        getData(mOrder_by,mStort,mPage,mPagesize,State.ONREFRESH.getFlag());
                        break;
                    case SORTED_TYPE_INDEX_DOWN:
                        mOrder_by = 4;
                        mStort = 2;
                        mPage = 1;
                        getData(mOrder_by,mStort,mPage,mPagesize,State.ONREFRESH.getFlag());
                        break;
                    default:
                        mOrder_by = 1;
                        mStort = 1;
                        mPage = 1;
                        getData(mOrder_by,mStort,mPage,mPagesize,State.ONREFRESH.getFlag());
                        break;
                }
            }
        });
        mMedicineListView = (RecyclerView) findViewById(R.id.medicine_list);

        mMedicineListView.setLayoutManager(new LinearLayoutManager(this));
        mMedicineListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mMedicineListAdapter = new MedicineListAdapter(this,mIsFrom);
        mMedicineListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                long doctor_id = Long.parseLong(YMUserService.getCurUserId());
                MedicineEntity medicineEntity = mMedicineList.get(position);
                int product_id = medicineEntity.getId();//商品id
                String url = MyConstant.H5_BASE_URL + "prescription/drug-detail?doctorid="+RSAUtils.encodeUserid(doctor_id+"")+"&id="+product_id;
                WebActivity.startActivity(MedicineListActivity.this, url,"药品详情");
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                LogUtils.i("onItemLongClick");
                if(mIsCommonMedicine){
                    showBottomPopuWindow(position);
                }
                return false;
            }
        });

        mMedicineListAdapter.setData(mMedicineList);
        mEmptyWrapper = new EmptyWrapper(mMedicineListAdapter);
        mLoadMoreWrapper = new XYWYLoadMoreWrapper(mEmptyWrapper, mMedicineListView);
        mLoadMoreWrapper.setLoadMoreView(R.layout.loading_more);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPage ++;
                LogUtils.e("load more .......mPage="+mPage+"---mPagesize="+mPagesize);
                getData(mOrder_by,mStort,mPage,mPagesize, State.LOADMORE.getFlag());

            }
        });
        mMedicineListView.setAdapter(mLoadMoreWrapper);

        mNum_text = (TextView) findViewById(R.id.num_text);
        mTv_total_price = (TextView) findViewById(R.id.tv_total_price);
        setTotalNumAndTotalPrice();

        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MedicineListActivity.this, RecipeDetailActivity.class));
            }
        });

    }

    private void setTotalNumAndTotalPrice() {
        int totalMedicineCount = 0;
        float totalPrice = 0f;
        List<MedicineCartEntity> medicineCartList = MedicineCartCenter.getInstance().getMedicineList();
        for (int i = 0; i < medicineCartList.size(); i++) {
            MedicineCartEntity medicineCartEntity =  medicineCartList.get(i);
            int count = medicineCartEntity.getEntity().getCount();
            totalMedicineCount += count;
            if(!TextUtils.isEmpty(medicineCartEntity.getEntity().getWksmj())){
                float price = Float.parseFloat(medicineCartEntity.getEntity().getWksmj());
                totalPrice += count * price;
            }

        }
        mNum_text.setText(totalMedicineCount+"");
        mTv_total_price.setText("￥"+remain2(totalPrice));
    }

    /**
     * 保留小数点后两位
     * @param price
     * @return
     */
    private String remain2(float price){
        DecimalFormat decimalFormat=new DecimalFormat("###,###,##0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String result= decimalFormat.format(price);//format 返回的是字符串
        return  result;
    }

    private void initData() {
        Intent intent = getIntent();
        if(null != intent){
            mCateId = getIntent().getIntExtra(MEDICINE_TYPE, -1);
            mDrugId = getIntent().getIntExtra(MEDICINE_DRUG_ID, -1);
            mDrugName = getIntent().getStringExtra(MEDICINE_DRUG_NAME);
            mIsCommonMedicine = getIntent().getBooleanExtra(FLAG,false);
            if(intent.hasExtra(IS_FROM)){
                mIsFrom = getIntent().getStringExtra(IS_FROM);
            }
            LogUtils.i("mDrugId="+mDrugId);
            mOrder_by = 1;
            mStort = 1;
            mPage = 1;
            getData(mOrder_by,mStort,mPage,mPagesize,State.INIT.getFlag());
        }


    }

    private void getData(int order_by,int sort,int page,int pagesize,final int state) {
        if(mCateId!=-1) {
            getDataByCateId(mCateId,order_by,sort,page,pagesize,state);
        } else if(mDrugId!=-1) {
            MedicineRequest.getInstance().getMedicineListForDrugId(mDrugId,order_by,sort,mPage,mPagesize).subscribe(new BaseRetrofitResponse<BaseData<List<MedicineEntity>>>(){
                @Override
                public void onStart() {
                    super.onStart();
                    mCurrentState = state;
                    if(state == State.LOADMORE.getFlag()){
                        hideProgressDialog();
                    }else {
                        showProgressDialog("loading");
                        mNetworkError.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    hideProgressDialog();
                    mSortView.setEnabled(true);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    hideProgressDialog();
                    mSortView.setEnabled(true);
                    if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                        mNetworkError.setVisibility(View.VISIBLE);
                        mPage++;
                    }
                }

                @Override
                public void onNext(BaseData<List<MedicineEntity>> entry) {
                    super.onNext(entry);
                    if(entry!=null && entry.getData()!=null) {
                        dealwithEntry(entry.getData(),state);
                    }
                }
            });
        }
    }

    private void getDataByCateId(int cateId,int order_by,int sort,int page,int pagesize,final int state){
        MedicineRequest.getInstance().getMedicineListForCateId(cateId,order_by,sort,page,pagesize).subscribe(new BaseRetrofitResponse<BaseData<List<MedicineEntity>>>(){
            @Override
            public void onStart() {
                super.onStart();
                mCurrentState = state;
                if(state == State.LOADMORE.getFlag()){
                    hideProgressDialog();
                }else {
                    showProgressDialog("");
                    mNetworkError.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                hideProgressDialog();
                mSortView.setEnabled(true);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hideProgressDialog();
                mSortView.setEnabled(true);
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    mNetworkError.setVisibility(View.VISIBLE);
                    mPage++;
                }

            }

            @Override
            public void onNext(BaseData<List<MedicineEntity>> entry) {
                super.onNext(entry);
                if(entry!=null && entry.getData()!=null) {
                    dealwithEntry(entry.getData(),state);
                }
            }
        });
    }

    private void dealwithEntry(List<MedicineEntity> entry,int state) {
        int size = entry.size();
        if(state == State.LOADMORE.getFlag()){
            if(0 == size){
                mLoadMoreWrapper.setLoadingState(true);
                return;
            }
            for (int i = 0; i < entry.size(); i++) {
                mMedicineList.add(entry.get(i));
            }
            mMedicineListAdapter.setData(mMedicineList);
            mLoadMoreWrapper.notifyDataSetChanged();
        }else {
            if(size == 0){
                LogUtils.i("mIsCommonMedicine=="+mIsCommonMedicine);
                if(mIsCommonMedicine){
                    mCommonDrugEmptyLayout.setVisibility(View.VISIBLE);
                }else {
                    mCommonDrugEmptyLayout.setVisibility(View.GONE);
                }
                mEmptyWrapper.setEmptyView(R.layout.item_pharmacy_record_list_empty);
                mLoadMoreWrapper.setLoadingState(true);
            }
            mMedicineList = entry;
            mMedicineListAdapter.setData(mMedicineList);
            mLoadMoreWrapper.notifyDataSetChanged();
        }
    }

    private void showBottomPopuWindow(final int position) {
        new MiddleListPopupWindow.Builder()
                .addItem("删除", new BtnClickListener() {
                    @Override
                    public void onItemClick() {
//                        UserBean user = UserManager.getInstance().getCurrentLoginUser();
//                        long doctor_id = Long.parseLong(user.getLoginServerBean().getUser_id());
                        long doctor_id = Long.parseLong(YMUserService.getCurUserId());
                        MedicineEntity medicineEntity = mMedicineList.get(position);
                        int product_id = medicineEntity.getId();//商品id
                        MedicineRequest.getInstance().deleteDrug(doctor_id,product_id).subscribe(new BaseRetrofitResponse<BaseData>(){
                            @Override
                            public void onStart() {
                                super.onStart();
                                showProgressDialog("");
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                hideProgressDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                hideProgressDialog();
                            }

                            @Override
                            public void onNext(BaseData entry) {
                                super.onNext(entry);
                                if(entry!=null && entry.getCode() == 10000) {
                                    mMedicineList.remove(position);
                                    if(mMedicineList.size()==0){
                                        if(mIsCommonMedicine){
                                            mCommonDrugEmptyLayout.setVisibility(View.VISIBLE);
                                        }else {
                                            mCommonDrugEmptyLayout.setVisibility(View.GONE);
                                        }
                                        mEmptyWrapper.setEmptyView(R.layout.item_pharmacy_record_list_empty);
                                        mLoadMoreWrapper.setLoadingState(true);
                                    }
                                    mMedicineListAdapter.setData(mMedicineList);
                                    mLoadMoreWrapper.notifyDataSetChanged();
                                }
                            }
                        });

                    }
                })
                .addItem("取消", new BtnClickListener() {
                    @Override
                    public void onItemClick() {
                    }
                })
                .build(MedicineListActivity.this).show();
    }

    private enum State{
        INIT(0),ONREFRESH(1),LOADMORE(2);
        private int flag;
        private State(int flag){
            this.flag = flag;
        }
        public int getFlag(){
            return this.flag;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null !=mLoadMoreWrapper){
            mLoadMoreWrapper.notifyDataSetChanged();
            setTotalNumAndTotalPrice();
        }
    }

}
