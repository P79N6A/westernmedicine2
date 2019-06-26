package com.xywy.askforexpert.module.discovery.answer.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.uilibrary.dialog.pndialog.XywyPNDialog;
import com.xywy.uilibrary.dialog.pndialog.listener.PNDialogListener;
import com.xywy.askforexpert.model.answer.api.answered.AnsweredListPageBean;
import com.xywy.askforexpert.model.answer.api.deletewrong.DeleteWrongSetResultBean;
import com.xywy.askforexpert.model.answer.show.AnsweredListItem;
import com.xywy.askforexpert.model.answer.show.PaperItem;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.module.discovery.answer.AnswerService;
import com.xywy.askforexpert.module.discovery.answer.adapter.AnsweredPaperListAdapter;
import com.xywy.askforexpert.module.discovery.answer.service.ConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已答试题 列表页
 * Created by bailiangjin on 16/4/27. done
 */
public class AnsweredPaperListActivity extends YMBaseActivity {

    private ListView lv_answered_paper;
    private AnsweredPaperListAdapter answeredPaperListAdapter;
    private List<AnsweredListItem> answeredListItemList = new ArrayList<>();

    /**
     * 是否 多选删除状态
     */
    private boolean isMultiDelete;

    private Map<String, AnsweredListItem> selectedItemMap = new HashMap<>();

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, AnsweredPaperListActivity.class));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_answered_paper_list;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {
        lv_answered_paper = (ListView) findViewById(R.id.lv_answered_paper);

        lv_answered_paper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isMultiDelete) {
                    //TODO:勾选或取消勾选
                    AnsweredListItem answeredListItem = answeredListItemList.get(position);
                    answeredListItem.setChecked(!answeredListItem.isChecked());
                    if (answeredListItem.isChecked()) {
                        selectedItemMap.put(answeredListItem.getMapKey(), answeredListItem);
                    } else {
                        if (selectedItemMap.containsKey(answeredListItem.getMapKey())) {
                            selectedItemMap.remove(answeredListItem.getMapKey());
                        }
                    }
                    answeredPaperListAdapter.notifyDataSetChanged();

                } else {
                    //跳转到试题详情页
                    toAnswerDetailPage(position);
                }
            }
        });

        lv_answered_paper.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

//                final AnsweredListItem answeredListItem = answeredListItemList.get(position);
//                selectedItemMap.put(answeredListItem.getMapKey(), answeredListItem);

                return showDeleteDialog(answeredListItemList.get(position));
            }
        });
    }

    private boolean showDeleteDialog(final AnsweredListItem item) {
        if (null == item) {
            shortToast("未选中要删除的条目");
            return true;
        }

        new XywyPNDialog.Builder().setContent("删除错题集或试卷？").create(AnsweredPaperListActivity.this, new PNDialogListener() {
            @Override
            public void onPositive() {
                AnswerService.deleteWrongQuestionSet(item.getId(), item.getTypeStr(), new CommonResponse<DeleteWrongSetResultBean>(YMApplication.getAppContext()) {
                    @Override
                    public void onNext(DeleteWrongSetResultBean deleteWrongSetResultBean) {
                        if (null != deleteWrongSetResultBean && deleteWrongSetResultBean.isResult()) {
                            //成功后清空选中数据状态
                            shortToast("删除成功");
                            answeredListItemList.remove(item);
                            resetDataAndNotify();
                            //updateTitleBtnState(isMultiDelete);
                        } else {
                            shortToast("删除失败");
                        }
                    }
                });
            }

            @Override
            public void onNegative() {

            }
        });
        return true;
    }

    private void resetDataAndNotify() {
        isMultiDelete = false;
        answeredPaperListAdapter.setMultiDelete(isMultiDelete);
        selectedItemMap.clear();
        if(!answeredListItemList.isEmpty()){
            for (AnsweredListItem item : answeredListItemList) {
                item.setChecked(false);
            }
        }
        answeredPaperListAdapter.setData(answeredListItemList);
        answeredPaperListAdapter.notifyDataSetChanged();
    }


    @Override
    protected void initData() {
        titleBarBuilder.setTitleText("已答试卷");

//        commonTitleView.setRightBtnText("批量操作");
//        commonTitleView.setRightBtnListener(new TitleClickListener() {
//            @Override
//            public boolean onClick() {
//                shortToast("批量操作");
//                if (null != answeredPaperListAdapter) {
//                    if (isMultiDelete) {
//                        if (selectedItemMap.size() == 0) {
//                            shortToast("未选择要删除的数据 请选择");
//                            return true;
//                        } else {
//                            shortToast("批量删除");
//                            //TODO: 调用批量删除接口
//                            showDeleteDialog(selectedItemMap);
//                        }
//                    }
//                } else {
//                    shortToast("数据未初始化");
//                }
//                return true;
//            }
//        });

        initMyData();

    }

//    private void updateTitleBtnState(boolean isMultiDelete) {
//        commonTitleView.setRightBtnText(isMultiDelete ? "删除" : "批量操作");
//
//        commonTitleView.setLeftImageVisibility(isMultiDelete ? View.GONE : View.VISIBLE);
//
//        if (isMultiDelete) {
//            commonTitleView.setLeftBtnText("取消");
//            commonTitleView.setLeftBtnListener(new TitleClickListener() {
//                @Override
//                public boolean onClick() {
//                    commonTitleView.setLeftBtnVisibility(View.GONE);
//                    commonTitleView.setLeftImageVisibility(View.VISIBLE);
//                    commonTitleView.setRightBtnText("批量操作");
//                    resetDataAndNotify();
//                    return true;
//                }
//            });
//        } else {
//            commonTitleView.setLeftBtnVisibility(View.GONE);
//        }
//
//    }

    /**
     * 调用刷新 GridView数据
     */
    private void initOrUpdateAdapter(List<AnsweredListItem> list) {
        if (null == answeredPaperListAdapter) {
            answeredPaperListAdapter = new AnsweredPaperListAdapter(AnsweredPaperListActivity.this, list);
            lv_answered_paper.setAdapter(answeredPaperListAdapter);
        } else {
            answeredPaperListAdapter.setData(list);
            answeredPaperListAdapter.notifyDataSetChanged();
        }
    }

    private void initMyData() {

        showLoadDataDialog();

        AnswerService.getAnsweredPaperList(new CommonResponse<AnsweredListPageBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(AnsweredListPageBean answeredPageBean) {
                hideProgressDialog();
                if (null == answeredPageBean) {
                    shortToast("服务返回数据为空或格式错误");
                    return;
                }
                int code = answeredPageBean.getCode();
                switch (code) {
                    case BaseResultBean.CODE_SUCCESS:
                        answeredListItemList = ConvertUtils.toAnsweredTestSet(answeredPageBean);
                        if (null != answeredListItemList && !answeredListItemList.isEmpty()) {
                            initOrUpdateAdapter(answeredListItemList);

                        } else {
                            shortToast("您还没有答题记录 请先答题");
                        }
                        break;
                    case BaseResultBean.CODE_PARAM_ERROR:
                        LogUtils.e("请求单数异常:错误码:" + code);
                        shortToast("请求失败 请求单数异常");
                        break;
                    default:
                        LogUtils.e("服务的未知异常:错误码:" + code);
                        shortToast("服务的未知异常:错误码:" + code);
                        break;
                }
            }
        });

    }

    /**
     * 跳到试题详情页
     *
     * @param position
     */
    private void toAnswerDetailPage(int position) {
        //跳转到试题详情页
        String paperId = answeredListItemList.get(position).getId();
        if (TextUtils.isEmpty(paperId)) {
            shortToast("服务端异常 传入的试卷ID为空 无法获取试题详情");
            return;
        }
        AnsweredListItem answeredListItem = answeredListItemList.get(position);
        PaperItem paperItem = answeredListItem.toPaperItem();
        int showType = answeredListItem.getType() == AnsweredListItem.TYPE_NORMAL ? AnswerDetailActivity.SHOW_TYPE_PAPER : AnswerDetailActivity.SHOW_TYPE_WONG_PAPER;
        AnswerDetailActivity.start(AnsweredPaperListActivity.this, paperItem, showType);

    }

}
