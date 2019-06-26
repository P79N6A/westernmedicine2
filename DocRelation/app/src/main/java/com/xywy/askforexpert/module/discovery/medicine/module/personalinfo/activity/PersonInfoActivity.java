package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.common.MyBaseActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.account.UserManager;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserBean;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.model.PersonInfoModel;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.util.ImageLoaderUtils;

import butterknife.Bind;
import butterknife.OnClick;


public class PersonInfoActivity extends MyBaseActivity {
    @Bind(R.id.m_iv_headimgs)
    ImageView mIvHeadimgs;
    @Bind(R.id.m_rl_headimg)
    RelativeLayout mRlHeadimg;
    @Bind(R.id.m_tv_name)
    TextView mTvName;
    @Bind(R.id.m_rl_name)
    RelativeLayout mRlName;
    @Bind(R.id.m_tv_jobname)
    TextView mTvJobname;
    @Bind(R.id.m_rl_jobname)
    RelativeLayout mRlJobname;
    @Bind(R.id.m_tv_hospital)
    TextView mTvHospital;
    @Bind(R.id.m_rl_hospital)
    RelativeLayout mRlHospital;
    @Bind(R.id.m_tv_depart)
    TextView mTvDepart;
    @Bind(R.id.m_rl_dep)
    RelativeLayout mRlDep;
    @Bind(R.id.m_tv_sex)
    TextView mTvSex;
    @Bind(R.id.m_rl_sex)
    RelativeLayout mRlSex;
    @Bind(R.id.m_tv_good)
    TextView mTvGood;
    @Bind(R.id.m_rl_good)
    RelativeLayout mRlGood;
    @Bind(R.id.m_tv_verify)
    TextView mTvVerify;
    @Bind(R.id.m_rl_verify)
    RelativeLayout mRlVerify;
    @Bind(R.id.m_tv_intro)
    TextView mTvIntro;
    @Bind(R.id.m_rl_intro)
    RelativeLayout mRlIntro;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    //一群箭头
    @Bind(R.id.enter_right)
    ImageView ivEnterRightHead;
    @Bind(R.id.enter_right_1)
    ImageView ivEnterRightName;
    @Bind(R.id.enter_right7)
    ImageView ivEnterRightSex;
    @Bind(R.id.enter_right4)
    ImageView ivEnterRightHospital;
    @Bind(R.id.enter_right5)
    ImageView ivEnterRightDepartment;
    @Bind(R.id.enter_right3)
    ImageView ivEnterJobName;
    @Bind(R.id.enter_right12)
    ImageView ivEnterRightUploadImg;
    @Bind(R.id.enter_right10)
    ImageView ivEnterRightGood;
    @Bind(R.id.enter_right11)
    ImageView ivEnterRightIntro;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_personal_info;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, PersonInfoActivity.class);
        context.startActivity(intent);
    }

    public void initView() {
        titleBarBuilder.setTitleText("个人信息");
        if (UserManager.getInstance().isPerInfoUpdateable(this, false)) {
            mRlVerify.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
            showEnterLeft(true);
        } else {
            mRlVerify.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            showEnterLeft(false);
        }
    }

    UserBean user = UserManager.getInstance().getCurrentLoginUser();

    protected void initData() {
        mTvDepart.setText(user.getMajorSecond().getName());
        mTvGood.setText(user.getBe_good_at());
        mTvHospital.setText(user.getHosp().getName());
        mTvJobname.setText(user.getJobTitle().getName());
        mTvName.setText(user.getLoginServerBean().getReal_name());
        mTvSex.setText(user.getGender().getName());
        mTvIntro.setText(user.getLoginServerBean().getIntroduce());
        ImageLoaderUtils.getInstance().displayImage(user.getPhoto(), mIvHeadimgs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
    }

    private void showEnterLeft(boolean isShow) {
        if (isShow) {
            ivEnterRightHead.setVisibility(View.VISIBLE);
            ivEnterRightName.setVisibility(View.VISIBLE);
            ivEnterRightSex.setVisibility(View.VISIBLE);
            ivEnterRightHospital.setVisibility(View.VISIBLE);
            ivEnterRightDepartment.setVisibility(View.VISIBLE);
            ivEnterJobName.setVisibility(View.VISIBLE);
            ivEnterRightUploadImg.setVisibility(View.VISIBLE);
            ivEnterRightGood.setVisibility(View.VISIBLE);
            ivEnterRightIntro.setVisibility(View.VISIBLE);
        } else {
            ivEnterRightHead.setVisibility(View.INVISIBLE);
            ivEnterRightName.setVisibility(View.INVISIBLE);
            ivEnterRightSex.setVisibility(View.INVISIBLE);
            ivEnterRightHospital.setVisibility(View.INVISIBLE);
            ivEnterRightDepartment.setVisibility(View.INVISIBLE);
            ivEnterJobName.setVisibility(View.INVISIBLE);
            ivEnterRightUploadImg.setVisibility(View.INVISIBLE);
            ivEnterRightGood.setVisibility(View.INVISIBLE);
            ivEnterRightIntro.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            model.updateHead(requestCode, resultCode, data, new Resp());
        }
    }

    PersonInfoModel model = new PersonInfoModel();

    @OnClick({R.id.btn_submit, R.id.m_rl_headimg, R.id.m_rl_name, R.id.m_rl_jobname, R.id.m_rl_hospital, R.id.m_rl_dep, R.id.m_rl_sex, R.id.m_rl_good, R.id.m_rl_intro, R.id.m_rl_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                model.commitToServer(this, new Resp(){
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        finish();
                    }
                });
                break;
            case R.id.m_rl_headimg:
                model.selectHead(this);
                break;
            case R.id.m_rl_name:
                PersonInfoModel.editName(this);
                break;
            case R.id.m_rl_jobname:
                PersonInfoModel.editJobTitle(this, new Resp());
                break;
            case R.id.m_rl_hospital:
                PersonInfoModel.editHosp(this, new Resp());
                break;
            case R.id.m_rl_dep:
                PersonInfoModel.editDep(this, new Resp());
                break;
            case R.id.m_rl_sex:
                PersonInfoModel.editSex(this, new Resp());
                break;
            case R.id.m_rl_good:
                PersonInfoModel.editGood(this);
                break;
            case R.id.m_rl_verify:
                PersonInfoModel.editIdentifyProve(this);
                break;
            case R.id.m_rl_intro:
                PersonInfoModel.editIntro(this);
                break;
        }
    }

    class Resp extends BaseRetrofitResponse {
        @Override
        public void onStart() {
            super.onStart();
            showProgressDialog("");
        }

        @Override
        public void onNext(Object o) {
            initData();
            hideProgressDialog();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            hideProgressDialog();
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
            hideProgressDialog();
        }
    }
}
