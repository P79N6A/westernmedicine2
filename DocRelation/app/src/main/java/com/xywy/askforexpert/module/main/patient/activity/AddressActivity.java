package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.AddressBean;
import com.xywy.askforexpert.model.AddressData;
import com.xywy.askforexpert.module.consult.ServiceProvider;

import butterknife.Bind;
import rx.Subscriber;

/**
 * Created by jason on 2018/11/15.
 */

public class AddressActivity extends YMBaseActivity{
    @Bind(R.id.adds_ll)
    LinearLayout adds_ll;
    @Bind(R.id.adds_tv)
    TextView adds_tv;
    @Bind(R.id.edit_ll)
    LinearLayout edit_ll;
    @Bind(R.id.submit_btn)
    TextView submit_btn;
    @Bind(R.id.edit_adds_ll)
    LinearLayout edit_adds_ll;
    @Bind(R.id.add_adds_ll)
    LinearLayout add_adds_ll;
    private String doctorId = YMUserService.getCurUserId();
    private AddressData data;
    private String id;
    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("物料申请");
        add_adds_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this,AddAddressActivity.class));
            }
        });
        edit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this,AddAddressActivity.class).putExtra("addressData",data));
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(id)){
                    ServiceProvider.apply(doctorId, id, new Subscriber<AddressBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(AddressBean addressBean) {
                            if (addressBean.getCode()==10000){
                                startActivity(new Intent(AddressActivity.this,ApplySuccessActivity.class));
                                finish();
                            }else{
                                ToastUtils.longToast(addressBean.getMsg());
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void loadData(){
        ServiceProvider.getAddress(doctorId, new Subscriber<AddressBean>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(AddressBean addressBean) {
                if (addressBean.getCode()==10000){
                    data = addressBean.getData();
                    edit_adds_ll.setVisibility(View.VISIBLE);
                    add_adds_ll.setVisibility(View.GONE);
                    adds_tv.setVisibility(View.VISIBLE);
                    adds_tv.setText(addressBean.getData().getProvince()
                            +addressBean.getData().getCity()
                            +addressBean.getData().getCounty()
                            +addressBean.getData().getAddress());
                    submit_btn.setBackgroundResource(R.drawable.patient_detail_im_button_bg);
                    submit_btn.setEnabled(true);
                    id = addressBean.getData().getId();
                }else{
                    edit_adds_ll.setVisibility(View.GONE);
                    add_adds_ll.setVisibility(View.VISIBLE);
                    adds_tv.setVisibility(View.GONE);
                    submit_btn.setBackgroundResource(R.drawable.not_address_shape);
                    submit_btn.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.address_layout;
    }
}
