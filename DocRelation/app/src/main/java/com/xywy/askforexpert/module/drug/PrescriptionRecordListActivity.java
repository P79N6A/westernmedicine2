package com.xywy.askforexpert.module.drug;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.module.discovery.medicine.common.ViewCallBack;
import com.xywy.askforexpert.module.drug.adapter.PrescriptionRecordListAdapter;
import com.xywy.askforexpert.module.drug.bean.DocQues;
import com.xywy.askforexpert.module.drug.request.DrugAboutRequest;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 处方记录列表 在线诊室IM stone
 */
public class PrescriptionRecordListActivity extends BasePageListActivity<DocQues> implements View.OnClickListener {

    private TextView choose;
    private TextView order;

    private View tv_sort1;
    private View tv_sort2;
    private View tv_sort3;
    private View tv_sort4;
    private View tv_sort5;
    private View tv_sort6;
    private View popRoot;

    private int selection = 0;

    private boolean isOrderTime;//倒叙

    private SelectBasePopupWindow mPopupWindow;

    private int type;
    private String condition = "";
    private String sort = "2";
    private String mDoctorId = YMUserService.getCurUserId();


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_prescription_record_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose:
                showPop();
                break;
            case R.id.order:

                if (isOrderTime) {
                    order.setSelected(false);
                    isOrderTime = false;
//                    shortToast("非时间排序请求---倒序");
                    sort = "2";
                } else {
                    order.setSelected(true);
                    isOrderTime = true;
//                    shortToast("时间排序请求---正序");
                    sort = "1";
                }
                if (mList.size()!=0) {
                    List<DocQues> list = new ArrayList<>();
                    list.clear();
                    Collections.reverse(mList);
                    list.addAll(mList);
                    adapter.getDatas().clear();
                    adapter.getDatas().addAll(list);
                    mLoadMoreWrapper.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(0);
                }
//                mPage=1;
//                getPrescriptionData(condition,sort,State.ONREFRESH.getFlag());
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();

        titleBarBuilder.setTitleText("处方记录");

        choose = (TextView) findViewById(R.id.choose);
        order = (TextView) findViewById(R.id.order);
        choose.setOnClickListener(this);
        order.setOnClickListener(this);
    }

    @Override
    public void loadData(final int state) {
        getPrescriptionData(condition,sort,state);
    }

    private void getPrescriptionData(String condition,String sort,final int state) {
        DrugAboutRequest.getInstance().getPrescriptionList(mDoctorId,condition,sort, mPage).subscribe(new BaseRetrofitResponse<BaseData<List<DocQues>>>() {
            @Override
            public void onError(Throwable e) {
                handleList(state, null);
            }

            @Override
            public void onNext(BaseData<List<DocQues>> entry) {
                handleList(state, entry);
            }
        });
    }

    @Override
    protected int getEmptyLayoutId() {
        return 0;
    }

    @Override
    protected MultiItemTypeAdapter<DocQues> getAdapter() {
        PrescriptionRecordListAdapter adapter = new PrescriptionRecordListAdapter(PrescriptionRecordListActivity.this, mList);
        adapter.setViewCallBack(new ViewCallBack() {
            @Override
            public void onClick(int tag, int position, Object data,boolean delete) {
                final DocQues entity = (DocQues) data;
                if (entity != null) {
                    if (tag == 0) {
                        //进入处方详情页
                        Intent intent = new Intent(PrescriptionRecordListActivity.this,PrescriptionDetailActivity.class);
                        intent.putExtra(Constants.KEY_ID,entity.id);
                        startActivity(intent);
                    }
                }
            }
        });
        return adapter;
    }

    private void showPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(true, this);

            popRoot = View.inflate(getBaseContext(), R.layout.pop_layout_onlineroom_record, null);
            tv_sort1 = popRoot.findViewById(R.id.tv_sort1);
            tv_sort2 = popRoot.findViewById(R.id.tv_sort2);
            tv_sort3 = popRoot.findViewById(R.id.tv_sort3);
            tv_sort4 = popRoot.findViewById(R.id.tv_sort4);
            tv_sort5 = popRoot.findViewById(R.id.tv_sort5);
            tv_sort6 = popRoot.findViewById(R.id.tv_sort6);
            tv_sort1.setOnClickListener(mPopOnClickListener);
            tv_sort2.setOnClickListener(mPopOnClickListener);
            tv_sort3.setOnClickListener(mPopOnClickListener);
            tv_sort4.setOnClickListener(mPopOnClickListener);
            tv_sort5.setOnClickListener(mPopOnClickListener);
            tv_sort6.setOnClickListener(mPopOnClickListener);

            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    choose.setSelected(false);
                }
            });
        }

        choose.setSelected(true);

        if (!mPopupWindow.isShowing()) {
//            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, (AppUtils.dpToPx(48, getResources()) - 24) / 2 + AppUtils.dpToPx(5, getResources()) - 30 - 27, AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight() - 30);
            mPopupWindow.init(popRoot);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, AppUtils.dpToPx(48 + 45, getResources()) + YMApplication.getStatusBarHeight());
        }
    }


    /**
     * pop监听器
     */
    private View.OnClickListener mPopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.tv_sort1:
                    initSelectionStatus(1);
                    break;
                case R.id.tv_sort2:
                    initSelectionStatus(2);
                    break;
                case R.id.tv_sort3:
                    initSelectionStatus(3);
                    break;
                case R.id.tv_sort4:
                    initSelectionStatus(4);
                    break;
                case R.id.tv_sort5:
                    initSelectionStatus(5);
                    break;
                case R.id.tv_sort6:
                    initSelectionStatus(6);
                    break;


                default:
                    break;
            }
        }

    };


    private void resetStatus() {
        tv_sort1.setSelected(false);
        tv_sort2.setSelected(false);
        tv_sort3.setSelected(false);
        tv_sort4.setSelected(false);
        tv_sort5.setSelected(false);
        tv_sort6.setSelected(false);
    }

    private void initSelectionStatus(int pos) {
        switch (pos) {
            case 1:
                if (selection != 1) {
                    mPage=1;
                    resetStatus();
                    selection = 1;
                    tv_sort1.setSelected(true);
                    choose.setText(getString(R.string.onlineroom_record_notice1));
                    condition = "";
                    getPrescriptionData(condition,sort,State.ONREFRESH.getFlag());
                }
                break;
            case 2:
                if (selection != 2) {
                    mPage=1;
                    resetStatus();
                    selection = 2;
                    tv_sort2.setSelected(true);
                    choose.setText(getString(R.string.onlineroom_record_notice2));
                    condition = "3";
                    getPrescriptionData(condition,sort,State.ONREFRESH.getFlag());
                }
                break;
            case 3:
                if (selection != 3) {
                    mPage=1;
                    resetStatus();
                    selection = 3;
                    tv_sort3.setSelected(true);
                    choose.setText(getString(R.string.onlineroom_record_notice3));
                    condition = "5";
                    getPrescriptionData(condition,sort,State.ONREFRESH.getFlag());
                }
                break;
            case 4:
                if (selection != 4) {
                    mPage=1;
                    resetStatus();
                    selection = 4;
                    tv_sort4.setSelected(true);
                    choose.setText(getString(R.string.onlineroom_record_notice4));
                    condition = "1";
                    getPrescriptionData(condition,sort,State.ONREFRESH.getFlag());
                }
                break;
            case 5:
                if (selection != 5) {
                    mPage=1;
                    resetStatus();
                    selection = 5;
                    tv_sort5.setSelected(true);
                    choose.setText(getString(R.string.onlineroom_record_notice5));
                    condition = "2";
                    getPrescriptionData(condition,sort,State.ONREFRESH.getFlag());
                }
                break;
            case 6:
                if (selection != 6) {
                    mPage=1;
                    resetStatus();
                    selection = 6;
                    tv_sort6.setSelected(true);
                    choose.setText(getString(R.string.onlineroom_record_notice6));
                    condition = "4";
                    getPrescriptionData(condition,sort,State.ONREFRESH.getFlag());
                }
                break;
        }
    }
}