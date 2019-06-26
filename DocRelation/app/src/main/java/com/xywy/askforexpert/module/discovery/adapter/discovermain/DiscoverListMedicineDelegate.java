package com.xywy.askforexpert.module.discovery.adapter.discovermain;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.UserType;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.medicine.SyncInfoRequest;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverItemBean;
import com.xywy.askforexpert.module.discovery.medicine.MedicineAssistantActivity;
import com.xywy.askforexpert.module.discovery.medicine.SellDrugRequest;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.askforexpert.widget.module.discover.DiscoverServiceItemView;
import com.xywy.base.view.ProgressDialog;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.LogUtils;
import com.xywy.util.T;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by xgxg on 2017/10/18.
 */

public class DiscoverListMedicineDelegate implements ItemViewDelegate<DiscoverItemBean> {
    Context context;
    private ProgressDialog progressDialog;
    public DiscoverListMedicineDelegate(Context context){
        this.context = context;
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_discovery_service_medicine;
    }

    @Override
    public boolean isForViewType(DiscoverItemBean item, int position) {
//        return false;
//        LogUtils.i("item.getType()="+item.getType());
        return item.getType()==DiscoverItemBean.TYPE_MEDICINE;
    }

    @Override
    public void convert(ViewHolder holder, DiscoverItemBean discoverItemBean, int position) {
        final DiscoverServiceItemView discoverServiceItemView = holder.getView(R.id.dsiv_item);
        //提醒数
        discoverServiceItemView.setName("药品助手");
        discoverServiceItemView.setDescTvContent("患者管理，药品推荐");
        discoverServiceItemView.setNumberTvVisibility(false);
        discoverServiceItemView.setNoticeDotVisibility(false);
//        discoverServiceItemView.setNumberText(9);
        discoverServiceItemView.setOpenTvVisibility(false);
        discoverServiceItemView.setIconRes(R.drawable.ypzs);
        discoverServiceItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StatisticalTools.eventCount(context, "medicalassistant");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(context).context);
                }else {
                    if (YMUserService.checkUserId(context, UserType.ApprovedDoctor)) {
                        if(YMApplication.getInstance().getHasSyncInfo()){
                            //如果已经同步过医生信息
                            if(YMApplication.getInstance().getSyncInfoResult()){
                                //医生信息同步成功
                                if(YMApplication.getInstance().getHasSellDrug()){
                                    //已经检查过医生是否具有售药的权限
                                    if(YMApplication.getInstance().getSellDrugResult()){
                                        //当前医生具有售药的权限
                                        LogUtils.i("跳转到用药助手");
                                        context.startActivity(new Intent(context, MedicineAssistantActivity.class));
                                    }else{
                                        ToastUtils.shortToast(YMApplication.getInstance().getSellDrugMsg());
                                    }
                                }else {
                                    //还未检查医生是否具有售药的权限
                                    sellDrug();
                                }
                            }else {
                                //医生信息同步失败
                                ToastUtils.shortToast(YMApplication.getInstance().getSyncInfoMsg());
                            }
                        }else{
                            //没有同步过医生信息,则同步医生信息
                            syncInfo();
                        }
                    }
                }
            }
        });
    }

    private void sellDrug() {
        SellDrugRequest.getInstance().getSellDrug(Integer.parseInt(YMUserService.getCurUserId())).subscribe(new BaseRetrofitResponse<BaseData>(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                hideProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    //还未调用检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(false);
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    //已经调用了检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(true);
                    //检测是否具有售药权限的接口返回的结果是：没有售药权限
                    YMApplication.getInstance().setSellDrugResult(false,e.getMessage());
                    T.showShort(RetrofitClient.getContext(),e.getMessage());
                }

            }

            @Override
            public void onNext(BaseData entry) {
                super.onNext(entry);
                if(entry!=null) {
                    //已经调用了检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(true);
                    //检测是否具有售药权限的接口返回的结果是：具有售药权限
                    YMApplication.getInstance().setSellDrugResult(true,"");
                    LogUtils.i("跳转到用药助手");
                    context.startActivity(new Intent(context, MedicineAssistantActivity.class));
                }
            }
        });
    }

    /**
     * 显示进度dialog
     *
     * @param content 提示文字内容
     */
    public void showProgressDialog(String content) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(context, content);
            progressDialog.setCanceledOnTouchOutside(false);

        } else {
            progressDialog.setTitle(content);
        }
        progressDialog.showProgersssDialog();
    }

    /**
     * 隐藏进度dialog
     */
    public void hideProgressDialog() {
        if (null == progressDialog) {
            return;
        }
        if (progressDialog.isShowing()) {
            progressDialog.closeProgersssDialog();
        }
    }

    /**
     * 同步医生信息
     */
    public void syncInfo() {
        long doctroId = Long.parseLong(YMUserService.getCurUserId());
        SyncInfoRequest.getInstance().syncInfo(doctroId).subscribe(new BaseRetrofitResponse<BaseData>(){
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    //未同步医生信息接口
                    YMApplication.getInstance().setHasSyncInfo(false);
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    //已经同步过医生信息接口
                    YMApplication.getInstance().setHasSyncInfo(true);
                    //同步过医生信息接口的返回结果是：同步医生信息失败
                    YMApplication.getInstance().setSyncInfoResult(false,e.getMessage());
                    T.showShort(RetrofitClient.getContext(),e.getMessage());
                }

            }

            @Override
            public void onNext(BaseData entry) {
                super.onNext(entry);
                if(entry!=null) {
                    dealwithEntry(entry);
                }
            }
        });
    }

    private void dealwithEntry(BaseData entry) {
        if(null != entry && entry.getCode()==10000){
            //已经同步过医生信息接口
            YMApplication.getInstance().setHasSyncInfo(true);
            //同步过医生信息接口的返回结果是：同步医生信息成功
            YMApplication.getInstance().setSyncInfoResult(true,"");
            sellDrug();
        }
    }
}
