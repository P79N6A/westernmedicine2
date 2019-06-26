package com.xywy.askforexpert.module.my.userinfo;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.model.IdNameBean;
import com.xywy.askforexpert.module.my.userinfo.adapter.JobTitleAdapter;

import java.util.List;

/**
 * 临床职称 stone
 */
public class BaseJobListActivity extends YMBaseActivity {

    private ListView listView;
    private JobTitleAdapter mAdapter;
    private List<IdNameBean> mList;
    private String str_json;

    private IdNameBean mJobTitle;
    private int mSelectedId = -1;

    @Override
    protected int getLayoutResId() {
        return R.layout.jobtitle_activity;
    }

    @Override
    protected void beforeViewBind() {
        if (str_json == null) {
            str_json = YMOtherUtils.getRawJson(this, R.raw.jobtitle);
            if (!TextUtils.isEmpty(str_json)) {
                mList = GsonUtils.parsJsonArrayStr2List(str_json, IdNameBean.class);
            }
        }

        mJobTitle = (IdNameBean) getIntent().getSerializableExtra(Constants.KEY_VALUE);

        if (mList != null && mJobTitle != null) {
            int len = mList.size();
            for (int i = 0; i < len; i++) {
                if (mJobTitle.id.equals(mList.get(i).id)) {
                    mSelectedId = i;
                    break;
                }
            }
        }
    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        ((TextView) findViewById(R.id.tv_title)).setText("临床职称");

        TextView btn22 = (TextView) findViewById(R.id.btn22);
        btn22.setText(getString(R.string.complete));
        btn22.setVisibility(View.VISIBLE);

        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void initData() {
        mAdapter = new JobTitleAdapter(BaseJobListActivity.this, mList, mSelectedId);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                mJobTitle = mList.get(pos);
                mAdapter.initSelecteStatus();
                mAdapter.getSelectionMap().put(pos, true);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);

//        map = (HashMap<String, String>) msg.obj;
//        switch (msg.what) {
//            case 100:
//                if (map.get("code").equals("0")) {
//                    // if (type.equals("approve"))
//                    // {
//                    // Intent intent = new Intent(BaseJobListActivity.this,
//                    // AppointActivity.class);
//                    // startActivity(intent);
//                    // } else if (type.equals("perinfo"))
//                    // {
//                    // Intent intent = new Intent(BaseJobListActivity.this,
//                    // PersonInfoActivity.class);
//                    // startActivity(intent);
//                    // }
//                    if (type.equals("approve_disease")) {
//                        YMUserService.getPerInfo().getData().setSpecial(str_job);
//
//                    } else {
//                        YMUserService.getPerInfo().getData().setJob(str_job);
//                        YMApplication.getLoginInfo().getData().setJob(str_job);
//                    }
//                    DLog.i(TAG, "打印输出。。" + map.get("msg") + "..");
//                    ToastUtils.shortToast("成功");
//                    finish();
//                }
//                break;
//
//            default:
//                break;
//        }
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
            case R.id.btn22:
                if (mJobTitle == null) {
                    ToastUtils.shortToast("请选择临床职称");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.KEY_VALUE, mJobTitle);
                    BaseJobListActivity.this.setResult(RESULT_OK, intent);
                    BaseJobListActivity.this.finish();
                }

//                str_job = getScholl();
//                if (str_job != null && !str_job.equals("")) {
//                    if (type.equals("approve_disease")) {
//                        UpdatePersonInfo.update("special", str_job, 100, uiHandler);
//                    } else {
//
//                        UpdatePersonInfo.update("job", str_job, 100, uiHandler);
//                    }
//                } else {
//                    if (type.equals("approve_disease")) {
//                        ToastUtils.shortToast("请选择擅长疾病");
//                    } else {
//                        ToastUtils.shortToast("请选择临床职称");
//
//                    }
//                }
            default:
                break;
        }
    }

}
