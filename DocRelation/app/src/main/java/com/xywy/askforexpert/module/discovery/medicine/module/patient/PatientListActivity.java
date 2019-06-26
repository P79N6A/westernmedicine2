package com.xywy.askforexpert.module.discovery.medicine.module.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRxBus;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.request.PatientListRequest;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.view.IndexBar;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.view.SuspensionDecoration;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.uilibrary.dialog.bottompopupdialog.listener.BtnClickListener;
import com.xywy.uilibrary.dialog.middlelistpopupwindow.MiddleListPopupWindow;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by xgxg on 2017/10/19.
 * 患者列表
 */

public class PatientListActivity extends YMBaseActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private NewPatientAdapter mNewPaientAdapter;
    private EmptyWrapper emptyWrapper;
    private List<Patient> mPatientList = new ArrayList<>();
    private int mDoctorId;
    private SuspensionDecoration mDecoration;
    private IndexBar mIndexBar;
    private final String RECIPE_SICK = "recipe_sick";
    private final String PATIENT_ID = "patientId";
    private final String PATIENT = "patient";
    private final String PATIENT_BUNDLE = "patient_bundle";
    private LinearLayout mNetworkError;
    private Patient mPatient;

    @Override
    protected void initView() {
        //监听到系统消息，刷新患者列表的接口数据
        MyRxBus.registerPatientInfoChanged(new EventSubscriber() {
            @Override
            public void onNext(Object o) {
                LogUtils.i("收到患者信息改变");
                initData(State.ONREFRESH.getFlag());
            }
        }, this);
        initTitle();
        mNetworkError = (LinearLayout) findViewById(R.id.network_error);
        initData(State.INIT.getFlag());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mNetworkError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(State.INIT.getFlag());
            }
        });
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        handleData(mPatientList);
        mNewPaientAdapter = new NewPatientAdapter(this, mPatientList);
        mNewPaientAdapter.setData(mPatientList);
        mRecyclerView.setAdapter(mNewPaientAdapter);
        emptyWrapper = new EmptyWrapper(mNewPaientAdapter);
        mRecyclerView.setAdapter(emptyWrapper);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(State.ONREFRESH.getFlag());
            }
        });
        mDecoration = new SuspensionDecoration(this, mPatientList);
        mRecyclerView.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        //使用indexBar
        TextView mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar

        //indexbar初始化
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager

        mNewPaientAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (view != null && view.getTag() != null && view.getTag() instanceof Patient) {
                    Patient entry = (Patient) view.getTag();
                    //stone 打开
//                    PatientManager.getInstance().setPatient(entry);
                    Intent intent = new Intent(PatientListActivity.this, PatientDetailsActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(PATIENT, entry);
                    intent.putExtra(PATIENT_BUNDLE, b);
                    startActivity(intent);

//
//                    String oldPatientId = SharedPreferencesHelper.getIns(PatientListActivity.this).getString(PATIENT_ID,"");
//                    if (!entry.getUId().equals(oldPatientId)) {
//                        MedicineCartCenter.getInstance().removeAllMedicine();//不进行缓存，如果给之前的用户添加了处方笺，
//                        //不进行缓存，清空诊断信息
//                        SharedPreferencesHelper.getIns(PatientListActivity.this).putString(RECIPE_SICK, "");
//                    }
//                    SharedPreferencesHelper.getIns(PatientListActivity.this).putString(PATIENT_ID,entry.getUId());
//                    //暂时注销
////                    HxUserHelper.addUser(entry.getPatientHxid(),entry.getRealName(),entry.getPhoto());
////
////                    Intent intent = new Intent(PatientListActivity.this, ChatActivity.class);
////                    intent.putExtra(Constant.EXTRA_USER_ID, entry.getPatientHxid());
////
////                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                showBottomPopuWindow(position);
                return false;
            }
        });
    }

    private void showBottomPopuWindow(final int position) {
        new MiddleListPopupWindow.Builder()
                .addItem("删除", new BtnClickListener() {
                    @Override
                    public void onItemClick() {
                        String doctor_id = YMUserService.getCurUserId();
                        mPatient = mPatientList.get(position);
                        final String patient_id = mPatient.getUId();//患者id
                        PatientListRequest.getInstance().removePatient(doctor_id, patient_id).subscribe(new BaseRetrofitResponse<BaseData>() {
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
                                if (entry != null && entry.getCode() == 10000) {

                                    //stone 刷新患者列表
                                    MyRxBus.notifyLatestPatientInfoChanged();

                                    //stone 删除和某个user会话，如果需要保留聊天记录，传false
                                    EMClient.getInstance().chatManager().deleteConversation(mPatient.getPatientHxid(), true);

                                    mPatientList.remove(mPatient);
                                    if (mPatientList.size() == 0) {
                                        mIndexBar.setVisibility(View.GONE);
                                        emptyWrapper.setEmptyView(R.layout.item_patient_list_empty);
                                    } else {
                                        mIndexBar.setVisibility(View.VISIBLE);
                                    }
                                    emptyWrapper.notifyDataSetChanged();
                                    mIndexBar.setmSourceDatas(mPatientList)//设置数据
                                            .invalidate();
                                    mDecoration.setmDatas(mPatientList);
                                    mNewPaientAdapter.setData(mPatientList);
                                    mNewPaientAdapter.notifyDataSetChanged();
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
                .build(PatientListActivity.this).show();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_patient_list_new;
    }

    private void handleData(List<Patient> data) {
//        //test
//        /***************************************************************/
//        Patient patient = new Patient();
//        patient.setUId("9999");
//        patient.setUpdated_at("1492170756");
//        patient.setRealName("");
//        patient.setSex("0");
//        patient.setAge("0");
//        patient.setPhoto("http://wx.qlogo.cn/mmopen/Q3auHgzwzM6GeB8g3Lzu7lZOrR7n5qHfOVypxRnUTtCVyGLVH6NZe9UNjDkSds5c7hIkxjJOIQz5wmZheRKBZQ/0");
//        patient.setHx_user("medicine_uid_9999_test");
//        data.add(patient);
//
//        patient = new Patient();
//        patient.setUId("9998");
//        patient.setUpdated_at("1492170755");
//        patient.setRealName("张武ji");
//        patient.setSex("0");
//        patient.setAge("0");
//        patient.setPhoto("http://wx.qlogo.cn/mmopen/Q3auHgzwzM6GeB8g3Lzu7lZOrR7n5qHfOVypxRnUTtCVyGLVH6NZe9UNjDkSds5c7hIkxjJOIQz5wmZheRKBZQ/0");
//        patient.setHx_user("medicine_uid_9998_test");
//        data.add(patient);
//
//        patient = new Patient();
//        patient.setUId("9998");
//        patient.setUpdated_at("1492170755");
//        patient.setRealName("1张武ji");
//        patient.setSex("0");
//        patient.setAge("0");
//        patient.setPhoto("http://wx.qlogo.cn/mmopen/Q3auHgzwzM6GeB8g3Lzu7lZOrR7n5qHfOVypxRnUTtCVyGLVH6NZe9UNjDkSds5c7hIkxjJOIQz5wmZheRKBZQ/0");
//        patient.setHx_user("medicine_uid_9998_test");
//        data.add(patient);
//        /***************************************************************/
        mPatientList.clear();
        for (int i = 0; i < data.size(); i++) {

            Patient p = new Patient();
            String username = data.get(i).getRealName();
            String pinyin = "";
            String firstLetter = "";
            if (username.matches("^[\u4e00-\u9fa5]+")) {
                //username是汉字则转化成拼音，获取首字母
                pinyin = ChineseToPinyinHelper.getInstance().getFirstSpell(username);
                firstLetter = pinyin.substring(0, 1).toUpperCase();
            } else if (username.matches("^[a-zA-Z]+")) {
                //username是英文的，直接获取首字母
                pinyin = username;
                firstLetter = username.substring(0, 1).toUpperCase();
            } else {
                //username既不是汉字也不是英文，则都归属于#
                if (!TextUtils.isEmpty(username)) {
                    if (username.substring(0, 1).matches("^[a-zA-Z]$")) {
                        pinyin = username;
                        firstLetter = username.substring(0, 1);
                    } else if (username.substring(0, 1).matches("^[\u4e00-\u9fa5]$")) {
                        //username是起始位置是汉字后面的不全是汉字，则将起始位置字符的转化成拼音，获取首字母
                        pinyin = ChineseToPinyinHelper.getInstance().getFirstSpell(username.substring(0, 1));
                        firstLetter = pinyin.substring(0, 1).toUpperCase();
                    } else {
                        //起始位置字符既不是字母也不是汉字
                        pinyin = "#" + username;
                        firstLetter = "#";
                    }
                } else {
                    //名字为空字符串
                    pinyin = "#" + username;
                    firstLetter = "#";
                }

            }
//            LogUtils.i("pinyin="+pinyin+"---firstLetter="+firstLetter+"---username="+username);
            if (!TextUtils.isEmpty(data.get(i).getUpdated_at())) {
                p.setUpdated_at(data.get(i).getUpdated_at());
            }
            p.setUId(data.get(i).getUId());
            p.setRealName(username);
            p.setAge(data.get(i).getAge());
            if (!TextUtils.isEmpty(data.get(i).getSex())) {
                p.setSex(data.get(i).getSex());
            } else {
                p.setSex("");
            }
            p.setHx_user(data.get(i).getHx_user());
            p.setPhoto(data.get(i).getPhoto());
            p.setPinyin(pinyin);
            p.setFirstLetter(firstLetter);
            p.setGroup(data.get(i).getGroup());
            p.setProvince(data.get(i).getProvince());
            p.setCity(data.get(i).getCity());
            mPatientList.add(p);
        }

        //totalList排序
        Collections.sort(mPatientList, new Comparator<Patient>() {
            @Override
            public int compare(Patient lhs, Patient rhs) {
                return lhs.getPinyin().toLowerCase().compareTo(rhs.getPinyin().toLowerCase());
            }
        });

    }


    private void initTitle() {
        hideCommonBaseTitle();
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData(final int state) {
//        UserBean user = UserManager.getInstance().getCurrentLoginUser();
//        if(null != user){
//            mDoctorId = Integer.parseInt(user.getLoginServerBean().getUser_id());
        //这里直接使用医脉的获取医生的id的方法，
        // 所以，不需要使用药品助手中的UserManager.getInstance().getCurrentLoginUser()
        //所以这里就不需要做user为null的判断
        mDoctorId = Integer.parseInt(YMUserService.getCurUserId());

        PatientListRequest.getInstance().getPatientList(mDoctorId).subscribe(new BaseRetrofitResponse<BaseData<List<Patient>>>() {
            @Override
            public void onStart() {
                super.onStart();
                mNetworkError.setVisibility(View.GONE);
                if (state == State.INIT.getFlag()) {
                    PatientListActivity.this.showProgressDialog("");
                } else {
                    PatientListActivity.this.hideProgressDialog();
                }
            }

            @Override
            public void onCompleted() {
                PatientListActivity.this.hideProgressDialog();
                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hideProgressDialog();
                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    mNetworkError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNext(BaseData<List<Patient>> entry) {
                super.onNext(entry);
                if (entry != null) {
                    if (entry.getCode() == 10000) {
                        if (entry.getData() != null) {
                            dealwithEntry(entry.getData());
                        } else {
                            LogUtils.i("entry.getData()== null");
                        }
                    } else {
                        LogUtils.i("数据返回有问题 code=" + entry.getCode());
                    }
                } else {
                    LogUtils.i("数据返回有问题 entry=" + entry);
                }
            }
        });
//        }

    }

    private void dealwithEntry(List<Patient> data) {
        handleData(data);
        mNewPaientAdapter.setData(mPatientList);
        if (mPatientList.size() == 0) {
            mIndexBar.setVisibility(View.GONE);
            emptyWrapper.setEmptyView(R.layout.item_patient_list_empty);
        } else {
            mIndexBar.setVisibility(View.VISIBLE);
        }
        emptyWrapper.notifyDataSetChanged();
        mIndexBar.setmSourceDatas(mPatientList)//设置数据
                .invalidate();
        mDecoration.setmDatas(mPatientList);

    }


    private enum State {
        INIT(0), ONREFRESH(1);
        private int flag;

        private State(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return this.flag;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyRxBus.unRegisterPatientInfoChanged(this);
    }
}
