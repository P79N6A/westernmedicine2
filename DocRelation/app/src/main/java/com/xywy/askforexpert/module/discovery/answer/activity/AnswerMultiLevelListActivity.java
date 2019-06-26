package com.xywy.askforexpert.module.discovery.answer.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.nostra13.universalimageloader.utils.L;
import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.uilibrary.titlebar.ItemClickListener;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.answer.api.paperlist.PaperListPageBean;
import com.xywy.askforexpert.model.answer.show.BaseItem;
import com.xywy.askforexpert.model.answer.show.PaperItem;
import com.xywy.askforexpert.model.answer.show.TestSet;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.module.discovery.answer.AnswerService;
import com.xywy.askforexpert.module.discovery.answer.adapter.AnswerMultiListAdapter;
import com.xywy.askforexpert.module.discovery.answer.constants.AnswerIntentKey;
import com.xywy.askforexpert.module.discovery.answer.constants.AnswerShareConstants;
import com.xywy.askforexpert.module.discovery.answer.service.ConvertUtils;
import com.xywy.askforexpert.module.message.share.ShareUtil;

import java.util.List;

import butterknife.Bind;
import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.NestType;
import pl.openrnd.multilevellistview.OnItemClickListener;

/**
 * Created by bailiangjin on 16/4/20. done
 */
public class AnswerMultiLevelListActivity extends YMBaseActivity {

    @Bind(R.id.listView)
    MultiLevelListView mListView;
    private AnswerMultiListAdapter listAdapter;

    private List<BaseItem> baseItemList;

    private String classId;
    private String className;

    private PaperListPageBean paperListPageBean;
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {


        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            L.d(this.getClass().getName(), "item clicked");
            //showItemDescription(item, itemInfo);
            AnswerDetailActivity.start(AnswerMultiLevelListActivity.this, (PaperItem) item, AnswerDetailActivity.SHOW_TYPE_PAPER);
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            //sowItemDescription(item, itemInfo);
            L.d(this.getClass().getName(), "group clicked");
        }
    };

    /**
     * 启动方法  分享调用
     *
     * @param activity
     * @param testSetId
     * @param testSetName
     */
    public static void start(Activity activity, String testSetId, String testSetName) {
        //跳转到相应的试题列表页
        Intent intent = new Intent(activity, AnswerMultiLevelListActivity.class);
        intent.putExtra(AnswerIntentKey.CLASS_ID_KEY, testSetId);
        intent.putExtra(AnswerIntentKey.CLASS_NAME_KEY, testSetName);
        activity.startActivity(intent);
    }

    public static void start(Activity activity, TestSet testSet) {
        start(activity, testSet.getId(), testSet.getName());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_answer_multilevel_list;
    }

    @Override
    protected void beforeViewBind() {
        classId = getIntent().getStringExtra(AnswerIntentKey.CLASS_ID_KEY);
        className = getIntent().getStringExtra(AnswerIntentKey.CLASS_NAME_KEY);

    }

    @Override
    protected void initView() {
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setNestType(NestType.SINGLE);

        titleBarBuilder.setTitleText(className).addShareMenuItem(new ItemClickListener() {
            @Override
            public void onClick() {

                StatisticalTools.eventCount(AnswerMultiLevelListActivity.this, "TestQuestionsShare");
                if (null == paperListPageBean) {
                    return;
                }

                new ShareUtil.Builder().setTitle(AnswerShareConstants.shareTitle)
                        .setText(paperListPageBean.getShare_text())
                        .setTargetUrl(paperListPageBean.getShare_url())
                        .setImageUrl(paperListPageBean.getShare_img())
                        .setShareId(AnswerShareConstants.shareId)
                        .setShareSource(AnswerShareConstants.shareSource)
                        .setAnswerData(className, classId, AnswerShareConstants.shareSourceType_List, "1").build(AnswerMultiLevelListActivity.this).innerShare();

                return;
            }
        }).build();


    }

    @Override
    protected void initData() {
        initMyData();
    }

    private void initMyData() {
        if (TextUtils.isEmpty(classId)) {
            shortToast("传入的类别id为空");
            return;
        }
        //从服务端请求试题列表
        requestFromSever(classId);

    }

    private void requestFromSever(String classId) {
        showLoadDataDialog();

        AnswerService.getPaperList(classId, new CommonResponse<PaperListPageBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(PaperListPageBean paperListPageBean) {
                hideProgressDialog();

                AnswerMultiLevelListActivity.this.paperListPageBean = paperListPageBean;

                if (null == paperListPageBean) {
//                    shortToast("内容暂未开放，敬请期待");
                    return;
                }
                int code = paperListPageBean.getCode();
                switch (code) {
                    case BaseResultBean.CODE_SUCCESS:
                        baseItemList = ConvertUtils.toPaperList(paperListPageBean);
                        if (null != baseItemList && !baseItemList.isEmpty()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initOrUpdateAdapter(baseItemList);
                                }
                            });
                        } else {
//                            shortToast("内容暂未开放，敬请期待");
                        }
                        break;
                    case BaseResultBean.CODE_PARAM_ERROR:
                        LogUtils.e("请求单数异常:错误码:" + code);
//                        shortToast("提交失败 请求单数异常");
                        break;
                    default:
                        LogUtils.e("服务的未知异常:错误码:" + code);
//                        shortToast("服务的未知异常:错误码:" + code);
                        break;

                }
            }
        });

    }

    /**
     * 调用刷新 GridView数据
     */
    private void initOrUpdateAdapter(List<BaseItem> list) {
        if (null == listAdapter) {

            listAdapter = new AnswerMultiListAdapter(AnswerMultiLevelListActivity.this);

            mListView.setAdapter(listAdapter);
            //设置假数据
            listAdapter.setDataItems(list);

        } else {
            listAdapter.setDataItems(list);
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
