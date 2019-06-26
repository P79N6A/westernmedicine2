package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.common.MyBaseActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.account.UserManager;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.adapter.PersonCertUploadAdapter;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.CertUploadEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.model.DialogModel;
import com.xywy.askforexpert.module.discovery.medicine.view.SelectPicPopupWindow;
import com.xywy.askforexpert.widget.view.HorizontalListView;
import com.xywy.util.CamaraUtil;
import com.xywy.util.ContextUtil;
import com.xywy.util.T;

import java.util.ArrayList;
import java.util.List;

/**
 * 医生身份证上传，证件上传 认证页面
 *
 * @author 王鹏
 * @2015-4-23下午7:43:58
 */
public class IDProveActivity extends MyBaseActivity {
    //    private MainGVAdapter adapter;
//    private MainGVAdapter adapter2;
    private PersonCertUploadAdapter certAdapter; //身份证
    private PersonCertUploadAdapter cardAdapter; //资格证
    //    private List<String> jobImageFiles = new ArrayList<String>();
//    private List<String> idcardImageFiles = new ArrayList<String>();
    private List<String> jobImageUrls = new ArrayList<String>();
    private List<String> idcardImageUrls = new ArrayList<String>();
    private LinearLayout idcard_main;
    private SelectPicPopupWindow menuWindow;
    private HorizontalListView idcard;
    private HorizontalListView mainGV;
    private String path_id = "";

    private CertUploadEntity selectEntity;

    public static void start(Context context) {
        Intent intent = new Intent(context,
                IDProveActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_idcardupload;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("证书上传");
        mainGV = (HorizontalListView) findViewById(R.id.jobcard);
        idcard_main = (LinearLayout) findViewById(R.id.idcard_main);
        idcard = (HorizontalListView) findViewById(R.id.idcard);
//        adapter = new MainGVAdapter(this, 5);
//        adapter.setData(jobImageUrls);
//        adapter2 = new MainGVAdapter(this, 2);
//        adapter2.setData(idcardImageUrls);
        certAdapter = new PersonCertUploadAdapter(this);
        cardAdapter = new PersonCertUploadAdapter(this);
//        idcard.setAdapter(adapter2);
//        mainGV.setAdapter(adapter);
        idcard.setAdapter(cardAdapter);
        mainGV.setAdapter(certAdapter);
    }

    private void initPicData() {
        String certUrl1 = "";
        String certUrl2 = "";
        String cardUrl = "";
        String cardUrlBack = "";
        if (UserManager.getInstance().getCurrentLoginUser().getIdImages() != null &&
                !UserManager.getInstance().getCurrentLoginUser().getIdImages().isEmpty()) {
            cardUrl = UserManager.getInstance().getCurrentLoginUser().getIdImages().get(0);
            if (UserManager.getInstance().getCurrentLoginUser().getIdImages().size() > 1) {
                cardUrlBack = UserManager.getInstance().getCurrentLoginUser().getIdImages().get(1);
            }
        }
        if (UserManager.getInstance().getCurrentLoginUser().getJobImages() != null &&
                !UserManager.getInstance().getCurrentLoginUser().getJobImages().isEmpty()) {
            certUrl1 = UserManager.getInstance().getCurrentLoginUser().getJobImages().get(0);
            if (UserManager.getInstance().getCurrentLoginUser().getJobImages().size() > 1) {
                certUrl2 = UserManager.getInstance().getCurrentLoginUser().getJobImages().get(1);
            }
        }
        List<CertUploadEntity> certList = new ArrayList<>();
        certList.add(new CertUploadEntity(certUrl1, ""));
        certList.add(new CertUploadEntity(certUrl2, ""));
        certAdapter.setData(certList);

        List<CertUploadEntity> cardList = new ArrayList<>();
        cardList.add(new CertUploadEntity(cardUrl, "正面"));
        cardList.add(new CertUploadEntity(cardUrlBack, "反面"));
        cardAdapter.setData(cardList);

        certAdapter.notifyDataSetChanged();
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        initPicData();
        mainGV.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectEntity = certAdapter.getData().get(position);
                if (view.getId() == R.id.iv_delete) {
                    selectEntity.setImgUrl("");
                    certAdapter.notifyDataSetChanged();
                    updatePaths();
                } else {
                    menuWindow = new SelectPicPopupWindow(IDProveActivity.this);
                    // 显示窗口
                    menuWindow.showAtLocation(idcard_main, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });
        idcard.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectEntity = cardAdapter.getData().get(position);
                if (view.getId() == R.id.iv_delete) {
                    selectEntity.setImgUrl("");
                    cardAdapter.notifyDataSetChanged();
                    updatePaths();
                } else {
                    menuWindow = new SelectPicPopupWindow(IDProveActivity.this);
                    // 显示窗口
                    menuWindow.showAtLocation(idcard_main, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });

    }

    private void updatePaths() {
        jobImageUrls.clear();
        idcardImageUrls.clear();

        for (int i = 0; i < certAdapter.getData().size(); i++) {
            String imgUrl = certAdapter.getData().get(i).getImgUrl();
            if (imgUrl != null && !"".equals(imgUrl)) {
                jobImageUrls.add(imgUrl);
            }
        }

        for (int i = 0; i < cardAdapter.getData().size(); i++) {
            String imgUrl = cardAdapter.getData().get(i).getImgUrl();
            if (imgUrl != null && !"".equals(imgUrl)) {
                idcardImageUrls.add(imgUrl);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path = CamaraUtil.handleResult(this, menuWindow.getImageUri(), requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && path != null) {
//            if (path_id.equals("job")) {
//                jobImageUrls.add(path);
//            } else if (path_id.equals("idcard")) {
//                idcardImageUrls.add(path);
//            }
//        }
        if (resultCode == RESULT_OK && path != null) {
            if (selectEntity != null) {
                selectEntity.setImgUrl(path);
                updatePaths();
                certAdapter.notifyDataSetChanged();
                cardAdapter.notifyDataSetChanged();

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        adapter2.notifyDataSetChanged();
//        adapter.notifyDataSetChanged();
        certAdapter.notifyDataSetChanged();
        cardAdapter.notifyDataSetChanged();
    }

    int toUploadCount = -1;

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                updatePaths();
                if (checkValidate()) {
//                    UserManager.getInstance().getCurrentLoginUser().getJobImages().clear();
//                    UserManager.getInstance().getCurrentLoginUser().getIdImages().clear();
                    showProgressDialog("图片上传中");
                    // toUploadCount = idcardImageUrls.size() + jobImageUrls.size();
                    toUploadCount = getUploadCount();
                    uploadImgs();
//                    for (String jobId : jobImageUrls) {
//                        PersonCardRequest.getInstance().uploadImage(new File(jobId)).subscribe(new BaseRetrofitResponse<BaseData<ImageBean>>() {
//                            @Override
//                            public void onNext(BaseData<ImageBean> baseData) {
//                                super.onNext(baseData);
//                                UserManager.getInstance().addJobImageUrl(baseData.getData().getUrl());
//                                onSuccess();
//                            }
//                        });
//                    }
//                    for (String id : idcardImageUrls) {
//                        PersonCardRequest.getInstance().uploadImage(new File(id)).subscribe(new BaseRetrofitResponse<BaseData<ImageBean>>() {
//                            @Override
//                            public void onNext(BaseData<ImageBean> baseData) {
//                                super.onNext(baseData);
//                                UserManager.getInstance().addIdImageUrl(baseData.getData().getUrl());
//                                onSuccess();
//                            }
//                        });
//                    }

                }
                break;

            case R.id.img_idcard:
                DialogModel.picDetail(IDProveActivity.this, R.drawable.idcard_big);
                break;
            case R.id.img_job:
                DialogModel.picDetail(IDProveActivity.this, R.drawable.job_big);
                break;
            default:
                break;
        }
    }

    private void onSuccess() {
        toUploadCount--;
        if (toUploadCount == 0) {
            hideProgressDialog();
            Intent intent = new Intent(IDProveActivity.this, PersonInfoActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private int getUploadCount() {
        int count = 0;
        for (int i = 0; i < idcardImageUrls.size(); i++) {
            if (idcardImageUrls.get(i).startsWith("http")) {

            } else {
                count++;
            }
        }
        for (int i = 0; i < jobImageUrls.size(); i++) {
            if (jobImageUrls.get(i).startsWith("http")) {

            } else {
                count++;
            }
        }
        return count;
    }

    private boolean checkValidate() {
        if (jobImageUrls.size() > 0
                && idcardImageUrls.size() == 2) {
            return true;
        }
        T.showLong(ContextUtil.getAppContext(), "图片不能为空");
        return false;
    }

    private void uploadImgs() {
        if (UserManager.getInstance().getJobImageUrl().size() < 2) {
            UserManager.getInstance().getJobImageUrl().add("");
            UserManager.getInstance().getJobImageUrl().add("");
        }
        if (UserManager.getInstance().getIdImageUrl().size() < 2) {
            UserManager.getInstance().getIdImageUrl().add("");
            UserManager.getInstance().getIdImageUrl().add("");
        }
        for (int i = 0; i < certAdapter.getData().size(); i++) {
            String imgUrl = certAdapter.getData().get(i).getImgUrl();
            if (imgUrl == null || imgUrl.equals("") || imgUrl.startsWith("http")) {

            } else {
                //暂时注销
//                final int index = i;
//                PersonCardRequest.getInstance().uploadImage(new File(imgUrl)).subscribe(new BaseRetrofitResponse<BaseData<ImageBean>>() {
//                    @Override
//                    public void onNext(BaseData<ImageBean> baseData) {
//                        super.onNext(baseData);
//                        UserManager.getInstance().getJobImageUrl().set(index, baseData.getData().getUrl());
//                        onSuccess();
//                    }
//                });
            }
        }
        for (int i = 0; i < cardAdapter.getData().size(); i++) {
            String imgUrl = cardAdapter.getData().get(i).getImgUrl();
            if (imgUrl == null || imgUrl.equals("") || imgUrl.startsWith("http")) {

            } else {
                //暂时注销
//                final int index = i;
//                PersonCardRequest.getInstance().uploadImage(new File(imgUrl)).subscribe(new BaseRetrofitResponse<BaseData<ImageBean>>() {
//                    @Override
//                    public void onNext(BaseData<ImageBean> baseData) {
//                        super.onNext(baseData);
//                        UserManager.getInstance().getIdImageUrl().set(index, baseData.getData().getUrl());
//                        onSuccess();
//                    }
//                });
            }
        }
    }


}