package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyRecordEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.request.MedicineRequest;
import com.xywy.askforexpert.module.discovery.medicine.view.XYWYLoadMoreWrapper;
import com.xywy.base.XywyBaseActivity;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.xywy.util.LogUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 用药记录
 * stone
 * 2017/10/30 下午1:56
 */
public class PharmacyRecordActivity extends XywyBaseActivity {
    public static String DOCTOR_ID = "doctor_id";
    private RecyclerView mPharmacyRecordRecyclerView;
    private PharmacyRecordAdapter mPharmacyRecordAdapter;

    List<PharmacyRecordEntity> mPharmacyRecordList = new ArrayList<>();

    private EditText mSearch;
    private int mDoctorId;
    private String mKeyword;
    private XYWYLoadMoreWrapper mLoadMoreWrapper;
    private int mPage, mPagesize = 10;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EmptyWrapper mEmptyWrapper;


    public static void start(Activity activity, String curUserId) {
        Intent startIntent = new Intent(activity, PharmacyRecordActivity.class);
        startIntent.putExtra(DOCTOR_ID, Integer.parseInt(curUserId));
        activity.startActivity(startIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 2017/10/30 假数据
//        putTestData();

        setContentView(R.layout.activity_pharmacy_record);
        //初始化统一bar，将系统栏的背景设置成透明色
        CommonUtils.initSystemBar(this);
        titleBarBuilder.setBackGround(R.drawable.toolbar_bg_no_alpha);
        titleBarBuilder.setTitleText("用药记录");
        titleBarBuilder.setBackIcon("", R.drawable.base_back_btn_selector_new, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//stone 使用 YMApplication.getLoginInfo().getData().getPid()
//        mDoctorId = getIntent().getIntExtra(DOCTOR_ID, -1);
        if (!TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getPid())) {
            mDoctorId = getIntent().getIntExtra(DOCTOR_ID, Integer.parseInt(YMApplication.getLoginInfo().getData().getPid()));
        }
        LogUtils.i("mDoctorId=" + mDoctorId);
        mKeyword = "";
        mPage = 1;
        getData(mDoctorId, mKeyword, mPage, mPagesize, State.INIT.getFlag());
        initView();
    }

    private void putTestData() {
        PharmacyRecordEntity entity=new PharmacyRecordEntity();
        entity.setOrder_status(1);
        entity.setOid("1");
        entity.setId("1");
        entity.setImage("http://doctor.club.xywy.com/images/upload/paper/2015033015112420729.jpg");
        entity.setTime(String.valueOf(System.currentTimeMillis()/1000));
        PharmacyRecordEntity.Drug drug=new PharmacyRecordEntity.Drug();
        drug.setPid(1);
        drug.setAmount("100");
        drug.setGname("小李先生");
        drug.setGid("1");
        drug.setTake_method(0);
        drug.setTake_num("1");
        drug.setTake_rate("3");
        drug.setTake_time("饭后");
        drug.setTake_unit(1);
        drug.setGsku("脑白金");
        drug.setNum("1000");
        drug.setPrice("99");
        drug.setRemark("两个疗程即可见效");
        drug.setSpecification("0.6g*36片");
        entity.setDrug_list(drug);
        mPharmacyRecordList.add(entity);

        PharmacyRecordEntity entity2=new PharmacyRecordEntity();
        entity2.setOrder_status(2);
        entity2.setOid("1");
        entity2.setId("1");
        entity2.setImage("http://doctor.club.xywy.com/images/upload/paper/2015033015112420729.jpg");
        entity2.setTime(String.valueOf(System.currentTimeMillis()/1000));
        PharmacyRecordEntity.Drug drug2=new PharmacyRecordEntity.Drug();
        drug2.setPid(1);
        drug2.setAmount("100");
        drug2.setGname("宝宝哥");
        drug2.setGid("1");
        drug2.setTake_method(1);
        drug2.setTake_num("1");
        drug2.setTake_rate("5");
        drug2.setTake_time("饭后");
        drug2.setTake_unit(1);
        drug2.setGsku("脑白金");
        drug2.setNum("1000");
        drug2.setPrice("99");
        drug2.setRemark("两个疗程即可见效");
        drug2.setSpecification("0.6g*36片/盒");
        entity2.setDrug_list(drug2);
        mPharmacyRecordList.add(entity2);


        PharmacyRecordEntity entity3=new PharmacyRecordEntity();
        entity3.setOrder_status(3);
        entity3.setOid("1");
        entity3.setId("1");
        entity3.setImage("http://doctor.club.xywy.com/images/upload/paper/2015033015112420729.jpg");
        entity3.setTime(String.valueOf(System.currentTimeMillis()/1000));
        PharmacyRecordEntity.Drug drug3=new PharmacyRecordEntity.Drug();
        drug3.setPid(1);
        drug3.setAmount("100");
        drug3.setGname("小李先生");
        drug3.setGid("1");
        drug3.setTake_method(2);
        drug3.setTake_num("1");
        drug3.setTake_rate("1");
        drug3.setTake_time("饭后");
        drug3.setTake_unit(1);
        drug3.setGsku("脑白金");
        drug3.setNum("1000");
        drug3.setPrice("99");
        drug3.setRemark("两个疗程即可见效");
        drug3.setSpecification("0.6g*36片");
        entity3.setDrug_list(drug3);
        mPharmacyRecordList.add(entity3);


        PharmacyRecordEntity entity4=new PharmacyRecordEntity();
        entity4.setOrder_status(4);
        entity4.setOid("1");
        entity4.setId("1");
        entity4.setImage("http://doctor.club.xywy.com/images/upload/paper/2015033015112420729.jpg");
        entity4.setTime(String.valueOf(System.currentTimeMillis()/1000));
        PharmacyRecordEntity.Drug drug4=new PharmacyRecordEntity.Drug();
        drug4.setPid(1);
        drug4.setAmount("100");
        drug4.setGname("光哥");
        drug4.setGid("1");
        drug4.setTake_method(3);
        drug4.setTake_num("1");
        drug4.setTake_rate("4");
        drug4.setTake_time("饭后");
        drug4.setTake_unit(4);
        drug4.setGsku("脑白金");
        drug4.setNum("1000");
        drug4.setPrice("99");
        drug4.setRemark("两个疗程即可见效");
        drug4.setSpecification("0.6g*36片/盒");
        entity4.setDrug_list(drug4);
        mPharmacyRecordList.add(entity4);

        PharmacyRecordEntity entity5=new PharmacyRecordEntity();
        entity5.setOrder_status(5);
        entity5.setOid("1");
        entity5.setId("1");
        entity5.setImage("http://doctor.club.xywy.com/images/upload/paper/2015033015112420729.jpg");
        entity5.setTime(String.valueOf(System.currentTimeMillis()/1000));
        PharmacyRecordEntity.Drug drug5=new PharmacyRecordEntity.Drug();
        drug5.setPid(1);
        drug5.setAmount("100");
        drug5.setGname("了了");
        drug5.setGid("1");
        drug5.setTake_method(4);
        drug5.setTake_num("1");
        drug5.setTake_rate("7");
        drug5.setTake_time("饭后");
        drug5.setTake_unit(2);
        drug5.setGsku("脑白金");
        drug5.setNum("1000");
        drug5.setPrice("99");
        drug5.setRemark("两个疗程即可见效");
        drug5.setSpecification("0.6g*36片");
        entity5.setDrug_list(drug5);
        mPharmacyRecordList.add(entity5);

        PharmacyRecordEntity entity6=new PharmacyRecordEntity();
        entity6.setOrder_status(6);
        entity6.setOid("1");
        entity6.setId("1");
        entity6.setImage("http://doctor.club.xywy.com/images/upload/paper/2015033015112420729.jpg");
        entity6.setTime(String.valueOf(System.currentTimeMillis()/1000));
        PharmacyRecordEntity.Drug drug6=new PharmacyRecordEntity.Drug();
        drug6.setPid(1);
        drug6.setAmount("100");
        drug6.setGname("敏敏");
        drug6.setGid("1");
        drug6.setTake_method(1);
        drug6.setTake_num("1");
        drug6.setTake_rate("3");
        drug6.setTake_time("饭后");
        drug6.setTake_unit(1);
        drug6.setGsku("脑白金");
        drug6.setNum("1000");
        drug6.setPrice("99");
        drug6.setRemark("诊断结果良好");
        drug6.setSpecification("0.6g*36片");
        entity6.setDrug_list(drug6);
        mPharmacyRecordList.add(entity6);


        PharmacyRecordEntity entity7=new PharmacyRecordEntity();
        entity7.setOrder_status(3);
        entity7.setOid("1");
        entity7.setId("1");
        entity7.setImage("http://doctor.club.xywy.com/images/upload/paper/2015033015112420729.jpg");
        entity7.setTime(String.valueOf(System.currentTimeMillis()/1000));
        PharmacyRecordEntity.Drug drug7=new PharmacyRecordEntity.Drug();
        drug7.setPid(1);
        drug7.setAmount("100");
        drug7.setGname("小金子");
        drug7.setGid("1");
        drug7.setTake_method(1);
        drug7.setTake_num("1");
        drug7.setTake_rate("5");
        drug7.setTake_time("饭后");
        drug7.setTake_unit(1);
        drug7.setGsku("脑白金");
        drug7.setNum("1000");
        drug7.setPrice("99");
        drug7.setRemark("诊断结果良好");
        drug7.setSpecification("0.6g*36片");
        entity7.setDrug_list(drug7);
        mPharmacyRecordList.add(entity7);

        mPharmacyRecordList.add(entity);
        mPharmacyRecordList.add(entity2);
        mPharmacyRecordList.add(entity3);
        mPharmacyRecordList.add(entity4);
        mPharmacyRecordList.add(entity5);
        mPharmacyRecordList.add(entity6);
        mPharmacyRecordList.add(entity7);
    }

    private void initView() {
        showCommonBaseTitle();

        mSearch = (EditText) findViewById(R.id.pharmacy_search);
        mSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    mKeyword = mSearch.getText().toString().trim();
                    mPage = 1;
                    getData(mDoctorId, mKeyword, mPage, mPagesize, State.INIT.getFlag());
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (imm.isActive()) {

                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.i("invoke onRefresh...");
                mPage = 1;
                getData(mDoctorId, mKeyword, mPage, mPagesize, State.INIT.getFlag());
            }
        });
        mPharmacyRecordRecyclerView = (RecyclerView) findViewById(R.id.pharmacy_record);
        mPharmacyRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPharmacyRecordRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mPharmacyRecordAdapter = new PharmacyRecordAdapter(this);
        //stone 添加用药记录跳转
        mPharmacyRecordAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                PharmacyRecordEntity item = mPharmacyRecordAdapter.getItem(position);
                YMOtherUtils.skip2PrecsciptionDetail(PharmacyRecordActivity.this,item.getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        mPharmacyRecordAdapter.setData(mPharmacyRecordList);
        mEmptyWrapper = new EmptyWrapper(mPharmacyRecordAdapter);


        mLoadMoreWrapper = new XYWYLoadMoreWrapper(mEmptyWrapper, mPharmacyRecordRecyclerView);
        mLoadMoreWrapper.setLoadMoreView(R.layout.loading_more);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPage++;
                LogUtils.i("load more .......mPage=" + mPage + "---mPagesize=" + mPagesize);
                getData(mDoctorId, mKeyword, mPage, mPagesize, State.LOADMORE.getFlag());

            }
        });
        mPharmacyRecordRecyclerView.setAdapter(mLoadMoreWrapper);
    }

    /**
     * 获取用药记录
     * stone
     * 2017/10/30 下午2:04
     */
    private void getData(int doctorId, final String keyword, int page, int pagesize, final int state) {
        if (-1 != doctorId) {
            MedicineRequest.getInstance().getPharmacyRecord(doctorId, keyword, page, pagesize).subscribe(new BaseRetrofitResponse<BaseData<List<PharmacyRecordEntity>>>() {
                @Override
                public void onStart() {
                    super.onStart();
                    if (state == State.LOADMORE.getFlag()) {
                        hideProgressDialog();
                    }
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    mPage--;
                    if(mPage<=1){
                        mPage=1;
                    }
                    mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
                }

                @Override
                public void onNext(BaseData<List<PharmacyRecordEntity>> entry) {
                    super.onNext(entry);
                    if (entry != null && entry.getData() != null) {
                        int size = entry.getData().size();
                        if (state == State.LOADMORE.getFlag()) {
                            if (size == 0) {
                                mLoadMoreWrapper.setLoadingState(true);
                                return;
                            }
                            for (int i = 0; i < size; i++) {
                                mPharmacyRecordList.add(entry.getData().get(i));
                            }
                            mPharmacyRecordAdapter.setData(mPharmacyRecordList);
                            mLoadMoreWrapper.notifyDataSetChanged();
                        } else {
                            if (size == 0) {
                                mEmptyWrapper.setEmptyView(R.layout.item_pharmacy_record_list_empty);
                                mLoadMoreWrapper.setLoadingState(true);
                            }
                            mPharmacyRecordList = entry.getData();
                            mPharmacyRecordAdapter.setData(mPharmacyRecordList);
                            mLoadMoreWrapper.notifyDataSetChanged();
                        }

                    }
                }
            });
        }
    }

    private enum State {
        INIT(0), ONREFRESH(1), LOADMORE(2);
        private int flag;

        private State(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return this.flag;
        }
    }

}
