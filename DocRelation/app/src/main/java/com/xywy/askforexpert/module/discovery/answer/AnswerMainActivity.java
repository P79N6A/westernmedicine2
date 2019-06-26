package com.xywy.askforexpert.module.discovery.answer;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.uilibrary.titlebar.ItemClickListener;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.answer.api.set.SetPageBean;
import com.xywy.askforexpert.model.answer.show.TestSet;
import com.xywy.askforexpert.module.discovery.answer.activity.AnswerMultiLevelListActivity;
import com.xywy.askforexpert.module.discovery.answer.adapter.TestSetGridAdapter;
import com.xywy.askforexpert.module.discovery.answer.constants.AnswerShareConstants;
import com.xywy.askforexpert.module.discovery.answer.service.ConvertUtils;
import com.xywy.askforexpert.module.message.share.ShareUtil;

import java.util.List;

import butterknife.Bind;

/**
 * 医学试题 stone
 * Created by bailiangjin on 16/4/20. done
 */
public class AnswerMainActivity extends YMBaseActivity {

    @Bind(R.id.gv_paper_list)
    GridView gv_paper_list;
    private TestSetGridAdapter paperGridAdapter;

    private SetPageBean setPageBean;
    private List<TestSet> testSetList;

    public static void start(Activity context) {
        context.startActivity(new Intent(context, AnswerMainActivity.class));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_answer_main;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("医学试题");
        titleBarBuilder.addShareMenuItem(new ItemClickListener() {
            @Override
            public void onClick() {
                StatisticalTools.eventCount(AnswerMainActivity.this, "TestQuestionsShare");
                if (null == setPageBean) {
                    return;
                }

                LogUtils.d("share:data:title:" + setPageBean.getShare_text());
                LogUtils.d("share:data:url:" + setPageBean.getShare_url());
                LogUtils.d("share:data:img:" + setPageBean.getShare_img());

                new ShareUtil.Builder().setTitle(AnswerShareConstants.shareTitle)
                        .setText(setPageBean.getShare_text())
                        .setTargetUrl(setPageBean.getShare_url())
                        .setImageUrl(setPageBean.getShare_img())
                        .setShareId(AnswerShareConstants.shareId)
                        .setShareSource(AnswerShareConstants.shareSource)
                        .setAnswerData("", "", AnswerShareConstants.shareSourceType_Detail, "").build(AnswerMainActivity.this).innerShare();

                return;
            }
        }).build();

        gv_paper_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StatisticalTools.eventCount(AnswerMainActivity.this, "VariousTypesOfTestQuestions" + (position + 1));
                AnswerMultiLevelListActivity.start(AnswerMainActivity.this, testSetList.get(position));

            }
        });

    }

    @Override
    protected void initData() {

        requestDataFromServer();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用刷新 GridView数据
     */
    private void initOrUpdateAdapter(List<TestSet> list) {
        if (null == paperGridAdapter) {
            paperGridAdapter = new TestSetGridAdapter(AnswerMainActivity.this, list);
            gv_paper_list.setAdapter(paperGridAdapter);
        } else {
            paperGridAdapter.setData(list);
            paperGridAdapter.notifyDataSetChanged();
        }
    }

    private void requestDataFromServer() {
        //请求网路数据
        showLoadDataDialog();

        AnswerService.getSetList(new CommonResponse<SetPageBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(SetPageBean setPageBean) {
                AnswerMainActivity.this.setPageBean = setPageBean;
                hideProgressDialog();
                LogUtils.d("setPageBean:::" + GsonUtils.toJson(setPageBean));

                if (null == setPageBean) {
                    shortToast("内容暂未开放，敬请期待");
                    return;
                }
                // 服务端 该接口返回数据中没有状态码
                testSetList = ConvertUtils.toTestSet(setPageBean);
                if (null != testSetList) {
                    initOrUpdateAdapter(testSetList);
                } else {
                    shortToast("内容暂未开放，敬请期待");
                }

            }
        });

    }

}
