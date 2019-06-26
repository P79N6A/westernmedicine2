package com.xywy.askforexpert.module.drug;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.module.drug.bean.DoctorPrice;
import com.xywy.askforexpert.module.drug.request.DrugAboutRequest;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 在线诊室管理 stone
 */
public class OnlineRoomSettingActivity extends YMBaseActivity {

    @Bind(R.id.lay_common)
    View lay_common;

    @Bind(R.id.lay_fee)
    View lay_fee;

    @Bind(R.id.tv_fee)
    TextView tv_fee;
    private String mDoctorId = YMUserService.getCurUserId();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_onlineroom_setting;
    }

    @OnClick({R.id.lay_common, R.id.lay_fee})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_common:
                Intent intent = new Intent(OnlineRoomSettingActivity.this, MyPharmacyActivity.class);
                intent.putExtra(Constants.KEY_VALUE, true);
                startActivity(intent);
                break;

            case R.id.lay_fee:
                startActivityForResult(new Intent(OnlineRoomSettingActivity.this, OnlineRoomFeeEditActivity.class), 1);
                break;
        }
    }


    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("问诊用药管理");
    }

    @Override
    protected void initData() {
        DrugAboutRequest.getInstance().getPrice(mDoctorId).subscribe(new BaseRetrofitResponse<BaseData<DoctorPrice>>() {
            @Override
            public void onNext(BaseData<DoctorPrice> baseData) {
                String ask_hlwyy_amount = baseData.getData().getDoctor().getAsk_hlwyy_amount();
                tv_fee.setText(ask_hlwyy_amount);
            }
            @Override
            public void onError(Throwable e) {
                ToastUtils.longToast(e.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bundle bundle = data.getExtras();
                String fee = bundle.getString(Constants.KEY_VALUE);
                tv_fee.setText(fee);
            }
        }
    }
}
