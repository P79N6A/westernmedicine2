package com.xywy.component.uimodules.photoPicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.component.R;
import com.xywy.component.uimodules.photoPicker.adapter.PreDeleteAdapter;
import com.xywy.component.uimodules.photoPicker.model.PhotoInfo;
import com.xywy.component.uimodules.photoPicker.view.PPViewPager;

import java.util.ArrayList;

public class PreDeletePhotoActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ArrayList<PhotoInfo> mSelectPhotoList = new ArrayList<PhotoInfo>();//新的被选择图片集合

    public static final String SELECT_PHOTO_LIST = "select_photo_list";

    public static final String POSITION = "position";

    private int mPosition;

    private PreDeleteAdapter mPhotoPreviewAdapter;


    private TextView mTvTitle;
    private ImageView mDelete;
    private PPViewPager mVpPager;
    private ImageView mBack;

    public static void startActivity(Context context, Fragment fragment, ArrayList<PhotoInfo> selectPhotoList, int position, int requestCode) {

        Intent intent = new Intent(context, PreDeletePhotoActivity.class);
        intent.putExtra(SELECT_PHOTO_LIST, selectPhotoList);
        intent.putExtra(POSITION, position);
        if (fragment == null) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_delete_photo);
        parseIntent();
        initView();

        mPhotoPreviewAdapter = new PreDeleteAdapter(this, mSelectPhotoList);
        mVpPager.setAdapter(mPhotoPreviewAdapter);
        mVpPager.setCurrentItem(mPosition);

    }


    private void parseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mSelectPhotoList.addAll((ArrayList<PhotoInfo>) intent.getSerializableExtra(SELECT_PHOTO_LIST));//创建一个新的被选中图片集合
            mPosition = intent.getIntExtra(POSITION, 0);
        }
    }

    private void initView() {

        mBack = (ImageView) findViewById(R.id.pp_iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mDelete = (ImageView) findViewById(R.id.pp_iv_delete);
        mVpPager = (PPViewPager) findViewById(R.id.vp_pager);

        mDelete.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mVpPager.addOnPageChangeListener(this);
        refreshTitle();

    }

    private void refreshTitle() {
        mTvTitle.setText((mPosition + 1) + "/" + mSelectPhotoList.size());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pp_iv_back) {
            returnData();
        } else if (id == R.id.pp_iv_delete) {
            if (mSelectPhotoList.size() == 1) {
                returnData();
            } else {
                mSelectPhotoList.remove(mPosition);
                mPhotoPreviewAdapter.setData(mSelectPhotoList);
                refreshTitle();
            }
        }
    }

    @Override
    public void onBackPressed() {

        returnData();
    }

    /**
     * 回传数据
     */
    private void returnData() {
        Intent intent = getIntent();
        intent.putExtra(PhotoSelectActivity.LIST, mSelectPhotoList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        refreshTitle();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
