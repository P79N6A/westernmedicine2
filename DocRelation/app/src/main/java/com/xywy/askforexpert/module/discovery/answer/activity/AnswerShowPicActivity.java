package com.xywy.askforexpert.module.discovery.answer.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.module.discovery.answer.adapter.ShowPicturePagerAdapter;
import com.xywy.askforexpert.module.discovery.answer.constants.AnswerIntentKey;
import com.xywy.askforexpert.widget.view.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 通用 图片展示Activity
 * Created by bailiangjin on 16/6/22.
 */
public class AnswerShowPicActivity extends YMBaseActivity {


    private static final String CUR_POSITION_KEY = "CUR_POSITION_KEY";


    @Bind(R.id.vp_picture)
    HackyViewPager viewPager;

    @Bind(R.id.tv_index)
    TextView tv_index;

    private ShowPicturePagerAdapter showPicturePagerAdapter;


    private List<String> picUrlList;
    private int startPosition;


    public static void start(Context context, List<String> picUrlList) {
        Intent intent = new Intent(context, AnswerShowPicActivity.class);
        intent.putStringArrayListExtra(AnswerIntentKey.PICTURE_INTENT_KEY, (ArrayList<String>) picUrlList);
        context.startActivity(intent);
    }

    /**
     * 展示图片
     *
     * @param context
     * @param picUrlList
     * @param startPosition 初始位置
     */
    public static void start(Context context, List<String> picUrlList, int startPosition) {
        Intent intent = new Intent(context, AnswerShowPicActivity.class);
        intent.putStringArrayListExtra(AnswerIntentKey.PICTURE_INTENT_KEY, (ArrayList<String>) picUrlList);
        intent.putExtra(CUR_POSITION_KEY, startPosition);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_answer_show_picture;
    }

    @Override
    protected void beforeViewBind() {
        picUrlList = getIntent().getStringArrayListExtra(AnswerIntentKey.PICTURE_INTENT_KEY);
        startPosition = getIntent().getIntExtra(CUR_POSITION_KEY, -1);
    }

    @Override
    protected void initView() {
        //hideCommonBaseTitle();
        titleBarBuilder.setBackGround(R.color.black);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * viewpager position 改变回调
             *
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                //更新页面相关数据
                refreshByPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void refreshByPosition(int position) {
        if (null != picUrlList && !picUrlList.isEmpty()) {
            titleBarBuilder.setTitleText((position + 1) + "/" + picUrlList.size());
            //tv_index.setText((position + 1) + "/" + picUrlList.size());
        }
    }

    @Override
    protected void initData() {

        if (null == picUrlList || picUrlList.isEmpty()) {
            shortToast("图片数据为空");
            finish();
            return;
        }
        showPicturePagerAdapter = new ShowPicturePagerAdapter(AnswerShowPicActivity.this, picUrlList);

        viewPager.setAdapter(showPicturePagerAdapter);


        if (-1 != startPosition) {
            viewPager.setCurrentItem(startPosition);
            refreshByPosition(startPosition);
        } else {
            refreshByPosition(0);
        }

    }


    @Override
    protected boolean isHideBar() {
        return true;
    }




}
