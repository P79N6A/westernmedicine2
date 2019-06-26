package com.xywy.askforexpert.module.drug;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.module.drug.bean.DoctorPrice;
import com.xywy.askforexpert.module.drug.request.DrugAboutRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import butterknife.Bind;

/**
 * 在线诊室费用管理 stone
 */
public class OnlineRoomFeeEditActivity extends YMBaseActivity {
    @Bind(R.id.et1)
    EditText et1;
    private String doctorId = YMUserService.getCurUserId();
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_onlineroom_fee;
    }

    @Override
    protected void initData() {
        DrugAboutRequest.getInstance().getPrice(doctorId).subscribe(new BaseRetrofitResponse<BaseData<DoctorPrice>>() {
            @Override
            public void onNext(BaseData<DoctorPrice> baseData) {
                String ask_hlwyy_amount = baseData.getData().getDoctor().getAsk_hlwyy_amount();
                if(!TextUtils.isEmpty(ask_hlwyy_amount)&&Double.parseDouble(ask_hlwyy_amount)>0){
                    et1.setText(ask_hlwyy_amount);
                }
            }
            @Override
            public void onError(Throwable e) {
                ToastUtils.longToast(e.toString());
            }
        });
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("医事服务费").addItem("保存", new ItemClickListener() {
            @Override
            public void onClick() {
                final String fee = et1.getText().toString().trim();
                if (TextUtils.isEmpty(fee)) {
                    shortToast("请设置服务费");
                    return;
                }else{
                    double price = Double.valueOf(fee);
                    if (price>=1) {
                        DrugAboutRequest.getInstance().setPrice(doctorId, fee).subscribe(new BaseRetrofitResponse<BaseData>() {
                            @Override
                            public void onNext(BaseData baseData) {
                                ToastUtils.longToast("设置成功");
                                Intent intent = new Intent();
                                intent.putExtra(Constants.KEY_VALUE, fee);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.longToast(e.toString());
                            }
                        });
                    }else{
                        shortToast("设置价格必须大于1元");
                    }

                }
            }
        }).build();

    }


}
